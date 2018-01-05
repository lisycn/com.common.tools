package com.common.tools.code.writelog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.common.tools.code.utils.DateUtil;
import com.common.tools.code.utils.StringUtils;

public class JavaFile {
	private Logger logger = Logger.getLogger(JavaFile.class);
	
	private static final String LOGGER_IMPORT = "import org.apache.log4j.Logger;";
	
	private String encoding = "utf-8";
	private File file;
	private String packageName;
	private int packageIndex;
	private String className;
	private int beginIndex;//类开始{的index
	private int endIndex;//类结束}的index
	private List<JavaMethod> methodList;
	private List<FileLine> content;
	private String contentStr;
	private boolean hasLogger = false;
	private boolean hasLoggerImport = false;
	private boolean isClass = false;
	
	public JavaFile(File file) throws Exception{
		if (!file.exists()) {
			throw new RuntimeException("文件不存在:" + file.getAbsolutePath());
		}
		
		this.file = file;
		init();
	}
	
	private void init() throws Exception {
		initContent();
		initPackage();
		initClass();
		if (!this.isClass) {
			logger.info("不是class,不继续初始化...");
			return;
		}
		initMethod();
		initLogger();
	}

	private void initLogger() {
		Pattern p = Pattern.compile("Logger\\s+logger\\s*=\\s*Logger.getLogger\\s*\\([^\\(\\)]+\\)\\s*;");
		Matcher m = p.matcher(this.contentStr);
		if (m.find()) {
			this.hasLogger = true;
		}else{
			this.hasLogger = false;
		}
		
		if (this.contentStr.indexOf(LOGGER_IMPORT) > 0) {
			this.hasLoggerImport = true;
		}else{
			this.hasLoggerImport = false;
		}
	}

	private void initMethod() throws Exception {
		String fullClassName = this.packageName + "." + this.className;
		Class clazz = Class.forName(fullClassName);
		Method[] methodArr = clazz.getDeclaredMethods();
		this.methodList = new ArrayList<>();
		if (methodArr == null || methodArr.length == 0) {
			return;
		}
		for(Method m : methodArr){
			if (m.getName().contains("$")) {
				continue;
			}
			JavaMethod jm = new JavaMethod();
			String methodName = m.getName();
			Class[] paramTypes = m.getParameterTypes();
			
			int modifiers = m.getModifiers();
			
			logger.info("添加方法,methodName:" + methodName + ",modifiers:" + modifiers + ",paramTypes:" + paramTypes);
			jm.setModifiers(modifiers);
			if (jm.isAbstract()) {
				logger.info("抽象方法不要:" + jm.getSignRegex());
				continue;
			}
			
			jm.setMethodName(methodName);
			jm.setParamTypes(paramTypes);
			jm.calRegion(this.contentStr);
			
			this.methodList.add(jm);
		}
	}

	private void initClass() throws Exception {
		initClassName();
		if (!this.isClass) {
			logger.info("不是class,不继续初始化...");
			return;
		}
		initBodyIndex();
	}

	/**
	 * 类括号的位置
	 * @throws Exception
	 */
	private void initBodyIndex() throws Exception {
		String classRegex = "class\\s+" + this.className;
		if (className.equals("ImportService")) {
			System.out.println("aa");
		}
		Bracket b = new Bracket(this.contentStr, classRegex);
		this.beginIndex = b.getStartIndex();
		this.endIndex = b.getEndIndex();
	}

	/**
	 * 类名
	 */
	private void initClassName() {
		String s = file.getName().trim();
		int index = s.lastIndexOf(".java");
		s = s.substring(0,index);
		this.className = s;
		
		Pattern p = Pattern.compile("class\\s+" + this.className);
		Matcher m = p.matcher(this.contentStr);
		if (m.find()) {
			this.isClass = true;
		}
	}

	private void initPackage() throws Exception {
		for(FileLine line : this.content){
			String s = line.getContent().trim();
			if (s.length() == 0) {
				continue;
			}
			
			
			if (!s.startsWith("package")) {
				throw new Exception("第一行必需以package开头,file:" + this.file.getAbsolutePath());
			}
			
			int packageIndex = s.indexOf("package");
			s = s.substring(packageIndex + 7);
			
			if (!s.endsWith(";")) {
				throw new Exception("第一行必需以;结尾,file:" + this.file.getAbsolutePath());
			}
			s = s.substring(0,s.length() - 1);
			
			this.packageName = s.trim();
			
			this.packageIndex = this.contentStr.indexOf(s);
			
			break;
		}
	}

