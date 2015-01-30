package com.tongyan.activity.risk;

import java.util.List;

import com.tongyan.activity.MyApplication;
import com.tongyan.activity.MainAct;
import com.tongyan.activity.R;
import com.tongyan.common.db.DBService;
import com.tongyan.common.entities._HolefaceSetting;
import com.tongyan.common.entities._HolefaceSettingRecord;
import com.tongyan.utils.MDialog;
import com.tongyan.utils.ScreenUtil;
import com.tongyan.widget.view.BaseLine;
import com.tongyan.widget.view.DefineButton;
import com.tongyan.widget.view.MSlipSwitch;
import com.tongyan.widget.view.MSlipSwitch.OnSwitchListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @ClassName P37_RiskCollapseAct.java
 * @Author Administrator
 * @Date 2013-8-29 pm 09:15:02
 * @Desc TODO
 */
public class P37A_RiskCollapseAct extends Activity {
	
	private Context mContext = this;
	
	private Button mHomeBtn;
	private Button mFinishBtn,mFlushBtn;
	private LinearLayout mRiskSettingContainer;
	
	//intent params 
	private String $holefaceId;
	private String $riskId;
	private String $riskType;
	
	private int mScreenHeight;
	
	private int mFontSize;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessM();
	}
	
	private void initPage() {
		setContentView(R.layout.risk_setting_layout);
		((MyApplication)getApplication()).addActivity(this);
		mFinishBtn = (Button)findViewById(R.id.p37_risk_finish_btn_id);
		mFlushBtn = (Button)findViewById(R.id.p37_risk_flush_btn_id);
		mHomeBtn = (Button)findViewById(R.id.p37_risk_collapse_home_btn);
		mRiskSettingContainer = (LinearLayout)findViewById(R.id.p37_risk_content_container);
	}
	
	
	private void setClickListener() {
		mHomeBtn.setOnClickListener(mHomeBtnListener);
		mFinishBtn.setOnClickListener(mFinishBtnListener);
		mFlushBtn.setOnClickListener(mFlushBtnListener);
		
	}
	private void businessM(){
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		mScreenHeight = display.getHeight();
		
		int fontSize = ((MyApplication)getApplication()).mFontSize;
		if(fontSize == 0) {
			mFontSize = 20;
		} else {
			mFontSize = fontSize;
		}
		
		if(getIntent() != null && getIntent().getExtras() != null) {
			 $holefaceId = (String) getIntent().getExtras().get("$holefaceId");       //HolefaceId
			 $riskId = (String) getIntent().getExtras().get("$riskId");               //RiskId
			 $riskType = (String) getIntent().getExtras().get("$riskType");  
			 new DBService(this).updateHolefaceSettingInfo($holefaceId,"1",$riskId,$riskType);
			 reloadView();
		}
	}
	
	public void reloadView() {
		List<String> mFirstCause = new DBService(this).getHolefaceRecordsByOneQuota($riskId, $holefaceId, $riskType);
		if(mFirstCause != null && mFirstCause.size() > 0)
		for(int i = 0; i < mFirstCause.size(); i ++) {
			TextView textView = new TextView(this);
			textView.setText(mFirstCause.get(i));
			textView.setTextColor(getResources().getColor(R.color.black));
			textView.setTextSize(18);
			mRiskSettingContainer.addView(textView);
			
			BaseLine line0 = new BaseLine(this);
			mRiskSettingContainer.addView(line0, new LayoutParams(LayoutParams.FILL_PARENT, 1));
			
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
								mSubArrow.setVisibility(View.GONE);
								mSecondCauseTextView.setText(mSetting.getTwoType());
								mCurrentSelectTextView.setText(mRecord.getSelectedType());
								mRiskSettingContainer.addView(convertView);
								
								mDefineBtn.setmSetting(mSetting);
								mDefineBtn.setmRecord(mRecord);
								mDefineBtn.setOnClickListener(mClickListener);
							} else {
								View convertView = LayoutInflater.from(this).inflate(R.layout.risk_setting_list_father_itema, null);
								TextView mSecondCauseTextView = (TextView)convertView.findViewById(R.id.p37_risk_setting_second_cause);
								TextView mCurrentSelectTextView = (TextView)convertView.findViewById(R.id.p37_risk_setting_content);
								MSlipSwitch mClickSwitch = (MSlipSwitch)convertView.findViewById(R.id.p37_risk_switch_btn);
								//mClickSwitch.setImageResource(R.drawable.p00_widget_switch_bluebround, R.drawable.p00_widget_switch_bluebround, R.drawable.p00_widget_switch_off_on_btn);
								mClickSwitch.setOnSwitchListener(onSwitchListener);
								mClickSwitch.setmSetting(mSetting);
								mClickSwitch.setmRecord(mRecord);
								mClickSwitch.setmTextSize(mFontSize);
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
												mSubArrow.setVisibility(View.VISIBLE);
												mSSecondCauseTextView.setText(mMSetting.getTwoType());
												mSCurrentSelectTextView.setText(mMRecord.getSelectedType());
												mSubDefineBtn.setmSetting(mMSetting);
												mSubDefineBtn.setmRecord(mMRecord);
												mSubDefineBtn.setOnClickListener(mClickListener);	
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
	/**
	 * 测量当前分辨率手机的初始化Button控件的高度
	 * @return
	 */
	public int measureButtonHeight(){
		Button mButton = new Button(this);
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		mButton.measure(w, h);	
		return mButton.getMeasuredHeight();
	}
	
	OnSwitchListener onSwitchListener = new OnSwitchListener(){
		@Override
		public void onSwitched(View switchView, boolean isSwitchOn) {
			MSlipSwitch mSlipSwitch = (MSlipSwitch)switchView;
			_HolefaceSettingRecord mRecord = mSlipSwitch.getmRecord();
			_HolefaceSetting mSetting = mSlipSwitch.getmSetting();
			if(mRecord != null && mSetting != null) {
				//opposite of the current state
				if(mRecord.getCurrentState().equals(mSetting.getClass1()) && "1".equals(mSetting.getIsChild())) {
					String mString = mSetting.getClass2();
					String[] mSubStringArr = mString.split(";");
					double val = 0;
					if(mSubStringArr != null)
					val = (double)(mSubStringArr.length * measureButtonHeight() + ScreenUtil.dp2px(mContext, 60))/(double)mScreenHeight;
					if(val == 0) {
						val = 0.3;
					}
					mSettingDialog = new MDialog(mContext, R.style.dialog);
					mSettingDialog.createDialog(R.layout.risk_holo_face_pop, 0.9, val, getWindowManager());
					mSettingDialog.setCanceledOnTouchOutside(false);
					LinearLayout container = (LinearLayout)mSettingDialog.findViewById(R.id.p35_risk_holo_face_container);
					if(mSubStringArr != null && mSubStringArr.length > 0) {
						for(int i = 0; i < mSubStringArr.length; i ++) {
							DefineButton mButton = new DefineButton(mContext);
							String[] mArr = mSubStringArr[i].split(",");
							mButton.setText(mArr[0]);
							mButton.setTextColor(getResources().getColor(R.color.white));
							mButton.setmClassType(mArr[0]);
							mButton.setmClassTypeNum(mArr[1]);
							mButton.setBackgroundResource(R.drawable.common_blue_btn_selector);
							mButton.setmRecord(mRecord);
							mButton.setmSetting(mSetting);
							mButton.setOnClickListener(mClickClassListener);
							container.addView(mButton, new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.dp2px(mContext, 46)));
							
							if(mSubStringArr.length - 1 != i) {
								TextView textView = new TextView(mContext);
								container.addView(textView, new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,5));
							}
							
						}
					}
				} else if(mRecord.getCurrentState().equals(mSetting.getClass2Tip()) && "1".equals(mSetting.getIsChild())){
					boolean isUpdate = new DBService(mContext).updateHolefaceSettingOneRecord(mRecord.get$id(), mSetting.getClass1(), "1", mSetting.getClass1());
					if(!isUpdate) {
						Toast.makeText(mContext, "操作失败，请重试", Toast.LENGTH_SHORT).show();
					}
					if(mRiskSettingContainer != null) {
						mRiskSettingContainer.removeAllViews();
					}
					reloadView();
				} else if(mRecord.getCurrentState().equals(mSetting.getClass1()) && "2".equals(mSetting.getIsChild())) {
					boolean isUpdate = new DBService(mContext).updateHolefaceSettingOneRecord(mRecord.get$id(), mSetting.getClass2Tip(), "2", mSetting.getClass2Tip());
					if(!isUpdate) {
						Toast.makeText(mContext, "操作失败，请重试", Toast.LENGTH_SHORT).show();
					}
					if(mRiskSettingContainer != null) {
						mRiskSettingContainer.removeAllViews();
					}
					reloadView();
				} else if(mRecord.getCurrentState().equals(mSetting.getClass2Tip()) && "2".equals(mSetting.getIsChild())) {
					boolean isUpdate = new DBService(mContext).updateHolefaceSettingOneRecord(mRecord.get$id(), mSetting.getClass1(), "1", mSetting.getClass1());
					if(!isUpdate) {
						Toast.makeText(mContext, "操作失败，请重试", Toast.LENGTH_SHORT).show();
					}
					if(mRiskSettingContainer != null) {
						mRiskSettingContainer.removeAllViews();
					}
					reloadView();
				} else if(mRecord.getCurrentState().equals(mSetting.getClass1()) && "0".equals(mSetting.getIsChild())) {
					boolean isUpdate = new DBService(mContext).updateHolefaceSettingOneRecord(mRecord.get$id(), mSetting.getClass2Tip(), "2", mSetting.getClass2Tip());
					if(!isUpdate) {
						Toast.makeText(mContext, "操作失败，请重试", Toast.LENGTH_SHORT).show();
					}
					if(mRiskSettingContainer != null) {
						mRiskSettingContainer.removeAllViews();
					}
					reloadView();
				} else if(mRecord.getCurrentState().equals(mSetting.getClass2Tip()) && "0".equals(mSetting.getIsChild())) {
					boolean isUpdate = new DBService(mContext).updateHolefaceSettingOneRecord(mRecord.get$id(), mSetting.getClass1(), "1", mSetting.getClass1());
					if(!isUpdate) {
						Toast.makeText(mContext, "操作失败，请重试", Toast.LENGTH_SHORT).show();
					}
					if(mRiskSettingContainer != null) {
						mRiskSettingContainer.removeAllViews();
					}
					reloadView();
				}
			}
		}
	};
	
	//====================================================================
	// Part of OnClickListener
	//====================================================================
	
	OnClickListener mHomeBtnListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(mContext,MainAct.class);
			startActivity(intent);
		}
	};
	
	OnClickListener mFinishBtnListener = new OnClickListener() {
		public void onClick(View v) {
			boolean isSuc = true;
			/*List<_HolefaceSettingRecord> mHolefaceRecordsList = new DBService(mContext).getHolefaceRecordsBy($riskId, $holefaceId, $riskType, null);
			if(mHolefaceRecordsList != null && mHolefaceRecordsList.size() > 0) {
				for(int i = 0; i < mHolefaceRecordsList.size(); i ++) {
					_HolefaceSettingRecord mRecord = mHolefaceRecordsList.get(i);
					if(mRecord != null) {
						if(mRecord.getClassType() == null || "".equals(mRecord.getClassType())) {
							isSuc = false;
							break;
						}
					}
				}
			}*/
			if(isSuc) {
				Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
				finish();
				setResult(123);
			} else {
				Toast.makeText(mContext, "请选择风险", Toast.LENGTH_SHORT).show();
			}
			
		}
	};
	OnClickListener mFlushBtnListener = new OnClickListener() {
		public void onClick(View v) {
			new DBService(mContext).updateFlushRecords($riskId, $holefaceId, $riskType);
			if(mRiskSettingContainer != null) {
				mRiskSettingContainer.removeAllViews();
			 }
			reloadView();
		}
	};
	
	MDialog mSettingDialog = null;
	OnClickListener mClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			DefineButton mDefineBtn = (DefineButton)v;
			
			_HolefaceSettingRecord mRecord = mDefineBtn.getmRecord();
			_HolefaceSetting mSetting = mDefineBtn.getmSetting();
			
			if(mRecord != null && mSetting != null) {
				
				String mString = mSetting.getClass2();
				String[] mSubStringArr = mString.split(";");
				double val = 0;
				if(mSubStringArr != null)
				val = (double)((mSubStringArr.length + 1) * measureButtonHeight() + ScreenUtil.dp2px(mContext, 44))/(double)mScreenHeight;// 
				if(val == 0) {
					val = 0.3;
				}
				mSettingDialog = new MDialog(mContext, R.style.dialog);
				mSettingDialog.createDialog(R.layout.risk_holo_face_pop, 0.9, val, getWindowManager());
				mSettingDialog.setCanceledOnTouchOutside(false);
				LinearLayout container = (LinearLayout)mSettingDialog.findViewById(R.id.p35_risk_holo_face_container);
				DefineButton mFButton = new DefineButton(mContext);
				mFButton.setText(mSetting.getClass1());
				mFButton.setTextColor(getResources().getColor(R.color.white));
				mFButton.setmClassType(mSetting.getClass1());
				mFButton.setmClassTypeNum("1");
				mFButton.setBackgroundResource(R.drawable.common_blue_btn_selector);
				mFButton.setmRecord(mRecord);
				mFButton.setmSetting(mSetting);
				mFButton.setOnClickListener(mClickClassListener);
				container.addView(mFButton, new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.dp2px(mContext, 40)));
				TextView textView0 = new TextView(mContext);
				container.addView(textView0, new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 5));
				if(mSubStringArr != null && mSubStringArr.length > 0) {
					for(int i = 0,len = mSubStringArr.length; i < len; i ++) {
						DefineButton mButton = new DefineButton(mContext);
						String[] mArr = mSubStringArr[i].split(",");
						mButton.setText(mArr[0]);
						mButton.setTextColor(getResources().getColor(R.color.white));
						mButton.setmClassType(mArr[0]);
						mButton.setmClassTypeNum(mArr[1]);
						mButton.setBackgroundResource(R.drawable.common_blue_btn_selector);
						mButton.setmRecord(mRecord);
						mButton.setmSetting(mSetting);
						mButton.setOnClickListener(mClickClassListener);
						container.addView(mButton, new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.dp2px(mContext, 40)));
						TextView textView = new TextView(mContext);
						container.addView(textView, new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,5));
					}
				}
			}
		}
	};
	
	OnClickListener mClickClassListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			DefineButton mDefineBtn = (DefineButton)v;
			_HolefaceSettingRecord mRecord = mDefineBtn.getmRecord();
			_HolefaceSetting mSetting = mDefineBtn.getmSetting();
			if(mRecord != null && mSetting != null) {
				boolean isUpdate = new DBService(mContext).updateHolefaceSettingOneRecord(mRecord.get$id(), mSetting.getClass2Tip(), mDefineBtn.getmClassTypeNum(), mDefineBtn.getmClassType());
				if(!isUpdate) {
					Toast.makeText(mContext, "操作失败，请重试", Toast.LENGTH_SHORT).show();
				}
				if(mSettingDialog != null) {
					mSettingDialog.dismiss();
				}
				if(mRiskSettingContainer != null) {
					mRiskSettingContainer.removeAllViews();
				}
				reloadView();
			}
		}
	};
}
