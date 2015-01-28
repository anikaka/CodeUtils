package com.tongyan.yanan.common.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.yanan.act.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * @Title: OaScheduleListAdapter.java 
 * @author Rubert
 * @date 2014-8-7 下午04:39:55 
 * @version V1.0 
 * @Description: 日程列表适配器
 */
public class OaScheduleListAdapter extends BaseAdapter {

	private ArrayList<HashMap<String, Object>> mArrayList;
	private LayoutInflater mInflater;
	
	public OaScheduleListAdapter(Context context, ArrayList<HashMap<String, Object>> list) {
		this.mArrayList = list;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return mArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView mHolderView;
		if(convertView==null){
			mHolderView = new HolderView();
			convertView = mInflater.inflate(R.layout.oa_calendar_schedule_list_item, null);
			mHolderView.mTitleName = (TextView)convertView.findViewById(R.id.common_single_line);
			mHolderView.mStartTime = (TextView)convertView.findViewById(R.id.start_date);
			mHolderView.mEndTime = (TextView)convertView.findViewById(R.id.end_date);
			convertView.setTag(mHolderView);
		} else {
		 	 mHolderView = (HolderView)convertView.getTag();
		}
		 HashMap<String, Object> mMap = mArrayList.get(position);
		 if(mMap != null){
			 mHolderView.mItemData= mMap;
			 mHolderView.mTitleName.setText("标题：" + (String)mMap.get("Title"));
			 mHolderView.mStartTime.setText("开始时间：" + (String)mMap.get("WorkTime"));
			 mHolderView.mEndTime.setText("结束时间：" + (String)mMap.get("WarnTime"));
		 }
		return convertView;
	}

	 public class  HolderView{
		  private TextView  mTitleName;
		  private TextView  mStartTime;
		  private TextView  mEndTime;
		  public  HashMap<String, Object> mItemData;
	 }
}
