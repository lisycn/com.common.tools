package com.common.tools.code.simpleadms.vo;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.common.tools.code.constants.DataTypeMapping;
import com.common.tools.code.constants.DataTypeMappingFile;
import com.common.tools.code.utils.IOUtils;
import com.common.tools.code.vo.ColumnVo;
import com.common.tools.code.vo.TableVo;


public class SimpleADMSTxtFile extends SimpleADMSFile{
	@Override
	protected List<ColumnVo> getColumns(TableVo table) throws Exception,SQLException {

		String basepath = getConfig("file.base.path");
		String pattern = getConfig("file.pattern");
		String pathname = basepath + "/" + table.getTableName() + ".txt";
		File f = new File(pathname);
		List<String> list = IOUtils.readLine(f, "UTF-8");
		List<ColumnVo> cols = new ArrayList<>();
		for(String s : list){
			String[] arr = s.trim().split("\\s+",-1);
			String colName = "";
			String colType = "";
			String comment = "";
			
			/*
			 * 1:字段 类型 备注
			 * 2:字段 备注
			 */
			if ("1".equals(pattern)) {
				colName = arr[0];
				colType = (arr.length > 1) ? arr[1] : "String";
				comment = (arr.length > 2) ? arr[2] : "";
			}else if("2".equals(pattern)){
				colName = arr[0];
				comment = (arr.length > 1) ? arr[1] : "";
				
				colType = "String";
			}else{
				throw new RuntimeException("file.pattern配置不正确：" + pattern);
			}
			
			ColumnVo vo = table.getCol(colName);
			if (vo == null) {
				vo = new ColumnVo();
			}
			vo.setColName(colName);
			vo.setColType(colType);
			vo.setLength(0);
			vo.setPrecision(0);
			vo.setScale(0);
			vo.setComment(comment);
			
			cols.add(vo);
		}
		
		
		calc(cols);
		
		return cols;
	}

	@Override
	protected DataTypeMapping getDataTypeMapping(ColumnVo col) {
		DataTypeMapping dtm = DataTypeMappingFile.getByColType(col);
		if (dtm == null) {
			throw new RuntimeException("不支付的数据库类型:" + col.getColType());
		}
		
		return dtm;
	}
	
}
