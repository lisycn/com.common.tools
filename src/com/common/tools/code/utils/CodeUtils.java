package com.common.tools.code.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;


public class CodeUtils{
	
	/**
	 * 下划线分隔命名转化为Pacical命名
	 * @param src
	 * @return
	 */
	public static String toPacical(String src){
		if (src == null) {
			return "";
		}
		src = src.toLowerCase();
		StringBuffer sb = new StringBuffer();
		char[] arr = src.toCharArray();
		
		for (int i = 0; i < arr.length; i++) {
			if (i == 0 && arr[i] != '_') {
				sb.append(String.valueOf(arr[0]).toUpperCase());
			}else if(arr[i] == '_'){
				while((i + 1) < arr.length){
					if(arr[++i] == '_'){
						continue;
					}
					sb.append(String.valueOf(arr[i]).toUpperCase());
					break;
				}
			}else{
				sb.append(arr[i]);
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * 驼峰命名转化为 下划线分隔命名
	 * @param src
	 * @return
	 */
	public static String toUnderline(String src){
		if (src == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		char[] arr = src.toCharArray();
		
		for(int i = 0; i < arr.length; i++){
			
			if (i == 0) {
				sb.append(String.valueOf(arr[0]).toUpperCase());
			}else if(String.valueOf(arr[i]).matches(".*[A-Z].*")){
				sb.append("_" +arr[i]);
			}else{
				sb.append(String.valueOf(arr[i]).toUpperCase());
			}
		}
		return sb.toString();
	}
	
	/**
	 * 下划线分隔命名转化为 驼峰命名
	 * @param src
	 * @return
	 */
	public static String toCamel(String src){
		String p = toPacical(src);
		String r = "";
		if (p != null && p.length() > 0) {
			r = p.substring(0,1).toLowerCase();
			if (p.length() > 1) {
				r += p.substring(1);
			}
		}
		
		return r;
	}
	
	/**
	 * 把包名解析为目录路径
	 * @param packageName
	 * @return
	 */
	public static String getPackagePath(String packageName){
		if (packageName == null) {
			return "";
		}
		
		return "/" + packageName.replaceAll("\\.", "/"); 
	}
	
	public static void appendCode(String pathname,String encoding,String pattern) throws FileNotFoundException, IOException{
		List<String> list = IOUtils.readLines(new FileInputStream(pathname), encoding);
		List<String> r = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			String s = pattern;
			String paramStr = list.get(i).trim();
			String[] arr = paramStr.split("\\s+");
//			String[] arr = {paramStr};
			for (int j = 0; j < arr.length; j++) {
				if (j == 0) {
				}
				s = s.replaceAll("\\$\\{" + j + "\\}",arr[j]);
			}
			s = s.replaceAll("\\$\\{uuid\\}", UUIDUtils.getUUID());
			s = s.replaceAll("\\$\\{i\\}", String.valueOf(i));
			
			s = s.replaceAll("\\$\\{\\d+\\}", "");
			r.add(s);
		}
		
		for(String s : r){
			System.out.println(s);
		}
	}

	private static void testAppend() throws FileNotFoundException, IOException {
		
		
		//appendCode("D:/temp.txt", "utf-8",  "private ${1} ${0} = \"\";//${6}(${7} ${8} ${9} ${10} ${11} ${12})(类型：${1})");
//		appendCode("D:/temp.txt", "utf-8", "public static String NODE_NAME_${0} = \"${0}\";");
//		appendCode("D:/temp.txt", "utf-8", "header.set${0}(headerEl.elementTextTrim(GfCommonHeader.NODE_NAME_${0}));");
//		appendCode("D:/temp.txt", "utf-8", "coreHeader.set${0}(coreHeaderEl.elementTextTrim(GfCoreRequestHeader.NODE_NAME_${0}));");
//		appendCode("D:/temp.txt", "utf-8", "coreHeader.set${0}(coreHeaderEl.elementTextTrim(GfCoreResponseHeader.NODE_NAME_${0}));");
//		appendCode("D:/svn/ecif/代码实现/ecif架构改造/报文结构/通用报文头.txt", "utf-8", "DomUtils.addElement(headerNext,NAME_SPACE_GATEWAY,GfCommonHeader.NODE_NAME_${0},this.getCommonHeader().get${0}());");
//		appendCode("D:/svn/ecif/代码实现/ecif架构改造/报文结构/核心请求头.txt", "utf-8", "DomUtils.addElement(coreHeader,NAME_SPACE_GATEWAY,GfCoreRequestHeader.NODE_NAME_${0},this.getCoreRequestHeader().get${0}());");
//		appendCode("D:/svn/ecif/代码实现/ecif架构改造/报文结构/核心返回头.txt", "utf-8", "DomUtils.addElement(coreHeader,NAME_SPACE_GATEWAY,GfCoreResponseHeader.NODE_NAME_${0},this.getCoreResponseHeader().get${0}());");
		
		appendCode("D:/temp/aaa.txt", "utf-8", "/**错误反馈码: ${1}*/\nCODE_${0}(\"${0}\",\"${1}\"),");
		
	}

	/**
	 * 开发计划
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void genPlan() throws FileNotFoundException, IOException {
		appendCode("D:/temp.txt", "UTF-8", "sb.append(\"${0}\");");
	}


	/**
	 * 交易码enum
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void genTransCodeEnum() throws FileNotFoundException, IOException {
		appendCode("D:/temp.txt", "utf-8", "\t/**交易码：${1}*/\n	${0}(\"${0}\",\"${1}\"),");
	}
	
	
	
	private static void testCopyClass() {
		copyClass(String.class);
	}

	private static void testGenMessageConfigSql() throws FileNotFoundException, IOException {
		genMessageConfigSql("D:/temp.txt", "utf-8", "17D286EABF724B1F8320E49ACBA92818", "F010101001", "5476012B12DF42C9B4208ABB0313C948");
	}

	/**
	 * 报文配置insert sql
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private static void genMessageConfigSql(String pathname,String encoding,String messageId,String transCode,String parentId) throws FileNotFoundException, IOException{
		String pattern = "insert into fmp_message_node_config (id, message_id, trans_code, parent_id, node_code, data_type, notempty, regex) VALUES ('${uuid}', '" + messageId + "', '" + transCode + "'," + (parentId == null ? "null" : "'" + parentId + "'") + ", '${0}', 'string', '0', null);";
		StringBuffer sbDelete = new StringBuffer("delete from fmp_message_node_config where id in (");
		StringBuffer sbInsert = new StringBuffer();
		List<String> list = IOUtils.readLines(new FileInputStream(pathname), encoding);
		List<String> r = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			String s = pattern;
			String paramStr = list.get(i).trim();
			String[] arr = paramStr.split("\\s+");
			for (int j = 0; j < arr.length; j++) {
				if (j == 0) {
				}
				s = s.replaceAll("\\$\\{" + j + "\\}",arr[j]);
			}
			String uuid = UUIDUtils.getUUID();
			s = s.replaceAll("\\$\\{uuid\\}", uuid);
			s = s.replaceAll("\\$\\{i\\}", String.valueOf(i));
			r.add(s);
			sbDelete.append("'"+ uuid +"',");
		}
		sbDelete.deleteCharAt(sbDelete.length() - 1);
		sbDelete.append(");");
		
		for(String s : r){
			System.out.println(s);
		}
		
		
		System.out.println(sbDelete.toString());
	}
	
	/**
	 * 判断属性类型
	 */
	public static String isAttribute(String type){
		
		if("class java.math.BigDecimal".equals(type)){
			return "BigDecimal";
		}else if("class java.lang.String".equals(type)){
			return "String";
		}else if("class java.lang.Long".equals(type)){
			return "Long";
		}else if("class java.lang.Integer".equals(type)){
			return "int";
		}else{
			return type;
		}
	}
	
	/**
	 * 复制一个自己
	 * @param clazz
	 */
	public static <T> void copyClass(Class<T> clazz){
		StringBuffer sb = new StringBuffer();
		String fromName = "interbankDeposit";
		String toName = "his";
		Method[] methods = clazz.getMethods();
		for(Method m : methods){
			//只处理set方法
			if (m.getName() == null || !m.getName().startsWith("set")) {
				continue;
			}
			String getMethodName = "get" + m.getName().substring(3, m.getName().length());
			
			sb.append(toName + "." + m.getName() + "(" + fromName + "." + getMethodName + "());\n");
		}
		
		System.out.println(sb.toString());
	}
	
	/**
	 * 反射生成 获取注入对象代码      如：obj.setCounterPartyAccount((String)map.get("COUNTER_PARTY_ACCOUNT"));
	 * @param clazz
	 */
	public static <T> void reflexInClass(Class<T> clazz){
		StringBuffer sb = new StringBuffer();
		String toName = clazz.getSimpleName();//对象名称
		String mapName = "bufferedMap";//map变量名称
		Method[] methods = clazz.getMethods();
		String bufferedMap="BufferedMap bufferedMap = new BufferedMap(map);\n";
		sb.append(bufferedMap);
		for(Method m : methods){
			//只处理set方法
			if (m.getName() == null || !m.getName().startsWith("set")) {
				continue;
			}
			//获取参数的属性
			Type  type = m.getGenericParameterTypes()[0];
//			String getMapMethod = "("+isAttribute(type.toString())+")"+mapName+".get("+"\""+toUnderline(m.getName().substring(3, m.getName().length()))+"\""+")";
//			String getMapMethod = mapName+".get("+"\""+toUnderline(m.getName().substring(3, m.getName().length()))+"\""+")";
			String getMapMethod = mapName+".getString("+"\""+toUnderline(m.getName().substring(3, m.getName().length()))+"\""+")";
//			sb.append(toName + "." + m.getName() + "(" + getMapMethod + " == null ? \"\" : " + getMapMethod + ".toString()" + ");\n");
			sb.append(toName + "." + m.getName() + "(" + getMapMethod + ");\n");
		}
		
		System.out.println(sb.toString());
	}
	
	/**
	 * 根据对象 获取注入MAP代码      如：map.put("ID","");
	 * @param clazz
	 */
	public static <T> void reflexOutMap(Class<T> clazz){
		StringBuffer sb = new StringBuffer();
		String mapName = "model";//map变量名称
		String toName = "obj";//对象名称
		Method[] methods = clazz.getMethods();
		int i=1;
		for(Method m : methods){
			//只处理get方法
			if (m.getName() == null || !m.getName().startsWith("get")) {
				continue;
			}
			
			String key = "\""+toUnderline(m.getName().substring(3, m.getName().length()))+"\"";
			String value = "\"" + i + "\"";// toName + "." + m.getName()+"()";// "\"\"";//默认空格
			sb.append(mapName + ".put("+ key +","+ value+");\n");
			i++;
		}
		
		System.out.println(sb.toString());
		
	}
	
	/**
	 * 根据对象 获取注入MAP代码      如：map.put("ID","");
	 * @param mapName  map变量名称
	 * @param clazz
	 */
	public static <T> void reflexOutMap(String mapName, Class<T> clazz){
		StringBuffer sb = new StringBuffer();
		Method[] methods = clazz.getMethods();
		int i=1;
		for(Method m : methods){
			//只处理get方法
			if (m.getName() == null || !m.getName().startsWith("get")) {
				continue;
			}
			
			String key = "\""+toUnderline(m.getName().substring(3, m.getName().length()))+"\"";
			String value = "\"" + i + "\"";// toName + "." + m.getName()+"()";// "\"\"";//默认空格
			sb.append(mapName + ".put("+ key +","+ value+");\n");
			i++;
		}
		
		System.out.println(sb.toString());
		
	}
	
	/**
	 * 根据对象 获取注入MAP代码组装请求报文      如：map.put("ID",model.getId());
	 * @param clazz
	 */
	public static <T> void reflexOutMapToMessage(Class<T> clazz){
		StringBuffer sb = new StringBuffer();
		String mapName = "body";//map变量名称
		String toName = "param";//对象名称
		Method[] methods = clazz.getMethods();
		int i=1;
		for(Method m : methods){
			//只处理get方法
			if (m.getName() == null || !m.getName().startsWith("get")) {
				continue;
			}
			
			String key = "\""+toUnderline(m.getName().substring(3, m.getName().length()))+"\"";
			String value = toName + "." + m.getName()+"()";// "\"\"";//默认空格
			sb.append(mapName + ".put("+ key +","+ value+");\n");
			i++;
		}
		
		System.out.println(sb.toString());
		
	}
	
	/**
	 * 根据对象 获取注入MAP代码组装请求报文      如：map.put("ID",对象名称.getId());
	 * @param mapName  map变量名称
	 * @param clazz
	 */
	public static <T> void reflexOutMapToMessage(String mapName, Class<T> clazz){
		StringBuffer sb = new StringBuffer();
		String toName = clazz.getSimpleName();//对象名称
		Method[] methods = clazz.getMethods();
		int i=1;
		for(Method m : methods){
			//只处理get方法
			if (m.getName() == null || !m.getName().startsWith("get")) {
				continue;
			}
			
			String key = "\""+toUnderline(m.getName().substring(3, m.getName().length()))+"\"";
			String value = toName + "." + m.getName()+"()";// "\"\"";//默认空格
			sb.append(mapName + ".put("+ key +","+ value+");\n");
			i++;
		}
		
		System.out.println(sb.toString());
		
	}
	
	/**
	 * 根据对象 获取注入MAP代码组装请求报文      如：body.put("SERIAL_NUM", param.get("serialNum"));
	 * @param clazz
	 */
	public static <T> void reflexOutMapToMessage2(Class<T> clazz){
		StringBuffer sb = new StringBuffer();
		String mapName = "body";//map变量名称
		String toName = "param";//对象名称
		Method[] methods = clazz.getMethods();
		int i=1;
		for(Method m : methods){
			//只处理get方法
			if (m.getName() == null || !m.getName().startsWith("get")) {
				continue;
			}
			
			String key = "\""+toUnderline(m.getName().substring(3, m.getName().length()))+"\"";
			//String paramName = "\""+m.getName().substring(3, m.getName().length())+"\"";
			String paramName1 =m.getName().substring(3, 3+1).toLowerCase();
			String paramName2 =m.getName().substring(4, m.getName().length());
			String paramName = "\""+paramName1+paramName2+"\"";//m.getName().substring(3, m.getName().length())
			String value = toName + ".get(" + paramName+")";// "\"\"";//默认空格
			sb.append(mapName + ".put("+ key +","+ value+");\n");
			i++;
		}
		
		System.out.println(sb.toString());
		
	}
	
	/**
	 * 根据对象 获取注入MAP代码组装请求报文      如：body.put("SERIAL_NUM", param.get("serialNum"));
	 * @param mapName  map变量名称
	 * @param clazz
	 */
	public static <T> void reflexOutMapToMessage2(String mapName, Class<T> clazz){
		StringBuffer sb = new StringBuffer();
		String toName = clazz.getSimpleName();//对象名称
		Method[] methods = clazz.getMethods();
		int i=1;
		for(Method m : methods){
			//只处理get方法
			if (m.getName() == null || !m.getName().startsWith("get")) {
				continue;
			}
			
			String key = "\""+toUnderline(m.getName().substring(3, m.getName().length()))+"\"";
			//String paramName = "\""+m.getName().substring(3, m.getName().length())+"\"";
			String paramName1 =m.getName().substring(3, 3+1).toLowerCase();
			String paramName2 =m.getName().substring(4, m.getName().length());
			String paramName = "\""+paramName1+paramName2+"\"";//m.getName().substring(3, m.getName().length())
			String value = toName + ".get(" + paramName+")";// "\"\"";//默认空格
			sb.append(mapName + ".put("+ key +","+ value+");\n");
			i++;
		}
		
		System.out.println(sb.toString());
		
	}
	
	/**
	 * 将一个 Map(下划线大写命名) 对象转化为一个 javaBean(驼峰命名) 
	 * @param type 要转化的类型
	 * @param map 包含属性值的 map
	 * @throws IntrospectionException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException */
	public static Object convertMap(Class type, Map map) throws IntrospectionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		BeanInfo beanInfo = Introspector.getBeanInfo(type);//获取类属性
		Object obj = type.newInstance();//创建java Bean对象
		
		PropertyDescriptor[] propertyDescriptor = beanInfo.getPropertyDescriptors();
		for(int i = 0;i<propertyDescriptor.length; i++){
			PropertyDescriptor descriptor = propertyDescriptor[i];
			String propertyName = descriptor.getName();
			//判断是否包含 
			if(map.containsKey(toUnderline(propertyName))){
				Object value = map.get(toUnderline(propertyName));
				Object[] args = new Object[1];
				args[0] = value;
				descriptor.getWriteMethod().invoke(obj, args);
				
			}
		}
		
		return obj;
	}
	
	/**
	 * 根据对象将驼峰命名转报文命名      如：serialNum-->SERIAL_NUM 
	 * @param clazz
	 */
	public static <T> void modelOutMap2(Class<T> clazz){
		StringBuffer sb = new StringBuffer();
		Method[] methods = clazz.getMethods();
		for(Method m : methods){
			//只处理get方法
			if (m.getName() == null || !m.getName().startsWith("get")) {
				continue;
			}
			
			String key = toUnderline(m.getName().substring(3, m.getName().length()));
			sb.append( key +"\n" );
		}
		
		System.out.println(sb.toString());
		
	}
	
	/**
	 * 根据对象将驼峰命名转报文命名      如：serialNum-->SERIAL_NUM 
	 * @param clazz
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void modelOutMap(String pathname ) throws FileNotFoundException, IOException{
//		StringBuffer sb = new StringBuffer();
//		Method[] methods = clazz.getMethods();
//		for(Method m : methods){
//			//只处理get方法
//			if (m.getName() == null || !m.getName().startsWith("get")) {
//				continue;
//			}
//			
//			String key = toUnderline(m.getName().substring(3, m.getName().length()));
//			sb.append( key +"\n" );
//		}
		StringBuffer sb = new StringBuffer();
//		String pathname ="D:/transferAccountName.txt";
		File f = new File(pathname);
		List<String> list = com.common.tools.code.utils.IOUtils.readLine(f, "UTF-8");
		for(String s : list){
			String[] arr = s.trim().split("\\s+",-1);
			String colName = "";
			String comment = "";
			colName = toUnderline(arr[0]);
			comment = (arr.length > 1) ? arr[1] : "";
			sb.append( colName + " " + comment + "\n" );
		}
		System.out.println(sb.toString());
	}
	
	/**
	 * new 一个新对象，并赋值
	 * @author LvXin
	 * @param clazz
	 */
	public static <T> void newPojo(Class<T> clazz){
		StringBuffer sb = new StringBuffer();
		String className = clazz.getSimpleName();
		
		sb.append(className + " aaaa = new " + className + "();\n");
		for(Method m : clazz.getMethods()){
			if (!m.getName().startsWith("set")) {
				continue;
			}
			sb.append("aaaa." + m.getName() + "(" + StringUtils.firstCharLowerCase(m.getName().substring(3)) + ");\n");
		}
		
		System.out.println(sb.toString());
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
//		testAppend();
//		genPlan();
//		testGenMessageConfigSql();
//		testCopyClass();
//		genTransCodeEnum();
		//copyClass(InterbankDepositHis.class);
		//String src = "getPageNum";
		//System.out.println(toUnderline(src));
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("ID", "123456789");
//		map.put("COUNTER_PARTY_ACCOUNT", "jun123456");
//		map.put("CERT_ORGAN_NO", "10.34252");
//		map.put("INPUT_ORGAN_NO", "12334687736");
//		try {
//			InterbankDepositHis in = (InterbankDepositHis) convertMap(InterbankDepositHis.class,map);
//			System.out.println(in.toString());
//		} catch (InstantiationException | IllegalAccessException
//				| IllegalArgumentException | InvocationTargetException
//				| IntrospectionException e) {
//			e.printStackTrace();
//		}

//		reflexOutMapToMessage(CustAssInfo.class);
//		reflexInClass(PtyCustCounterpartyInfo.class);
//		reflexOutMap(AccountingEntry.class);
//		modelOutMap("D:/123.txt");
		
		System.exit(0);

	}
}
