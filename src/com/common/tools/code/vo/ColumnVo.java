package com.common.tools.code.vo;

import com.common.tools.code.simpleadms.constants.QueryEnum;
import com.common.tools.code.utils.StringUtils;


/**
 * 字段
 * @author lx
 */
public class ColumnVo {	
	/**配置文件中节点名称:是否进行非空验证*/
	public static final String NODE_NAME_CHECK_EMPTY = "checkEmpty";
	/**配置文件中节点名称:是否进行正则验证(js)*/
	public static final String NODE_NAME_REGEX_JS = "regexJs";
	/**配置文件中节点名称:是否进行正则验证(java)*/
	public static final String NODE_NAME_REGEX_JAVA = "regexJava";
	/**配置文件中节点名称:是否日期类型*/
	public static final String NODE_NAME_DATE = "date";
	/**配置文件中节点名称:是否可以修改*/
	public static final String NODE_NAME_MODIFY = "modify";
	/**配置文件中节点名称:查询方式*/
	public static final String NODE_NAME_QUERY = "query";
	
	private String colName;
	private String colType;
	private String fieldName;
	private String fieldType;
	private String fieldTypeFull;//属性类型全路径，如java.lang.String
	private int precision;//精度,整数位
	private int scale;//精度小数位
	private int length;//长度
	private String comment;//备注

	private boolean inids;//是否为主键字段
	private boolean conifgCol;//是否配置过的列,如果table的全部生成项为false,则只生成配置过的列
	
	private boolean checkEmpty;//是否进行非空验证
	private String regexJs;//不为空时进行js格式验证
	private String regexJava;//不为空时进行java格式验证
	private boolean date;//是否日期
	private boolean modify = true;//是否可修改,默认可修改
	private String query = QueryEnum.NON.getValue();//查询方式，默认不为作为查询条件
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public boolean isModify() {
		return modify;
	}
	public void setModify(boolean modify) {
		this.modify = modify;
	}
	public boolean isConifgCol() {
		return conifgCol;
	}
	public void setConifgCol(boolean conifgCol) {
		this.conifgCol = conifgCol;
	}
	public boolean isInids() {
		return inids;
	}
	public void setInids(boolean inids) {
		this.inids = inids;
	}
	public boolean isCheckEmpty() {
		return checkEmpty;
	}
	public void setCheckEmpty(boolean checkEmpty) {
		this.checkEmpty = checkEmpty;
	}
	public String getRegexJs() {
		return regexJs;
	}
	public void setRegexJs(String regexJs) {
		this.regexJs = regexJs;
	}
	public String getRegexJava() {
		return regexJava;
	}
	public void setRegexJava(String regexJava) {
		this.regexJava = regexJava;
	}
	public boolean isDate() {
		return date;
	}
	public void setDate(boolean date) {
		this.date = date;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getFieldTypeFull() {
		return fieldTypeFull;
	}
	public void setFieldTypeFull(String fieldTypeFull) {
		this.fieldTypeFull = fieldTypeFull;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public String getColType() {
		return colType;
	}
	public void setColType(String colType) {
		this.colType = colType;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	public int getScale() {
		return scale;
	}
	public void setScale(int scale) {
		this.scale = scale;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	
	public String getGetFuntionName(){
		if (this.getFieldName() == null || this.getFieldName().length() == 0) {
			return "";
		}
		String r = "get";
		
		return r + StringUtils.firstCharUpCase(this.getFieldName());
		
	}
	
	public String getSetFuntionName(){
		if (this.getFieldName() == null || this.getFieldName().length() == 0) {
			return "";
		}
		String r = "set";
		
		return r + StringUtils.firstCharUpCase(this.getFieldName());
	}
	
	/**
	 * 是否是查询字段
	 * @return
	 */
	public boolean isQueryCol(){
		if (QueryEnum.NON.getValue().equals(this.getQuery())) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 是否范围查询
	 * @return
	 */
	public boolean isQueryRange(){
		return QueryEnum.RANGE.getValue().equals(this.getQuery());
	}
	
	/**
	 * 检查查询方式是否合法
	 */
	public void checkQueryCol(){
		QueryEnum.checkQuery(this.getQuery());
		
		if (QueryEnum.isDateOnlyQuery(this.getQuery())) {
			if (!this.isDate()) {
				throw new RuntimeException("查询方式[" + this.getQuery() + "]只能在date为true时使用");
			}
		}
	}
	/**
	 * 是否进行js正则验证
	 * @return
	 */
	public boolean isCheckRegexJs(){
		return !StringUtils.isEmpty(this.getRegexJs());
	}
	/**
	 * 是否进行java正则验证
	 * @return
	 */
	public boolean isCheckRegexJava(){
		return !StringUtils.isEmpty(this.getRegexJava());
	}
	
	/**
	 * 是否进行js验证
	 * @return
	 */
	public boolean isNeedCheckJs(){
		return isCheckEmpty() || isCheckRegexJs();
	}
}
