package com.common.tools.code.writelog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;

/**
 * 括号
 * @author Administrator
 *
 */
public class Bracket {
	private static final Logger logger = LoggerFactory.getLogger(Bracket.class);
	
	/**
	 * 
	 * @param content 全文
	 * @param regex “{”之前的字符,regex
	 * @param updown 前还是向后找第一个{,-1向前，1向后
	 * @throws Exception
	 */
	public Bracket(String content,String regex) throws Exception{
		
		Pattern p = Pattern.compile(regex);
		Matcher matcher = p.matcher(content);
		String methodSign = null;
		if (matcher.find()) {
			methodSign = matcher.group();
		}else{
			throw new Exception("未找到匹配字符:" + regex);
		}
		
		logger.info("methodSign:" + methodSign);
		int begin = content.indexOf(methodSign);
		int end = -1;
		int innerGhNum = 0;//内部括号数
		for (int i = begin; i < content.length(); i++) {
			String curChar = content.substring(i,i + 1);//当前字符 
			if ("{".equals(curChar)) {
				if (innerGhNum == 0) {
					begin = i;
				}
				innerGhNum ++;
				continue;
			}
			
			if ("}".equals(curChar)) {
				if (innerGhNum > 1) {
					innerGhNum--;
				}else{
					end = i;
					break;
				}
			}
		}
		if (end < 0) {
			throw new Exception("未找到结束符");
		}

		this.setStartIndex(begin);
		this.setEndIndex(end);
	}
	
	private int startIndex;//{开始index
	private int endIndex;//}结束index
	private String content;//全文
	private String regex;//{之前的字符,regex
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public int getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRegex() {
		return regex;
	}
	public void setRegex(String regex) {
		this.regex = regex;
	}
	
	public static void main(String[] args) {
		
	}
}
