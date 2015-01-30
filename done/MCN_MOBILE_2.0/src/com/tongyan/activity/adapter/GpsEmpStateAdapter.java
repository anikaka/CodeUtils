package com.tongyan.activity.adapter;

import java.util.LinkedList;

import com.tongyan.activity.R;
import com.tongyan.common.entities._LocationInfo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * @ClassName P04_GpsEmpStateAdapter.java
 * @Author wanghb
 * @Date 2013-9-24 pm  07:19:51
 * @Desc TODO
 */
public class GpsEmpStateAdapter extends BaseAdapter {
	
	private LayoutInflater layoutInflater;
	private  LinkedList<_LocationInfo> mLocationStateList;
	
	public GpsEmpStateAdapter(Context context, LinkedList<_LocationInfo> mLocationStateList) {
		layoutInflater = LayoutInflater.from(context);
		this.mLocationStateList = mLocationStateList;
	}
	
	@Override
	public int getCount() {
		return mLocationStateList.size();
	}

	@Override
	public Object getItem(int position) {
		return mLocationStateList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.gps_emp_state_list_item, null);
			holder = new ViewHolder();
			holder.mNameTextView = (TextView)convertView.findViewById(R.id.p04_gps_emp_state_item_name_content);
			holder.mLocationTextView = (TextView)convertView.findViewById(R.id.p04_gps_emp_state_item_locnums_content);
			holder.mDepartmentTextView = (TextView)convertView.findViewById(R.id.p04_gps_emp_state_item_department_content);
			holder.mJobTextView = (TextView)convertView.findViewById(R.id.p04_gps_emp_state_item_job_content);
			//change gray textview
			holder.mCnameTextView = (TextView)convertView.findViewById(R.id.p04_gps_emp_state_item_name_hint);
			holder.mClocnumsTextView = (TextView)convertView.findViewById(R.id.p04_gps_emp_state_item_locnums_hint);
			holder.mClickBtn = (ImageView)convertView.findViewById(R.id.p04_gps_emp_state_click_btn);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		holder.mLocatinInfo = mLocationStateList.get(position);
		holder.mNameTextView.setText(holder.mLocatinInfo.getEmp_name());
		holder.mLocationTextView.setText(holder.mLocatinInfo.getNum());
		holder.mDepartmentTextView.setText(holder.mLocatinInfo.getDpt_name());
		holder.mJobTextView.setText(holder.mLocatinInfo.getR_name());
		
		if("0".equals(holder.mLocatinInfo.getNum())) {
			holder.mCnameTextView.setTextColor(Color.parseColor("#808080"));
			holder.mClocnumsTextView.setTextColor(Color.parseColor("#808080"));
			holder.mNameTextView.setTextColor(Color.parseColor("#808080"));
			holder.mLocationTextView.setTextColor(Color.parseColor("#808080"));
			holder.mClickBtn.setBackgroundResource(R.drawable.p00_next_bow_btn);
		} else {
			holder.mClickBtn.setBackgroundResource(R.drawable.gps_pop_btn_selector);
			holder.mCnameTextView.setTextColor(Color.parseColor("#000000"));
			holder.mClocnumsTextView.setTextColor(Color.parseColor("#000000"));
			holder.mNameTextView.setTextColor(Color.parseColor("#000000"));
			holder.mLocationTextView.setTextColor(Color.parseColor("#000000"));
		}
		return convertView;
	}
	
	public final class ViewHolder {
		public TextView mNameTextView;
		public TextView mDepartmentTextView;
		public TextView mLocationTextView;
		public TextView mJobTextView;
		
		public TextView mCnameTextView;
		public TextView mClocnumsTextView;
		
		public ImageView mClickBtn;
		
		public _LocationInfo mLocatinInfo;
	}
	
}
