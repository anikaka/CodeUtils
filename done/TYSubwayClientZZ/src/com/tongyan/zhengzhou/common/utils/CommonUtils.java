package com.tongyan.zhengzhou.common.utils;


import com.tongyan.zhengzhou.act.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class CommonUtils {
	
	
	public static String textIsNull(String text) {
		if(text == null || "".equals(text.trim()) || "null".equalsIgnoreCase(text.trim())) {
			return "";
		}
		return text;
	}
	
	public static String parseStringToNum(String text) {
		if(text == null || "".equals(text.trim()) || "null".equalsIgnoreCase(text.trim())) {
			return "0";
		}
		return text;
	}
	
	public static int px2dp(Context context,float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(pxValue/scale+0.5f);
	}
	
	public static int dp2px(Context context,float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(dpValue*scale+0.5f);
	}
	
	public static int measureValue() {
		return  View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
	}
	
	public static int measureHeight() {
		return  View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
	}
	
	public static float density(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}
	
	/**
	 * 创建自定义dialog
	 * 
	 * @param layout 布局ID
	 * @param dialogWidthPercent 占父屏宽%
	 * @param dialogHeightPercent 占父屏高%
	 * @param m
	 * @author Rubert
	 */
	public static Dialog createDialog(Context mContext, int layout, double dialogWidthPercent, double dialogHeightPercent, WindowManager m) {
		Dialog dialog = new Dialog(mContext,R.style.mDialog);
		dialog.setContentView(layout);
		/** 将对话框的大小按屏幕大小的百分比设置 */
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		LayoutParams params = dialog.getWindow().getAttributes();
		params.height = (int) (d.getHeight() * dialogHeightPercent); // 高度设置为屏幕的0.6
		params.width = (int) (d.getWidth() * dialogWidthPercent); // 宽度设置为屏幕的0.95
		dialog.onWindowAttributesChanged(params);
//		setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
		dialog.show();
		return dialog;
	}
	
	/**
	 * 创建加载动画
	 * @param mContext
	 * @return
	 * @author Rubert
	 */
	public static Dialog creatLoadingDialog(Context mContext) {
		Dialog mDialog = new AlertDialog.Builder(mContext).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		return mDialog;
	}
	
	/**
	 * 获取屏幕宽度
	 * @param m
	 * @return
	 */
	public static int getScreenWidth(WindowManager m) {
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		//LayoutParams params = dialog.getWindow().getAttributes();
		return d.getWidth();
	}
	
	
	
	/**
	 * http://192.168.0.3:8088/WebService/BaseInfoService.asmx
	 * @param mContext
	 * @return
	 */
	public static String getRouteAndSubUrl(Context mContext) {
		return getRoute(mContext) + Constants.SERVER_SUB_URL;
	}
	/**
	 * http://192.168.0.3:8088
	 * @param mContext
	 * @return
	 */
	public static String getRoute(Context mContext) {
		String mIpAndPort = getIpAndPort(mContext);
		if(mContext != null) {
			if(mIpAndPort.contains(Constants.HTTP)) {
				return getIpAndPort(mContext);
			}
		}
		return Constants.HTTP + mIpAndPort;
	}
	/**
	 * 获取系统设置的ip+端口,如果为空则返回常量类里面的
	 * 192.168.0.3:8088
	 * @param mContext
	 * @return
	 */
	public static String getIpAndPort(Context mContext) {
		String mPath = Constants.SERVER_URL_IP_PORT;
		if(mContext != null) {
//			SharedPreferences mShared = PreferenceManager.getDefaultSharedPreferences(mContext);
			SharedPreferences mShared = mContext.getSharedPreferences(Constants.PRFERENCES_IP_ROUTE_KEY, 0);
//			mPath = mShared.getString(Constants.PREFERENCES_URL_ROUTE, "");
			mPath = mShared.getString(Constants.PREFERENCES_IP_ROUTE, "");
			if(mPath == null || "".equals(mPath)) {
				mPath = Constants.SERVER_URL_IP_PORT;
			}
		}
		return mPath;
	}
	
}
