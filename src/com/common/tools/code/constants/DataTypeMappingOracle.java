package com.common.tools.code.constants;


import com.common.tools.code.vo.ColumnVo;


public enum DataTypeMappingOracle implements DataTypeMapping{
//	T_VARCHAR2("VARCHAR2","String","java.lang.String"),
//	T_DECIMAL("NUMBER","String","java.lang.String"),
//	T_DATE("DATE","String","java.lang.String"),
//	T_LONG("NUMBER","String","java.lang.String"),
//	T_CHAR("CHAR","String","java.lang.String"),
//	T_TIMESTAMP("TIMESTAMP(6)","String","java.lang.String"),
//	T_CLOB("CLOB","String","java.lang.String");
	
	T_VARCHAR2("VARCHAR2","String","java.lang.String"),
	T_DECIMAL("NUMBER","BigDecimal","java.math.BigDecimal"),
	T_DATE("DATE","Date","java.util.Date"),
	T_LONG("NUMBER","Long","java.lang.Long"),
	T_INTEGER("NUMBER","Integer","java.lang.Integer"),
	T_CHAR("CHAR","String","java.lang.String"),
	T_TIMESTAMP("TIMESTAMP(6)","String","java.util.Date"),
	T_CLOB("CLOB","String","java.lang.String");
	
	private DataTypeMappingOracle(String colType,String javaType,String javaTypeFull){
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
	
	public static DataTypeMappingOracle getByColType(ColumnVo col){
		if (col == null || col.getColType() == null) {
			return null;
		}	
		
		if (T_LONG.getColType().toLowerCase().equals(col.getColType().toLowerCase())) {
			if (col.getScale() == 0) {
				if (col.getPrecision() <= 6) {
					return T_INTEGER;
				}else{
					return T_LONG;					
				}
			}else{
				return T_DECIMAL;
			}
		}
		
		for(DataTypeMappingOracle item : DataTypeMappingOracle.values()){
			if (item.getColType().toLowerCase().equals(col.getColType().toLowerCase())) {
				return item;
			}
		}
		
		return null;
	}
	
}
