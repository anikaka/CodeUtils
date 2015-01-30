package com.tongyan.activity.risk;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.R;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.entities._RiskInfo;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.WebServiceUtils;
import com.tongyan.widget.view.DefineButton;
/**
 * 
 * @ClassName P40_RiskDetailAct.java
 * @Author wanghb
 * @Date 2013-10-14 am 10:35:00
 * @Desc a detail page of  a part of one hole face risk
 */
public class P40_RiskDetailAct extends AbstructCommonActivity {
	
	private Context mContext = this;
	private Button mSeePictureBtn;
	private LinearLayout mDetailContainer;
	private TextView mImageView;
	
	private MyApplication mApplication;
	private Dialog mDialog;
	private _User mLocalUser;
	private String mIsSucc;
	List<Map<String,Map<String,List<_RiskInfo>>>> mRemoteDetailList;
	
	private String maincheckid;
	private String position;
	private String mIntentType;//0:风险展示，1：风险提示，2：监督监理
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessM();
	}
	
	private void initPage() {
		setContentView(R.layout.risk_holeface_detail);
		mSeePictureBtn = (Button)findViewById(R.id.p40_risk_see_pic_btn);
		mDetailContainer = (LinearLayout)findViewById(R.id.p40_risk_content_container);
		mImageView = (TextView)findViewById(R.id.p39_risk_title_words);
		
	}
	
	private void setClickListener() {
		mSeePictureBtn.setOnClickListener(mSeePictureBtnListener);
	}
	
	private void businessM(){
		mApplication = ((MyApplication)getApplication());
		mApplication.addActivity(this);
		mLocalUser = mApplication.localUser;
		if(getIntent() != null && getIntent().getExtras() != null) {//from P39_RiskHolefaceAct
			getRemote(getIntent().getExtras().getString("checkid"));
			maincheckid = getIntent().getExtras().getString("maincheckid");
			position = getIntent().getExtras().getString("position");
			 mIntentType = getIntent().getExtras().getString("mIntentType");
			 if("0".equals(mIntentType)) {
				 //mImageView.setBackgroundResource(R.drawable.p42_title_image_words);//风险展示
				 mImageView.setText(getResources().getString(R.string.main_risk_showing_text));
			 }
		}
	}
	
	public void getRemote(final String checkid) {
		mDialog = new AlertDialog.Builder(this).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		
		 new Thread(new Runnable() {
				@Override
				public void run() {
					Map<String,String> properties = new HashMap<String,String>();
					properties.put("publicKey", Constansts.PUBLIC_KEY);
					properties.put("userName", mLocalUser.getUsername());
					properties.put("Password", mLocalUser.getPassword());
					properties.put("type", "GetRiskPositionItem");//
					properties.put("id", checkid);
					try {
						String jsonStr = WebServiceUtils.requestM(properties, Constansts.METHOD_OF_GETCONTENT, mContext);
						Map<String,Object> mR = new Str2Json().checkHoleDetail(jsonStr);
							if(mR != null) {
								mIsSucc = (String)mR.get("s");
								if("ok".equals(mIsSucc)) {
									mRemoteDetailList = (List<Map<String,Map<String,List<_RiskInfo>>>>)mR.get("v");
									sendMessage(Constansts.SUCCESS);
								} else {
									sendMessage(Constansts.ERRER);
								}
							} else {
								sendMessage(Constansts.CONNECTION_TIMEOUT);
							}
					} catch (Exception e) {
						sendMessage(Constansts.CONNECTION_TIMEOUT);
						e.printStackTrace();
					} 
				};
			}).start(); 
	}
	/**
	 * compute and load items 
	 */
	public void loadLayout() {
		//mDetailContainer
		if(null != mRemoteDetailList && mRemoteDetailList.size() > 0) {
			for(int i = 0; i < mRemoteDetailList.size(); i ++) {
				Map<String,Map<String,List<_RiskInfo>>> m = mRemoteDetailList.get(i);
				if(m != null && !m.isEmpty()) {
					Iterator iter = m.entrySet().iterator();
					while (iter.hasNext()) {//ahhhhh...
						Map.Entry entry = (Map.Entry)iter.next();
						Object key = entry.getKey();
						TextView mOutTitle = new TextView(this);
						mOutTitle.setText(String.valueOf(key));
						mOutTitle.setBackgroundResource(R.drawable.oa_contact_letter_bg);
						mOutTitle.setTextColor(Color.parseColor("#000000"));
						mOutTitle.setTextSize(18);
						mDetailContainer.addView(mOutTitle);
						Map<String, List<_RiskInfo>> val = (Map<String, List<_RiskInfo>>)entry.getValue();
						for(Map.Entry<String, List<_RiskInfo>> s : val.entrySet()) {
							String mSubKey = s.getKey();
							TextView mInTitle = new TextView(this);
							mInTitle.setText(mSubKey);
							mInTitle.setTextColor(Color.parseColor("#146cc3"));
							mInTitle.setTextSize(18);
							mDetailContainer.addView(mInTitle);
							List<_RiskInfo> mInList = s.getValue();
							if(mInList != null && mInList.size() > 0) {
								for(int j = 0; j < mInList.size(); j ++) {
									_RiskInfo mInfo = mInList.get(j);
									if(mInfo != null) {
										View convertView = LayoutInflater.from(this).inflate(R.layout.risk_setting_list_item_a, null);
										TextView mSubArrow = (TextView)convertView.findViewById(R.id.sub_item_target);
										TextView mSecondCauseTextView = (TextView)convertView.findViewById(R.id.p37_risk_setting_second_cause);
										TextView mCurrentSelectTextView = (TextView)convertView.findViewById(R.id.p37_risk_setting_content);
										DefineButton mDefineBtn = (DefineButton)convertView.findViewById(R.id.p37_risk_setting_btn);
										mDefineBtn.setVisibility(View.GONE);
										mSubArrow.setVisibility(View.GONE);
										mSecondCauseTextView.setText(mInfo.getTwoType());
										mSecondCauseTextView.setTextColor(Color.parseColor("#000000"));
										mCurrentSelectTextView.setText(mInfo.getRiskContent());
										mDetailContainer.addView(convertView);
										
										if(mInfo.getmList() != null && mInfo.getmList().size() > 0) {
											for(int k = 0; k < mInfo.getmList().size(); k ++) {
												_RiskInfo mSInfo = mInfo.getmList().get(k);
												if(mSInfo != null) {
													View convertSView = LayoutInflater.from(this).inflate(R.layout.risk_setting_list_item_a, null);
													TextView mSubSArrow = (TextView)convertSView.findViewById(R.id.sub_item_target);
													TextView mSecondSCauseTextView = (TextView)convertSView.findViewById(R.id.p37_risk_setting_second_cause);
													TextView mCurrentsSelectTextView = (TextView)convertSView.findViewById(R.id.p37_risk_setting_content);
													DefineButton mSDefineBtn = (DefineButton)convertSView.findViewById(R.id.p37_risk_setting_btn);
													mSDefineBtn.setVisibility(View.GONE);
													mSubSArrow.setVisibility(View.VISIBLE);
													mSecondSCauseTextView.setText(mSInfo.getTwoType());
													mCurrentsSelectTextView.setText(mSInfo.getRiskContent());
													mDetailContainer.addView(convertSView);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	OnClickListener mSeePictureBtnListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext, P43_ShowPictureAct.class);
			intent.putExtra("maincheckid", maincheckid);
			intent.putExtra("position", position);
			intent.putExtra("mIntentType", mIntentType);
			startActivity(intent);
		}
	};
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			if(mDialog != null)
				mDialog.dismiss();
			loadLayout();
			break;
		case Constansts.ERRER:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(mContext, mIsSucc, Toast.LENGTH_SHORT).show();
			break;
		case Constansts.NET_ERROR:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.CONNECTION_TIMEOUT:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(mContext, "连接超时", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
}
