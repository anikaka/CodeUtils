package com.tygeo.highwaytunnel.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.tygeo.highwaytunnel.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

public class UpdateManager {
	
	private Context mContext;
	
	// 提示语
	private String updateMsg = "发现新版本~";
	private static final String savePath = mapUtil.getSDPath() + "/updatedemo/";
	private static final String saveFileName = savePath + "HighwayTunnel.apk";
	private Dialog noticeDialog;
	private ProgressBar mProgress;
	private Dialog downloadDialog;
	private static final int DOWN_UPDATE = 1;
	String jsonurl = "http://www.geodigital.cn/MobileIntelligent/softwares/vjson.txt";
	private Thread downLoadThread;

	private static final int DOWN_OVER = 2;
	public static  String apkUrls ="";
	private int progress;
	private boolean interceptFlag = false;

	public  boolean getServerVer() {
		
		int newVerCode;
		boolean flag = false;
		String newVerName;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(jsonurl);
			HttpResponse response;
			response = client.execute(get);
			  StringBuilder builder = new StringBuilder();
			  HttpEntity entity = response.getEntity(); 
			  	               
			                   InputStream content = entity.getContent(); 

			                   BufferedReader reader = new BufferedReader( 
			                		  
			                   new InputStreamReader(content)); 
			                  
			                   String line; 
			                   
			                   while ((line = reader.readLine()) != null) { 
			                	   
			                       builder.append(line); 
			                       
			                   } 
			                   System.out.println(builder.toString());
			JSONArray array = new JSONArray(builder.toString());
			if (array.length() > 0) {
				JSONObject obj = array.getJSONObject(0);
				try {
					newVerCode = Integer.parseInt(obj.getString("verCode"));
					System.out.println("WebVerCode: "+newVerCode);
					newVerName = obj.getString("verName");
					apkUrls="http://www.geodigital.cn/MobileIntelligent/softwares/"+obj.getString("apkname");
					System.out.println(apkUrls);
					int sevc = getVersionCode(mContext);
					if (newVerCode > sevc) {
						
						flag = true;
					} else {
						flag = false;
					}

				} catch (Exception e) {
					newVerCode = -1;
					newVerName = "";
					return false;
				}
			}
		} catch (Exception e) {
			Log.e("ss", e.getMessage());
			e.printStackTrace();
			return false;
		}
		return flag;
	}
	
	public UpdateManager(Context context) {	
		this.mContext = context;
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:
				
				installApk();
				break;
			default:
				break;
			}
		};
	};
	
	
	public void checkUpdateInfo() {
		if (getServerVer() == true) {			
			showNoticeDialog();
		} else {
		}
	}
	
	private void showNoticeDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("软件版本更新");
		builder.setMessage(updateMsg);
		builder.setPositiveButton("下载", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();
			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		noticeDialog = builder.create();
		noticeDialog.show();
	}

	private void showDownloadDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("软件版本更新");

		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.progress);

		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		downloadDialog.show();

		downloadApk();
	}

	private void downloadApk() {

		downLoadThread = new Thread(mdownApkRunnable);

		downLoadThread.start();
	}

	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);

	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(apkUrls);
				
				HttpURLConnection conn = (HttpURLConnection) url
					.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdir();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载.
				
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	};

	public int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			// 获取软件版本号，
			versionCode = context.getPackageManager().getPackageInfo(
					"com.tygeo.highwaytunnel", 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}
		
	public boolean isupdate() {
		boolean flag = false;
		if (getVersionCode(mContext) == 0) {
			
		}
		
		return flag;
	}
	
}
