package com.tongyan.yanan.common.utils;

import android.content.Context;
import android.view.View;

/**
 * 
 * Created by Eclipse3.6.2
 * @ClassName: ScreenUtil
 * @Author wanghb
 * @Date 2012-11-21 am 10:17:02 
 * @Desc: TODO
 */
public class ScreenUtil {
	
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
}
