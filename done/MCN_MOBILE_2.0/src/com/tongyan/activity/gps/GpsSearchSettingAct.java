package com.tongyan.activity.gps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tongyan.activity.MyApplication;
import com.tongyan.activity.R;
import com.tongyan.common.db.DBService;
import com.tongyan.common.entities._Item;
import com.tongyan.common.entities._Project;
import com.tongyan.common.entities._Section;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @ClassName P04_GpsSelectAct.java
 * @Author wanghb
 * @Date 2013-9-12 pm  02:53:16
 * @Desc 在岗查询-条件设置
 */
public class GpsSearchSettingAct extends Activity {
	
	private EditText mTimeEditText;
	
	private Spinner mDistanceSpinner;
	
	private MSpinner mSectionSpinner,mProjectSpinner,mItemSpinner;
	
	
	private String mTimeHide,mDistance,mProjectId,mProjectName,mSectionName,mItemName;
	
	private Button mFinishBtn;
	
	private Context mContext = GpsSearchSettingAct.this;
	
	
	private List<_Item> mItemList = new ArrayList<_Item>();
	private List<_Section> mSectionList = new ArrayList<_Section>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessM();
	}
	
	private void initPage() {
		setContentView(R.layout.gps_search_setting);
		((MyApplication)getApplication()).addActivity(this);
		mTimeEditText = (EditText)findViewById(R.id.p04_gps_select_time_edittext);
		mItemSpinner = (MSpinner)findViewById(R.id.p04_gps_select_item_spinner);
		mSectionSpinner = (MSpinner)findViewById(R.id.p04_gps_select_section_spinner);
		mProjectSpinner = (MSpinner)findViewById(R.id.p04_gps_select_project_spinner);
		mDistanceSpinner = (Spinner)findViewById(R.id.p04_gps_select_distance_spinner);
		
		mFinishBtn = (Button)findViewById(R.id.p04_gps_select_sure_btn_id);
	}
	
	private void setClickListener() {
		mTimeEditText.setInputType(InputType.TYPE_NULL);//首次禁用软键盘
		mTimeEditText.setCursorVisible(false);//去光标
		mTimeEditText.setOnClickListener(mTimeEditListener);
		mFinishBtn.setOnClickListener(mFinishBtnListener);
	}
	
	private void businessM(){
		List<_Item> itemSecList = new DBService(mContext).selectItemSec();
		//itemSecList = null;
		if(itemSecList != null && itemSecList.size() > 0) {
			if(mItemList != null) {
				mItemList.clear();
			}
			mItemList.addAll(itemSecList);
			ArrayAdapter<_Item> mItemAdapter = new ArrayAdapter<_Item>(this,R.layout.common_spinner, mItemList){
				 @Override
				public View getDropDownView(int position, View convertView,
						ViewGroup parent) {
					 if (convertView == null) {
							convertView = LayoutInflater.from(mContext).inflate(R.layout.gps_list_item, null);
						} 
						TextView textView = (TextView)convertView.findViewById(R.id.p04_gps_content_item_text);
						if(mItemList.get(position) != null) {
							textView.setText(mItemList.get(position).getText());
						}
						return convertView;
				}
				 
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					if (convertView == null) {
						convertView = LayoutInflater.from(mContext).inflate(R.layout.common_spinner, null);
					} 
					TextView textView = (TextView)convertView.findViewById(R.id.spinner_id);
					if(mItemList.get(position) != null) {
						String mTextContent= mItemList.get(position).getText();
						if(mTextContent == null || "".equals(mTextContent) ) {
							mTextContent = getResources().getString(R.string.p04_gps_select_project_hint);
							textView.setTextColor(Color.GRAY);
						} 
						textView.setText(mTextContent);
					}
					return convertView;
				}
			 };
			mItemSpinner.setAdapter(mItemAdapter);
			
		} else {
			mItemSpinner.setmInitText("选择项目");
			mItemSpinner.setAdapter(mItemSpinner.getmAdapter());
			mItemSpinner.setmTextColor(Color.GRAY);
			mSectionSpinner.setmInitText("选择标段");
			mSectionSpinner.setAdapter(mSectionSpinner.getmAdapter());
			mSectionSpinner.setmTextColor(Color.GRAY);
			mProjectSpinner.setmInitText("选择工程");
			mProjectSpinner.setAdapter(mProjectSpinner.getmAdapter());
			mProjectSpinner.setmTextColor(Color.GRAY);
		}
		mItemSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				_Item item = (_Item) parent.getAdapter().getItem(position);
				if(item == null) {
					return;
				}
				mItemName = item.getText();//项目名
				mProjectId = "";
				mProjectName = "";
				mSectionName = "";
				if(item.getSecList() != null) {
					mSectionList.clear();
				}
				mSectionList.addAll(item.getSecList());
				
				if(mSectionList.size() > 0) {
					 ArrayAdapter<_Section> mSectionAdapter = new ArrayAdapter<_Section>(mContext,R.layout.common_spinner, mSectionList){
						 @Override
						public View getDropDownView(int position, View convertView,
								ViewGroup parent) {
							 	if (convertView == null) {
									convertView = LayoutInflater.from(mContext).inflate(R.layout.gps_list_item, null);
								} 
								TextView textView = (TextView)convertView.findViewById(R.id.p04_gps_content_item_text);
								if(mSectionList.get(position) != null) {
									textView.setText(mSectionList.get(position).getText());
								}
								return convertView;
						}
						 
						@Override
						public View getView(int position, View convertView, ViewGroup parent) {
							if (convertView == null) {
								convertView = LayoutInflater.from(mContext).inflate(R.layout.common_spinner, null);
							} 
							TextView textView = (TextView)convertView.findViewById(R.id.spinner_id);
							if(mSectionList.get(position) != null) {
								String mTextContent = mSectionList.get(position).getText();
								if(mTextContent == null || "".equals(mTextContent)) {
									textView.setTextColor(Color.GRAY);
									mTextContent = "选择标段";
								}
								textView.setText(mTextContent);
							}
							return convertView;
						}
					 };
					 mSectionSpinner.setAdapter(mSectionAdapter);
				} else {
					mSectionSpinner.setmInitText("选择标段");
					mSectionSpinner.setAdapter(mSectionSpinner.getmAdapter());
					mSectionSpinner.setmTextColor(Color.GRAY);
					mProjectSpinner.setmInitText("选择工程");
					mProjectSpinner.setAdapter(mProjectSpinner.getmAdapter());
					mProjectSpinner.setmTextColor(Color.GRAY);
				}
				
				 mSectionSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
					@Override
					public void onItemSelected(AdapterView<?> parent2,
							View view, int position2, long id) {
						
						_Section section = (_Section) parent2.getAdapter().getItem(position2);
						if(section == null) {
							return;
						}
						mSectionName = section.getText();
						mProjectId = "";
						mProjectName = "";
						final List<_Project> mProjectList = new DBService(mContext).selectProjectListBySecId(section.getId());
						
						if(mProjectList != null && mProjectList.size() > 0) {
							ArrayAdapter<_Project> mProjectAdapter = new ArrayAdapter<_Project>(mContext,R.layout.common_spinner, mProjectList){
								 @Override
								public View getDropDownView(int position, View convertView,
										ViewGroup parent) {
									 if (convertView == null) {
											convertView = LayoutInflater.from(mContext).inflate(R.layout.gps_list_item, null);
										} 
										TextView textView = (TextView)convertView.findViewById(R.id.p04_gps_content_item_text);
										if(mProjectList.get(position) != null) {
											textView.setText(mProjectList.get(position).getaName());
										}
										return convertView;
								}
								@Override
								public View getView(int position, View convertView, ViewGroup parent) {
									if (convertView == null) {
										convertView = LayoutInflater.from(mContext).inflate(R.layout.common_spinner, null);
									} 
									TextView textView = (TextView)convertView.findViewById(R.id.spinner_id);
									if(mProjectList.get(position) != null) {
										String mTextContent = mProjectList.get(position).getaName();
										if(mTextContent == null || "".equals(mTextContent)) {
											textView.setTextColor(Color.GRAY);
											mTextContent = "选择工程";
										}
										textView.setText(mTextContent);
									}
									return convertView;
								}
							 };
							mProjectSpinner.setAdapter(mProjectAdapter);
						} else {
							mProjectSpinner.setmInitText("选择工程");
							mProjectSpinner.setAdapter(mProjectSpinner.getmAdapter());
							mProjectSpinner.setmTextColor(Color.GRAY);
						}
						mProjectSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
							@Override
							public void onItemSelected(AdapterView<?> parent,
									View view, int position, long id) {
								_Project project = (_Project) parent.getAdapter().getItem(position);
								if(project == null) {
									return;
								}
								mProjectId = project.getRowId();
								mProjectName = project.getaName();
							}
							@Override
							public void onNothingSelected(AdapterView<?> parent) {
								
							}
						});
					}
					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						
					}
				 });
				 
				 
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
		//#### distance ####
		final ArrayList<String> mDisList = new ArrayList<String>();
		String[] mDisAry = getResources().getStringArray(R.array.p04_gps_distance);
		Collections.addAll(mDisList, mDisAry);
		
		ArrayAdapter<String> mDistanceAdapter = new ArrayAdapter<String>(mContext, R.layout.common_spinner, mDisList){
			 @Override
			public View getDropDownView(int position, View convertView,
					ViewGroup parent) {
				 if (convertView == null) {
						convertView = LayoutInflater.from(mContext).inflate(R.layout.gps_list_item, null);
					} 
					TextView textView = (TextView)convertView.findViewById(R.id.p04_gps_content_item_text);
					textView.setText(mDisList.get(position)+ "米");
					return convertView;
			}
			 
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = LayoutInflater.from(mContext).inflate(R.layout.common_spinner, null);
				} 
				TextView textView = (TextView)convertView.findViewById(R.id.spinner_id);
				textView.setText(mDisList.get(position) + "米");
				return convertView;
			}
		 };
		mDistanceSpinner.setAdapter(mDistanceAdapter);
		mDistanceSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mDistance = mDisList.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
			
		});
	}
	
	// ===========================================================
	// part of Listener
	// ===========================================================
	OnClickListener mTimeEditListener = new OnClickListener() {
		public void onClick(View v) {
			new MyDatePickerDialog(mContext, new OnDateTimeSetListener() {
				@Override
				public void onDateTimeSet(int year, int monthOfYear, int dayOfMonth,
						int hour, int minute) {
					String mMonth = monthOfYear < 10 ? "0" + monthOfYear : String.valueOf(monthOfYear);
					String mDay =  dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
					
					mTimeHide = year+"-"+mMonth +"-"+mDay;
					mTimeEditText.setText(year + "年" + mMonth + "月" + mDay + "日");
				}
			}).show();
		}
	};
	
	OnClickListener mFinishBtnListener = new OnClickListener() {
		public void onClick(View v) {
			if(mTimeHide == null || "".equals(mTimeHide)) {//Time
				Toast.makeText(mContext, "请选择时间", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(mItemName == null || "".equals(mItemName)) {//Item
				Toast.makeText(mContext, "请选择项目", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(mSectionName == null || "".equals(mSectionName)) {//section
				Toast.makeText(mContext, "请选择标段", Toast.LENGTH_SHORT).show();
				return;
			}
			
			
			if(mProjectId == null || "".equals(mProjectId) || mProjectName == null || "".equals(mProjectName) ) {//project
				Toast.makeText(mContext, "请选择工程", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(mDistance == null || "".equals(mDistance)) {//Distance
				Toast.makeText(mContext, "请选择距离", Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent = new Intent(mContext,GpsSearchEmpListAct.class);
			intent.putExtra("mTimeHide", mTimeHide);
			intent.putExtra("mItemName", mItemName);
			intent.putExtra("mSectionName", mSectionName);
			intent.putExtra("mProjectId", mProjectId);
			intent.putExtra("mProjectName", mProjectName);
			intent.putExtra("mDistance", mDistance);
			startActivity(intent);
		}
	};
	
}
