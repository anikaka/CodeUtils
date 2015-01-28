package com.tongyan.yanan.act;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author ChenLang
 * @category Login Layout
 * @category 程序入口,编码格式UTF-8
 * @date   2014/06/03-
 * @version  1.0
 * 
 */
public class LoginAct extends  FinalActivity {
	
    //获取组件的id
	@ViewInject(id=R.id.editUser_login)  EditText  mTxtLogin;
	@ViewInject(id=R.id.editPwd_login)	   EditText  mtTxtPwd;
	@ViewInject(id=R.id.cbSelected) CheckBox  mCbSelect; 
	@ViewInject(id=R.id.butCommit, click="commit")  Button  mButCommit;
	@ViewInject(id=R.id.title_common_content)  TextView  mTitleName;
	@ViewInject(id=R.id.setting_url_port, click="setServerClick") TextView mSetServer;
	
	
	private String mAccount; 
	private String mPassword;
	
	private  int  mLoginState = NO_SAVE_PASSWORD; 
	private final static int  NO_SAVE_PASSWORD = 0;
	private final static int  IS_SAVE_PASSWORD = 1;
	
    private Dialog mDialog = null;
    private Context mContext = this;
    private String mUserId = null;
    private String mBackInfo = "";
    private SharedPreferences mPreferences = null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		initUserInfo();
		setLocation();
	}
	
	/** 
	 * 登录
	 * 1.登录验证
	 * 2.验证通过后加载合同段期段数据(删除所有数据，把最新加载的数据插入数据库中)
	 *  */
	private synchronized void login() {
		mAccount = mTxtLogin.getText().toString();
		mPassword = mtTxtPwd.getText().toString();
		//客户是否选择记住密码复选框
		if(mCbSelect.isChecked()){
			mLoginState = IS_SAVE_PASSWORD;
		} else {
			mLoginState = NO_SAVE_PASSWORD;
		}
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("method", Constants.METHOD_OF_LOGIN);
		params.put("key", Constants.PUBLIC_KEY);
		params.put("userName", mAccount);
		params.put("password", mPassword);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String mUrl = HttpUtils.getUrlWithParas(Constants.LOGIN_URL, params, mContext);
				Log.i("test", "url"+mUrl);
				try {
					String mResponseBody = HttpUtils.httpGetString(mUrl);
					HashMap<String, Object> mR = new JsonTools().getLoginMap(mResponseBody);
					mBackInfo = (String)mR.get("s");
					if ("ok".equalsIgnoreCase(mBackInfo)) {
						HashMap<String, String> mUMap = (HashMap<String, String>) mR.get("v");
						if (mUMap != null) {
							mUMap.put("Password", mPassword);
							mUMap.put("SavePassword", String.valueOf(mLoginState));
							mUserId = mUMap.get("UserId");
							HashMap<String, String> mLoginMap = new DBService(mContext).queryUserByAccount(mAccount);
							//请求数据，并更新数据
							HashMap<String, String> param = new HashMap<String, String>();
							param.put("method", Constants.METHOD_OF_TERMPART_COMPACT);
							param.put("key", Constants.PUBLIC_KEY);
							param.put("userId", mUserId);
							param.put("fieldList", "NewId,ProjectId,LotName,LotCode,CompactionUnit,SupervisorUnit,ProjectName,ProjectCount,ProjectArea,StationArea");
							//标段返回值
							String mTermResponseBody = HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_PACTSELECT, param, mContext));
							HashMap<String, Object> mTermBaseData = JsonTools.getBaseData(mTermResponseBody);
							if(mTermBaseData != null && "ok".equalsIgnoreCase((String)mTermBaseData.get("s"))) {
								//insertTermPartPact
								ArrayList<HashMap<String, String>> list = (ArrayList<HashMap<String, String>>)mTermBaseData.get("v");
								if(list != null && list.size() > 0) {
									if(new DBService(mContext).delAll(Constants.TABLE_TERMPART_PACT)) {
										if(new DBService(mContext).insertTermPartPact(list)) {
											loginSend(mLoginMap,mUMap);
										}
									} else {
										sendFMessage(Constants.OPERATION_ERROR);
									}
								} else {
									sendFMessage(Constants.OPERATION_ERROR);
								}
							} else {
								sendFMessage(Constants.DATA_EMPTY);
							}
							
						} else {
							sendFMessage(Constants.OPERATION_ERROR);//返回数据为空
						}
					} else {
						mBackInfo = (String)mR.get("v");
						if(mBackInfo == null || "".equals(mBackInfo)) {
							sendFMessage(Constants.OPERATION_ERROR);//返回后台提示信息
						} else {
							sendFMessage(Constants.ERROR);//返回后台提示信息
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (e instanceof IOException) {
						sendFMessage(Constants.CONNECTION_TIMEOUT);
					} else if (e instanceof JSONException) {
						sendFMessage(Constants.OPERATION_ERROR);//字符解析失败
					} else if(e instanceof NullPointerException){
						sendFMessage(Constants.OPERATION_ERROR);//返回数据为空
					} else {
						sendFMessage(Constants.OPERATION_ERROR);
					}
					
				}
			}
		}).start();
	}
	
	
	public void loginSend(HashMap<String, String> mLoginMap,HashMap<String, String> mUMap) {
		if (mLoginMap != null && mAccount.equals(mLoginMap.get("LoginAccount"))) {
			if (new DBService(mContext).updateUserState(mPassword, mLoginState, mAccount)) {
				saveUserInfoPreferences(mUMap.get("UserName"));
				sendFMessage(Constants.SUCCESS);
			} else {
				sendFMessage(Constants.OPERATION_ERROR);
			}
		} else {
			if (new DBService(mContext).insertUser(mUMap)) {
				saveUserInfoPreferences(mUMap.get("UserName"));
				sendFMessage(Constants.SUCCESS);
			} else {
				sendFMessage(Constants.OPERATION_ERROR);
			}
		}
	}
	/**
	 * 设置服务器路径
	 */
	public void setServerClick(View v) {
		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.show();
    	dialog.setContentView(R.layout.common_route_input_dialog);
    	Button mOkBtn = (Button) dialog.findViewById(R.id.common_save_btn);
    	Button mCancleBtn = (Button) dialog.findViewById(R.id.common_clear_btn);
    	TextView mTitleText = (TextView) dialog.findViewById(R.id.title_common_content);
    	final EditText mContentText = (EditText) dialog.findViewById(R.id.common_content_edit);
    	mTitleText.setText("设置服务器路径");
    	
    	final SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    	String mPath = mPreferences.getString(Constants.PREFERENCES_INFO_ROUTE, "");
    	if(mPath == null || "".equals(mPath)) {
    		mPath = Constants.COMMON_URL_IP;
    	}
    	mContentText.setText(mPath);
    	mContentText.setSelection(mPath.length());
    	mOkBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				String s = mContentText.getText().toString();
				Editor mEditor = mPreferences.edit();
				mEditor.putString(Constants.PREFERENCES_INFO_ROUTE, s);
				mEditor.commit();
			}
		});
    	
    	mCancleBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
	
	
	/**
	 * 保存当前用户信息
	 * @param mUserName
	 */
	public void saveUserInfoPreferences(String mUserName) {
		Editor mEditor = mPreferences.edit();
		mEditor.putString(Constants.PREFERENCES_INFO_USERID, mUserId);
		mEditor.putString(Constants.PREFERENCES_INFO_USERNAME, mUserName);
		mEditor.putString(Constants.PREFERENCES_INFO_ACCOUNT, mAccount);
		mEditor.commit();
	}
	
	
	protected void handleOtherMessage(int flag) {
		switch(flag){
		case Constants.SUCCESS:
				if(mDialog!=null){				
					mDialog.dismiss();
				}
				Intent intent = new Intent(LoginAct.this, MainAct.class);
				startActivity(intent);
				overridePendingTransition(R.anim.out_from_right_,R.anim.out_to_left);
			break;
		case Constants.ERROR:
			if(mDialog!=null){				
				mDialog.dismiss();
			}
			Toast.makeText(LoginAct.this, mBackInfo, Toast.LENGTH_SHORT).show();
			break;
		case Constants.CONNECTION_TIMEOUT:
			if(mDialog!=null){				
				mDialog.dismiss();
			}
			Toast.makeText(LoginAct.this, "访问超时,请检查网络", Toast.LENGTH_SHORT).show();
			break;
		case Constants.OPERATION_ERROR :
			if(mDialog!=null){				
				mDialog.dismiss();
			}
			Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT).show();
			break;
		case  Constants.DATA_EMPTY:
//			Toast.makeText(mContext, "无标段信息显示", Toast.LENGTH_SHORT).show();
		}
	};
	
	/** 登陆按钮点击事件*/
	public void commit(View view) {
		if (lookLoginInfo()) {
//			showRoundProcessDialog(this, R.layout.loading_anim);
			mDialog=new Dialog(mContext, R.style.dialog);
			mDialog.setCanceledOnTouchOutside(false);
			mDialog.setContentView(R.layout.common_normal_progressbar);
	        mDialog.show();
			login();
		} 
	}
 
	
	/** 当进入登陆界面,初始化*/
	private void initUserInfo() {
		mTitleName.setText(getResources().getString(R.string.loing_text));//设置登录标题
		mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		HashMap<String, String> mUserMap = new DBService(this).queryUser();
		if (mUserMap != null) {
			if ("1".equals(mUserMap.get("SavePassword"))) {
				mTxtLogin.setText(mUserMap.get("LoginAccount"));
				mtTxtPwd.setText(mUserMap.get("Password"));
				mLoginState = IS_SAVE_PASSWORD;
				mCbSelect.setChecked(true);
			} else {
				mTxtLogin.setText(mUserMap.get("LoginAccount"));
				mLoginState = NO_SAVE_PASSWORD;
				mCbSelect.setChecked(false);
			}
		}
	}

	/** 登录验证*/
	private boolean lookLoginInfo() {
		mAccount = mTxtLogin.getText().toString();
		mPassword = mtTxtPwd.getText().toString();
		if (mAccount == null || "".equals(mAccount)) {
			Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (mPassword == null || "".equals(mPassword)) {
			Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	/**把输入框的光标移动到最后面 */
	private  void setLocation(){
		CharSequence  mTextLogin=mTxtLogin.getText();
		CharSequence  mTextPwd=mTxtLogin.getText();
		if(mTextLogin instanceof Spannable || mTextPwd instanceof  Spannable){
			Spannable mSpanTextLogin=(Spannable)mTextLogin;
			Spannable mSpanTextPwd=(Spannable)mTextPwd;
			Selection.setSelection(mSpanTextLogin, mTextLogin.length());
			Selection.setSelection(mSpanTextPwd, mTextLogin.length());
		}
	}
	
	/** 登录请求动画*/
    public void showRoundProcessDialog(Context mContext, int layout){
        OnKeyListener mKeyListener = new OnKeyListener(){
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event){
                if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_SEARCH){
                    return true;
                }
                return false;
            }
        };
        mDialog = new AlertDialog.Builder(mContext).create();
        mDialog.setOnKeyListener(mKeyListener);
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams  mParams= window.getAttributes();
        mParams.y=350;
        mDialog.onWindowAttributesChanged(mParams);
        mDialog.onWindowFocusChanged(true);
        mDialog.show();
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setContentView(layout);
    }

}
