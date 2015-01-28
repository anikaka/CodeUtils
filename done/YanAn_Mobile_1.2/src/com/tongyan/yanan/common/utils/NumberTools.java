package com.tongyan.yanan.common.utils;

import java.util.Random;

public class NumberTools {

	/**
	 * 随机数据
	 */
	
	public static  int  getNumber(){
		Random ran=new Random();
		return ran.nextInt()+10;
	}
	
}
