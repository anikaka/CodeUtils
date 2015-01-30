package com.tongyan.fragment;


import java.util.HashMap;
import java.util.Map;

import com.readystatesoftware.viewbadger.BadgeView;
import com.tongyan.activity.R;
import com.tongyan.activity.oa.OaMsgListAct;
import com.tongyan.activity.oa.OaAgendaScheduleAct;
import com.tongyan.activity.oa.OaUnHandlerDocAct;
import com.tongyan.activity.oa.OAContactsAct;
import com.tongyan.activity.oa.OaNoticeAct;
import com.tongyan.activity.oa.OaStandardFileAct;
import com.tongyan.common.data.Str2Json;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.WebServiceUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
/**
 * 
 * @className MainMobileOaFragment
 * @author wanghb
 * @date 2014-6-11 PM 03:18:57
 * @Desc 主页面-移动OA
 */
public class MainMobileOaFragment extends Fragment implements OnClickListener{
	
	private Context  mMContext;
	private Button mMainOaMessageBtn,mMainOaCalendarBtn,mMainOaDocumentGetBtn,mMainOaDocumentSendBtn,mMainOaContactsBtn,mMainOaNoticeBtn,mMainOaFileBtn;
	private Dialog mDialog;
	private String mTipMessage;
	private int mMesCounts;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constansts.SUCCESS:
				if(mDialog != null) {
					mDialog.dismiss();
				}
				BadgeView badge = new BadgeView(mMContext, mMainOaMessageBtn);
				badge.setText(String.valueOf(mMesCounts));
				badge.show();
				break;
			case Constansts.ERRER:
				if(mDialog != null) {
					mDialog.dismiss();
				}
				Toast.makeText(mMContext, mTipMessage, Toast.LENGTH_SHORT).show();
				break;
			case Constansts.CONNECTION_TIMEOUT:
				if(mDialog != null) {
					mDialog.dismiss();
				}
				Toast.makeText(mMContext, "连接超时", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};
	
	public static MainMobileOaFragment newInstance(Context mContext) {
		MainMobileOaFragment mFragment = new MainMobileOaFragment();
		mFragment.mMContext = mContext;
		return mFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_mobile_oa_fragment, container, false);
		mMContext=getActivity();
		mMainOaMessageBtn = (Button) view.findViewById(R.id.main_oa_message_btn);
		mMainOaCalendarBtn = (Button) view.findViewById(R.id.main_oa_calendar_btn);
		mMainOaDocumentGetBtn = (Button) view.findViewById(R.id.main_oa_document_get_btn);
		mMainOaDocumentSendBtn = (Button) view.findViewById(R.id.main_oa_document_send_btn);
		mMainOaContactsBtn = (Button) view.findViewById(R.id.main_oa_contacts_btn);
		mMainOaNoticeBtn = (Button) view.findViewById(R.id.main_oa_notice_btn);
		mMainOaFileBtn = (Button) view.findViewById(R.id.main_oa_file_btn);
		mMainOaMessageBtn.setOnClickListener(this);
		mMainOaCalendarBtn.setOnClickListener(this);
		mMainOaDocumentGetBtn.setOnClickListener(this);
		mMainOaDocumentSendBtn.setOnClickListener(this);
		mMainOaContactsBtn.setOnClickListener(this);
		mMainOaNoticeBtn.setOnClickListener(this);
		mMainOaFileBtn.setOnClickListener(this);
		getMessageCount();
		return view;
	}
	
	
	public void getMessageCount() {
		mDialog = new AlertDialog.Builder(mMContext).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		 new Thread(new Runnable() {
				@Override
				public void run() {
					SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(mMContext);
					String mAccount = mPreferences.getString(Constansts.INFO_USER_ACCOUNT, "");
					String mUserId = mPreferences.getString(Constansts.INFO_USER_ID, "");
					String mPassword = mPreferences.getString(Constansts.INFO_USER_PASSWORD, "");
					
					Map<String,String> properties = new HashMap<String,String>();
					properties.put("publicKey", Constansts.PUBLIC_KEY);
					properties.put("userName", mAccount);
					properties.put("Password", mPassword);
					properties.put("empId", mUserId);
					Message message = new Message();
					try {
						String str = WebServiceUtils.requestM(properties, Constansts.METHOD_OF_GETMESSAGECOUNT, mMContext);
						Map<String,Object> mR = new Str2Json().getMessageCount(str);
							if(mR != null) {
								mTipMessage = (String)mR.get("s");
								if("ok".equals(mTipMessage)) {
									mMesCounts = (Integer)mR.get("v");
									message.what = Constansts.SUCCESS;
									mHandler.sendMessage(message);
								} else {
									message.what = Constansts.ERRER;
									mHandler.sendMessage(message);
								}
							} else {
								message.what = Constansts.CONNECTION_TIMEOUT;
								mHandler.sendMessage(message);
							}
					} catch (Exception e) {
						message.what = Constansts.CONNECTION_TIMEOUT;
						mHandler.sendMessage(message);
						e.printStackTrace();
					} 
				};
				
			}).start(); 
	}
	
	
	@Override
	public void onClick(View v) {
		//消息提醒
		if(v.equals(mMainOaMessageBtn)) {
			Intent intent = new Intent(mMContext,OaMsgListAct.class);
			startActivityForResult(intent, 104);
			return;
		}
		//日程安排
		if(v.equals(mMainOaCalendarBtn)) {
			Intent intent = new Intent(mMContext,OaAgendaScheduleAct.class);
			startActivityForResult(intent, 104);
			return;
		}
		//收文管理
		if(v.equals(mMainOaDocumentGetBtn)) {
			Intent intent = new Intent(mMContext,OaUnHandlerDocAct.class);
			intent.putExtra("IntentType", Constansts.TYPE_OF_DOCUMENTGET);
			startActivityForResult(intent, 104);
			return;
		}
		//发文管理
		if(v.equals(mMainOaDocumentSendBtn)) {
			Intent intent = new Intent(mMContext,OaUnHandlerDocAct.class);
			intent.putExtra("IntentType", Constansts.TYPE_OF_DOCUMENTSEND);
			startActivityForResult(intent, 104);
			return;
		}
		//通讯录
		if(v.equals(mMainOaContactsBtn)) {
			SharedPreferences mShared = PreferenceManager.getDefaultSharedPreferences(mMContext);
			String mLinkRoute = mShared.getString("v_Link", "0");
			if(mLinkRoute != null && "1".equals(mLinkRoute)) {
				Intent intent = new Intent(mMContext,OAContactsAct.class);
				startActivity(intent);
			} else {
				Toast.makeText(mMContext, "您暂无该权限", Toast.LENGTH_SHORT).show();
			}
			return;
		}
		//公告通知
		if(v.equals(mMainOaNoticeBtn)) {
			Intent intent = new Intent(mMContext,OaNoticeAct.class);
			startActivityForResult(intent, 104);
			return;
		}
		//规范文件
		if(v.equals(mMainOaFileBtn)) {
			Intent intent = new Intent(mMContext,OaStandardFileAct.class);
			startActivity(intent);
			return;
		}
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 104 || resultCode ==0 ) {
			getMessageCount();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
