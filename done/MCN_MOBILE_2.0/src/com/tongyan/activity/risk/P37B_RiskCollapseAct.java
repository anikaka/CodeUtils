  package com.tongyan.activity.risk;

import java.util.List;

import com.tongyan.activity.MyApplication;
import com.tongyan.activity.MainAct;
import com.tongyan.activity.R;
import com.tongyan.common.db.DBService;
import com.tongyan.common.entities._HolefaceSetting;
import com.tongyan.common.entities._HolefaceSettingRecord;
import com.tongyan.common.entities._LocRisk;
import com.tongyan.widget.view.BaseLine;
import com.tongyan.widget.view.DefineButton;
import com.tongyan.widget.view.MSlipSwitch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 
 * @ClassName P37_RiskCollapseAct.java
 * @Author Administrator
 * @Date 2013-8-29 pm 09:15:02
 * @Desc TODO
 */
public class P37B_RiskCollapseAct extends Activity {
	
	
	private Button mHomeBtn;
	//
	private LinearLayout mRiskSettingContainer;
	
	//intent params 
	private String $holefaceId;
	private String $riskId;
	private String $riskType;
	
	private int mFontSize;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessM();
	}
	
	private void initPage() {
		setContentView(R.layout.risk_setting_layout_b);
		mHomeBtn = (Button)findViewById(R.id.p37_risk_collapse_home_btn);
		mRiskSettingContainer = (LinearLayout)findViewById(R.id.p37_risk_content_container);
	}
	
	
	private void setClickListener() {
		mHomeBtn.setOnClickListener(mHomeBtnListener);
	}
	_LocRisk mLocalRisk;
	private void businessM(){
		((MyApplication)getApplication()).addActivity(this);
		if(getIntent() != null && getIntent().getExtras() != null) {
			 $holefaceId = (String) getIntent().getExtras().get("$holefaceId");       //HolefaceId
			 $riskId = (String) getIntent().getExtras().get("$riskId");               //RiskId
			 $riskType = (String) getIntent().getExtras().get("$riskType");           
			 reloadView();
		}
		int fontSize = ((MyApplication)getApplication()).mFontSize;
		if(fontSize == 0) {
			mFontSize = 20;
		} else {
			mFontSize = fontSize;
		}
	}
	
	public void reloadView() {
		List<String> mFirstCause = new DBService(this).getHolefaceRecordsByOneQuota($riskId, $holefaceId, $riskType);
		if(mFirstCause != null && mFirstCause.size() > 0)
		for(int i = 0; i < mFirstCause.size(); i ++) {
			TextView textView = new TextView(this);
			textView.setText(mFirstCause.get(i));
			textView.setTextColor(Color.parseColor("#ffffff"));
			textView.setTextSize(18);
			mRiskSettingContainer.addView(textView);
			List<_HolefaceSettingRecord> mHolefaceRecordsList = new DBService(this).getHolefaceRecordsByAllParams($riskId, $holefaceId, $riskType, mFirstCause.get(i));
			if(mHolefaceRecordsList != null && mHolefaceRecordsList.size() > 0) {
				for(int j = 0; j < mHolefaceRecordsList.size(); j ++) {
					_HolefaceSettingRecord mRecord = mHolefaceRecordsList.get(j);
					if(mRecord != null) {
						_HolefaceSetting mSetting = new DBService(this).findRiskSettingsById(mRecord.get$rowId());
						if(mSetting != null) {
							if(!"是".equals(mSetting.getClass2Tip()) && !"否".equals(mSetting.getClass2Tip()) && !"有".equals(mSetting.getClass2Tip())  && !"无".equals(mSetting.getClass2Tip())) {
								View convertView = LayoutInflater.from(this).inflate(R.layout.risk_setting_list_item_a, null);
								TextView mSubArrow = (TextView)convertView.findViewById(R.id.sub_item_target);
								TextView mSecondCauseTextView = (TextView)convertView.findViewById(R.id.p37_risk_setting_second_cause);
								TextView mCurrentSelectTextView = (TextView)convertView.findViewById(R.id.p37_risk_setting_content);
								DefineButton mDefineBtn = (DefineButton)convertView.findViewById(R.id.p37_risk_setting_btn);
								mDefineBtn.setVisibility(View.INVISIBLE);
								mSubArrow.setVisibility(View.GONE);
								mSecondCauseTextView.setText(mSetting.getTwoType());
								mCurrentSelectTextView.setText(mRecord.getSelectedType());
								mRiskSettingContainer.addView(convertView);
								mDefineBtn.setmSetting(mSetting);
								mDefineBtn.setmRecord(mRecord);
								//mDefineBtn.setOnClickListener(mClickListener);
							} else {
								View convertView = LayoutInflater.from(this).inflate(R.layout.risk_setting_list_father_itema, null);
								TextView mSecondCauseTextView = (TextView)convertView.findViewById(R.id.p37_risk_setting_second_cause);
								TextView mCurrentSelectTextView = (TextView)convertView.findViewById(R.id.p37_risk_setting_content);
								MSlipSwitch mClickSwitch = (MSlipSwitch)convertView.findViewById(R.id.p37_risk_switch_btn);
								//mClickSwitch.setImageResource(R.drawable.p00_widget_switch_bluebround, R.drawable.p00_widget_switch_bluebround, R.drawable.p00_widget_switch_off_on_btn);
								//mClickSwitch.setOnSwitchListener(onSwitchListener);
								mClickSwitch.setmTextSize(mFontSize);
								mClickSwitch.setmSetting(mSetting);
								mClickSwitch.setmRecord(mRecord);
								if("是".equals(mSetting.getClass2Tip()) || "否".equals(mSetting.getClass2Tip())) {
									mClickSwitch.setmTureText("是");//true
									mClickSwitch.setmFalseText("否");//false
									if("是".equals(mRecord.getCurrentState())) {
										mClickSwitch.setSwitchState(true);
									} else {
										mClickSwitch.setSwitchState(false);
									}
								} else {
									mClickSwitch.setmTureText("有");//true
									mClickSwitch.setmFalseText("无");//false
									if("有".equals(mRecord.getCurrentState())) {
										mClickSwitch.setSwitchState(true);
									} else {
										mClickSwitch.setSwitchState(false);
									}
								}
								mSecondCauseTextView.setText(mSetting.getTwoType());
								mCurrentSelectTextView.setText(getSelectValue(mRecord.getSelectedType()));
								mRiskSettingContainer.addView(convertView);
								
								if(mRecord.getCurrentState().equals(mSetting.getClass2Tip()) && "2".equals(mSetting.getIsChild())) {
									if(mRecord.getmHolefaceRecordList() != null && mRecord.getmHolefaceRecordList().size() > 0) {
										for(int k = 0; k < mRecord.getmHolefaceRecordList().size(); k ++) {
											_HolefaceSettingRecord mMRecord = mRecord.getmHolefaceRecordList().get(k);
											_HolefaceSetting mMSetting = new DBService(this).findRiskSettingsById(mMRecord.get$rowId());
											if(mMRecord != null && mMSetting != null) {
												View convertSubView = LayoutInflater.from(this).inflate(R.layout.risk_setting_list_item_a, null);
												TextView mSubArrow = (TextView)convertSubView.findViewById(R.id.sub_item_target);
												TextView mSSecondCauseTextView = (TextView)convertSubView.findViewById(R.id.p37_risk_setting_second_cause);
												TextView mSCurrentSelectTextView = (TextView)convertSubView.findViewById(R.id.p37_risk_setting_content);
												DefineButton mSubDefineBtn = (DefineButton)convertSubView.findViewById(R.id.p37_risk_setting_btn);
												mSubDefineBtn.setVisibility(View.INVISIBLE);
												mSubArrow.setVisibility(View.VISIBLE);
												mSSecondCauseTextView.setText(mMSetting.getTwoType());
												mSCurrentSelectTextView.setText(mMRecord.getSelectedType());
												mSubDefineBtn.setmSetting(mMSetting);
												mSubDefineBtn.setmRecord(mMRecord);
												//mSubDefineBtn.setOnClickListener(mClickListener);	
												mRiskSettingContainer.addView(convertSubView);
											}
										}
									}
								} else {
									
								}
								
							}
						}
						
						BaseLine line = new BaseLine(this);
						mRiskSettingContainer.addView(line, new LayoutParams(LayoutParams.FILL_PARENT, 1));
					}
				}
			}
		}
	}
	public String getSelectValue(String mSelectedType) {
		if(!"是".equals(mSelectedType) && !"否".equals(mSelectedType) && !"有".equals(mSelectedType)  && !"无".equals(mSelectedType)){
			return mSelectedType;
		} else {
			return "";
		}
	}
	//====================================================================
	// Part of OnClickListener
	//====================================================================
	
	OnClickListener mHomeBtnListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(P37B_RiskCollapseAct.this,MainAct.class);
			startActivity(intent);
		}
	};
}
