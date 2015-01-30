package com.tongyan.utils;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;

/**
 * 
 * @Title: CommonUtils.java 
 * @author Rubert
 * @date 2014-8-28 AM 10:12:26 
 * @version V1.0 
 * @Description: 公共工具类
 */
public class CommonUtils {
	
	
	
	public static String toHandlerString(String obj) {
		if(obj == null || "null".equalsIgnoreCase(obj)) {
			return "";
		}
		return obj;
	}
	/**
	 * 判断GPS是否开启
	 * 开启：true
	 * 关闭：false
	 */
	public static boolean getLocationServiceState(Context mContext) {
		LocationManager alm = (LocationManager)mContext.getSystemService( Context.LOCATION_SERVICE );
		if (!alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * 如果GPS是已开启，则关闭；如果GPS是关闭，则开启
	 * @param mContext
	 */
	public static void toggleGPS(Context mContext) {
		Intent gpsIntent = new Intent();  
        gpsIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");  
        gpsIntent.addCategory("android.intent.category.ALTERNATIVE");  
        gpsIntent.setData(Uri.parse("custom:3"));  
        try {  
            PendingIntent.getBroadcast(mContext, 0, gpsIntent, 0).send();  
        }  
        catch (CanceledException e) {  
            e.printStackTrace();  
        }  
	}
	
	
	
	
	
	
}
