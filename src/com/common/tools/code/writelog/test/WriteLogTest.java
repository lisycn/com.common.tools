package com.common.tools.code.writelog.test;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;

public class WriteLogTest {
    private static Logger logger = Logger.getLogger(WriteLogTest.class);
	private int returnA = 0;
	public void aaa(String aa,int bb){
	    logger.debug("进入方法,WriteLogTest.aaa(java.lang.String,int)");
	    logger.debug("结束方法,WriteLogTest.aaa(java.lang.String,int)");
	}
	protected void returnA(int bb,String aa,
				String aaa,String ccc,
				String aaaa,String cccc)  
	{
	    logger.debug("进入方法,WriteLogTest.returnA(int,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)");
	    logger.debug("结束方法,WriteLogTest.returnA(int,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)");
	}
	void aaa(int bb,String aa,int aaa
			,String aaaa,String cccc )  {
			    logger.debug("进入方法,WriteLogTest.aaa(int,java.lang.String,int,java.lang.String,java.lang.String)");
		returnA = 0;
		bb = aaa;
	    logger.debug("结束方法,WriteLogTest.aaa(int,java.lang.String,int,java.lang.String,java.lang.String)");
	}
	
	int aaa(int bb,int bbb,String aa){
	    logger.debug("进入方法,WriteLogTest.aaa(int,int,java.lang.String)");
		logger.debug("结束方法,WriteLogTest.aaa(int,int,java.lang.String)");
		return 0;
	
	}
	
	int aaa(String[] aaa,List<String> bbb,String aa) throws Exception{
	    logger.debug("进入方法,WriteLogTest.aaa([Ljava.lang.String;,java.util.List,java.lang.String)");
		if(aaa == null){
			
			logger.debug("结束方法,WriteLogTest.aaa([Ljava.lang.String;,java.util.List,java.lang.String)");
			return 1;
		}
		logger.debug("结束方法,WriteLogTest.aaa([Ljava.lang.String;,java.util.List,java.lang.String)");
		return 0;
	}
	
	public static void main(String[] args) {
	}
}
