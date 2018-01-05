package com.common.tools.code.simpleadms.vo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.common.tools.code.vo.ColumnVo;
import com.common.tools.code.vo.TableVo;

public abstract class SimpleADMSDB extends SimpleADMS{
	
	/**
	 * 查询表的列信息的sql
	 * 需要查询出以下列<br />
	 * colName,colType,length,pre,scale,comment<br />
	 * 即：列名,数据类型,长度,数字精度整数部分,数字精度小数部分,备注
	 * @return
	 * @throws IOException 
	 */
	protected abstract String getSqlSelectCol(String tableName) throws Exception;
	
	/**
	 * 获取表的列
	 * @param tableName 表名
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	@Override
	public List<ColumnVo> getColumns(TableVo table) throws Exception,SQLException {
		String sql = getSqlSelectCol(table.getTableName());
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ColumnVo> cols = new ArrayList<>();
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql);
			rs = getRs(con, pstmt, sql, null);
			while(rs.next()){
				String colName = rs.getString("colName");
				ColumnVo vo = table.getCol(colName);
				if (vo == null) {
					vo = new ColumnVo();
				}
				vo.setColName(colName);
				vo.setColType(rs.getString("colType"));
				vo.setLength(rs.getInt("length_1"));
				vo.setPrecision(rs.getInt("pre"));
				vo.setScale(rs.getInt("scale"));
				vo.setComment(rs.getString("comment_1"));
				
				cols.add(vo);
			}		
		} catch (Exception e) {
			throw e;
		} finally{
			close(con, pstmt, rs);
		}
		
		calc(cols);
		
		return cols;
	}

}
