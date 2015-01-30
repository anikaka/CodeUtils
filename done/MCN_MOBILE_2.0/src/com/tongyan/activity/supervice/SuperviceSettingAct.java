package com.tongyan.activity.supervice;

import java.util.List;
import java.util.Map;

import com.tongyan.activity.MyApplication;
import com.tongyan.activity.R;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.db.DBService;
import com.tongyan.common.entities._ListObj;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.WebServiceUtils;
import com.tongyan.widget.view.MSpinner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
/**
 * 
 * @className P29_SuperviceSettingAct
 * @author wanghb
 * @date 2013-8-13 pm 02:43:09
 * @Desc 监理监督-结果查看
 */
public class SuperviceSettingAct extends Activity {
	private Context mContext = this;
	private MSpinner mItemSpinner,mSectionSpinner,mTunnelSpinner;
	private _ListObj mSelectItem,mSelectSection,mSelectTunnel;
	private Button mButton;
	
	private _User localUser;
	private MyApplication mApplication;
	private String mIsSucc;
	private List<Map<String,String>> mRemoteList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		businessM();
	}
	
	private void initPage() {
		setContentView(R.layout.supervice_search_setting);
		mItemSpinner = (MSpinner)findViewById(R.id.p29_supervice_item_spinner);
		mSectionSpinner = (MSpinner)findViewById(R.id.p29_supervice_section_spinner);
		mTunnelSpinner = (MSpinner)findViewById(R.id.p29_supervice_time_edittext);
		mButton = (Button)findViewById(R.id.p29_supervice_sure_btn_id);
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null == mSelectItem || mSelectItem.getId() == null || "".equals(mSelectItem.getId())) {
					Toast.makeText(mContext, "请选择项目", Toast.LENGTH_SHORT).show();
				return;
			}
			if (null == mSelectSection  || mSelectSection.getId() == null || "".equals(mSelectSection.getId())) {
				Toast.makeText(mContext, "请选择标段", Toast.LENGTH_SHORT).show();
				return;
			}
			if(null == mSelectTunnel  || mSelectTunnel.getId() == null || "".equals(mSelectTunnel.getId())) {
				Toast.makeText(mContext, "请选择工程", Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent = new Intent(mContext,SearchSuperviceAct.class);
			intent.putExtra("unitId", mSelectTunnel.getId());
			startActivity(intent);
			}
		});
	}
	private void businessM(){
		mApplication = ((MyApplication)getApplication());
		mApplication.addActivity(this);
		localUser = mApplication.localUser;
		getRemote();
		final List<_ListObj> mItemList = new DBService(mContext).getItemListByObj();
		if(mItemList != null && mItemList.size() > 0) {
			ArrayAdapter<_ListObj> mHighWayAdapter = new ArrayAdapter<_ListObj>(mContext,R.layout.common_spinner, mItemList){
				 @Override
				public View getDropDownView(int position, View convertView, ViewGroup parent) {
					 	if (convertView == null) {
							convertView = LayoutInflater.from(mContext).inflate(R.layout.gps_list_item, null);
						} 
						TextView textView = (TextView)convertView.findViewById(R.id.p04_gps_content_item_text);
						String mTextContent = "选择项目";
						if(mItemList.get(position) != null) {
							mTextContent = mItemList.get(position).getText();
							if(mTextContent == null || "".equals(mTextContent)) {
								mTextContent = "选择项目";
							}
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
					String mTextContent = "选择项目";
					if(mItemList.get(position) != null) {
						mTextContent = mItemList.get(position).getText();
						if(mTextContent == null || "".equals(mTextContent)) {
							mTextContent = "选择项目";
							textView.setTextColor(Color.GRAY);
						}
					} else {
						textView.setTextColor(Color.GRAY);
					}
					textView.setText(mTextContent);
					return convertView;
				}
			 };
			 mItemSpinner.setAdapter(mHighWayAdapter);
			 mItemSpinner.setOnItemSelectedListener(mItemSpinnerListener);
		} else {
			initItemSpinner();
			initSectionSpinner();
			initProjectSpinner();
		}
	}
	
	OnItemSelectedListener mItemSpinnerListener = new OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			_ListObj item = (_ListObj) parent.getAdapter().getItem(position);
			if(item != null) {
			final List<_ListObj> mSectionList = new DBService(mContext).getSectionListByObj(item.getId());
			if(mSectionList != null && mSectionList.size() > 0) {
				ArrayAdapter<_ListObj> mSectionAdapter = new ArrayAdapter<_ListObj>(mContext,R.layout.common_spinner, mSectionList){
					 @Override
					public View getDropDownView(int position, View convertView, ViewGroup parent) {
						 if (convertView == null) {
								convertView = LayoutInflater.from(mContext).inflate(R.layout.gps_list_item, null);
							} 
							TextView textView = (TextView)convertView.findViewById(R.id.p04_gps_content_item_text);
							
							_ListObj mObj = mSectionList.get(position);
							if(mObj == null || mObj.getId()== null || "".equals(mObj.getId())) {
								textView.setText("选择标段");
							} else {
								textView.setText(mObj.getText());
							}
							return convertView;
					}
					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						if (convertView == null) {
							convertView = LayoutInflater.from(mContext).inflate(R.layout.common_spinner, null);
						} 
						TextView textView = (TextView)convertView.findViewById(R.id.spinner_id);
						_ListObj mObj = mSectionList.get(position);
						if(mObj == null || mObj.getId()== null || "".equals(mObj.getId())) {
							textView.setText("选择标段");
							textView.setTextColor(Color.GRAY);
						} else {
							textView.setText(mObj.getText());
						}
						return convertView;
					}
				 };
				 mSectionSpinner.setAdapter(mSectionAdapter);
				 mSectionSpinner.setOnItemSelectedListener(mSectionEditListener);
			} else {
				initSectionSpinner();
				initProjectSpinner();
			}
			} else {
				initSectionSpinner();
				initProjectSpinner();
			}
			mSelectItem = item;
			mSelectSection = null;
			mSelectTunnel = null;
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			
		}
	};
	public void initItemSpinner() {
		mItemSpinner.setmBackgroundView(R.layout.common_spinner);
		mItemSpinner.setmInitText("选择项目");
		mItemSpinner.setAdapter(mItemSpinner.getAdapter());
		mItemSpinner.setmTextColor(Color.GRAY);
	}
	public void initSectionSpinner() {
		mSectionSpinner.setmBackgroundView(R.layout.common_spinner);
	    mSectionSpinner.setmInitText("选择标段");
	    mSectionSpinner.setAdapter(mSectionSpinner.getmAdapter());
	    mSectionSpinner.setmTextColor(Color.GRAY);
	}
	
	public void initProjectSpinner() {
		mTunnelSpinner.setmBackgroundView(R.layout.common_spinner);
		mTunnelSpinner.setmInitText("选择工程");
		mTunnelSpinner.setAdapter(mTunnelSpinner.getmAdapter());
		mTunnelSpinner.setmTextColor(Color.GRAY);
	}
	
	
	
	OnItemSelectedListener mSectionEditListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			_ListObj item = (_ListObj) parent.getAdapter().getItem(position);
			if(item == null) {
				mTunnelSpinner.setmBackgroundView(R.layout.common_spinner);
				mTunnelSpinner.setmInitText("选择工程");
			    mTunnelSpinner.setAdapter(mTunnelSpinner.getmAdapter());
				return;
			}
			final List<_ListObj> mTunnelList = new DBService(mContext).getTunnelListByObj(item.getId());
			if(mTunnelList != null && mTunnelList.size() > 0) {
				ArrayAdapter<_ListObj> mSectionAdapter = new ArrayAdapter<_ListObj>(mContext,R.layout.common_spinner, mTunnelList){
					 @Override
					public View getDropDownView(int position, View convertView, ViewGroup parent) {
						 ////GetListNoPage
						 
						 if (convertView == null) {
								convertView = LayoutInflater.from(mContext).inflate(R.layout.supervice_spinner_item, null);
							} 
							TextView textView = (TextView)convertView.findViewById(R.id.p04_gps_content_item_text);
							TextView numsText = (TextView)convertView.findViewById(R.id.p29_project_nums);
							
							if(mRemoteList != null && mRemoteList.size() > 0) {
								for(int i = 0; i < mRemoteList.size(); i ++) {
									Map<String, String> map = mRemoteList.get(i);
									if(map != null && mTunnelList.get(position) != null && mTunnelList.get(position).getId() != null && mTunnelList.get(position).getId().equals(map.get("rowId"))) {
										numsText.setText(map.get("cCount") == null ? "0" : map.get("cCount"));
									}
								}
							}
							textView.setText(mTunnelList.get(position).getText());
							return convertView;
					}
					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						if (convertView == null) {
							convertView = LayoutInflater.from(mContext).inflate(R.layout.common_spinner, null);
						} 
						TextView textView = (TextView)convertView.findViewById(R.id.spinner_id);
						if(mTunnelList.get(position) != null) {
							String mTextContent= mTunnelList.get(position).getText();
							if(mTextContent == null || "".equals(mTextContent)) {
								mTextContent = "选择隧道";
								textView.setTextColor(Color.GRAY);
							}
							textView.setText(mTextContent);
						}
						return convertView;
					}
				 };
				 mTunnelSpinner.setAdapter(mSectionAdapter);
				 mTunnelSpinner.setOnItemSelectedListener(mTunnelEditListener);
			} else {
				initProjectSpinner();
			}
			mSelectSection = item;
			mSelectTunnel = null;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	};
	
	OnItemSelectedListener mTunnelEditListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			_ListObj item = (_ListObj) parent.getAdapter().getItem(position);
			if(item == null || item.getId() == null || "".equals(item)) {
				mSelectTunnel = null;
			} else {
				mSelectTunnel = item;
			}
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			
		}
	};
	
	public void getRemote() {
		 new Thread(new Runnable(){
				@Override
				public void run() {
					Map<String,Object> mR = null;
					try {
					String str = WebServiceUtils.getRequestStr(localUser.getUsername(), localUser.getPassword(), null, null, "GetSuperviseCount", "", Constansts.METHOD_OF_GETLISTNOPAGE, mContext);
					mR = new Str2Json().checkProjectNumsList(str);}catch(Exception e){}
					
					if(mR != null) {
						mIsSucc = (String)mR.get("s");
						if("ok".equals(mIsSucc)) {
							mRemoteList = (List<Map<String,String>>)mR.get("v");
						} 
					}
				}}).start();
	}
	
	
}
