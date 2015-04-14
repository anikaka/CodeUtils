package com.tongyan.zhengzhou.act;

import java.io.IOException;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParserException;

import com.tongyan.zhengzhou.common.afinal.MFinalActivity;
import com.tongyan.zhengzhou.common.afinal.annotation.view.ViewInject;
import com.tongyan.zhengzhou.common.db.DBService;
import com.tongyan.zhengzhou.common.service.BaseDataService;
import com.tongyan.zhengzhou.common.utils.Constants;
import com.tongyan.zhengzhou.common.utils.JSONParseUtils;
import com.tongyan.zhengzhou.common.utils.WebServiceUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
/**
 * 
 * @Title: LoginAct.java 
 * @author Rubert
 * @date 2015-3-2 AM 11:00:42 
 * @version V1.0 
 * @Description: TODO
 */

public class LoginAct extends MFinalActivity  {
	
	@ViewInject(id=R.id.txtUserName) EditText mTxtUserName;
	@ViewInject(id=R.id.txtPassword)   EditText mTxtPassword;
	@ViewInject(id=R.id.btnLogin ,click="onLogin")  Button   mBtnLogin;
	@ViewInject(id=R.id.btnErrorInfo)   Button mBtnErrorInfo;
	@ViewInject(id=R.id.cbRememberPwd ) CheckBox  mCBRememberPwd;
	

	private Context mContext=this;
	public static final String  PASSWORD_SAVE="1";
	public static final String  PASSWORD_NO_SAVE="0";
	private HashMap<String, String> mMapLogin=new HashMap<String, String>();
	private Dialog mDialog;
	private SharedPreferences mPreferences = null;
	private String UserName;// 用户名
	private String Password;// 密码
	private int IsSave;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		MApplication.getInstance().addActivity(this); 
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		initUserInfo();
		setEditTextIndex();
	}
	
	/** 设置光标的位置*/
	private void  setEditTextIndex(){
		mTxtUserName.setSelection(mTxtUserName.getText().toString().length());
	}
	
	/** 
	 * 初始化用户信息*/
	private void initUserInfo(){
		HashMap<String, String > map=new  DBService(mContext).queryLastUser();
		if(map!=null  && map.size()>0){
			mTxtUserName.setText(map.get("loginAccount"));
			if("1".equals(map.get("savePassword"))){
				mTxtPassword.setText(map.get("password"));
				mCBRememberPwd.setChecked(true);
			}else{
				mCBRememberPwd.setChecked(false);
			}
		}
	}
	
	/**
	 * */ 
	public void login(){
		new Thread(new Runnable() {
			@Override
			public void run() {
					HashMap<String, String> mapLogin=new HashMap<String, String>();
					mapLogin.put("userName", UserName);
					mapLogin.put("Password", Password);//
//					mapLogin.put("password", Password);//
					String stream=null;
//					ArrayList<HashMap<String, String>> list;
//					list = new DBService(mContext).getMetroLineID();
//					if(list != null){
//						insertCheckData(list);
//					}
					try {
						stream=WebServiceUtils.requestM(mContext, mapLogin, Constants.METHOD_OF_CLIENT_LOGIN);
						if(stream!=null){
							mMapLogin=new JSONParseUtils().getLoginInfo(stream);
							if(mMapLogin.size()>0){
								String mUserCode = mMapLogin.get("empId");
								if(mUserCode != null) {
									HashMap<String, String> mLoginUser = new DBService(mContext).getCurrentUser(2, UserName);	
									if(mLoginUser != null) {
										//do update
										mLoginUser.put("UserName", UserName);
										mLoginUser.put("UserPassword", Password);
										mLoginUser.put("SavePassword", String.valueOf(IsSave));
										mLoginUser.put("UserCode", mUserCode);
										if(new DBService(LoginAct.this).updateUser(mLoginUser)) {
											saveUserInfo(mLoginUser);//保存用户信息在 SharedPreference
										}else {
											sendMessage(Constants.DEFAULT);
										}
									}else{
										//do insert 
										mLoginUser = new HashMap<String, String>();
										mLoginUser.put("LoginAccount", mMapLogin.get("empAccount"));
										mLoginUser.put("UserName", UserName);
										mLoginUser.put("UserPassword", Password);
										mLoginUser.put("SavePassword", String.valueOf(IsSave));
										mLoginUser.put("UserCode", mUserCode);
										if(new DBService(mContext).insertUser(mLoginUser)) {
											saveUserInfo(mLoginUser);
										}else {
											sendMessage(Constants.DEFAULT);
										}
									}
								}
								sendMessage(Constants.SUCCESS);
							}else{
								sendMessage(Constants.GET_DATA_ERROR);
							}
						}else{
							sendMessage(Constants.DEFAULT);
						}
					} catch (IOException e) {
						e.printStackTrace();
						sendMessage(Constants.CONNECTION_TIMEOUT);
					} catch (XmlPullParserException e) {
						e.printStackTrace();
						sendMessage(Constants.CONNECTION_TIMEOUT);
					}
			}

		}).start();
	}
	
	/**
	 * 保存用户信息
	 * @param mLoginUser
	 */
	public void saveUserInfo(HashMap<String, String> mLoginUser) {
		Editor mEditor = mPreferences.edit();
		mEditor.putString(Constants.PREFERENCES_INFO_USERID, mLoginUser.get("UserCode"));
		mEditor.putString(Constants.PREFERENCES_INFO_USERNAME, mLoginUser.get("UserName"));
		mEditor.commit();
	}
	
	/** 登陆 */
	public void onLogin(View v){
		if(lookLoginInfo()){
			mDialog = new AlertDialog.Builder(mContext).create();
			mDialog.show();
	    	//注意此处要放在show之后 否则会报异常
			mDialog.setContentView(R.layout.common_loading_process_dialog);
			mDialog.setCanceledOnTouchOutside(false);
			login();
		}
	}
