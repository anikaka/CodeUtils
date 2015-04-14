package com.tongyan.zhengzhou.act;

import java.util.ArrayList;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tongyan.zhengzhou.common.db.DBHelper;
import com.tongyan.zhengzhou.common.db.DBService;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
/**
 * 
 * @Title: MApplication.java 
 * @author Rubert
 * @date 2015-3-3 AM 10:40:22 
 * @version V1.0 
 * @Description: TODO
 */

public class MApplication extends Application {
	
	public static ArrayList<Activity> actList = new ArrayList<Activity>();
	private static MApplication instance;
	
	//49:AD:90:05:51:05:C7:CD:12:A1:FA:0B:D5:5B:EA:B1:D1:35:C3:94
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
//		if (Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
//			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
//			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
//		}
		super.onCreate();
		SDKInitializer.initialize(getApplicationContext());	
//	    new DBService(this);
		initImageLoader(getApplicationContext());
	}
	public static void initImageLoader(Context context) {
		/** 
		 *This configuration tuning is custom. You can tune every option, you may tune some of them,
		 *or you can create default configuration by
		 *ImageLoaderConfiguration.createDefault(this);
		 *method.
		 */
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
	
	public  static MApplication getInstance() { 
        if (null == instance) { 
            instance = new MApplication(); 
        } 
        return instance; 
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
