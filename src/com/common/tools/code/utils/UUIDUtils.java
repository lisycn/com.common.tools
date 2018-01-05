package com.common.tools.code.utils;

import java.util.UUID;

public class UUIDUtils {
	public static String getUUID(){
		 String s = UUID.randomUUID().toString();
		 s = s.replaceAll("-", "").toUpperCase();
		 
		 return s;
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			System.out.println(getUUID());
		}
	}
}
