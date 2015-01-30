package com.tongyan.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tongyan.activity.gps.GpsSectionUpdateListAct;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.db.DBService;
import com.tongyan.common.entities._Contacts;
import com.tongyan.common.entities._HolefaceSetting;
import com.tongyan.common.entities._ItemSec;
import com.tongyan.common.entities._Project;
import com.tongyan.common.entities._User;
import com.tongyan.service.MGPSService;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.WebServiceUtils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @ClassName P44_SettingAct.java
 * @Author wanghb
 * @Date 2013-10-18 am 08:30:16 //2014-06-22 12:58:00
 * @Desc TODO
 */
public class SettingAct extends AbstructCommonActivity {
	
	private Context mContext = this;
	private Button mContactsBtn,mProjectBtn,mItemSec,mRisk,mOneKey,mExitBtn;
	private TextView  mContactsUpText,mProjectUpText,mItemUpText,mRiskUpText,mMenuUpText;
	private RelativeLayout mSectionMenu;
	private _User localUser;
	private MyApplication mApplication;
	
	private static final int MSG_CONTACTS_SUCCESS = 0x5001;
	private static final int MSG_CONTACTS_FAULT = 0x5002;
	private static final int MSG_PROJECT_SUCCESS = 0x5003;
	private static final int MSG_PROJECT_FAULT = 0x5004;
	private static final int MSG_ITEMSEC_SUCCESS = 0x5005;
	private static final int MSG_ITEMSEC_FAULT = 0x5006;
	private static final int MSG_RISK_SUCCESS = 0x5007;
	private static final int MSG_RISK_FAULT = 0x5008;
	
	private SharedPreferences mPreferences = null;
	
