package com.common.tools.code.simpleadms.constants;

/**
 * 查询方式
 * @author lx
 *
 */
public enum QueryEnum {
	/**查询方式:不是查询条件*/
	NON("non","不是查询条件"),
	/**查询方式:相等*/
	EQUALS("equals","相等"),
	/**查询方式:模糊查询*/
	LIKE("like","模糊查询"),
	/**查询方式:在当前日期之前*/
	BEFORE("before","在当前日期之前"),
	/**查询方式:在当前日期之后*/
	AFTER("after","在当前日期之后"),
	/**查询方式:范围查找*/
	RANGE("range","范围查找");
	
	private String value;
	private String text;
	
	private QueryEnum(String value,String text){
		this.value = value;
		this.text = text;
	}

	public String getValue() {
		return value;
	}

	public String getText() {
		return text;
	}
	
	/**
	 * 检查方式是否合法
	 * @param value
	 */
	public static void checkQuery(String value){
		for(QueryEnum q : QueryEnum.values()){
			if (q.getValue().equals(value)) {
				return;
			}
		}
		
		throw new RuntimeException("不正确的查询方式:" + value);
	}
	
	/**
	 * date类型独有的查询方式
	 * @return
	 */
	public static QueryEnum[] getDateOnlyQuery(){
		return new QueryEnum[]{BEFORE,AFTER};
	}
	
	/**
	 * 是否date类型独有的查询方式
	 * @param value 查询方式值
	 * @return
	 */
	public static boolean isDateOnlyQuery(String value){
		for(QueryEnum q : getDateOnlyQuery()){
			if (q.getValue().equals(value)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return this.getValue();
	}
}
