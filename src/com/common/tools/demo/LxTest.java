package com.common.tools.demo;

import java.math.BigDecimal;

/**
 * 测试类，并没有什么用
 * @author LvXin
 */
public class LxTest {
	public static void main(String[] args) throws Exception {
		BigDecimal b1 = new BigDecimal(555);
		BigDecimal b2 = new BigDecimal(333);
		System.out.println(b1.add(b2));
		System.out.println(b1.add(b2.negate()));
	}

}
