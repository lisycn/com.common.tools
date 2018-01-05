package com.common.tools.code.writelog;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFRow;

public class JavaMethod {
	private int benginIndex;//开始{的index
	private int endIndex;//结束}的index
	private String methodName;
	private Class[] paramTypes;
	private List<Integer> returnIndex;
	private int modifiers = 0;
	
	public int getModifiers() {
		return modifiers;
	}
	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}

	public List<Integer> getReturnIndex() {
		return returnIndex;
	}
	
	public void setReturnIndex(List<Integer> returnIndex) {
		this.returnIndex = returnIndex;
	}
	public int getBenginIndex() {
		return benginIndex;
	}
	public void setBenginIndex(int benginIndex) {
		this.benginIndex = benginIndex;
	}
	public int getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Class[] getParamTypes() {
		return paramTypes;
	}
	public void setParamTypes(Class[] paramTypes) {
		this.paramTypes = paramTypes;
	}
	
	public String getSignRegex() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append(this.getMethodName());
		sb.append("\\s*");
		sb.append("\\(");
		sb.append("\\s*");
		sb.append("[^\\{;]*");
		sb.append("\\s*");
		if (this.getParamTypes() != null && this.getParamTypes().length > 0) {
			for(Class clazz : this.getParamTypes()){
				String typeName = getTypeName(clazz);
				sb.append(typeName);
				sb.append("[\\s]+");
				sb.append("[^\\(\\),]+[\\n\\s]*,[\\n\\s]*");
			}
			sb.delete(sb.length() - 8, sb.length());
		}
		sb.append("\\)[^\\{;]*\\{");
		
		return sb.toString();
	}
	
	private String getTypeName(Class clazz) throws Exception {
		if (clazz == String.class) {
			return "String";
		}
		if (clazz == Integer.class) {
			return "Integer";
		}
		if (clazz == int.class) {
			return "int";
		}
		if (clazz == BigDecimal.class) {
			return "BigDecimal";
		}
		if (clazz == Long.class) {
			return "Long";
		}
		if (clazz == long.class) {
			return "long";
		}
		if (clazz == String[].class) {
			return "String\\s*\\[\\s*\\]";
		}
		if (clazz == int[].class) {
			return "int\\s*\\[\\s*\\]";
		}
		if (clazz == Object[].class) {
			return "(Object\\s*\\[\\s*\\]|T\\s*\\[\\s*\\])";
		}		
		if (clazz == List.class) {
			return "List[^\\s]*";
		}
		if (clazz == byte[].class) {
			return "byte\\s*\\[\\s*\\]";
		}
		if (clazz == Date.class) {
			return "Date";
		}
		if (clazz == Character.class) {
			return "Character";
		}
		if (clazz == char.class) {
			return "char";
		}
		if (clazz == Timestamp.class) {
			return "Timestamp";
		}
		if (clazz == Collection.class) {
			return "Collection[^\\s]*";
		}
		if (clazz == Map.class) {
			return "Map[^\\s]*";
		}
		if (clazz == OutputStream.class) {
			return "OutputStream";
		}
		if (clazz == HSSFRow.class) {
			return "HSSFRow";
		}
		if (clazz == Short.class) {
			return "Short";
		}
		if (clazz == short.class) {
			return "short";
		}
		if (clazz == Boolean.class) {
			return "Boolean";
		}
		if (clazz == boolean.class) {
			return "boolean";
		}
		if (clazz == Object.class) {
			return "(Object|T|String)";
		}
		
		String name = clazz.getName();
		int dotIndex = name.lastIndexOf(".");
		if (dotIndex > 0) {
			name = name.substring(dotIndex + 1);
		}
		
		return name;
	}
	
	/**
	 * 计算方法是从第几行到第几行
	 * @param content 全文
	 * @throws Exception 
	 */
	public void calRegion(String content) throws Exception{
		Bracket b = new Bracket(content, this.getSignRegex());
		this.setBenginIndex(b.getStartIndex());
		this.setEndIndex(b.getEndIndex());
		
		String mContent = content.substring(this.getBenginIndex(),this.getEndIndex() + 1);
		Pattern p = Pattern.compile("return\\s+[^;]+;");
		Matcher m = p.matcher(mContent);
		List<String> returnList = new ArrayList<>();
		while(m.find()){
			returnList.add(m.group());
		}
		if (returnList.size() > 0) {
			this.returnIndex = new ArrayList<>();
			for(String s : returnList){
				int index = this.getBenginIndex() + mContent.indexOf(s) + 1;
				this.returnIndex.add(index);
			}
		}
	}
	
	public boolean isAbstract(){
		return hasModifiers(10);
	}
	
	/**
	 * 是否有该标识符
	 * @param num 第几位，从右开始，第一位为0
	 * @return
	 */
	private boolean hasModifiers(int num) {
		int m = this.getModifiers() >> num;
		if (m % 2 == 1) {
			return true;
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		JavaMethod m = new JavaMethod();
//		m.setModifiers(1025);
//		System.out.println(m.isAbstract());
		
		System.out.println(m);
	}
	
}
