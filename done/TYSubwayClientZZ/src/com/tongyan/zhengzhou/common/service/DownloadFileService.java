package com.tongyan.zhengzhou.common.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.common.utils.Constants;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;
import android.widget.Toast;

public class DownloadFileService  extends Service{

	private static final int NOTIFY_ID = 0;//通知id,每个通知都必须有一个唯一的id
	private int mProgress;//进度%几
	private NotificationManager mNotificationManager;
	private boolean mCanceled;//取消下载标识
	private boolean serviceIsDestroy = false;//该服务是否被回收标志
	private Context mContext = this;
	
	private MFileBinder mBinder;
	private IFileCallbackResult mCallback;
	
	private Notification mNotification;
	
	private Thread mDownloadThread;
	
	//file
	private String mFilePath;
	
	
	private int lastRate = 0;
	
	//
	private String mFileName;
	private int mDownloadIcon;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				// 下载完毕 取消通知
				mNotificationManager.cancel(NOTIFY_ID);
//				installApk();
				break;
			case 1 :
				mNotificationManager.cancel(NOTIFY_ID);
				Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
				mCallback.onBackResult("error");
				break;
			case 3 :
				mNotificationManager.cancel(NOTIFY_ID);
				Toast.makeText(mContext, "目标文件不存在", Toast.LENGTH_SHORT).show();
				mCallback.onBackResult("error");
				break;
			case 2:
				int rate = msg.arg1;
				if (rate < 100) {
					RemoteViews contentview = mNotification.contentView;
					contentview.setTextViewText(R.id.progress_number, rate + "%");
					contentview.setProgressBar(R.id.progress, 100, rate, false);
				} else {
					// 下载完毕后变换通知形式
					stopSelf();// 停掉服务自身
				}
				mNotificationManager.notify(NOTIFY_ID, mNotification);
				break;
			}
		}
	};
	
	public void onCreate() {
		super.onCreate();
		mBinder = new MFileBinder();
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		//setForeground(true);// 这个不确定是否有作用
		//mFilePath = mApplication.mFilePath;
	};
	
	
	
	@Override
	public IBinder onBind(Intent intent) {
		if(intent != null && intent.getExtras() != null) {
			mFileName = intent.getExtras().getString("fileName");
			mFilePath = intent.getExtras().getString("filePath");
		}
		return mBinder;
	}
	
	private void downloadFile() {
		mDownloadThread = new Thread(mdownApkRunnable);
		mDownloadThread.start();
	}
	
	
	
	
	
	private Runnable mdownApkRunnable = new Runnable() {
		
		@Override
		public void run() {
			try {
				if(!new File(Constants.PATH_OF_FILE).exists()){
					new File(Constants.PATH_OF_FILE).mkdirs();
				}
				String filesuffix=mFilePath.substring(mFilePath.lastIndexOf("."), mFilePath.length());
				File file=new File(Constants.PATH_OF_FILE+mFileName+filesuffix);
				if(!file.exists()){
					file.createNewFile();
				}
				if(mFilePath.contains("~")){
					mFilePath=mFilePath.replace("~", "");
				}
				mFilePath = mFilePath.replace("\\", "/");
				mFilePath = URLEncoder.encode(mFilePath, "utf-8");
				mFilePath =  mFilePath.replaceAll("%2F", "/");
				String url="http://"+Constants.SERVER_URL_IP_PORT+mFilePath;
				URL myURL = new URL(url);
				URLConnection conn = myURL.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				// 将文件写入临时盘
				FileOutputStream fos = new FileOutputStream(file);
				int count = 0;
				byte buf[] = new byte[1024];
				do {
					int numread = is.read(buf);
					count += numread;
					mProgress = (int) (((float) count / length) * 100);
					// 更新进度
					Message msg = mHandler.obtainMessage();
					msg.what = 2;
					msg.arg1 = mProgress;
					if (mProgress >= lastRate + 1) {
						mHandler.sendMessage(msg);
						lastRate = mProgress;
						if (mCallback != null)
							mCallback.onBackResult(mProgress);
					}
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(0);
						// 下载完了，cancelled也要设置
						mCanceled = true;
						break;
					}
					fos.write(buf, 0, numread);
				} while (!mCanceled);// 点击取消就停止下载.
					is.close();
					fos.close();
				 
			} catch(FileNotFoundException e) {
				e.printStackTrace();
				Message msg = mHandler.obtainMessage();
				msg.what = 3;
				mHandler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = mHandler.obtainMessage();
				msg.what = 1;
				mHandler.sendMessage(msg);
			}
		}
	};
	
	private void startDownload() {
		mCanceled = false;
		downloadFile();
	}
	
	/**
	 * 创建通知
	 */
	private void setUpNotification() {
	//	CharSequence tickerText = "开始下载";
		mNotification = new Notification(R.drawable.common_arrow, "文件下载", System.currentTimeMillis());
		// 放置在"正在运行"栏目中
		RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.common_version_update_progress);
		contentView.setTextViewText(R.id.progress_text, mFileName);
		contentView.setImageViewResource(R.id.app_icon, mDownloadIcon);
		mNotification.contentView = contentView;
		mNotificationManager.notify(NOTIFY_ID, mNotification);
	}
	
	/** 自定义Binder类 */
	public class MFileBinder extends Binder {
		public void start() {
			if (mDownloadThread == null || !mDownloadThread.isAlive()) {
				mProgress = 0;
				setUpNotification();
				new Thread() {
					public void run() {
						startDownload();
					};
				}.start();
			} else {
			}
		}
		public void cancel() {
			mCanceled = true;
		}

		public int getProgress() {
			return mProgress;
		}

		public boolean isCanceled() {
			return mCanceled;
		}

		public boolean serviceIsDestroy() {
			return serviceIsDestroy;
		}

		public void cancelNotification() {
			mHandler.sendEmptyMessage(2);
		}

		public void addCallback(IFileCallbackResult callback) {
			mCallback = callback;
		}
	}
	
	public interface IFileCallbackResult {
		public void onBackResult(Object result);
	}

	

}
