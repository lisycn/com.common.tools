package com.common.tools.code.writelog;

import java.io.File;
import java.io.FilenameFilter;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;


/**
 * 自动加上日志
 * @author Administrator
 *
 */
public class WriteLog {
	private Logger logger = LoggerFactory.getLogger(WriteLog.class);
	
	/**
	 * 
	 * @param basePath 要处理的类的根目录
	 */
	public WriteLog(String basePath){
		this.basePath = basePath;
	}
	
	private String basePath;//要处理的类的根目录

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	
	public void addLogToEveryMethod() throws Exception{
		File dir = new java.io.File(this.getBasePath());
		if (!dir.exists()) {
			throw new Exception("根目录不存在:" + this.getBasePath());
		}
		
		writeLog(dir);
	}

	private void writeLog(File file) throws Exception {
		if (file == null || !file.exists()) {
			return;
		}
		
		if (file.isDirectory()) {
			File[] list = file.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					if (name == null) {
						return false;
					}
					
					if (dir.isDirectory()) {
						return true;
					}
					
					if (name.endsWith(".java")) {
						return true;
					}
					
					return false;
				}
			});
			
			if (list == null || list.length == 0) {
				return;
			}
			
			for(File f : list){
				writeLog(f);
			}
			
			return;
		}
		
		if (file.isFile()) {
			logger.info("fileName:" + file.getAbsolutePath());
			JavaFile f = new JavaFile(file);
			f.addLog();
			
			return;
		}

		throw new Exception("unkown file type:" + file.getAbsolutePath());
	}
	
	public static void main(String[] args) throws Exception {
		WriteLog wl = new WriteLog("D:/svn/fbs64/com.indicator.web/src/com");
//		WriteLog wl = new WriteLog("D:/svn/fbs64/com.fbs.pt.tools/src/com/fbs/pt/tools/code/writelog/test");
		
		wl.addLogToEveryMethod();
	}
	
}
