package com.tongyan.yanan.act.oa;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.OaScheduleListAdapter;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
/**
 * 
 * @Title: OaScheduleListAct.java 
 * @author Rubert
 * @date 2014-8-5 AM 10:47:35 
 * @version V1.0 
 * @Description: 日程列表
 */
public class OaScheduleListAct extends FinalActivity {
	
	@ViewInject(id=R.id.title_common_content) TextView mTitleName;
	@ViewInject(id=R.id.listView_data) ListView mListView;
	
	private Context mContext = this;
	private SharedPreferences mPreferences;
	private String mUserid  = null;
	private ArrayList<HashMap<String, Object>> mAgendasList = new ArrayList<HashMap<String, Object>>();
	private OaScheduleListAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		mUserid = mPreferences.getString(Constants.PREFERENCES_INFO_USERID, "");
		setContentView(R.layout.common_listview_layout);
		mAdapter = new OaScheduleListAdapter(mContext, mAgendasList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(mItemListener);
		Bundle mBundle = getIntent().getExtras();
		if(mBundle != null) {
			mTitleName.setText(mBundle.getString("title"));
			ArrayList<HashMap<String, Object>> list  = (ArrayList<HashMap<String, Object>>)mBundle.get("agendasList");
			refreshListView(list);
		}
	}
	
	public void refreshListView(ArrayList<HashMap<String, Object>> list) {
		if(mAgendasList != null) {
			mAgendasList.clear();
			if(list != null) {
				mAgendasList.addAll(list);
			}
		}
		mAdapter.notifyDataSetChanged();
	}
	
	OnItemClickListener mItemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			OaScheduleListAdapter.HolderView mHolderView = (OaScheduleListAdapter.HolderView)arg1.getTag();
			HashMap<String, Object> map = mHolderView.mItemData;
			if(map != null) {
				if(!mUserid.equals(map.get("UserId"))) {
					Intent intent = new Intent(mContext, OaScheduleDetailAct.class);
					intent.putExtra("IntentType", "OaScheduleListAct");
					intent.putExtra("IntentMap",map);
					startActivity(intent);
				} else {
					Intent intent = new Intent(mContext, OaScheduleAddAct.class);
					intent.putExtra("IntentType", "OaScheduleListAct");
					intent.putExtra("IntentMap",map);
					startActivityForResult(intent, 9897);
				}
			}
		}
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 2345) {
			if(resultCode == 8888) {
				finish();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
