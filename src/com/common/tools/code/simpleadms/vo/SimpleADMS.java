package com.common.tools.code.simpleadms.vo;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.common.tools.code.constants.DataTypeMapping;
import com.common.tools.code.utils.CodeUtils;
import com.common.tools.code.utils.StringUtils;
import com.common.tools.code.utils.UUIDUtils;
import com.common.tools.code.vo.ColumnVo;
import com.common.tools.code.vo.TableVo;

/**
 * 单表增删改查
 * 1,建表,注意列名需要加上注释
 * 2,配置adms.properties中的table.list=表名1,表名2,表名3
 * 3,添加 表名.xml 配置文件，如果不加则使用默认设置<br/>
 * 	  默认设置见ColumnVo中的字段默认值<br/>
 * 	table.xml 的各配置见demo_table.xml
 * @author lx
 */
public abstract class SimpleADMS {
	private static Logger logger = LoggerFactory.getLogger(SimpleADMS.class);
	
	/**配置文件,从classpath开始*/
	public static final String CONFIG_FILE = "com/common/tools/code/simpleadms/config/adms.properties";
	/**表配置文件目录,从classpath开始*/
	public static final String TABLE_CONFIG_FILE_DIR = "com/common/tools/code/simpleadms/config/tables";
	
	/**  要生成的表配置来源： table.xml */
	public static final String DATA_TYPE_XML = "xml";
	/**  要生成的表配置来源： 参数传入 */
	public static final String DATA_TYPE_PARAM = "param";
	/**默认主键列名*/
	public static final String DEFAULT_ID_COL = "id";
	
	public static SimpleADMS getInstance() throws Exception{
		String dbtype = getConfig("db.type");
		SimpleADMS s;
		if ("mysql".equals(dbtype)) {
			s = new SimpleADMSMysql();
		}else if ("oracle".equals(dbtype)){
			s = new SimpleADMSOracle();
		}else if ("txt".equals(dbtype)){
			s = new SimpleADMSTxtFile();
		}else{
			throw new RuntimeException("不支付的数据库类型:" + dbtype);
		}
		
		s.initTableConfig();
		return s;
	}

	private String encoding = "utf-8";
	private static Properties config = null;
	private GroupTemplate gt;
//	private boolean cutTablePrefix = true;//实体是否去掉表前缀,为true会去掉第一个'_'之前的部分
//	private boolean cutColumnPrefix = true;//实体是否去掉字段前缀，为true会去掉第一个'_'之前的部分
	/**要生成的表*/
	private List<TableVo> tableConfigList;
	
