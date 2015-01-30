package com.tongyan.activity.risk;

import java.util.List;

import com.tongyan.activity.MyApplication;
import com.tongyan.activity.R;
import com.tongyan.common.db.DBService;
import com.tongyan.common.entities._ListObj;
import com.tongyan.widget.view.MSpinner;
import com.tongyan.widget.view.MyDatePickerDialog;
import com.tongyan.widget.view.MyDatePickerDialog.OnDateTimeSetListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
/**
 * 
 * @ClassName P42_RiskShowSetAct.java
 * @Author wanghb
 * @Date 2013-10-15 am 11:20:42
 * @Desc TODO
 */
public class P42_RiskShowSetAct extends Activity {
	
	private Context mContext = this;
	private MSpinner mItemSpinner,mSectionSpinner;
	private EditText mEditText;
	private Button mCommitBtn;
	_ListObj mSelectItem,mSelectSection;
	String mDateTiem = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessM();
	}
	
	private void initPage() {
		setContentView(R.layout.risk_showing_setting);
		mItemSpinner = (MSpinner)findViewById(R.id.p42_risk_showing_item_spinner);
		mSectionSpinner = (MSpinner)findViewById(R.id.p42_risk_showing_section_spinner);
		mEditText = (EditText)findViewById(R.id.p42_risk_showing_time_edittext);
		mCommitBtn = (Button)findViewById(R.id.p42_risk_showing_sure_btn_id);
	}
	
	private void setClickListener() {
		((MyApplication)getApplication()).addActivity(this);
		mEditText.setInputType(InputType.TYPE_NULL);//首次禁用软键盘
		mEditText.setCursorVisible(false);//去光标
		mEditText.setOnClickListener(mDateClickListener);
		mCommitBtn.setOnClickListener(mClickSureBtnListener);
	}
	
	private void businessM(){
		final List<_ListObj> mItemList = new DBService(mContext).getItemListByObj();
		if(mItemList != null && mItemList.size() > 0) {
			ArrayAdapter<_ListObj> mHighWayAdapter = new ArrayAdapter<_ListObj>(mContext,R.layout.common_spinner, mItemList){
				 @Override
				public View getDropDownView(int position, View convertView, ViewGroup parent) {
					 	if (convertView == null) {
							convertView = LayoutInflater.from(mContext).inflate(R.layout.gps_list_item, null);
						} 
						TextView textView = (TextView)convertView.findViewById(R.id.p04_gps_content_item_text);
						String mTextContent= mItemList.get(position).getText();
						if(mTextContent == null || "".equals(mTextContent) ) {
							mTextContent = getResources().getString(R.string.p04_gps_select_project_hint);
						} 
						textView.setText(mTextContent);
						return convertView;
				}
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					if (convertView == null) {
						convertView = LayoutInflater.from(mContext).inflate(R.layout.common_spinner, null);
					} 
					TextView textView = (TextView)convertView.findViewById(R.id.spinner_id);
					String mTextContent= mItemList.get(position).getText();
					if(mTextContent == null || "".equals(mTextContent) ) {
						mTextContent = getResources().getString(R.string.p04_gps_select_project_hint);
						textView.setTextColor(Color.GRAY);
					} 
					textView.setText(mTextContent);
					return convertView;
				}
			 };
			 mItemSpinner.setAdapter(mHighWayAdapter);
			 mItemSpinner.setOnItemSelectedListener(mHighWayEditListener);
		} else {
			mItemSpinner.setmBackgroundView(R.layout.common_spinner);
			mItemSpinner.setmInitText("选择项目");
			mItemSpinner.setAdapter(mItemSpinner.getAdapter());
			mItemSpinner.setmTextColor(Color.GRAY);
			
			mSectionSpinner.setmBackgroundView(R.layout.common_spinner);
		    mSectionSpinner.setmInitText("选择标段");
		    mSectionSpinner.setAdapter(mSectionSpinner.getmAdapter());
		    mSectionSpinner.setmTextColor(Color.GRAY);
		}
	}
	
	OnItemSelectedListener mHighWayEditListener = new OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			_ListObj item = (_ListObj) parent.getAdapter().getItem(position);
			final List<_ListObj> mSectionList = new DBService(mContext).getSectionListByObj(item.getId());
			if(mSectionList != null && mSectionList.size() > 0) {
				ArrayAdapter<_ListObj> mSectionAdapter = new ArrayAdapter<_ListObj>(mContext,R.layout.common_spinner, mSectionList){
					 @Override
					public View getDropDownView(int position, View convertView, ViewGroup parent) {
						 if (convertView == null) {
								convertView = LayoutInflater.from(mContext).inflate(R.layout.gps_list_item, null);
							} 
							TextView textView = (TextView)convertView.findViewById(R.id.p04_gps_content_item_text);
							String mTextContent= mSectionList.get(position).getText();
							if(mTextContent == null || "".equals(mTextContent) ) {
								mTextContent = getResources().getString(R.string.p04_gps_select_section_hint);
							} 
							textView.setText(mTextContent);
							return convertView;
					}
					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						if (convertView == null) {
							convertView = LayoutInflater.from(mContext).inflate(R.layout.common_spinner, null);
						} 
						TextView textView = (TextView)convertView.findViewById(R.id.spinner_id);
						String mTextContent= mSectionList.get(position).getText();
						if(mTextContent == null || "".equals(mTextContent) ) {
							mTextContent = getResources().getString(R.string.p04_gps_select_section_hint);
							textView.setTextColor(Color.GRAY);
						} 
						textView.setText(mTextContent);
						return convertView;
					}
				 };
				 mSectionSpinner.setAdapter(mSectionAdapter);
				 mSectionSpinner.setOnItemSelectedListener(mSectionEditListener);
			} else {
				mSectionSpinner.setmBackgroundView(R.layout.common_spinner);
			    mSectionSpinner.setmInitText("选择标段");
			    mSectionSpinner.setAdapter(mSectionSpinner.getmAdapter());
			    mSectionSpinner.setmTextColor(Color.GRAY);
			}
			mSelectItem = item;
			mSelectSection = null;
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			
		}
	};
	OnItemSelectedListener mSectionEditListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			_ListObj item = (_ListObj) parent.getAdapter().getItem(position);
			mSelectSection = item;
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};
	
	OnClickListener mDateClickListener = new OnClickListener() {
		public void onClick(View v) {
			new MyDatePickerDialog(mContext, new OnDateTimeSetListener() {
				@Override
				public void onDateTimeSet(int year, int monthOfYear, int dayOfMonth,
						int hour, int minute) {
					String mMonth = monthOfYear < 10 ? "0" + monthOfYear : String.valueOf(monthOfYear);
					String mDay =  dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
					
					mDateTiem = year+"-" + mMonth + "-" + mDay;
					mEditText.setText(mDateTiem);
				}
			}).show();
		}
	};
	
	OnClickListener mClickSureBtnListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext,P38_RiskShowingAct.class);
			intent.putExtra("mItemId", mSelectItem.getId());
			intent.putExtra("mSectionId", mSelectSection.getId());
			intent.putExtra("mDateTiem", mDateTiem);
			startActivity(intent);
		}
	};
}
