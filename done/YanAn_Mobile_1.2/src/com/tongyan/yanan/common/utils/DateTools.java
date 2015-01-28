package com.tongyan.yanan.common.utils;

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
	public static ArrayList<HashMap<String,String>> getDateList(String startDate,String endDate){
		 ArrayList<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
		//2014-04-01
		//截取开始时间的年月日
		String year=startDate.substring(0, 4);
		String month=startDate.substring(5, startDate.lastIndexOf("-"));
		String day=startDate.substring(startDate.lastIndexOf("-")+1,startDate.lastIndexOf("-")+3);
		
		//截取结束时间的年月日
		String yearEndDate=endDate.substring(0, 4);
		String monthEndDate=endDate.substring(5, startDate.lastIndexOf("-"));
		String dayEndDate=endDate.substring(startDate.lastIndexOf("-")+1,startDate.lastIndexOf("-")+3);
		
		Calendar    c1=Calendar.getInstance();
		c1.set(Calendar.YEAR, Integer.parseInt(year));
		c1.set(Calendar.MONTH, Integer.parseInt(month)-1);
		c1.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
		
		Calendar  c2=Calendar.getInstance();
		c2.set(Calendar.YEAR, Integer.parseInt(yearEndDate));
		c2.set(Calendar.MONTH, Integer.parseInt(monthEndDate)-1);
		c2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayEndDate));
		HashMap<String, String> mapFirst=new HashMap<String, String>();
		mapFirst.put("date", c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.MONTH)+1)+"-"+c1.get(Calendar.DAY_OF_MONTH));
		list.add(mapFirst);
		while(true){
		HashMap<String, String> map=new HashMap<String, String>();
		c1.add(Calendar.DAY_OF_MONTH, 1);
		map.put("date", c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.MONTH)+1)+"-"+c1.get(Calendar.DAY_OF_MONTH));
		list.add(map);
		if(c1.get(Calendar.DAY_OF_MONTH)==c2.get(Calendar.DAY_OF_MONTH)){
			
			break;
			}
		}
		return list;
	}
	class downLoadTask extends AsyncTask<String, Void, Bitmap>{

		@Override
		protected Bitmap doInBackground(String... params) {

			return null;
		}


		
	}
}
