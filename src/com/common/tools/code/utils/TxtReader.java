package com.common.tools.code.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;

public class TxtReader {
	private static Logger logger = LoggerFactory.getLogger(TxtReader.class);
	
	public static void main(String[] args) throws Exception {
		
		int pageIndex = 101;
		printLine("E:/tools/b/aaa.txt", pageIndex * 2000, 2000);
//		printLine("D:/temp.txt", pageIndex * 2000, 2000);
	}
	
	public static void printLine(String pathname,int startRow,int size) throws Exception{
		List<String> list = readLine(pathname);
		for (int i = startRow; i < list.size() && i < startRow + size; i++) {
//			System.out.println("[DEBUG] 2016-10-12," + (i + 1) + "," + list.get(i));
			logger.info((i + 1) + "," + list.get(i));
		}
	}
	
	public static List<String> readLine(String pathname) throws Exception{
		File file = new File(pathname);
		if (!file.exists()) {
			throw new Exception("file not exists:" + file.getAbsolutePath());
		}
		List<String> list = IOUtils.readLines(new FileInputStream(file), "UTF-8");
		List<String> r = new ArrayList<>();
		for(String s : list){
			if (s == null || s.trim().length() == 0) {
				continue;
			}
			
			r.add(s);
		}
		
		return r;
	}
}
