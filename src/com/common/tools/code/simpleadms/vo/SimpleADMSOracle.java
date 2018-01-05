package com.common.tools.code.simpleadms.vo;

import com.common.tools.code.constants.DataTypeMapping;
import com.common.tools.code.constants.DataTypeMappingOracle;
import com.common.tools.code.vo.ColumnVo;

public class SimpleADMSOracle extends SimpleADMSDB{
	@Override
	protected String getSqlSelectCol(String tableName) throws Exception {
		tableName = tableName.toUpperCase();
		StringBuffer sb = new StringBuffer();
		sb.append("select t.column_name colName,t.data_type colType,t.data_length length_1,t.data_precision pre,t.data_scale scale,c.comments comment_1");
		sb.append(" from user_tab_columns t");
		sb.append(" left join user_col_comments c on (t.column_name = c.column_name and t.table_name = c.table_name) ");
		//sb.append(" where t.table_name = 'FMP_INTERBANK_DEPOSIT_DRAW'");
		sb.append(" where t.table_name = '"+tableName+"'");
		sb.append(" order by t.column_id");

		return sb.toString();
	}

	@Override
	protected DataTypeMapping getDataTypeMapping(ColumnVo col) {
		DataTypeMapping dtm = DataTypeMappingOracle.getByColType(col);
		if (dtm == null) {
			throw new RuntimeException("不支付的数据库类型:" + col.getColType());
		}
		
		return dtm;
	}
	
	public static void main(String[] args) throws Exception {
		SimpleADMSOracle o = new SimpleADMSOracle();
		String s = o.getSqlSelectCol("fmp_nons_asset_effect");
		System.out.println(s);
	}
}
