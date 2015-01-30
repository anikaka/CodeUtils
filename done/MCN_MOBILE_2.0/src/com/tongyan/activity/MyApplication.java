package com.tongyan.activity;

import java.util.ArrayList;

import com.baidu.mapapi.SDKInitializer;
import com.tongyan.common.db.DBService;
import com.tongyan.common.entities._User;

import android.app.Activity;
import android.app.Application;
/**
 * 
 * @author wanghb
 * @date 07-12-2013 14:28
 */
public class MyApplication extends Application {
	
	public _User localUser;
	public String userId;
	public boolean  mIsLogin = false;
	public ArrayList<Activity> actList = new ArrayList<Activity>();
	
	
	public String mFilePath;
	public int mFontSize;
	
	@Override
	public void onCreate() {
		localUser =  new DBService(this).getCurrentUser();
		if(localUser != null) {
			userId = localUser.getUserid();
		} else {
			localUser = new _User();
		}
		try {
			SDKInitializer.initialize(this);
		} catch(Exception e) {
			
		}
		super.onCreate();
	}
	
	
	public void addActivity(Activity act) {
		actList.add(act);
	}
	
	public void exit() {
		for(Activity  act : actList) {
			if(act != null && !act.isFinishing()) {
				act.finish();
			}
		}
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
}
