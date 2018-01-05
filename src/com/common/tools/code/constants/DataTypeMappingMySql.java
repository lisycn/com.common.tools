package com.common.tools.code.constants;

import com.common.tools.code.vo.ColumnVo;

public enum DataTypeMappingMySql implements DataTypeMapping{
	T_VARCHAR("varchar","String","java.lang.String"),
	T_BIGINT("bigint","Integer","java.lang.Integer"),
	T_BLOB("blob","String","java.lang.String"),
	T_CHAR("char","String","java.lang.String"),
	T_DECIMAL("decimal","BigDecimal","java.math.BigDecimal"),
	T_DOUBLE("double","BigDecimal","java.math.BigDecimal"),
	T_FLOAT("float","BigDecimal","java.math.BigDecimal"),
	T_INT("int","Integer","java.lang.Integer"),
	T_MEDIUMINT("mediumint","Integer","java.lang.Integer"),
	T_MEDIUMTEXT("mediumtext","String","java.lang.String"),
	T_SMALLINT("smallint","Integer","java.lang.Integer"),
	T_TEXT("text","String","java.lang.String"),
	T_TINYBLOB("tinyblob","String","java.lang.String"),
	T_TINYINT("tinyint","Integer","java.lang.Integer"),
	T_TINYTEXT("tinytext","String","java.lang.String");
	
	private DataTypeMappingMySql(String colType,String javaType,String javaTypeFull){
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
	
	public static DataTypeMappingMySql getByColType(ColumnVo col){
		if (col == null || col.getColType() == null) {
			return null;
		}
		
		for(DataTypeMappingMySql item : DataTypeMappingMySql.values()){
			if (item.getColType().equals(col.getColType().toLowerCase())) {
				return item;
			}
		}
		
		return null;
	}
	
}