/*	*//**
	 * 将红线、车站、隧道的数据插入数据库
	 * @param list
	 *//*
	private void insertCheckData(ArrayList<HashMap<String, String>> list) {
		if(list != null && list.size() > 0) {
			for(HashMap<String, String> map : list) {
				if(map != null) {
					//车站
					HashMap<String, String> StationBaseInfoMap = new HashMap<String, String>();
					StationBaseInfoMap.put("Type", "Station");
					StationBaseInfoMap.put("MetroLineID", map.get("MetroLineId"));
					try {
						String mBackJson = WebServiceUtils.requestM(mContext, StationBaseInfoMap , Constants.METHOD_OF_CLIENT_STATIONINFO);
						HashMap<String,Object> mR = new JSONParseUtils().getBaseInfoStation(mBackJson);
						if(mR != null) {
							String mS = (String)mR.get("s");
							if("ok".equalsIgnoreCase(mS)) {
								ArrayList<HashMap<String, String>> mBaseInfoList = (ArrayList<HashMap<String, String>>)mR.get("v");
								if(mBaseInfoList != null && mBaseInfoList.size() > 0) {
									for(HashMap<String, String> mBaseInfoMap : mBaseInfoList) {
										new DBService(mContext).insertStationBaseInfo(mBaseInfoMap);
									}
								}
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
					//红线
					HashMap<String, String> RedLineBaseInfoMap = new HashMap<String, String>();
					RedLineBaseInfoMap.put("Type", "RedLine");
					RedLineBaseInfoMap.put("MetroLineID", map.get("MetroLineId"));
					try {
						String mBackJson = WebServiceUtils.requestM(mContext, RedLineBaseInfoMap, Constants.METHOD_OF_CLIENT_REDLINE);
						HashMap<String,Object> mR = new JSONParseUtils().getBaseInfoRedLine(mBackJson);
						if(mR != null) {
							ArrayList<HashMap<String, String>> mBaseInfoList = (ArrayList<HashMap<String, String>>)mR.get("v");
							String mS = (String)mR.get("s");
							if("ok".equalsIgnoreCase(mS)) {
								if(mBaseInfoList != null && mBaseInfoList.size() > 0) {
									for(HashMap<String, String> mBaseInfoMap : mBaseInfoList) {
										new DBService(mContext).insertRedLineBaseInfo(mBaseInfoMap);
									}
								}
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
					//隧道
					HashMap<String, String> TunnelBaseInfoMap = new HashMap<String, String>();
					TunnelBaseInfoMap.put("Type", "Tunnel");
					TunnelBaseInfoMap.put("MetroLineID", map.get("MetroLineId"));
					try {
						String mBackJson = WebServiceUtils.requestM(mContext,TunnelBaseInfoMap, Constants.METHOD_OF_CLIENT_TUNNELINFO);
						HashMap<String,Object> mR = new JSONParseUtils().getBaseInfoTunnel(mBackJson);
						if(mR != null) {
							ArrayList<HashMap<String, String>> mBaseInfoList = (ArrayList<HashMap<String, String>>)mR.get("v");
							String mS = (String)mR.get("s");
							if("ok".equalsIgnoreCase(mS)) {
								if(mBaseInfoList != null && mBaseInfoList.size() > 0) {
									for(HashMap<String, String> mBaseInfoMap : mBaseInfoList) {
										new DBService(mContext).insertTunnelBaseInfo(mBaseInfoMap);
									}
								}
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}*/
	
	/** 用户格式信息验证*/
	private boolean lookLoginInfo() {
		if (mCBRememberPwd.isChecked()) {
			IsSave = 1;
		}else{
			IsSave = 0;
		}
		UserName = mTxtUserName.getText().toString();
		Password = mTxtPassword.getText().toString();
		if (UserName == null || "".equals(UserName) || Password == null || "".equals(Password)) {
			mBtnErrorInfo.setVisibility(View.VISIBLE);
			mBtnErrorInfo.setText("登陆信息未填写完整");
			return false;
		}
		mBtnErrorInfo.setVisibility(View.GONE);
		return true;
	}
	
	
	@Override
		protected void handleOtherMessage(int flag) {
			super.handleOtherMessage(flag);
			switch(flag){
				case Constants.SUCCESS:
					Intent service = new Intent(mContext, BaseDataService.class);
					stopService(service);
					startService(service);
					
					if(new DBService(mContext).userIsExist(mTxtUserName.getText().toString().trim())){
						new DBService(mContext).delUser(mTxtUserName.getText().toString().trim());
					}
					HashMap<String, String> mapUser=new HashMap<String, String>();
					mapUser.put("LoginAccount", mMapLogin.get("loginAccount"));
					mapUser.put("UserName", mMapLogin.get("userName"));
					mapUser.put("UserPassword", mMapLogin.get("password"));
					mapUser.put("UserCode", mMapLogin.get("empId"));
					if(mCBRememberPwd.isChecked()){
						mapUser.put("SavePassword", PASSWORD_SAVE);
					}else{
						mapUser.put("SavePassword", PASSWORD_NO_SAVE);
					}
					new DBService(mContext).insertUser(mapUser);
					mDialog.cancel();
					Intent intent=new Intent(mContext,MainAct.class);
					startActivity(intent);
					break;
				case Constants.ERROR:
					mDialog.cancel();
					mBtnErrorInfo.setVisibility(View.VISIBLE);
					mBtnErrorInfo.setText("用户名或密码错误");
					break;
				case Constants.CONNECTION_TIMEOUT:
					mDialog.cancel();
					mBtnErrorInfo.setVisibility(View.VISIBLE);
					mBtnErrorInfo.setText("网络连接超时");
					break;
				case Constants.DEFAULT:
					mDialog.cancel();
					mBtnErrorInfo.setVisibility(View.VISIBLE);
					mBtnErrorInfo.setText("操作失败");
					break;
				case Constants.GET_DATA_ERROR:
					mDialog.cancel();
					mBtnErrorInfo.setVisibility(View.VISIBLE);
					mBtnErrorInfo.setText("获取数据失败");
					break;
			default: break;
			}
		}


}
