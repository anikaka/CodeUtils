package com.tongyan.yanan.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import com.tongyan.yanan.act.LoadingAct;
import com.tongyan.yanan.act.LoginAct;
import com.tongyan.yanan.act.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

/**'
 * 文件下载服务
 * @author ChenLang
 */

public class DownloadFileService  extends Service{
	
	private  Notification mNotification=null;
	private NotificationManager mNotificationManager=null;
	private PendingIntent mPendingIntent=null;
	private  Context mContext=this;
	private SharedPreferences mSP;
	private  int  mFileSizeCount=0;//文件的总大小
	private int mProgressCurrently=0;//当前进度
	private int  mNotifyFlag=0;
	private File mFile;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mSP=PreferenceManager.getDefaultSharedPreferences(mContext);
		setNofication();
		Intent mIntent=new Intent(mContext, LoadingAct.class);
		mPendingIntent = PendingIntent.getActivity(mContext, 0, mIntent, 0);  
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);  
		//使用自定义通知布局
		mNotification.contentView=new RemoteViews(getPackageName(), R.layout.common_version_notification);
		mNotification.contentView.setProgressBar(R.id.progressVersion, 100,0, false);
		mNotification.contentIntent=mPendingIntent;
		//创建跟新通知
		mNotificationManager.notify(0, mNotification);
		if(intent!=null){
			HashMap<String, String> mMapFile=(HashMap<String, String>)intent.getExtras().get("fileInfo");
			appDownload(mMapFile.get("fileName"),"http://"+mSP.getString(Constants.PREFERENCES_INFO_ROUTE, Constants.COMMON_URL_IP)+"/"+mMapFile.get("url"));
		}else{
			sendMessage(Constants.ERROR);
		}
		return super.onStartCommand(intent, flags, startId);
		
	}
	
	
	/** 设置通知*/
	public void setNofication(){
		if(mNotificationManager==null){
			mNotificationManager=(NotificationManager)getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		}
		if(mNotification==null){
			mNotification=new Notification();
			mNotification.icon=R.drawable.logo;
			mNotification.tickerText="延安检测更新";
		}
	}
	
	/** app文件下载 */
	 public void appDownload(final String fileName,final String url){
		 
		 new Thread(new Runnable() {
			@Override
			public void run() {
				try{
				URL mUrl=new URL(url);
				 HttpURLConnection mConn = (HttpURLConnection)mUrl.openConnection();
				 mConn.connect();
				 mConn.setConnectTimeout(1000*5);
				 mFileSizeCount=mConn.getContentLength();
				 InputStream mIs=mConn.getInputStream();
				  mFile=new File(Environment.getExternalStorageDirectory().getPath()+"/"+fileName);
				 if(mFile.exists()){
				 mFile.createNewFile();
				 }
				 FileOutputStream mOS=new FileOutputStream(mFile);
				 int mLen=0;
				 byte []mBs=new byte[1024];
				 while((mLen=mIs.read(mBs))!=-1){
					 mProgressCurrently+=mLen;
					 mOS.write(mBs, 0, mLen);
					 mNotifyFlag++;
					 if(mNotifyFlag%10==0 || mProgressCurrently==mFileSizeCount){		
						 sendMessage(Constants.COMMON_MESSAGE_1);;
					 }
						 
				 }
				 mIs.close(); 
				 mOS.close();
				 sendMessage(Constants.SUCCESS);
				} catch (Exception e) {
					sendMessage(Constants.ERROR);
				 e.printStackTrace();
			}
		}}).start();
	 }
	 
	 public void sendMessage(int result){
		 Message msg=Message.obtain();
		 msg.what=result;
		 handler.sendMessage(msg);
	 }
	 /**关闭通知*/
	 public void  cancelNotificaiton(int flag){
			if(mNotificationManager!=null){
				mNotificationManager.cancel(flag);
			}
	 }
	
	 
	 Handler handler=new Handler(){ 
		 public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constants.COMMON_MESSAGE_1://(mProgressCurrently/mFileSizeCount)*100
				mNotification.contentView.setProgressBar(R.id.progressVersion, 100,(mProgressCurrently*100)/mFileSizeCount , false);
				mNotificationManager.notify(0, mNotification);
				break;
			case Constants.SUCCESS:
					//下载成功
				cancelNotificaiton(0);
				if(mFile!=null){
					startActivity(FileOpen.getApkFileIntent(mFile));
				}
				break;
			case Constants.ERROR:
				cancelNotificaiton(0);
				break;
		 };
	 };
	 };
}

