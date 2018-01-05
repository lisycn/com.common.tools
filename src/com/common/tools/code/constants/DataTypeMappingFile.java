package com.common.tools.code.constants;

import com.common.tools.code.vo.ColumnVo;

public enum DataTypeMappingFile implements DataTypeMapping{
	T_STRING("String","String","java.lang.String"),
	T_BIGDECIMAL("BigDecimal","BigDecimal","java.math.BigDecimal"),
	T_LONG("Long","Long","java.lang.Long");
	
	private DataTypeMappingFile(String colType,String javaType,String javaTypeFull){
		this.colType = colType;
		this.javaType = javaType;
		this.javaTypeFull = javaTypeFull;
	}
	private String colType;
	private String javaType;
	private String javaTypeFull;
	@Override
	public String getColType() {
		return colType;
	}
	@Override
	public String getJavaType() {
		return javaType;
	}
	@Override
	public String getJavaTypeFull() {
		return javaTypeFull;
	}
	
	public static DataTypeMappingFile getByColType(ColumnVo col){
		if (col == null || col.getColType() == null) {
			return null;
		}	
		
		for(DataTypeMappingFile item : DataTypeMappingFile.values()){
			if (item.getColType().toLowerCase().equals(col.getColType().toLowerCase())) {
				return item;
			}
		}
		
		return null;
	}
	
}
