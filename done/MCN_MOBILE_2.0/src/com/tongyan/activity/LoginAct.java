package com.tongyan.activity;

import java.io.File;
import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import com.tongyan.activity.secamera.SectionIndexNoNetAct;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.db.DBService;
import com.tongyan.common.entities._Contacts;
import com.tongyan.common.entities._HolefaceSetting;
import com.tongyan.common.entities._ItemSec;
import com.tongyan.common.entities._LocalMsg;
import com.tongyan.common.entities._Project;
import com.tongyan.common.entities._User;
import com.tongyan.service.MGPSService;
import com.tongyan.utils.CommonUtils;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.FileUtils;
import com.tongyan.utils.ImageUtil;
import com.tongyan.utils.WebServiceUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @ClassName P02_LoginAct 
 * @author wanghb
 * @date 07-12-2013 14:35
 * @Desc login(initialize User、& start a server to get global positioning system info)
 */
public class LoginAct extends AbstructCommonActivity {
	
	public static final int MSG_0x2001 = 0x2001;
	public static final int MSG_0x2002 = 0x2002;
	public static final int MSG_0x2003 = 0x2003;
	public static final int MSG_0x2004 = 0x2004;
	public static final int MSG_0x2005 = 0x2005;
	public static final int MSG_0x2006 = 0x2006;
	public static final int MSG_0x2007 = 0x2007;
	public static final int MSG_0x2008 = 0x2008;
	public static final int MSG_0x2009 = 0x2009;
	public static final int MSG_0x200A = 0x200A;
	public static final int MSG_0x200B = 0x200B;
	public static final int MSG_0x200C = 0x200C;
	
	public static final int MSG_0x200D = 0x200D;
	public static final int MSG_0x200E = 0x200E;
	
	
	private Context mContext = this;
	private EditText mUsernameEdit;
	private EditText mPasswordEdit;
	private Button mLoginBtn;
	private CheckBox checkBox, mAutoLoginBox;
	private TextView mSetting;
	private String mUsernameText;
	private String mPasswordText;
	private Integer savePwd;
	private Integer autoLogin = 0;
	
	
	private Map<String, String> loginBackMap;
	private _User localUser;
	private MyApplication mApplication;
	private Dialog mDialog;
	
	private SharedPreferences mPreferences;
	
	
	private Button mSectionBtn = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		mUsernameEdit = (EditText)findViewById(R.id.p02_login_username);
		mPasswordEdit = (EditText)findViewById(R.id.p02_login_password_edit);
		mLoginBtn = (Button)findViewById(R.id.p02_login_loging_btn);
		checkBox = (CheckBox)findViewById(R.id.p02_login_checkbox_savepwd);
		mAutoLoginBox = (CheckBox)findViewById(R.id.p02_login_checkbox_auto_login);
		mSetting = (TextView)findViewById(R.id.p02_logig_setting);
		
