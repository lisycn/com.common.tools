package com.common.tools.code.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;


public class StringUtils {
	private static Logger logger = LoggerFactory.getLogger(StringUtils.class);
	
	/**
	 * 获取字符串前面的空格,缩进等
	 * @param src
	 * @return
	 */
	public static String getStartSpace(String src){
		if (src == null) {
			return "";
		}
		String[] spaceArr = {" ","\t"};
		String s = "";
		for (int i = 0; i < src.length(); i++) {
			String sub = src.substring(i,i + 1);
			boolean isSpace = false;
			for(String space : spaceArr){
				if (space.equals(sub)) {
					s += sub;
					isSpace = true;
					continue;
				}
			}
			if (!isSpace) {
				break;
			}
		}
		
		return s;
	}
	
	public static boolean isEmpty(Object o){
		return o == null || o.toString().trim().length() == 0;
	}

	/**
	 * str 是否满足格式 regex
	 * @param str
	 * @param regex
	 */
	public static void checkPattern(String str,String strName,String regex){
		logger.debug("进入方法:checkPattern,参数,str:" + str + ",strName:" + strName + ",regex:" + regex );
		if (regex == null) {
			throw new RuntimeException("格式表达示不能为空");
		}
		
		if (str == null) {
			throw new RuntimeException(strName + "不能为空");
		}
		
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		if (!m.matches()) {
			throw new RuntimeException(strName + "格式不正确:" + str);
		}
		logger.debug("结束方法:checkPattern,参数,str:" + str + ",strName:" + strName + ",regex:" + regex );
	}
	
	/**
	 * 首字母大写
	 * @param src
	 * @return
	 */
	public static String firstCharUpCase(String src){
		logger.debug("进入方法:firstCharUpCase,参数,src:" + src );
		if (src == null || src.length() == 0) {
			logger.debug("结束方法:firstCharUpCase,参数,src:" + src );
			return "";
		}
		
		String r = "";
		r += src.substring(0,1).toUpperCase();
		
		if (src.length() > 1) {
			r += src.substring(1);
		}
		logger.debug("结束方法:firstCharUpCase,参数,src:" + src );
		return r;
	}
	
	/**
	 * 首字母小写
	 * @param src
	 * @return
	 */
	public static String firstCharLowerCase(String src){
		logger.debug("进入方法:firstCharLowerCase,参数,src:" + src );
		if (src == null || src.length() == 0) {
			logger.debug("结束方法:firstCharLowerCase,参数,src:" + src );
			return "";
		}
		
		String r = "";
		r += src.substring(0,1).toLowerCase();
		
		if (src.length() > 1) {
			r += src.substring(1);
		}
		logger.debug("结束方法:firstCharLowerCase,参数,src:" + src );
		return r;
	}
	
	public static String join(Object[] arr,String separator){
		logger.debug("进入方法:join,参数,arr:" + arr + ",separator:" + separator );
		logger.debug("结束方法:join,参数,arr:" + arr + ",separator:" + separator );
		return org.apache.commons.lang.StringUtils.join(arr, separator);
	}
	
	/**
	 * 元素是否在集合中
	 * @param arr 集合 
	 * @param t 元素，需实现equals接口
	 */
	public static <T> boolean isInArr(T[] arr,T t){
		logger.debug("进入方法:join,参数,arr:" + arr + ",t:" + t );
		if (arr == null || t == null) {
			logger.debug("结束方法:join,参数,arr:" + arr + ",t:" + t );
			return false;
		}
		
		for(T o : arr){
			if (t.equals(o)) {
				logger.debug("结束方法:join,参数,arr:" + arr + ",t:" + t );
				return true;
			}
		}
		logger.debug("结束方法:join,参数,arr:" + arr + ",t:" + t );
		return false;
	}
	
	/**
	 * 数字变字符串，不足位前面补0
	 * @param num 数字
	 * @param length 长度
	 * @return
	 * @author LvXin
	 */
	public static String number2Str(Number num,int length){
		logger.debug("进入方法:number2Str,参数,num:" + num + ",length:" + length );
		String pattern = "";
		for (int i = 0; i < length; i++) {
			pattern += "0";
		}
		NumberFormat f = new DecimalFormat(pattern);
		logger.debug("结束方法:number2Str,参数,num:" + num + ",length:" + length );
		return f.format(num);
	}
	
	/**
	 * 截取字符串
	 * @param src 原字符串
	 * @param length 要截取的长度
	 * @param suffix 后缀，null则不加后缀
	 * @return
	 * @author LvXin
	 */
	public static String cutString(String src,int length){
		return cutString(src, length, null);
	}
	
	/**
	 * 截取字符串
	 * @param src 原字符串
	 * @param length 要截取的长度
	 * @param suffix 后缀，null则不加后缀
	 * @return
	 * @author LvXin
	 */
	public static String cutString(String src,int length,String suffix){
		if (src == null || length <= 0) {
			return "";
		}
		if (src.length() <= length) {
			return src;
		}
		if (suffix == null) {
			suffix = "";
		}
		
		return src.substring(0,length) + suffix;
	}
	
	public static String replaceParam(String src,Map<String, String> param){
		if (src == null || src.length() == 0) {
			return "";
		}
		
		if (param == null || param.size() == 0) {
			return src;
		}
		
		for(String key : param.keySet()){
			src = src.replaceAll("\\$\\{" + key + "\\}", param.get(key));
		}
		
		return src;
	}
	
	public static void main(String[] args) {
		Object[] arr = {"a",new BigDecimal(11),"hehe",new Date()};
		System.out.println(join(arr, ","));
	}
}