	private void initContent() throws IOException, FileNotFoundException {
		List<String> list = IOUtils.readLines(new FileInputStream(this.file), this.encoding);
		List<FileLine> lineList = new ArrayList<>();
		
		this.contentStr = "";
		for(String s : list){
			FileLine line = new FileLine();
			line.setContent(s);
			line.setBeginIndex(this.contentStr.length());
			line.setEndIndex(this.contentStr.length() + s.length());
			this.contentStr += s;
			
			lineList.add(line);
		}
		
		this.content = lineList;
	}

	/**
	 * 添加日志
	 */
	public void addLog() throws Exception{
		logger.info("开始添加日志,fileName:" + this.className);
		long start = DateUtil.now().getTime();
		if (!this.isClass) {
			logger.info("不是class,不生成,用时:" + (DateUtil.now().getTime() - start) + "毫秒...");
		}
		if (this.methodList == null || this.methodList.size() == 0) {
			logger.info("无method,什么也不作...");
			return;
		}
		
		List<String> result = new ArrayList<>();
		for (int i = 0; i < this.content.size(); i++) {
			FileLine line = this.content.get(i);
			FileLine beforeLine = null;
			FileLine nextLine = null;
			if (i - 1 >= 0) {
				beforeLine = this.content.get(i - 1);
			}
			if (i + 1 < this.content.size()) {
				nextLine = this.content.get(i + 1);
			}
			
			result.add(line.getContent());
			if (!hasLogger && this.beginIndex >= line.getBeginIndex() && this.beginIndex <= line.getEndIndex()) {
				result.add(StringUtils.getStartSpace(line.getContent()) + "    " + "private static Logger logger = Logger.getLogger(" + this.className + ".class);");
			}
			if (!hasLoggerImport && this.packageIndex >= line.getBeginIndex() && this.packageIndex <= line.getEndIndex()) {
				result.add(LOGGER_IMPORT);
			}
			
			for(JavaMethod m : this.methodList){
				if (m.getBenginIndex() >= line.getBeginIndex() && m.getBenginIndex() <= line.getEndIndex()) {
					String message = getMethodLog("进入方法",line,"    ", m);
					if (!(nextLine != null && nextLine.getContent().contains(message))) {
						result.add(message);
					}
				}
				if (m.getReturnIndex() != null && m.getReturnIndex().size() > 0) {
					for(Integer index : m.getReturnIndex()){
						if (index >= line.getBeginIndex() && index <= line.getEndIndex()) {
							String message = getMethodLog("结束方法",line,"", m);
							if (!(beforeLine != null && beforeLine.getContent().contains(message))) {
								result.add(result.size() - 1,message);
							}
							
						}
					}
				}else{
					if (m.getEndIndex() >= line.getBeginIndex() && m.getEndIndex() <= line.getEndIndex()) {
						String message = getMethodLog("结束方法",line,"    ", m);if (!(beforeLine != null && beforeLine.getContent().contains(message))) {
							result.add(result.size() - 1,message);
						}
					}
				}
			}
		}
		
		IOUtils.writeLines(result, "\n", new FileOutputStream(this.file),this.encoding);

		logger.info("添加日志完成,用时:" + (DateUtil.now().getTime() - start) + "毫秒...");
	}

	/**
	 * 获得方法进出的日志
	 * @param line
	 * @param m
	 * @param message
	 * @param sj 缩进
	 * @return
	 */
	private String getMethodLog(String message,FileLine line,String sj, JavaMethod m) {
		String r = StringUtils.getStartSpace(line.getContent()) + sj;
		r += "logger.debug(\"" + message + "," + this.className + "." + m.getMethodName() + "(";
		if(m.getParamTypes() != null && m.getParamTypes().length > 0){
			for(Class c : m.getParamTypes()){
				r += c.getName() + ",";
			}
			r = r.substring(0,r.length() - 1);
		}
		r += ")\");";
		return r;
	}
}
