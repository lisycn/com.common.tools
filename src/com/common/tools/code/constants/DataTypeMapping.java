package com.common.tools.code.constants;

public interface DataTypeMapping {
	/**列名*/
	String getColType();
	/**java类型*/
	String getJavaType();
	/**java类型带包名,如:java.lang.String*/
	String getJavaTypeFull();
}
