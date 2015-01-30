package com.tongyan.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.R.integer;
import android.graphics.Bitmap;
import android.os.AsyncTask;

public class DateTools {

	/**
	 * @author ChenLang
	 * @category 日期工具类
	 * @category 获取当前系统日期
	 */
	
	/** 得到当前的日期*/
	public static String getDateTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());
		return formatter.format(curDate);
	}
	
	public static String getTime(){
		SimpleDateFormat fromat=new SimpleDateFormat("yyyyMMddHHmmss");
		return fromat.format(new Date(System.currentTimeMillis()));
	}
	
	public static String getDate(){
	SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");
	Date  curDate=new Date(System.currentTimeMillis());
	return formatter.format(curDate);
	}
	
	/**
	 * @category取出开始日期到结束日期每一天的日期
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return	ArrayList
	 */
	class downLoadTask extends AsyncTask<String, Void, Bitmap>{

		@Override
		protected Bitmap doInBackground(String... params) {

			return null;
		}


		
	}
}
