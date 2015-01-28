package com.tongyan.yanan.common.utils;

import android.app.Application;

/**
 * Application 
 * @author ChenLang
 */

public class CrashApplication  extends Application{

	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler crashHandler=CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
	}
	
}
