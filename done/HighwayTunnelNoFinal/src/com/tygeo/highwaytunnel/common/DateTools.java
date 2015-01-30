package com.tygeo.highwaytunnel.common;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

public class DateTools {

	/**
	 * @author ChenLang
	 * @category 日期工具�?
	 * @category 获取当前系统日期
	 */
	

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
	public static String getRouteAndSubUrl(Context mContext) {
		return getRoute(mContext) + Constant.SERVER_SUB_URL;
	}

	public static String getRoute(Context mContext) {
		return Constant.HTTP + getIpAndPort(mContext);
	}
	
	public static String getIpAndPort(Context mContext) {
		SharedPreferences mShared = PreferenceManager.getDefaultSharedPreferences(mContext);
		String mPath = mShared.getString(Constant.PREFERENCES_URL_ROUTE, "");
		if(mPath == null || "".equals(mPath)) {
			mPath = Constant.SERVER_URL_IP_PORT;
		}
		return mPath;
	}

}