		mSectionBtn = (Button)findViewById(R.id.gps_section_carema);
		mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		setClickListener();
		businessM();
	}
	private void setClickListener() {
		mLoginBtn.setOnClickListener(onLoginClick);
		mSetting.setOnClickListener(onSettingClick);
		mUsernameEdit.setInputType(InputType.TYPE_NULL);//首次禁用软键盘
		mUsernameEdit.setCursorVisible(false);//去光标
		checkBox.setOnCheckedChangeListener(boxListener);
		mAutoLoginBox.setOnCheckedChangeListener(mAutoLoginListener);
		mUsernameEdit.setOnTouchListener(editTouchListener);
		mSectionBtn.setOnClickListener(onSectionCameraClick);
	}
	
	/**
	 * 输入框-点击触摸监听事件
	 */
	OnTouchListener editTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			mUsernameEdit.setInputType(InputType.TYPE_CLASS_TEXT);
			mUsernameEdit.setCursorVisible(true);
			return false;
		}
	};
	
	private void businessM() {
		mApplication = ((MyApplication)getApplication());
		mApplication.addActivity(this);
		localUser = mApplication.localUser;
		if(localUser != null) {
			mUsernameText = localUser.getUsername();
			mUsernameEdit.setText(mUsernameText);
			mPasswordText = localUser.getPassword();
			savePwd = localUser.getSavepassword();
			autoLogin = localUser.getAutologin();
		}
		
		if(savePwd != null &&  savePwd == 1) {
			mPasswordEdit.setText(mPasswordText);
			checkBox.setChecked(true);
			//savePwd = 1;
		}
		
		if(autoLogin != null && autoLogin == 1) {
			mAutoLoginBox.setChecked(true);
		}
		
	}
	
	OnCheckedChangeListener boxListener = new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
			if(isChecked) {
				savePwd = 1;
			} else {
				savePwd = 0;
			}
		}
	};
	
	OnCheckedChangeListener mAutoLoginListener = new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
			if(isChecked) {
				autoLogin = 1;
			} else {
				autoLogin = 0;
			}
		}
	};
	
	OnClickListener onSectionCameraClick = new OnClickListener(){
		@Override
		public void onClick(View v) {
			//验证标段数据是否存在
			if(new DBService(mContext).isEmpty("LOCAL_ITEM_SECTION")) {
				Toast.makeText(mContext, "请先登录获取标段数据", Toast.LENGTH_SHORT).show();
				return;
			}
			//验证工程编号数据库是否存在
			String cnSectionOA = FileUtils.getSDCardPath() + Constansts.CN_DB_PATH + Constansts.CN_SECTION_DB_NAME;
			File file = new File(cnSectionOA);
			if(file != null && !file.exists()) {
				Toast.makeText(mContext, "请下载工程编号数据", Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent = new Intent(mContext, SectionIndexNoNetAct.class);
			startActivity(intent);
		}
	};
	
	
	OnClickListener onLoginClick = new OnClickListener(){
		public void onClick(View v) {
			boolean isSuc = checkLogin();//登录校验
			if(isSuc) {
				mDialog = new AlertDialog.Builder(mContext).create();
				mDialog.show();
		    	//注意此处要放在show之后 否则会报异常
				mDialog.setContentView(R.layout.common_loading_process_dialog);
				mDialog.setCanceledOnTouchOutside(false);
				
				new Thread(new Runnable() {
					@Override
					public void run() {
						//登录后台验证
						mUsernameText = mUsernameEdit.getText().toString();
						mPasswordText = mPasswordEdit.getText().toString();
						try {
							String str = WebServiceUtils.getRequestStr(mUsernameText, mPasswordText, null, null, null, null, Constansts.METHOD_OF_LOGIN,mContext);
							if(str != null) {
								loginBackMap = new Str2Json().getLoginInfo(str);
								
								//sectionId
								if(loginBackMap != null && "ok".equals(loginBackMap.get("succ"))) {//登录成功
									
									sendMessage(Constansts.SUCCESS);
									String userid = loginBackMap.get("empId");
									//User route
									getUserRoute(mUsernameText, mPasswordText,userid);
									//project
									initProject();
									//Item-Section
									initItemSection();
									//Contacts
									initConcacts();
									//location msg
									initLocMsg();
									//risk
									initRisk();
									//upload photos of project code
									commitPhoto();
									//commit project collection points
									commitProjectPoints();
									
									sendMessage(MSG_0x200B);
								} else {//登录失败
									sendMessage(Constansts.MES_TYPE_2);
								}
							} else {
								sendMessage(Constansts.MES_TYPE_1);
							}
						} catch (Exception e) {
							sendMessage(Constansts.ERRER);
							e.printStackTrace();
						}
					}
				}).start();
			}
		}
	};
	//2014-03-19 just do it
	OnClickListener onSettingClick = new OnClickListener(){
		public void onClick(View v) {
			final SharedPreferences mShared = PreferenceManager.getDefaultSharedPreferences(mContext);
			String mRoute = mShared.getString("ROUTE_OF_SERVICE", "");
			if(mRoute == null || "".equals(mRoute)) {
				Editor mEditor = mShared.edit();
				mEditor.putString("ROUTE_OF_SERVICE", Constansts.SERVER_URL_IP_PORT);
				mEditor.commit();
				mRoute = mShared.getString("ROUTE_OF_SERVICE", "");
			} 
			final Dialog mSettingDialog =  new Dialog(mContext);
			mSettingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mSettingDialog.show();
	    	//注意此处要放在show之后 否则会报异常
			mSettingDialog.setContentView(R.layout.login_route_setting_dialog);
			
			final EditText mEditText = (EditText)mSettingDialog.findViewById(R.id.p02_route_edittext);
			Button mSureBtn = (Button)mSettingDialog.findViewById(R.id.p02_route_sure_btn);
			Button mCancelBtn = (Button)mSettingDialog.findViewById(R.id.p02_route_cancel_btn);
			if(mRoute != null && !"".equals(mRoute)) {
				mEditText.setText(mRoute);
				mEditText.setSelection(mEditText.getText().length()); 
			} 
			mSureBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Editor mEditor = mShared.edit();
					mEditor.putString("ROUTE_OF_SERVICE", mEditText.getText().toString());
					mEditor.commit();
					if(mSettingDialog != null)
						mSettingDialog.dismiss();
				}
			});
			mCancelBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mSettingDialog != null)
					mSettingDialog.dismiss();
				}
			});
		}
	};
	/**
	 * 登录验证
	 * @return
	 */
	private boolean checkLogin() {
		mUsernameText = mUsernameEdit.getText().toString();
		mPasswordText = mPasswordEdit.getText().toString();
		if(mUsernameText == null || "".equals(mUsernameText)) {
			Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(mPasswordText == null || "".equals(mPasswordText)) {
			Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	/**
	 * 上传工程编号 采点数据
	 */
	public void commitProjectPoints() {
		final ArrayList<HashMap<String, String>> mPointList = new DBService(mContext).getUpdatePointsList();
		if(mPointList != null && mPointList.size() > 0) {
			 sendMessage(MSG_0x200E);
			for(HashMap<String, String> m : mPointList) {
				if(m != null) {
					String param = "{ProjectId:'"+ m.get("project_code_id") +"', GeographyString:'LINESTRING ("+ m.get("content") +")'}";
					try {
						String str = WebServiceUtils.getRequestStr(mUsernameText, mPasswordText, null, null, "ProjectGeometryInfo", param, Constansts.METHOD_OF_UPDATE, mContext);
						if(new Str2Json().commonJsonParse(str)) {
							new DBService(mContext).deleteProjectPoints(m.get("project_code_id"));
						}
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
				
			}
		}
	}
	
	
	
	/**
	 * 上传工程编号所拍的图片
	 */
	public void commitPhoto() {
		//TODO
		 ArrayList<HashMap<String, String>> mLocalImageList = new DBService(mContext).getSectionImageList(null);
		 if(mLocalImageList != null && mLocalImageList.size() > 0) {
			 sendMessage(MSG_0x200D);
			 for(HashMap<String, String> map : mLocalImageList) {
				 if(map != null) {
					 String mLocalPath = map.get("local_img_path");
						if(mLocalPath != null) {
							File file = new File(mLocalPath);
							if(file.exists()) {
								ImageUtil utils = new ImageUtil();
								SoftReference<Bitmap> bmp2 = new SoftReference<Bitmap>(utils.getBmpByPath(file.getPath()));
								String changeStr = Base64.encodeToString(utils.Bitmap2Bytes(bmp2.get()), 0);
								Map<String, String> properties = new HashMap<String, String>();
								properties.put("publicKey", Constansts.PUBLIC_KEY);
								properties.put("userName", mUsernameText);
								properties.put("Password", mPasswordText);
								properties.put("img", changeStr);
								properties.put("fileName", map.get("photo_name"));
								properties.put("objid", map.get("new_id"));
								try {
									String strJ = WebServiceUtils.requestM(properties, Constansts.METHOD_OF_ATTACHMENTUPLOAD, mContext);
									 Map<String, String> mBackMap = new Str2Json().getSectionUpdateBack(strJ);
									if(mBackMap != null && "ok".equalsIgnoreCase(mBackMap.get("s"))) {
										if(new DBService(mContext).updateImageState("1", mBackMap.get("v"), map.get("ID"))) {
											//上传成功
										} else {
											//sendMessage(Constansts.MES_TYPE_1);
										}
									} else {
										//sendMessage(Constansts.MES_TYPE_1);
									}
								} catch (Exception e) {
									e.printStackTrace();
									//sendMessage(Constansts.MES_TYPE_1);
								} 
							} else {
								//TODO delete
							}
						} else {
							//TODO delete
						}
				 }
			 }
			 
		 }
		
	}
	
	private void loginSuc() {
		if (loginBackMap != null) {
			String userid = loginBackMap.get("empId");
			if (userid != null && !"".equals(userid.trim())) {
				localUser = new DBService(mContext).findUser(userid);
				_User _u = new _User();
				_u.setDepartment(loginBackMap.get("dptName"));
				_u.setUserid(userid);
				_u.setDptRowId(loginBackMap.get("dptRowId"));
				_u.setEmpLevel(loginBackMap.get("empLevel"));
				_u.setEmpName(loginBackMap.get("empName"));
				_u.setUsername(mUsernameText);
				_u.setPassword(mPasswordText);
				_u.setSavepassword(savePwd);
				_u.setAutologin(autoLogin);
				if (localUser == null) {
					new DBService(mContext).insertUser(_u);
				} else {
					new DBService(mContext).updateLoginType(_u);
				}
			} else {
				Toast.makeText(this, "登录失败，请重试", Toast.LENGTH_SHORT).show();
				return;
			}
			// 登录验证成功-重新配置系统级变量
			localUser = new DBService(mContext).findUser(userid);
			((MyApplication) getApplication()).localUser = localUser;
			((MyApplication) getApplication()).mIsLogin = true;
			mApplication.userId = userid;
			
			Editor mEditor = mPreferences.edit();
			mEditor.putString(Constansts.INFO_USER_ACCOUNT, localUser.getUsername());
			mEditor.putString(Constansts.INFO_USER_ID, localUser.getUserid());
			mEditor.putString(Constansts.INFO_USER_NAME, localUser.getEmpName());
			mEditor.putString(Constansts.INFO_USER_PASSWORD, localUser.getPassword());
			mEditor.commit();
			// open gps
			if (localUser.getIsGps() != null && 1 == localUser.getIsGps()) {
				notificationGPS();
			}
		}
	}
	
	/**
	 *  获取GPS现在的状态（打开或是关闭状态）
	 */
	public void notificationGPS(){
		if (!CommonUtils.getLocationServiceState(mContext)) {
			Toast.makeText(this, "请打开GPS", Toast.LENGTH_SHORT).show();
		} else {
			//Intent servicIntentStart = new Intent(this,GPSService.class);
			Intent servicIntentStart = new Intent(this,MGPSService.class);
			stopService(servicIntentStart);
			startService(servicIntentStart);
		}
	}
	/**
	 * 权限
	 */
	public void getUserRoute(String mUsernameText,String mPasswordText,String userid) {
		Map<String,Object> mR = null;
		try {
			Map<String,String> properties = new HashMap<String,String>();
			properties.put("publicKey", Constansts.PUBLIC_KEY);
			properties.put("userName", mUsernameText);
			properties.put("Password", mPasswordText);
			properties.put("type", "GetRole");//
			properties.put("id", userid);
			String jsonStr = WebServiceUtils.requestM(properties, Constansts.METHOD_OF_GETCONTENT, mContext);
			mR = new Str2Json().getUserRoute(jsonStr);
		} catch(Exception e) {
			sendMessage(MSG_0x200C);
			e.printStackTrace();
		}
		if(mR != null) {
			String isSucc = (String) mR.get("s");
			if ("ok".equals(isSucc)) {
				ArrayList<HashMap<String, String>> mRouteList = (ArrayList<HashMap<String, String>>) mR.get("v");
				if(mRouteList != null && mRouteList.size() > 0) {
					SharedPreferences mShared = PreferenceManager.getDefaultSharedPreferences(mContext);
					Editor mEditor = mShared.edit();
					for(HashMap<String, String> map : mRouteList) {
						if(map != null) {
							if(map.get("menu") != null && !"".equals(map.get("menu"))) {
								mEditor.putString(map.get("menu"), map.get("role"));
							}
						}
					}
					mEditor.commit();
				} else {
					sendMessage(MSG_0x200C);
				}
			} else {
				sendMessage(MSG_0x200C);
			}
		} else {
			sendMessage(MSG_0x200C);
		}
	}
	/**
	 * 初始化项目数据
	 */
	public void initProject() {
		if(new DBService(mContext).isEmpty("LOCAL_PROJECT") || (localUser.getIsUpdate() != null && 1 == localUser.getIsUpdate())) {
			getRemoteProject();
		} 
	}
	public void getRemoteProject() {
		Map<String,Object> mR = null;
		try {
			String params = "{updatetime:''}";
			String str = WebServiceUtils.getRequestStr(localUser.getUsername(), localUser.getPassword(), null, null, "GetUnitNew", params, Constansts.METHOD_OF_GETLISTNOPAGE,mContext);
			mR = new Str2Json().getProjectList(str);
		} catch(Exception e) {e.printStackTrace();}
		if (mR != null) {
			String isSucc = (String) mR.get("s");
			if ("ok".equals(isSucc)) {
				List<_Project> proList = (List<_Project>) mR.get("v");
				if (new DBService(mContext).delAll("LOCAL_PROJECT")) {
					if(proList != null && proList.size() > 0) {
						for (int i = 0; i < proList.size(); i++) {
							if (proList.get(i) != null) {
								new DBService(mContext).insertProject(proList.get(i));
							}
						}
						if(new DBService(mContext).updateProjectTime(localUser.getUserid(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))) {
							sendMessage(MSG_0x2001);
						} else {
							sendMessage(MSG_0x2002);
						}
					}
				}
			}
		}
	  }
	
	/**
	 * 初始化工程标段
	 */
	public void initItemSection() {
		if(new DBService(mContext).isEmpty("LOCAL_ITEM_SECTION") || (localUser.getIsUpdate() != null && 1 == localUser.getIsUpdate())) {
			getRemoteItemSec();
		} 
	}
	public void getRemoteItemSec() {
		Map<String,Object> mR = null;
		try {
			String str = WebServiceUtils.getRequestStr(localUser.getUsername(), localUser.getPassword(),null, null, null, null,Constansts.METHOD_OF_GETPROJECT,mContext);
			mR = new Str2Json().getItemSecInfo(str);
		} catch(Exception e) {e.printStackTrace();}
		if(mR != null) {
			String isSucc = (String)mR.get("s");
			if("ok".equals(isSucc)) {
				List<_ItemSec> itemSecList = (List<_ItemSec>)mR.get("v");
					if (new DBService(mContext).delAll("LOCAL_ITEM_SECTION")) {
						for(int i = 0; i < itemSecList.size(); i ++) {
							_ItemSec itemSec = itemSecList.get(i);
							if(itemSec != null) {
								new DBService(mContext).insertItemSec(itemSec);
							}
						}
						if(new DBService(mContext).updateItemSecTime(localUser.getUserid(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))) {
							sendMessage(MSG_0x2003);
						} else {
							sendMessage(MSG_0x2004);
						}
				}
			}
		}
	}
	/**
	 * 初始化通讯录数据
	 */
	public void initConcacts() {
			if (new DBService(mContext).isEmpty("LOCAL_CONTACTS") || (localUser.getIsUpdate() != null && 1 == localUser.getIsUpdate())) {
				getRemoteContacts();
			} 
	}
	public void getRemoteContacts() {
		Map<String,Object> mR = null;
		try {
			String str = WebServiceUtils.getRequestStr(localUser.getUsername(), localUser.getPassword(), null, null, null, null, Constansts.METHOD_OF_GETLINKMAN,mContext);
			mR = new Str2Json().getLinkManInfo(str);
		} catch(Exception e) {e.printStackTrace();}
		if (mR != null) {
			String isSucc = (String)mR.get("s");
			if ("ok".equals(isSucc)) {
				List<_Contacts> contactsList = (List<_Contacts>)mR.get("v");
				//List<_ContactsData> newlist = new ContactsUtils().getDepartmentContacts(contactsList);
				if(contactsList != null && contactsList.size() > 0) {
					if (new DBService(mContext).delAll("LOCAL_CONTACTS")) {
						if(new DBService(mContext).insertContacts(contactsList)) {
							if(new DBService(mContext).updateContactTime(localUser.getUserid(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))) {
								sendMessage(MSG_0x2005);
							} else {
								sendMessage(MSG_0x2006);
							}
						}
					}
				}
				/*if (new DBService(mContext).delAll("LOCAL_CONTACTS")) {
					if (newlist != null && newlist.size() > 0) {
						for (int i = 0; i < newlist.size(); i++) {
							_ContactsData contacts = newlist.get(i);
							if (contacts != null) {
								new DBService(mContext).insertContacts(contacts);
							}
						}
						if(new DBService(mContext).updateContactTime(localUser.getUserid(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))) {
							sendMessage(MSG_0x2005);
						} else {
							sendMessage(MSG_0x2006);
						}
					}
				}*/
			}
		}
	}
	
	
	/**
	 * 初始化风险数据
	 */
	public void initRisk() {
		if(new DBService(mContext).isEmpty("LOCAL_RISK_SETTINGS") || (localUser.getIsUpdate() != null && 1 == localUser.getIsUpdate())) {getRemoteRisk();} 
	}
	public void getRemoteRisk() {
		Map<String,String> properties = new HashMap<String,String>();
		properties.put("publicKey", Constansts.PUBLIC_KEY);
		properties.put("userName", localUser.getUsername());
		properties.put("Password", localUser.getPassword());
		properties.put("type", "RiskItme");//
		properties.put("id", "");
		Map<String,Object> mR = null;
		try {
			String jsonStr = WebServiceUtils.requestM(properties, Constansts.METHOD_OF_GETCONTENT, mContext);
			mR = new Str2Json().getRiskSettings(jsonStr);
		} catch (Exception e) {e.printStackTrace();}
		if(mR != null) {
			String isSucc = (String)mR.get("s");
			if ("ok".equals(isSucc)) {
				List<_HolefaceSetting> mHolefaceSettingList = (List<_HolefaceSetting>)mR.get("v");
				if(mHolefaceSettingList != null && mHolefaceSettingList.size() > 0) {
					if (new DBService(mContext).delAll("LOCAL_RISK_SETTINGS")) {
					for(int i = 0; i < mHolefaceSettingList.size(); i ++) {
						_HolefaceSetting mHolefaceSetting = mHolefaceSettingList.get(i);
						if(mHolefaceSetting != null) {
							Long $id = new DBService(mContext).insertHolefaceSetting(mHolefaceSetting);
							if("2".equals(mHolefaceSetting.getIsChild()) && null != mHolefaceSetting.getmClildList()) {
								if(mHolefaceSetting.getmClildList().size() > 0) {
									for(int j = 0; j < mHolefaceSetting.getmClildList().size(); j ++) {
										_HolefaceSetting mSHolefaceSetting = mHolefaceSetting.getmClildList().get(j);
										mSHolefaceSetting.set$cId($id.toString());
										new DBService(mContext).insertHolefaceSetting(mSHolefaceSetting);
									}
								}
							}
						}
					}
						if(new DBService(mContext).updateRiskTime(localUser.getUserid(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))) {
							sendMessage(MSG_0x2007);
						} else {
							sendMessage(MSG_0x2008);
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * 初始化定位数据
	 */
	public void initLocMsg() {
		try {
			List<_LocalMsg> locList = new DBService(mContext)
					.selectLocalMsg();
			if (locList != null && locList.size() > 0) {
				for (int i = 0; i < locList.size(); i++) {
					_LocalMsg msg = locList.get(i);
					//Log.i("P02_LoginAct", "LocalMsg:" + msg.getParams());
					if (msg != null) {
						_User user = new DBService(mContext).findUser(msg.getUsrid());
						if (user != null) {
							Map<String, String> properties = new HashMap<String, String>();
							properties.put("publicKey", Constansts.PUBLIC_KEY);
							properties.put("userName", user.getUsername());
							properties.put("Password", user.getPassword());
							properties.put("type", "AddGpsData");
							properties.put("parms", msg.getParams());
							String str = WebServiceUtils.requestM(properties, Constansts.METHOD_OF_ADD, mContext);
							//Log.i("P02_LoginAct", "str:" + str);
							Map<String,Object> mR = new Str2Json().addResult(str);
							String isSuc = "";
							if (mR != null)
								isSuc = (String)mR.get("s");
							if ("ok".equals(isSuc)) {
								if(new DBService(mContext).deleteLocalMsg(String.valueOf(msg.get_id()))) {
									sendMessage(MSG_0x2009);
								} else {
									sendMessage(MSG_0x200A);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {e.printStackTrace();} 
	}
	
	/*protected void dialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setMessage("确认退出吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
				//Intent servicIntentStart = new Intent(mContext,GPSService.class);
				Intent servicIntentStart = new Intent(mContext,MGPSService.class);
				stopService(servicIntentStart);
				
				ActivityManager activityMgr= (ActivityManager) getApplication().getSystemService(ACTIVITY_SERVICE); 
				activityMgr.killBackgroundProcesses(getPackageName()); 
				
				mApplication.exit();
			}
		});
		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}*/

	/*public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			dialog();
		}
		return false;
	}*/
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			loginSuc();
			break;
		case Constansts.ERRER:
			if (mDialog != null)
				mDialog.dismiss(); 
			Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.MES_TYPE_2:
			if (mDialog != null)
				mDialog.dismiss(); 
			Toast.makeText(this, "登录失败，请检查用户名和密码", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.MES_TYPE_1 :
			if (mDialog != null)
				mDialog.dismiss(); 
			Toast.makeText(mContext, "请配置服务器路径", Toast.LENGTH_SHORT).show();
			break;
		case MSG_0x2001 :
			Toast.makeText(mContext, "初始化项目数据成功", Toast.LENGTH_SHORT).show();
			break;
		case MSG_0x2002 :
			Toast.makeText(mContext, "初始化项目数据失败，请在设置菜单重新获取", Toast.LENGTH_SHORT).show();
			break;
		case MSG_0x2003 :
			Toast.makeText(mContext, "初始化工程标段数据成功", Toast.LENGTH_SHORT).show();
			break;
		case MSG_0x2004 :
			Toast.makeText(mContext, "初始化工程标段数据失败，请在设置菜单重新获取", Toast.LENGTH_SHORT).show();
			break;
		case MSG_0x2005 :
			Toast.makeText(mContext, "初始化通讯录数据成功", Toast.LENGTH_SHORT).show();
			break;
		case MSG_0x2006 :
			Toast.makeText(mContext, "初始化通讯录数据失败，请在设置菜单重新获取", Toast.LENGTH_SHORT).show();
			break;
		case MSG_0x2007 :
			Toast.makeText(mContext, "初始化风险数据成功", Toast.LENGTH_SHORT).show();
			break;
		case MSG_0x2008 :
			Toast.makeText(mContext, "初始化风险数据失败，请在设置菜单重新获取", Toast.LENGTH_SHORT).show();
			break;	
		case MSG_0x2009 :
			Toast.makeText(mContext, "初始化定位数据成功", Toast.LENGTH_SHORT).show();
			break;
		case MSG_0x200A :
			Toast.makeText(mContext, "初始化定位数据失败，请在设置菜单重新获取", Toast.LENGTH_SHORT).show();
			break;
			
			
			
		case MSG_0x200B :
			if (mDialog != null)
				mDialog.dismiss();  
			Intent intent = new Intent(this, MainAct.class);
			startActivity(intent);
			mApplication.mIsLogin = true;
			break;
		case MSG_0x200C :
			Toast.makeText(mContext, "获取权限数据失败，请重新登录", Toast.LENGTH_SHORT).show();
			break;
		case MSG_0x200D :
			Toast.makeText(mContext, "上传工程拍照图片", Toast.LENGTH_SHORT).show();
			break;	
		case MSG_0x200E :
			Toast.makeText(mContext, "上传工程采点数据", Toast.LENGTH_SHORT).show();
			break;		
		default:
			if (mDialog != null)
				mDialog.dismiss();  
			break;
		}
	}
	
	
	
}
