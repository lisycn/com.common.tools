package com.common.tools.code.vo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;

import com.common.tools.code.utils.StringUtils;

/**
 * 表 
 * @author lx
 */
public class TableVo {
	private static Logger logger = LoggerFactory.getLogger(TableVo.class);
	
	private String tableName;
	private String pojoName;
	private String schema;
	private List<ColumnVo> ids;
	private List<ColumnVo> cols;
	private boolean isAll = true;//是否全表生成，如果是，生成全部,与cols相同的部分使用cols中的配置。否则只生成cols中的列
	private boolean dealNull = false;//pojo的get方法是否处理空值，如果是1,则把空值都返回空字符串
	
	public boolean isHasSchema(){
		return !StringUtils.isEmpty(this.getSchema());
	}
	
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public boolean isDealNull() {
		return dealNull;
	}
	public void setDealNull(boolean dealNull) {
		this.dealNull = dealNull;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getPojoName() {
		return pojoName;
	}
	public void setPojoName(String pojoName) {
		this.pojoName = pojoName;
	}
	public List<ColumnVo> getCols() {
		return cols;
	}
	public void setCols(List<ColumnVo> cols) {
		this.cols = cols;
	}
	public List<ColumnVo> getIds() {
		return ids;
	}
	public void setIds(List<ColumnVo> ids) {
		this.ids = ids;
	}
	public boolean isAll() {
		return isAll;
	}
	public void setAll(boolean isAll) {
		this.isAll = isAll;
	}
	/**
	 * pojo作为变量时的变量名
	 * @return
	 */
	public String getPojoNameVar(){
		return StringUtils.firstCharLowerCase(getPojoName());
	}
	
	/**
	 * 字段包含的所有类型
	 * @return
	 */
	public List<String> getAllFiledTypeFull(){
		List<String> list = new ArrayList<>();
		if (this.getCols() == null) {
			return list;
		}
		
		for(ColumnVo col : this.getCols()){
			String type = col.getFieldTypeFull();
			if (list.contains(type)) {
				continue;
			}
			list.add(type);
		}
		
		return list;
	}
	
	/**
	 * 设置列是否为id的属性
	 */
	public void calcIdColumn(){
		if (this.getCols() == null) {
			return;
		}
		if (this.getIds() == null) {
			throw new RuntimeException("必需设置id列");
		}
		for(ColumnVo col : this.getCols()){
			for(ColumnVo colId : this.getIds()){
				if (colId.getColName() == null) {
					continue;
				}
				
				if (colId.getColName().toLowerCase().equals(col.getColName().toLowerCase())) {
					col.setInids(true);
				}
			}
		}
	}
	
	/**
	 * 根据字段名称获取字段
	 * @param colName
	 * @return
	 */
	public ColumnVo getCol(String colName){
		if (this.getCols() == null || this.getCols().size() == 0 || colName == null) {
			return null;
		}
		
		for(ColumnVo col : this.getCols()){
			if(colName.equalsIgnoreCase(col.getColName())){
				return col;
			}
		}
		
		return null;
	}
	
	/**
	 * 获取可编辑的列
	 * @return
	 */
	public List<ColumnVo> getEditableCols(){
		if (this.getCols() == null || this.getCols().size() == 0) {
			return new ArrayList<>();
		}
		List<ColumnVo> list = new ArrayList<>();
		for(ColumnVo vo : this.getCols()){
			if (vo.isInids() || !vo.isModify()) {
				continue;
			}
			list.add(vo);
		}
		
		return list;
	}
	
	public List<ColumnVo> getQueryCols(){
		List<ColumnVo> r = new ArrayList<>();
		if (this.getCols() == null || this.getCols().size() == 0) {
			return r;
		}
		for(ColumnVo col : this.getCols()){
			if (col.isQueryCol()) {
				r.add(col);
			}
		}
		
		return r;
	}
	
	public static void main(String[] args) {
		File file = new File("D:/app/cts/com.indicator.web/src/com/dplatform/code/simpleadms/config/tables/fmp_interbank_deposit.xml");
		
		System.out.println(file.getName());
	}
	
}