	private LocationManager alm = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessM();
	}
	
	private void initPage() {
		mApplication = ((MyApplication)getApplication());
		mApplication.addActivity(this);
		localUser = mApplication.localUser;
		setContentView(R.layout.main_user_setting);
		TextView nickname = (TextView) findViewById(R.id.main_user_setting_nickname);
		TextView username = (TextView) findViewById(R.id.p44_setting_user_name);
		TextView department = (TextView) findViewById(R.id.p44_setting_user_department);
		TextView job = (TextView) findViewById(R.id.p44_setting_user_job);
		TextView mVersionText = (TextView)findViewById(R.id.p44_setting_app_version);
		mSectionMenu = (RelativeLayout)findViewById(R.id.main_user_setting_section_menu_container);
		mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		//get values to page
		if(localUser != null) {
			nickname.setText(localUser.getUsername());
			username.setText(localUser.getEmpName());
			department.setText(localUser.getDepartment());
			job.setText(localUser.getEmpLevel());
			mVersionText.setText(Constansts.VERSION_NAME);
		}
		alm = (LocationManager)this.getSystemService( Context.LOCATION_SERVICE );
		mContactsBtn = (Button) findViewById(R.id.p44_setting_update_contacts_btn);
		mProjectBtn = (Button) findViewById(R.id.p44_setting_update_project_btn);
		mItemSec = (Button) findViewById(R.id.p44_setting_update_itemSec_btn);
		mRisk = (Button) findViewById(R.id.p44_setting_update_risk_btn);
		mExitBtn = (Button) findViewById(R.id.exit_out_btn);
		
		//update time text
		mContactsUpText = (TextView) findViewById(R.id.p44_setting_contacts_updatetime);
		mProjectUpText = (TextView) findViewById(R.id.p44_setting_project_updatetime);
		mItemUpText = (TextView) findViewById(R.id.p44_setting_itemSec_updatetime);
		mRiskUpText = (TextView) findViewById(R.id.p44_setting_risk_updatetime);
		mOneKey = (Button) findViewById(R.id.p44_setting_update_key);
		mMenuUpText = (TextView) findViewById(R.id.p44_setting_section_menu_updatetime);
		
		String text = mPreferences.getString(Constansts.PREFERENCES_SQLITE_VERSION, "");
		mMenuUpText.setText("版本：" + text);
		
		CheckBox mUpdateBox = (CheckBox)findViewById(R.id.main_user_setting_update_checkbox);
		mUpdateBox.setOnCheckedChangeListener(mUpdateListener);
		if(localUser.getIsUpdate() != null && 1 == localUser.getIsUpdate()) {
			mUpdateBox.setChecked(true);
		} else {
			mUpdateBox.setChecked(false);
		}
		
		CheckBox mGPsBox = (CheckBox)findViewById(R.id.main_user_setting_gps_checkbox);
		mGPsBox.setOnCheckedChangeListener(mGpsListener);
		if(localUser.getIsGps() != null && 1 == localUser.getIsGps()) {
			mGPsBox.setChecked(true);
		} else {
			mGPsBox.setChecked(false);
		}
	}
	private void setClickListener() {
		mContactsBtn.setOnClickListener(mContactsBtnListener);
		mProjectBtn.setOnClickListener(mProjectBtnListener);
		mItemSec.setOnClickListener(mItemSecBtnListener);
		mRisk.setOnClickListener(mRiskBtnListener);
		mOneKey.setOnClickListener(mOneKeyListener);
		mSectionMenu.setOnClickListener(mSectionMenuListener);
		mExitBtn.setOnClickListener(mExitListener);
	}
	private void businessM(){
		resetPage();
	}
	
	OnClickListener mExitListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext, LoginAct.class);
			startActivity(intent);
		}
	};
	
	OnClickListener mSectionMenuListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext, GpsSectionUpdateListAct.class);
			startActivity(intent);
		}
	};
	
	
	
	
	OnCheckedChangeListener mUpdateListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			String i = "0";
			if(isChecked) {
				i = "1";
			} else {
				i = "0";
			}
			if(new DBService(mContext).updateDataType(localUser.getUserid(), i)) {
				mApplication.localUser.setIsUpdate(Integer.valueOf(i));
			}
		}
	};
	
	OnCheckedChangeListener mGpsListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			String i = "0";
			if(isChecked) {
				i = "1";
				if (!alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
					Toast.makeText(mContext, "请打开GPS", Toast.LENGTH_SHORT).show();
				} else {
					boolean isSuc = new DBService(mContext).updateGPSType(localUser.getUserid(), i);
					if(isSuc) {
						mApplication.localUser.setIsGps(Integer.valueOf(i));
						Intent servicIntentStart = new Intent(mContext, MGPSService.class);
						if(isChecked) {
							stopService(servicIntentStart);
							startService(servicIntentStart);
						} else {
							stopService(servicIntentStart);
						}
					}
				}
			} else {
				i = "0";
			}
		}
	};
	AnimationDrawable mContactsAnimation = null;
	AnimationDrawable mProjectAnimation = null;
	AnimationDrawable mItemSecAnimation = null;
	AnimationDrawable mRiskAnimation = null;
	
	OnClickListener mContactsBtnListener = new OnClickListener() {
		public void onClick(View v) {
			update(true,false,false,false);
		}
	};
	OnClickListener mProjectBtnListener = new OnClickListener() {
		public void onClick(View v) {
			update(false,true,false,false);
			v.setBackgroundResource(R.drawable.common_refresh_animation);
			mProjectAnimation = (AnimationDrawable) v.getBackground();
			mProjectAnimation.start();
		}
	};
	OnClickListener mItemSecBtnListener = new OnClickListener() {
		public void onClick(View v) {
			update(false,false,true,false);
			v.setBackgroundResource(R.drawable.common_refresh_animation);
			mItemSecAnimation = (AnimationDrawable) v.getBackground();
			mItemSecAnimation.start();
		}
	};
	OnClickListener mRiskBtnListener = new OnClickListener() {
		public void onClick(View v) {
			update(false,false,false,true);
			v.setBackgroundResource(R.drawable.common_refresh_animation);
			mRiskAnimation = (AnimationDrawable) v.getBackground();
			mRiskAnimation.start();
		}
	};
	OnClickListener mOneKeyListener = new OnClickListener() {
		public void onClick(View v) {
			update(true,true,true,true);
		}
	};
	long midClickTime;
	private synchronized void update(final boolean contacts,final boolean project,final boolean itemSec,final boolean risk) {
		if(contacts) {
			mContactsBtn.setBackgroundResource(R.drawable.common_refresh_animation);
			mContactsAnimation = (AnimationDrawable) mContactsBtn.getBackground();
			mContactsAnimation.start();
		}
		if(project) {
			mProjectBtn.setBackgroundResource(R.drawable.common_refresh_animation);
			mProjectAnimation = (AnimationDrawable) mProjectBtn.getBackground();
			mProjectAnimation.start();
		}
		if(itemSec) {
			mItemSec.setBackgroundResource(R.drawable.common_refresh_animation);
			mItemSecAnimation = (AnimationDrawable) mItemSec.getBackground();
			mItemSecAnimation.start();
		}
		if(risk) {
			mRisk.setBackgroundResource(R.drawable.common_refresh_animation);
			mRiskAnimation = (AnimationDrawable) mRisk.getBackground();
			mRiskAnimation.start();
		}
		
		new Thread(new Runnable(){
			public void run() {
				if ((System.currentTimeMillis() - midClickTime) <= 1000)   {
					return;
				} 
				midClickTime = System.currentTimeMillis();
				if(contacts) {getContacts();}
				if (project) {project();}
				if (itemSec) {itemSec();}
				if (risk) {risk();}
			}
		}).start();
	}
	
	private void getContacts() {
		Map<String,Object> mR = null;
		try {
			String str = WebServiceUtils.getRequestStr(localUser.getUsername(), localUser.getPassword(), null, null, null, null, Constansts.METHOD_OF_GETLINKMAN,mContext);
			mR = new Str2Json().getLinkManInfo(str);
		} catch(Exception e) {
			e.printStackTrace();
			sendMessage(MSG_CONTACTS_FAULT);
		}
		if (mR != null) {
			String isSucc = (String)mR.get("s");
			if ("ok".equals(isSucc)) {
				List<_Contacts> contactsList = (List<_Contacts>)mR.get("v");
				//List<_ContactsData> newlist = new ContactsUtils().getDepartmentContacts(contactsList);
				if(contactsList != null && contactsList.size() > 0) {
					if (new DBService(mContext).delAll("LOCAL_CONTACTS")) {
						if(new DBService(mContext).insertContacts(contactsList)) {
							if(new DBService(mContext).updateContactTime(localUser.getUserid(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))) {
								sendMessage(MSG_CONTACTS_SUCCESS);
								sendMessage(Constansts.SUCCESS);
							} else {
								sendMessage(MSG_CONTACTS_FAULT);
							}
						}
					} else {
						sendMessage(MSG_CONTACTS_FAULT);
					}
				}
				/*if (new DBService(mContext).delAll("LOCAL_CONTACTS")) {
					if (newlist != null && newlist.size() > 0) {
						for (_ContactsData contacts : newlist) {
							if (contacts != null) {
								new DBService(mContext).insertContacts(contacts);
							}
						}
						new DBService(mContext).updateContactTime(localUser.getUserid(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						sendMessage(MSG_CONTACTS_SUCCESS);
						sendMessage(Constansts.SUCCESS);
					} else {
						sendMessage(MSG_CONTACTS_FAULT);
					}
				} else {
					sendMessage(MSG_CONTACTS_FAULT);
				}*/
			} else {
				sendMessage(MSG_CONTACTS_FAULT);
			}
		} else {
			sendMessage(MSG_CONTACTS_FAULT);
		}
	}
	
	public void closeContactAnimation() {
		if(mContactsAnimation != null) {
			mContactsAnimation.stop();
			mContactsAnimation = null;
		}
	}
	private void project() {
		Map<String,Object> mR = null;
		try {
			String params = "{updatetime:''}";
			String str = WebServiceUtils.getRequestStr(localUser.getUsername(), localUser.getPassword(), null, null, "GetUnitNew", params, Constansts.METHOD_OF_GETLISTNOPAGE,mContext);
			mR = new Str2Json().getProjectList(str);
		} catch(Exception e) {
			e.printStackTrace();
			sendMessage(MSG_PROJECT_FAULT);
		}
		if (mR != null) {
			String isSucc = (String)mR.get("s");
			if ("ok".equals(isSucc)) {
				List<_Project> proList = (List<_Project>)mR.get("v");
				if(proList != null && proList.size() > 0) {
					if (new DBService(mContext).delAll("LOCAL_PROJECT")) {
						for (_Project m : proList) {
							if (m != null) {
								new DBService(mContext).insertProject(m);
							}
						}
						new DBService(mContext).updateProjectTime(localUser.getUserid(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						sendMessage(Constansts.SUCCESS);
						sendMessage(MSG_PROJECT_SUCCESS);
					} else {
						sendMessage(MSG_PROJECT_FAULT);
					}
				} else {
					sendMessage(MSG_PROJECT_FAULT);
				}
			} else {
				sendMessage(MSG_PROJECT_FAULT);
			}
		} else {
			sendMessage(MSG_PROJECT_FAULT);
		}
	}
	
	public void closeProjectAnimation() {
		if(mProjectAnimation != null) {
			mProjectAnimation.stop();
			mProjectAnimation = null;
		}
	}
	
	
	private void itemSec() {
		Map<String,Object> mR = null;
		try {
			String str = WebServiceUtils.getRequestStr(localUser.getUsername(), localUser.getPassword(),null, null, null, null, Constansts.METHOD_OF_GETPROJECT,mContext);
			mR = new Str2Json().getItemSecInfo(str);
		} catch(Exception e) {
			e.printStackTrace();
			sendMessage(MSG_ITEMSEC_FAULT);
		}
		if(mR != null) {
			String isSucc = (String)mR.get("s");
			if("ok".equals(isSucc)) {
				List<_ItemSec> itemSecList = (List<_ItemSec>)mR.get("v");
				if(itemSecList != null && itemSecList.size() > 0) {
					if (new DBService(mContext).delAll("LOCAL_ITEM_SECTION")) {
						for(_ItemSec itemSec : itemSecList) {
							if(itemSec != null) {
								new DBService(mContext).insertItemSec(itemSec);
							}
						}
						new DBService(mContext).updateItemSecTime(localUser.getUserid(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						sendMessage(Constansts.SUCCESS);
						sendMessage(MSG_ITEMSEC_SUCCESS);
					} else {
						sendMessage(MSG_ITEMSEC_FAULT);
					}
				} else {
					sendMessage(MSG_ITEMSEC_FAULT);
				}
			} else {
				sendMessage(MSG_ITEMSEC_FAULT);
			}
		} else {
			sendMessage(MSG_ITEMSEC_FAULT);
		}
	}
	
	public void closeItemSecAnimation() {
		if(mItemSecAnimation != null) {
			mItemSecAnimation.stop();
			mItemSecAnimation = null;
		}
	}
	
	
	private void risk() {
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
		} catch (Exception e) {}
		if(mR != null) {
			String isSucc = (String)mR.get("s");
			if ("ok".equals(isSucc)) {
				List<_HolefaceSetting> mHolefaceSettingList = (List<_HolefaceSetting>)mR.get("v");
				if(mHolefaceSettingList != null && mHolefaceSettingList.size() > 0) {
					if (new DBService(mContext).delAll("LOCAL_RISK_SETTINGS")) {
					for(_HolefaceSetting mHolefaceSetting : mHolefaceSettingList) {
						//_HolefaceSetting mHolefaceSetting = mHolefaceSettingList.get(i);
						if(mHolefaceSetting != null) {
							Long $id = new DBService(mContext).insertHolefaceSetting(mHolefaceSetting);//TODO 
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
						new DBService(mContext).updateRiskTime(localUser.getUserid(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						sendMessage(Constansts.SUCCESS);
						sendMessage(MSG_RISK_SUCCESS);
					} else {
						sendMessage(MSG_RISK_FAULT);
					}
				} else {
					sendMessage(MSG_RISK_FAULT);
				}
			} else {
				sendMessage(MSG_RISK_FAULT);
			}
		} else {
			sendMessage(MSG_RISK_FAULT);
		}
	}
	
	public void closeRiskAnimation() {
		if(mRiskAnimation != null) {
			mRiskAnimation.stop();
			mRiskAnimation = null;
		}
	}
	
	
	public void reloadPage() {
		localUser = new DBService(mContext).findUser(localUser.getUserid());
		((MyApplication)getApplication()).localUser = localUser;
		resetPage();
	}
	
	private void resetPage() {
		String mContactsTime = "",mProjectTime = "",mItemSecTime = "",mRiskTypeTime = "";
		if(localUser.getContactsTime() != null && !"null".equals(localUser.getContactsTime())) {
			mContactsTime = localUser.getContactsTime();
		} 
		if(localUser.getProjectTime() != null && !"null".equals(localUser.getProjectTime())) {
			mProjectTime = localUser.getProjectTime();
		} 
		if(localUser.getItemSecTime() != null && !"null".equals(localUser.getItemSecTime())) {
			mItemSecTime = localUser.getItemSecTime();
		} 
		if(localUser.getRiskTypeTime() != null && !"null".equals(localUser.getRiskTypeTime())) {
			mRiskTypeTime = localUser.getRiskTypeTime();
		} 
		mContactsUpText.setText("上次更新:" + mContactsTime);
		mProjectUpText.setText("上次更新:" + mProjectTime);
		mItemUpText.setText("上次更新:" + mItemSecTime);
		mRiskUpText.setText("上次更新:" + mRiskTypeTime);
	}
	
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			reloadPage();
			break;
		case MSG_CONTACTS_SUCCESS :
			closeContactAnimation();
			break;
		case MSG_CONTACTS_FAULT :
			Toast.makeText(this, "通讯录数据更新失败", Toast.LENGTH_SHORT).show();
			closeContactAnimation();
			break;
		case MSG_PROJECT_SUCCESS :
			closeProjectAnimation();
			break;
		case MSG_PROJECT_FAULT :
			Toast.makeText(this, "工程数据更新失败", Toast.LENGTH_SHORT).show();
			closeProjectAnimation();
			break;
		case MSG_ITEMSEC_SUCCESS :
			closeItemSecAnimation();
			break;
		case MSG_ITEMSEC_FAULT :
			Toast.makeText(this, "项目标段数据更新失败", Toast.LENGTH_SHORT).show();
			closeItemSecAnimation();
			break;
		case MSG_RISK_SUCCESS:
			closeRiskAnimation();
			break;
		case MSG_RISK_FAULT :
			Toast.makeText(this, "风险数据更新失败", Toast.LENGTH_SHORT).show();
			closeRiskAnimation();
			break;	
		case Constansts.NET_ERROR:
			Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.CONNECTION_TIMEOUT:
			Toast.makeText(this, "连接超时", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.NO_DATA :
			Toast.makeText(this, "无最新数据", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
	
}