	public boolean isCutColumnPrefix() {
		try {
			return Boolean.parseBoolean(getConfig("mod.cutColumnPrefix"));
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
		return true;
	}

//	public void setCutColumnPrefix(boolean cutColumnPrefix) {
//		this.cutColumnPrefix = cutColumnPrefix;
//	}
	
	public boolean isCutTablePrefix() {
		try {
			return Boolean.parseBoolean(getConfig("mod.cutTablePrefix"));
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
		return true;
	}

//	public void setCutTablePrefix(boolean cutTablePrefix) {
//		this.cutTablePrefix = cutTablePrefix;
//	}

	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	public List<TableVo> getTableConfigList() {
		return tableConfigList;
	}

	public void setTableConfigList(List<TableVo> tableConfigList) {
		this.tableConfigList = tableConfigList;
	}

	public static void initConfig() throws IOException{		
		Properties p = new Properties();
		InputStream is = SimpleADMS.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
		if (is == null) {
			throw new RuntimeException("未找到配置文件:" + CONFIG_FILE);
		}
		InputStreamReader reader = new InputStreamReader(is, "UTF-8");
		
		p.load(reader);
		
		config = p;
	}
	
	public static String getConfig(String key) throws IOException{
		if (config == null) {
			initConfig();
		}
		
		String v = config.getProperty(key);
		if (v == null) {
			throw new RuntimeException("key not exists:" + key);
		}
		return v;
	}
	
	/**
	 * 查询所有表对象
	 * @return
	 * @throws Exception 
	 */
	private List<TableVo> getTableList() throws Exception {
		if (this.getTableConfigList() == null || this.getTableConfigList().size() == 0) {
			throw new RuntimeException("请设置要生成的表名");
		}
		
		for(TableVo tableConfig : this.getTableConfigList()){
			String pojoName = "";
			String tableName = tableConfig.getTableName();
			if (StringUtils.isEmpty(tableName)) {
				throw new RuntimeException("tableName can not bu empty!");
			}
			if (isCutTablePrefix()) {
				int index = tableName.indexOf("_");
				pojoName = CodeUtils.toPacical(tableName.substring(index + 1));
			}else{
				pojoName = CodeUtils.toPacical(tableName);
			}
			tableConfig.setPojoName(pojoName);
			tableConfig.setCols(getColumns(tableConfig)) ;
			tableConfig.calcIdColumn();
		}
		return this.getTableConfigList();
	}

	/**
	 * 获取表的列
	 * @param tableName 表名
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	protected abstract List<ColumnVo> getColumns(TableVo table) throws Exception,SQLException;
	
	/**
	 * 根据数据库查询出的字段信息，计息fieldName,fieldType等信息
	 * 增加实体是否去掉字段前缀
	 */
	public void calc(List<ColumnVo> cols){
		for(ColumnVo col : cols){
			String colName = col.getColName();
			if (isCutColumnPrefix()) {
				int index = col.getColName().indexOf("_");
				colName = col.getColName().substring(index + 1);
			}
			
			col.setFieldName(CodeUtils.toCamel(colName));
			DataTypeMapping mapping = getDataTypeMapping(col);
			
			col.setFieldType(mapping.getJavaType());
			col.setFieldTypeFull(mapping.getJavaTypeFull());
		}
	}
	
	/**
	 * 根据字段获得java数据类型
	 * @param colType
	 * @return
	 */
	protected abstract DataTypeMapping getDataTypeMapping(ColumnVo col);
	
	/**
	 * 生成dao
	 * @param tableVo
	 * @throws IOException
	 */
	private void genDao(TableVo tableVo) throws IOException {
		GroupTemplate gt = getGt();
		Template t = gt.getTemplate(getConfig("template.dao"));

		String packageBase = getConfig("package.base");
		String packageDao = getConfig("package.dao"); 
		String packagePojo = getConfig("package.pojo"); 
		
		logger.info("packageBase:" + packageBase + ",packageDao:" + packagePojo);
		t.binding("packageBase",packageBase);
		t.binding("tableVo", tableVo);
		t.binding("packageDao",packageDao);
		t.binding("packagePojo",packagePojo);

		File distFile = FileUtils.getFile(getConfig("dist.src") + CodeUtils.getPackagePath(packageDao) + "/" + tableVo.getPojoName() + "Dao.java");
		FileUtils.write(distFile, t.render(),getEncoding());
	}

	/**
	 * 生成daoImpl
	 * @param tableVo
	 * @throws IOException
	 */
	private void genDaoImpl(TableVo tableVo) throws IOException {
		GroupTemplate gt = getGt();
		Template t = gt.getTemplate(getConfig("template.daoImpl"));
		

		String packageBase = getConfig("package.base");
		String packageDao = getConfig("package.dao"); 
		String packagePojo = getConfig("package.pojo"); 
		
		t.binding("packageBase",packageBase);
		t.binding("tableVo", tableVo);
		t.binding("packageDao",packageDao);
		t.binding("packagePojo",packagePojo);

		File distFile = FileUtils.getFile(getConfig("dist.src") + CodeUtils.getPackagePath(packageDao) + "/impl/" + tableVo.getPojoName() + "DaoImpl.java");
		FileUtils.write(distFile, t.render(),getEncoding());
	}

	/**
	 * 生成service
	 * @param tableVo
	 * @throws IOException
	 */
	private void genService(TableVo tableVo) throws IOException {
		GroupTemplate gt = getGt();
		Template t = gt.getTemplate(getConfig("template.service"));
		

		String packageBase = getConfig("package.base");
		String packageService = getConfig("package.service"); 
		String packagePojo = getConfig("package.pojo"); 
		
		t.binding("packageBase",packageBase);
		t.binding("tableVo", tableVo);
		t.binding("packageService",packageService);
		t.binding("packagePojo",packagePojo);

		File distFile = FileUtils.getFile(getConfig("dist.src") + CodeUtils.getPackagePath(packageService) + "/" + tableVo.getPojoName() + "Service.java");
		FileUtils.write(distFile, t.render(),getEncoding());
	}

	/**
	 * 生成service
	 * @param tableVo
	 * @throws IOException
	 */
	private void genServiceImpl(TableVo tableVo) throws IOException {
		GroupTemplate gt = getGt();
		Template t = gt.getTemplate(getConfig("template.serviceImpl"));
		

		String packageBase = getConfig("package.base");
		String packageDao = getConfig("package.dao"); 
		String packageService = getConfig("package.service"); 
		String packagePojo = getConfig("package.pojo"); 
		
		t.binding("packageBase",packageBase);
		t.binding("tableVo", tableVo);
		t.binding("packageDao",packageDao);
		t.binding("packageService",packageService);
		t.binding("packagePojo",packagePojo);

		File distFile = FileUtils.getFile(getConfig("dist.src") + CodeUtils.getPackagePath(packageService) + "/impl/" + tableVo.getPojoName() + "ServiceImpl.java");
		FileUtils.write(distFile, t.render(),getEncoding());
	}

	/**
	 * 生成controller
	 * @param tableVo
	 * @throws IOException
	 */
	private void genController(TableVo tableVo) throws IOException {
		GroupTemplate gt = getGt();
		Template t = gt.getTemplate(getConfig("template.controller"));
		

		String packageBase = getConfig("package.base");
		String packageController = getConfig("package.controller"); 
		String packageService = getConfig("package.service"); 
		String packagePojo = getConfig("package.pojo");
		
		t.binding("packageBase",packageBase); 
		t.binding("tableVo", tableVo);
		t.binding("packageController",packageController);
		t.binding("packageService",packageService);
		t.binding("packagePojo",packagePojo);
		t.binding("jspFold",getConfig("dist.jsp.fold"));

		File distFile = FileUtils.getFile(getConfig("dist.src") + CodeUtils.getPackagePath(packageController) + "/" + tableVo.getPojoName() + "Controller.java");
		FileUtils.write(distFile, t.render(),getEncoding());
	}
	
	
	
	private void genPojo(TableVo tableVo) throws IOException{
		GroupTemplate gt = getGt();
		Template t = gt.getTemplate(getConfig("template.pojo"));
		
		String packageBase = getConfig("package.base");
		String packagePojo = getConfig("package.pojo");
		
		t.binding("packageBase",packageBase);		
		t.binding("tableVo", tableVo);
		t.binding("packagePojo",packagePojo);

		File distFile = FileUtils.getFile(getConfig("dist.src") + CodeUtils.getPackagePath(packagePojo) + "/" + tableVo.getPojoName() + ".java");
		FileUtils.write(distFile, t.render(),getEncoding());
	}
	
	private void genListJsp(TableVo tableVo) throws Exception {
		GroupTemplate gt = getGt();
		Template t = gt.getTemplate(getConfig("template.listJsp"));

		String packageBase = getConfig("package.base");
		
		t.binding("packageBase",packageBase);
		t.binding("tableVo", tableVo);
		t.binding("jsFold",getConfig("dist.js.fold"));
		t.binding("jspFold",getConfig("dist.jsp.fold"));

		File distFile = FileUtils.getFile(getJspFold() + "/" + tableVo.getPojoNameVar() + "/" + tableVo.getPojoNameVar() + "List.jsp");
		FileUtils.write(distFile, t.render(),getEncoding());
	}
	
	private void genListJs(TableVo tableVo) throws Exception {
		GroupTemplate gt = getGt();
		Template t = gt.getTemplate(getConfig("template.listJs"));

		String packageBase = getConfig("package.base");
		
		t.binding("packageBase",packageBase);
		t.binding("tableVo", tableVo);

		File distFile = FileUtils.getFile(getJsFold() + "/" + tableVo.getPojoNameVar() + "/" + tableVo.getPojoNameVar() + "List.js");
		FileUtils.write(distFile, t.render(),getEncoding());
	}
	
	private void genEditJsp(TableVo tableVo) throws Exception {
		GroupTemplate gt = getGt();
		Template t = gt.getTemplate(getConfig("template.editJsp"));

		String packageBase = getConfig("package.base");
		
		t.binding("packageBase",packageBase);
		t.binding("tableVo", tableVo);
		t.binding("jsFold",getConfig("dist.js.fold"));
		t.binding("jspFold",getConfig("dist.jsp.fold"));

		File distFile = FileUtils.getFile(getJspFold() + "/" + tableVo.getPojoNameVar() + "/" + tableVo.getPojoNameVar() + "Edit.jsp");
		FileUtils.write(distFile, t.render(),getEncoding());
	}
	
	private void genEditJs(TableVo tableVo) throws Exception {
		GroupTemplate gt = getGt();
		Template t = gt.getTemplate(getConfig("template.editJs"));

		String packageBase = getConfig("package.base");
		
		t.binding("packageBase",packageBase);
		t.binding("tableVo", tableVo);

		File distFile = FileUtils.getFile(getJsFold() + "/" + tableVo.getPojoNameVar() + "/" + tableVo.getPojoNameVar() + "Edit.js");
		FileUtils.write(distFile, t.render(),getEncoding());
	}
	
	/**
	 * 创建groupTemplate
	 * @return
	 * @throws IOException
	 */
	private GroupTemplate getGt() throws IOException {
		if (this.gt != null) {
			return this.gt;
		}
		
		ClasspathResourceLoader loader = new ClasspathResourceLoader();
		Configuration config = Configuration.defaultConfiguration();
		this.gt = new GroupTemplate(loader,config);
		return this.gt;
	}
    
    public Connection getConnection() throws Exception {
        String className = getConfig("jdbc.driver");
        String url = getConfig("jdbc.url");
        String userName = getConfig("jdbc.username");
        String passwd = getConfig("jdbc.password");
        Connection conn = null;
        try {
            Class.forName(className);
            conn = DriverManager.getConnection(url, userName, passwd);
        } catch (ClassNotFoundException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        }
        return conn;
    }

    
    /**
     * <p><strong>Description:</strong>获取结果集resultSet,使用完后需要自己关闭resultSet,pstmt,con</p>
     * @param connection 连接对象
     * @param pstmt 操作对象,不需要初始化，只是用于关闭
     * @param sql 用于查询的sql语句
     * @param prmtrs 参数集合
     * @return
     * @throws Exception 
     * @update 日期: 2014-1-14
     */
    public static ResultSet getRs(Connection con,PreparedStatement pstmt,String sql,Object[] prmtrs) throws Exception{
        ResultSet rs = null;
        logger.info("sql:" + sql + ",\nprmtrs:{" + StringUtils.join(prmtrs,",") + "}");
        pstmt = con.prepareStatement(sql);
        if (prmtrs != null && prmtrs.length > 0) {
            setPrmtrs(prmtrs, pstmt);
        }
        rs = pstmt.executeQuery();
        return rs;
    }

    /**
     * <p><strong>Description:</strong>设置参数列表到pstmt</p>
     * @param prmtrs
     * @param pstmt
     * @throws Exception 
     * @update 日期: 2014-1-14
     */
    private static void setPrmtrs(Object[] prmtrs, PreparedStatement pstmt)
            throws Exception {
        for (int i = 0; i < prmtrs.length; i++) {
            Object o = prmtrs[i];
            if(o == null){
                pstmt.setNull(i + 1, Types.NULL);
            }else if(o instanceof String) {
                pstmt.setString(i + 1, (String)o);
            }else if(o instanceof Integer){
                pstmt.setInt(i + 1, Integer.parseInt(o.toString()) );
            }else if (o instanceof BigDecimal) {
                pstmt.setBigDecimal(i + 1, new BigDecimal(o.toString()));
            }else if (o instanceof Double) {
                pstmt.setDouble(i + 1, new Double(o.toString()));
            }else if (o instanceof Date){
                pstmt.setDate(i + 1, (Date)o);
            }else if(o instanceof Long){
                pstmt.setLong(i + 1, Long.valueOf(o.toString()) );
            }else{
                throw new Exception("无法识别的参数类型");
            }
        }
    }

    /**
     * 关闭数据库连接
     * 
     * @param connection
     * @param stmt
     * @param rs
     * @throws SQLException
     */
    public static void close(Connection connection, Statement stmt, ResultSet rs)
            throws SQLException {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw e;
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                throw e;
            }
        }
        if (connection != null && !connection.isClosed()) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw e;
            }
        }
    }

	private void initTableConfig() throws Exception {
		String dataType = getConfig("data.type");
		//不是从xml读取，则不初始化
		if (DATA_TYPE_XML.equals(dataType)) {
			logger.info("从配置文件获取表配置");
			URL url = SimpleADMS.class.getClassLoader().getResource(SimpleADMS.TABLE_CONFIG_FILE_DIR);
			
			String tableNames = getConfig("table.list");
			String[] tableNameArr = tableNames == null ? null : tableNames.split(",");
			String filePathname = URLDecoder.decode(url.getFile(), "UTF-8");
			setTableConfigList(fromTableConifg(tableNameArr,filePathname));
		}
		
	}



	/**
	 * 获得要生成的表配置
	 * 每个表名对应一个表配置，从表配置文件中读取配置信息，如果配置了表名页没有表配置文件，则使用默认配置
	 * @param tableNames 所有表名
	 * @param xmlDir 表配置文件目录
	 * @return
	 * @throws Exception
	 */
	public static List<TableVo> fromTableConifg(String[] tableNames,String xmlDir) throws Exception{
		if (tableNames == null || tableNames.length == 0) {
			return null;
		}
		Map<String, File> tableFileMap = getTableFileMap(tableNames,xmlDir);
		
		List<TableVo> list = new ArrayList<>();
		for(String tableName : tableNames){
			File tableFile = tableFileMap.get(tableName);
			TableVo t = getTableConfigVo(tableName,tableFile);
			list.add(t);
			logger.info("add one table:" + t.getTableName());
		}		
		
		
		return list;
	}

	/**
	 * 默认不修改的列
	 * @return
	 * @throws Exception 
	 */
	private static String[] getNonModifyCols() throws Exception {
		String str = getConfig("non.modify");
		if (str == null || str.trim().length() == 0) {
			return null;
		}
		
		return str.split(",");
	}

	/**
	 * 获得table对象
	 * @param tableEl table元素
	 * @param tableFile ${table}.xml 文件
	 * @return
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	@SuppressWarnings("unchecked")
	private static TableVo getTableConfigVo(String tableName,File tableFile) throws Exception {
		if (tableFile == null) {
			return getDefaultTable(tableName);
		}

		TableVo t = new TableVo();
		t.setTableName(tableName);
		byte[] b = FileUtils.readFileToByteArray(tableFile);
		Document d = DocumentHelper.parseText(new String(b));
		Element root = d.getRootElement();
		List<Element> list = root.elements();
		for(Element el : list){
			if ("all".equals(el.getName())) {
				t.setAll(Boolean.valueOf(el.getText()));
			}
			if ("ids".equals(el.getName())) {
				List<ColumnVo> cols = getCols(el);
				t.setIds(cols);
			}
			if ("columns".equals(el.getName())) {
				List<ColumnVo> cols = getCols(el);
				t.setCols(cols);
			}
		}		
		
		initIds(t);
		initNonModifyCol(t);
		return t;
	}

	/**
	 * 初始化ids,如果配置没有设置ids,使用默认列
	 * @param t
	 */
	private static void initIds(TableVo t) {
		if (t.getIds() == null || t.getIds().size() == 0) {
			t.setIds(getDefaultIds());
		}
	}

	/**
	 * 没有表配置文件时的默认表配置
	 * @param tableName
	 * @return
	 * @throws Exception 
	 */
	private static TableVo getDefaultTable(String tableName) throws Exception {
		TableVo t = new TableVo();
		t.setTableName(tableName);
		List<ColumnVo> ids = getDefaultIds();
		t.setIds(ids);
		
		initNonModifyCol(t);
		
		return t;
	}

	/**
	 * 设置默认ids列
	 * @return
	 */
	private static List<ColumnVo> getDefaultIds() {
		List<ColumnVo> ids = new ArrayList<>();
		ColumnVo c = new ColumnVo();
		c.setColName(DEFAULT_ID_COL);
		c.setFieldName(CodeUtils.toCamel(c.getColName()));
		
		ids.add(c);
		return ids;
	}

	/**
	 * 初始化不可修改的列
	 * @param t
	 * @throws Exception
	 */
	private static void initNonModifyCol(TableVo t) throws Exception {
		List<ColumnVo> cols = t.getCols();
		if (cols == null) {
			cols = new ArrayList<>();
			t.setCols(cols);
		}

		String[] nonModifyCols = getNonModifyCols();
		if (nonModifyCols == null) {
			return;
		}
		
		for(String nonModifyCol : nonModifyCols){
			ColumnVo old = null;
			for(ColumnVo col : t.getCols()){
				//如果已经配置,则使用配置的信息,否则设为不可修改
				if (nonModifyCol.equals(col.getColName())) {
					old = col;
				}
			}
			//如果没配置，设为不可修改
			if (old == null) {
				old = new ColumnVo();
				old.setColName(nonModifyCol);
				old.setModify(false);
				t.getCols().add(old);
			}
			
		}
	}

	/**
	 * 获得cols
	 * @param colsEl columns元素
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static List<ColumnVo> getCols(Element colsEl) {
		List<Element> colElList = colsEl.elements();
		if (colElList == null || colElList.size() == 0) {
			return null;
		}
		
		List<ColumnVo> cols = new ArrayList<>();
		for(Element colEl : colElList){
			ColumnVo col = new ColumnVo();
			col.setConifgCol(true);
			cols.add(col);
			
			Attribute colName = colEl.attribute("name");
			if (colName == null || StringUtils.isEmpty(colName.getText())) {
				throw new RuntimeException("name attribute must not be null in column!");
			}
			col.setColName(colName.getText());
			col.setFieldName(CodeUtils.toCamel(col.getColName()));
			
			List<Element> colItemElList = colEl.elements();
			if (colItemElList == null) {
				continue;
			}
			
			for(Element colItemEl : colItemElList){
				if (ColumnVo.NODE_NAME_CHECK_EMPTY.equals(colItemEl.getName())) {
					col.setCheckEmpty(Boolean.valueOf(colItemEl.getText()));
				}
				if (ColumnVo.NODE_NAME_REGEX_JS.equals(colItemEl.getName())) {
					col.setRegexJs(colItemEl.getText());
				}
				if (ColumnVo.NODE_NAME_REGEX_JAVA.equals(colItemEl.getName())) {
					col.setRegexJava(colItemEl.getText());
				}
				if (ColumnVo.NODE_NAME_DATE.equals(colItemEl.getName())) {
					col.setDate(Boolean.valueOf(colItemEl.getText()));
				}
				if (ColumnVo.NODE_NAME_MODIFY.equals(colItemEl.getName())) {
					col.setModify(Boolean.valueOf(colItemEl.getText()));
				}
				if (ColumnVo.NODE_NAME_QUERY.equals(colItemEl.getName())) {
					col.setQuery(colItemEl.getText());
					col.checkQueryCol();
				}
			}
			
		}
		
		return cols;
	}

	/**
	 * 获取配置文件	
	 * @param xmlDir
	 * @return key:tableName,value:表配置文件
	 * @throws IOException 
	 */
	private static Map<String, File> getTableFileMap(final String[] tableNames,String xmlDir) throws IOException {
		if (tableNames == null) {
			return null;
		}
		
		File dir = new File(xmlDir);
		if (!dir.exists()) {
			throw new RuntimeException("未找到配置文件目录:" + xmlDir);
		}
		File[] tableFileArr = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				for(String tableName : tableNames){
					if ((tableName + ".xml").equals(pathname.getName())) {
						return true;
					}
				}
				
				return false;
			}
		});

		Map<String, File> map = new HashMap<String, File>();
		if (tableFileArr == null || tableFileArr.length == 0) {
			return map;
		}
		for(File file : tableFileArr){
			map.put(file.getName().substring(0,file.getName().length() - 4), file);
		}
		
		return map;
	}
	
	
	public void genCode() throws Exception{
		logger.info("start to genCode...");
		
		List<TableVo> tableList = getTableList();
		if (tableList == null || tableList.size() == 0) {
			throw new RuntimeException("请输入表名");
		}
		for(TableVo t : tableList){
			initTableVo(t);
			genDao(t);
			genDaoImpl(t);
			genService(t);
			genServiceImpl(t);
			genController(t);
			genPojo(t);
			genListJsp(t);
			genListJs(t);
			genEditJsp(t);
			genEditJs(t);
		}
		genSql(tableList);
		
		logger.info("\ngen code finished...\nsrc:" + getConfig("dist.src") + "\njspFold:" + getJspFold() + "\njsFold:" + getJsFold());
	}

	/**
	 * 生成一些sql
	 * @param t
	 * @throws IOException 
	 */
	private void genSql(List<TableVo> tableList) throws IOException {
		String parentId = "DFD7B458E3EA40078300617DD22D18DA";//父级menu_id，根据不同环境具体设置
		
		StringBuffer sbDelete = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		if (tableList == null || tableList.size() == 0) {
			return ;
		}
		
		int order = 99;
		for(TableVo item : tableList){
			String id = UUIDUtils.getUUID();
			
			sbDelete.append("delete from t_menu t where t.F_MENU_ID = '" + id + "';\n");
			
			sb.append("insert into t_menu (F_MENU_ID, F_PID, F_DESC, F_HIDDEN, F_IS_FOLDER, F_NAME, F_ORDER, F_URL)\n");
			sb.append(" values ('" + id + "', '" + parentId + "', 'aaaaaa', 0, '0', 'aaaaa', " + (order-=10) + ", '" + item.getPojoNameVar() + "/" + item.getPojoNameVar() + "List.do');\n\n");
			
			sb2.append("insert into t_role_purview (F_ROLE_ID, F_OP_ID, F_MENU_ID)\n");
			sb2.append(" values ('admins', '1', '" + id + "');\n");
			sb2.append("insert into t_role_purview (F_ROLE_ID, F_OP_ID, F_MENU_ID)\n");
			sb2.append(" values ('admins', '2', '" + id + "');\n");
			sb2.append("insert into t_role_purview (F_ROLE_ID, F_OP_ID, F_MENU_ID)\n");
			sb2.append(" values ('admins', '3', '" + id + "');\n");
			sb2.append("insert into t_role_purview (F_ROLE_ID, F_OP_ID, F_MENU_ID)\n");
			sb2.append(" values ('admins', '4', '" + id + "');\n");
			sb2.append("insert into t_role_purview (F_ROLE_ID, F_OP_ID, F_MENU_ID)\n");
			sb2.append(" values ('admins', '5', '" + id + "');\n\n");
			
		}
		
		sb.append(sb2).append("\n").append(sbDelete);
		File distFile = FileUtils.getFile(getConfig("dist.src") + "/update.sql");
		FileUtils.write(distFile, sb.toString(),getEncoding());
	}

	private void initTableVo(TableVo t) throws IOException {
		String dealNullStr = getConfig("deal.null");
		if ("1".equals(dealNullStr)) {
			t.setDealNull(true);
		}

		String schema = getConfig("schema");
		t.setSchema(schema);
	}

	/**
	 * 生成jsp文件的目录
	 * @return
	 * @throws IOException
	 */
	private String getJspFold() throws IOException {
		return getConfig("dist.webroot") + getConfig("dist.jsp.fold");
	}

	/**
	 * 生成js文件的目录
	 * @return
	 * @throws IOException
	 */
	private String getJsFold() throws IOException {
		return getConfig("dist.webroot") + getConfig("dist.js.fold");
	}

	public static void main(String[] args) throws Exception {
		SimpleADMS s = SimpleADMS.getInstance();
		s.genCode();
	}
}	
