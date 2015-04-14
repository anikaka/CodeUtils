package com.tongyan.zhengzhou.act.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tongyan.zhengzhou.act.MApplication;
import com.tongyan.zhengzhou.act.MainAct;
import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.act.ServerIPInputAct;
import com.tongyan.zhengzhou.common.afinal.MFinalFragment;
import com.tongyan.zhengzhou.common.db.DBService;
import com.tongyan.zhengzhou.common.db.WebServiceDBService;
import com.tongyan.zhengzhou.common.service.BaseDataService;
import com.tongyan.zhengzhou.common.service.DownloadService;
import com.tongyan.zhengzhou.common.service.DownloadService.ICallbackResult;
import com.tongyan.zhengzhou.common.service.DownloadService.MBinder;
import com.tongyan.zhengzhou.common.utils.CommonUtils;
import com.tongyan.zhengzhou.common.utils.Constants;
import com.tongyan.zhengzhou.common.utils.JSONParseUtils;
import com.tongyan.zhengzhou.common.utils.WebServiceUtils;

public class MainFragmentSetUp extends MFinalFragment implements OnClickListener{

	private TextView mTitle;
	private TextView userName;
	private TextView serverPathSet;
	private TextView editionUpdate;
	private TextView currentVersion;
	private TextView dataUpdate;
	private TextView lastUpdate;
	private LinearLayout mTitleBackBtn;
	private Button mBackBtn;
	private Context mContext;
	private RelativeLayout serverLayout;
	private RelativeLayout versionUpdateLayout;
	private RelativeLayout dataUpdateLayout;
	private Dialog mDialog = null;
	private String UpdateInfo;
	private String ApkUrl;
	private MBinder binder;
	private boolean isBinded;
	private Thread thread = null;
	private boolean mFlag = true;
	private String localUpdateTime;
	
	public static MainFragmentSetUp newInstance(Context context){
		MainFragmentSetUp fragmentSetUp = new MainFragmentSetUp();
		fragmentSetUp.mContext = context;
		return fragmentSetUp;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_fragmentsetup, container, false);
		mTitle = (TextView) view.findViewById(R.id.title_content);
		mTitleBackBtn = (LinearLayout) view.findViewById(R.id.title_back_btn);
		userName = (TextView) view.findViewById(R.id.user_info);
		serverPathSet = (TextView) view.findViewById(R.id.server_path_info);
		serverLayout = (RelativeLayout) view.findViewById(R.id.server_path_info_layout);
		editionUpdate = (TextView) view.findViewById(R.id.edition_update_check);
		versionUpdateLayout = (RelativeLayout) view.findViewById(R.id.edition_update_check_layout);
		currentVersion = (TextView) view.findViewById(R.id.current_version);
		dataUpdate = (TextView) view.findViewById(R.id.data_update_check);
		dataUpdateLayout = (RelativeLayout) view.findViewById(R.id.data_update_check_layout);
		lastUpdate = (TextView) view.findViewById(R.id.last_update);
		mBackBtn =  (Button) view.findViewById(R.id.main_back_btn);
		
		String name = new DBService(mContext).getUserName();
		localUpdateTime = new WebServiceDBService(mContext).getLastUpdateTime();
		mTitle.setText("设置");
		if(name == null || "".equals(name)){
			userName.setText("用户名");
		}else{
			userName.setText(name);
		}
		serverPathSet.setText("服务器路径配置");
		editionUpdate.setText("版本更新检查");
		currentVersion.setText("当前版本 "+ Constants.VERSION);
		dataUpdate.setText("数据更新检查");
		if(localUpdateTime != null && !"".equals(localUpdateTime)){
			lastUpdate.setText("上次更新 "+localUpdateTime);
		}else{
			lastUpdate.setText("尚未更新");
		}
		
		mBackBtn.setText("退出程序");
		
		mTitleBackBtn.setOnClickListener(this);
		mBackBtn.setOnClickListener(this);
		serverLayout.setOnClickListener(this);
		versionUpdateLayout.setOnClickListener(this);
		dataUpdateLayout.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back_btn:
			if(MainAct.mSlidingMenu != null) {
				MainAct.mSlidingMenu.showMenu();
			}
			break;
		case R.id.main_back_btn:
			MApplication.getInstance().exit();
			break;
		case R.id.server_path_info_layout:
			SharedPreferences setting = mContext.getSharedPreferences(Constants.PRFERENCES_IP_ROUTE_KEY, 0);
			String mServiceIp = setting.getString(Constants.PREFERENCES_IP_ROUTE, "");
			if(mServiceIp == null || "".equals(mServiceIp)) {
				mServiceIp = Constants.SERVER_URL_IP_PORT;
			}
			Intent intent = new Intent(mContext,ServerIPInputAct.class);
			intent.putExtra("setttingValue", mServiceIp);
			startActivity(intent);
			break;
		case R.id.edition_update_check_layout:
			mDialog = new AlertDialog.Builder(mContext).create();
			mDialog.show();
	    	//注意此处要放在show之后 否则会报异常
			mDialog.setContentView(R.layout.common_loading_process_dialog);
			mDialog.setCanceledOnTouchOutside(false);
			
			thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("versionCode", String.valueOf(Constants.VERSION));
						map.put("appTypeID", Constants.VERSION_KEY);
						try{
							String stream = WebServiceUtils.requestM(mContext, map, Constants.METHOD_OF_CheckVersionNew);
//							Log.i("111", stream);
							HashMap<String, Object> vMap= new JSONParseUtils().getVersion(stream,"CheckVersionNewResult");
							if(vMap != null && vMap.size() > 0){
								if ("Ok".equals(vMap.get("s").toString())) {
									if (vMap.get("v") != null) {
										HashMap<String, String> mInfoMap = (HashMap<String, String>)vMap.get("v");
										if(Constants.VERSION >= Float.parseFloat(mInfoMap.get("apkVersion"))){
											if(!mFlag) {
												return;
											}
											sendMessage(Constants.ERROR);
										}else{
											UpdateInfo = mInfoMap.get("apkContent");
											ApkUrl = CommonUtils.getRoute(mContext) + "/" + mInfoMap.get("apkPath");
											if(!mFlag) {
												return;
											}
											sendMessage(Constants.SUCCESS);
										}
									}else{
										if(!mFlag) {
											return;
										}
										sendMessage(Constants.ERROR);
									}
								}else{
									if(!mFlag) {
										return;
									}
									sendMessage(Constants.ERROR);
								}
							}else{
								if(!mFlag) {
									return;
								}
								sendMessage(Constants.ERROR);
							}
						}catch (Exception e) {
							if(!mFlag) {
								return;
							}
							sendMessage(Constants.ERROR);
							e.printStackTrace();
						}
					}catch (Exception e) {
						if(!mFlag) {
							return;
						}
						sendMessage(Constants.ERROR);
						e.printStackTrace();
					}
				}
			});
			thread.start(); 
			break;
		
		case R.id.data_update_check_layout:
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			String time=format.format(new Date()); 
			lastUpdate.setText("上次更新 "+time);
			new WebServiceDBService(mContext).setLocalUpdateTime(time);
			
			String updateTime = new WebServiceDBService(mContext).getUpdateTime("LineInfo");
			String damageUpdateTime = new WebServiceDBService(mContext).getUpdateTime("DamageTypeInfo");
			Intent service = new Intent(mContext, BaseDataService.class);
			mContext.stopService(service);
			service.putExtra("updateTime", updateTime);
			service.putExtra("damageUpdateTime", damageUpdateTime);
			mContext.startService(service);
			break;
		default:
			break;
		}
		
	}

	/**
	 * 关闭dialog
	 */
	public void closeMDialgo() {
		if(mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
	}
	/**
	 * 下载最新apk
	 */
	private void showUpdateInfo() {
		final Dialog msDialog = new AlertDialog.Builder(mContext).create();
		msDialog.show();
		
		msDialog.setContentView(R.layout.common_sure_cancel_dialog);
		msDialog.setCanceledOnTouchOutside(false);
		
		TextView mTitleTextView = (TextView) msDialog.findViewById(R.id.prompt_title);
		mTitleTextView.setText("发现新版本");
		
		TextView mTextView = (TextView) msDialog.findViewById(R.id.prompt_content);
		mTextView.setText("发现新版本"+UpdateInfo);
		
		Button mSureBtn = (Button) msDialog.findViewById(R.id.sure_btn);
		mSureBtn.setText("确定更新");
		Button mCancleBtn = (Button) msDialog.findViewById(R.id.cancel_btn);
		mCancleBtn.setText("以后再说");
		
		mSureBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(msDialog != null) {
					msDialog.dismiss();
				}
				Intent intent = new Intent(mContext,DownloadService.class);
				intent.putExtra("filePath", ApkUrl);
				intent.putExtra("downloadContent", UpdateInfo);
				mContext.bindService(intent, conn, Context.BIND_AUTO_CREATE);
			}
		});
		
		mCancleBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(msDialog != null) {
					msDialog.dismiss();
				}
			}
		});
		
	}
	
	private ICallbackResult callback = new ICallbackResult() {

		@Override
		public void onBackResult(Object result) {
			if ("finish".equals(result)) {
				if(null != conn && isBinded) {
					mContext.unbindService(conn);
				}
			} else if("error".equals(result)) {
				sendMessage(Constants.MSG_0x2001);
			}
		}
	};
	
	ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			isBinded = false;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			binder = (MBinder) service;
			// 开始下载
			isBinded = true;
			binder.addCallback(callback);
			binder.start();
		}
	};
	
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constants.SUCCESS:
			closeMDialgo();
			showUpdateInfo();
			break;
		case Constants.ERROR:
			closeMDialgo();
			Toast.makeText(mContext, "无最新版本", Toast.LENGTH_SHORT).show();
			break;
		case Constants.MSG_0x2001:
			Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
	@Override
	public void onDestroy() {
		if(null != conn && isBinded) {
			mContext.unbindService(conn);
			conn = null;
		}
		super.onDestroy();
	}
	
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			mFlag = false;
			thread = null;
		}
		return true;
	}

}
