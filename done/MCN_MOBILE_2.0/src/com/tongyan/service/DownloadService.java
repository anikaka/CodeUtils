package com.tongyan.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.tongyan.activity.R;
import com.tongyan.utils.Constansts;

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
/**
 * 
 * @ClassName DownloadService.java
 * @Author wanghb
 * @Date 2013-9-17 pm 02:27:47
 * @Desc TODO
 */
public class DownloadService extends Service {
	
	private static final int NOTIFY_ID = 0;//通知id,每个通知都必须有一个唯一的id
	private int mProgress;//进度%几
	private NotificationManager mNotificationManager;
	private boolean mCanceled;//取消下载标识
	private boolean serviceIsDestroy = false;//该服务是否被回收标志
	
	private Context mContext = this;
	
	private MBinder mBinder;
	private ICallbackResult mCallback;
	
	private Notification mNotification;
	
	private Thread mDownloadThread;
	
	//file
	private String mFilePath;
	
	private File mTempFile;
	
	private int lastRate = 0;
	
	//
	private String mDownloadContent;
	private int mDownloadIcon;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				// 下载完毕 取消通知
				mNotificationManager.cancel(NOTIFY_ID);
				installApk();
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
		mBinder = new MBinder();
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		//setForeground(true);// 这个不确定是否有作用
		//mFilePath = mApplication.mFilePath;
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		if(intent != null && intent.getExtras() != null) {
			mDownloadContent = intent.getExtras().getString("DownloadContent");
			mFilePath = intent.getExtras().getString("DownloadPath");
			mDownloadIcon = intent.getExtras().getInt("DownloadIcon");
		}
		return mBinder;
	}
	
	private void downloadApk() {
		mDownloadThread = new Thread(mdownApkRunnable);
		mDownloadThread.start();
	}
	
	
	/**
	 * 安装apk
	 * @param url
	 */
	private void installApk() {
		if (!mTempFile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.fromFile(mTempFile), "application/vnd.android.package-archive");
		mContext.startActivity(i);
		//mCallback.onBackResult("finish");
	}
	
	//private String mSaveFileName;
	
	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				String fileEx = mFilePath.substring(mFilePath.lastIndexOf(".") + 1,mFilePath.length()).toLowerCase();
				// 获得软件存在于服务器的目录名称
				String fileNa = mFilePath.substring(mFilePath.lastIndexOf("/") + 1,mFilePath.lastIndexOf("."));
				
				URL myURL = new URL(mFilePath);
				// 建立联机
				URLConnection conn = myURL.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				// InputStream 下载文件
				InputStream is = conn.getInputStream();
				// 建立临时文件
				String sdStatus = Environment.getExternalStorageState();
				 if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
					 mTempFile = File.createTempFile(fileNa , "." + fileEx);
		             return;
		         } else {
		        	 String apkDir = Environment.getExternalStorageDirectory().getPath() + Constansts.CN_APK_PATH;
		        	 String apkPath = apkDir + fileNa + "." + fileEx;
		        	 File fileDir = new File(apkDir);
		        	 mTempFile = new File(apkPath);
		        	 
					if (!fileDir.exists()) {
						fileDir.mkdirs();
					}
					if (!mTempFile.exists()) {
						mTempFile.createNewFile();
					} else {
						mTempFile.delete();
					}
		         }
				// 将文件写入临时盘
				FileOutputStream fos = new FileOutputStream(mTempFile);
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
		downloadApk();
	}
	
	/**
	 * 创建通知
	 */
	private void setUpNotification() {
		CharSequence tickerText = "开始下载";
		mNotification = new Notification(mDownloadIcon, tickerText, System.currentTimeMillis());
		// 放置在"正在运行"栏目中
		RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.common_alert_dialog_progress);
		contentView.setTextViewText(R.id.progress_text, mDownloadContent);
		contentView.setImageViewResource(R.id.app_icon, mDownloadIcon);
		mNotification.contentView = contentView;
		mNotificationManager.notify(NOTIFY_ID, mNotification);
	}
	
	/** 自定义Binder类 */
	public class MBinder extends Binder {
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

		public void addCallback(ICallbackResult callback) {
			mCallback = callback;
		}
	}
	
	public interface ICallbackResult {
		public void onBackResult(Object result);
	}
	
}
