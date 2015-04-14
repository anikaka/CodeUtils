package com.tongyan.zhengzhou.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;


/**
 * @ClassName ConnectivityUtils 
 * @author wanghb
 * @date 2013-7-12 pm 04:21:55
 * @desc 测试ConnectivityManager ConnectivityManager主要管理和网络连接相关的操作
	  相关的TelephonyManager则管理和手机、运营商等的相关信息；WifiManager则管理和wifi相关的信息。
	  想访问网络状态，首先得添加权限
	 <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
	 NetworkInfo类包含了对wifi和mobile两种网络模式连接的详细描述,通过其getState()方法获取的State对象则代表着
	  连接成功与否等状态。
 */
public class ConnectivityUtils {
	private ConnectivityManager connManager;
	private NetworkInfo networkInfo;
	private Context context;
	
	public ConnectivityUtils() {}
	
	public ConnectivityUtils(Context context) {
		 this.setContext(context);
		 connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 // 获取代表联网状态的NetWorkInfo对象
		 networkInfo = connManager.getActiveNetworkInfo();
	}
	
	/**
	 * 判断是否连接网络
	 * @param context
	 */
	public boolean isConnection() {
		if (null == networkInfo || !networkInfo.isAvailable()) {
			return false;
		} 
		return true;
	}
	
	
	/**
	 * 判断是否是GPRS
	 * @param context
	 * @return
	 */
	public boolean isGPRSConn() {
		State state = connManager.getNetworkInfo(
				ConnectivityManager.TYPE_MOBILE).getState();
		if (State.CONNECTED == state) {
			//Log.i("通知", "GPRS网络已连接");
			//Toast.makeText(context, "GPRS网络已连接", Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否是Wiff
	 * @param context
	 * @return
	 */
	public boolean isWiffConn() {
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (State.CONNECTED == state) {
			// Log.i("通知", "WIFI网络已连接");
			// Toast.makeText(context, "WIFI网络已连接", Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}


	public void setContext(Context context) {
		this.context = context;
	}


	public Context getContext() {
		return context;
	}
}
