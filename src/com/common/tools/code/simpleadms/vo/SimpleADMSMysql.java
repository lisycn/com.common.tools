package com.common.tools.code.simpleadms.vo;

import java.io.IOException;

import com.common.tools.code.constants.DataTypeMapping;
import com.common.tools.code.constants.DataTypeMappingMySql;
import com.common.tools.code.vo.ColumnVo;

public class SimpleADMSMysql extends SimpleADMSDB{
	@Override
	protected String getSqlSelectCol(String tableName) throws IOException {
		String dbName = getConfig("db.name");
		
		String sql = "select COLUMN_NAME colName,DATA_TYPE colType,CHARACTER_MAXIMUM_LENGTH length_1,NUMERIC_PRECISION pre,NUMERIC_SCALE scale,COLUMN_COMMENT comment_1 from information_schema.COLUMNS where TABLE_NAME = '" + tableName + "' and TABLE_SCHEMA = '" + dbName + "'";
		
		return sql;
	}

	@Override
	protected DataTypeMapping getDataTypeMapping(ColumnVo col) {
		DataTypeMapping dtm = DataTypeMappingMySql.getByColType(col);
		if (dtm == null) {
			throw new RuntimeException("不支付的数据库类型:" + col);
		}
		
		return dtm;
	}

}
