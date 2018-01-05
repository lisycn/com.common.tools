
package com.common.tools.code.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class AppUtils {
	private static Logger logger = Logger.getLogger(AppUtils.class);
	/**
	 * length位随机数
	 * @param length
	 * @return
	 */
	public static int getRadmonInt(int length){
		logger.debug("进入方法:getRadmonInt,参数,length:" + length);
		Random r = new Random();
		int max = 1;
		for (int i = 0; i < length; i++) {
			max = max * 10;
		}
		int num = r.nextInt(max);
		logger.debug("结束方法:getRadmonInt,参数,length:" + length);
		return num;
	}
	
	/**
	 * length位随机数,不足位在前面补0
	 * @param length
	 * @return
	 */
	public static String getRadmonIntStr(int length){
		logger.debug("进入方法:getRadmonIntStr,参数,length:" + length);
		int num = getRadmonInt(length);
		return numberToString(num,length);
	}

	/**
	 * 数字变字符串
	 * @param num 数据
	 * @param length 变多长，不足前面补0
	 * @return
	 * @author LvXin
	 */
	public static String numberToString(int num,int length) {
		String p = "";
		for (int i = 0; i < length; i++) {
			p += "0";
		}
		DecimalFormat f = new DecimalFormat(p);
		logger.debug("结束方法:getRadmonIntStr,参数,length:" + length);
		return f.format(num);
	}

	@SuppressWarnings("unused")
	private static void testFormat() {
		logger.debug("进入方法:testFormat,参数无");
		DecimalFormat f = new DecimalFormat("0,000.0000");
		DecimalFormat f1 = new DecimalFormat("00.00");
		DecimalFormat f2 = new DecimalFormat("#,###.####");
		DecimalFormat f3 = new DecimalFormat("##.##");
		
		double num = 103.14;
		
		System.out.println(f.format(num));
		System.out.println(f1.format(num));
		System.out.println(f2.format(num));
		System.out.println(f3.format(num));
		logger.debug("结束方法:testFormat,参数无");
	}
	
	/**
	 * 是否调试状态
	 * @return
	 */
	public static boolean isDebug(){
		logger.debug("进入方法:isDebug,参数无");
		logger.debug("结束方法:isDebug,参数无");
		return false;
	}
	
	/**
	 * 获取所有堆栈信息 
	 * @param e
	 * @return
	 * @author LvXin
	 */
	public static String getStackTrace(Throwable e){
		if (e == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(e.toString() + "\n");
		StackTraceElement[] sts = e.getStackTrace();
		if (sts == null || sts.length == 0) {
			return "";
		}
		
		for(StackTraceElement st : sts){
			sb.append("    " + st.getClassName() + "." + st.getMethodName() + "(" + st.getFileName() + ":" + st.getLineNumber() + ")\n");
		}
		
		Throwable p = e.getCause();
		if (p != null) {
			sb.append("caused by: ");
			sb.append(getStackTrace(p));
		}
		
		return sb.toString();
	}
	
	/**
	 * 比较两个big的大小
	 * @param b1
	 * @param b2
	 * @param scale
	 * @return
	 * @author LvXin
	 */
	public static boolean equals(BigDecimal b1,BigDecimal b2,int scale){
		if(b1 == null || b2 == null){
			return false;
		}
		
		b1.setScale(scale, RoundingMode.HALF_UP);
		b2.setScale(scale, RoundingMode.HALF_UP);
		
		return b1.equals(b2);
	}
    
    /**
	 * 是否ajax请求
	 * @param request
	 * @return
	 */
	public static boolean isAjax(HttpServletRequest request){
		String requestType = request.getHeader("X-Requested-With");
		logger.info("uri:" + request.getRequestURI() + ",X-Requested-With:" + requestType);
		String ajaxString = "XMLHttpRequest";
//		String[] notAjaxArr = {"com.tencent.mm","com.youbest.w.youbest"};

		if (ajaxString.equals(requestType)) {
			return true;
		}
		
		return false;
	}

	
	/**
	 * 选择b1与b2中，与num更相近的一个
	 * @author LvXin
	 * @param b1
	 * @param b2
	 * @param num
	 * @return b1或b2的引用，注意不能修改b1或者b2
	 * @throws Exception 
	 */
	public static BigDecimal getNearOne(BigDecimal b1,BigDecimal b2,BigDecimal num) throws Exception{
		if (b1 == null || b2 == null || num == null) {
			throw new Exception("所有值都不能为空");
		}
		
		BigDecimal dif1 = b1.subtract(num).abs();
		BigDecimal dif2 = b2.subtract(num).abs();
		
		if (dif1.compareTo(dif2) > 0) {
			return b2;
		}else{
			return b1;
		}
	}
	
	public static void main(String[] args) {
		try {
			testException();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("**********************************************");
			System.out.println(getStackTrace(e));
		}
	}
	
	public static void testException() throws Exception{
		try {
			DateUtil.fromYyyyMMdd("");
		} catch (Exception e) {
			throw new Exception("呵呵",e);
		}
	}
}
