package com.tongyan.activity.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

import com.tongyan.activity.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 
 * @ClassName P30_SuperviceAdapter 
 * @author wanghb
 * @date 2013-7-29 am 10:44:13
 * @desc TODO
 */
public class SuperviceResultAdapter extends BaseAdapter {
	
	private LayoutInflater layoutInflater;
	private LinkedList<Map<String,String>> checkList;
	
	public SuperviceResultAdapter(Context context, LinkedList<Map<String,String>> checkList) {
		layoutInflater = LayoutInflater.from(context);
		this.checkList = checkList;
	}
	
	
	@Override
	public int getCount() {
		return checkList == null ? 0 : checkList.size();
	}

	@Override
	public Object getItem(int position) {
		if (checkList != null) {
			return checkList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	/*
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.supervice_result_list_item, null);
			holder = new ViewHolder();
			holder.mItemTextView = (TextView) convertView.findViewById(R.id.p30_supervice_item);
			holder.mSectionTextView = (TextView) convertView.findViewById(R.id.p30_supervice_section);
			holder.mProjectTextView = (TextView) convertView.findViewById(R.id.p30_supervice_project);
			holder.mMileTextView = (TextView) convertView.findViewById(R.id.p30_supervice_mile);
			holder.mTimeTextView = (TextView) convertView.findViewById(R.id.p30_supervice_time);
			holder.mCheckTextView = (TextView) convertView.findViewById(R.id.p30_supervice_check);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		Map<String,String> check = checkList.get(position);
		if (check != null) {
			String currdate = "";
			try {
				Date date = new SimpleDateFormat("yyyy-MM-dd").parse(check.get("aCheckDate"));
				currdate = new SimpleDateFormat("yyyy年MM月dd日").format(date);
			} catch (ParseException e) {
				currdate = check.get("aCheckDate") == null ? "": check.get("aCheckDate");
				e.printStackTrace();
			}
			holder.mCheckTextView.setText(check.get("checkEmp"));
			holder.mItemTextView.setText(check.get("projName"));
			holder.mSectionTextView.setText(check.get("sectionName"));
			holder.mProjectTextView.setText(check.get("unitName"));
			holder.mMileTextView.setText(check.get("aStartMile") + "~" + check.get("aEndMile"));
			holder.mTimeTextView.setText(currdate);
			holder.checkObj = check;
		}
		return convertView;
	}
	
	public final class ViewHolder {
		public TextView mItemTextView;
		public TextView mSectionTextView;
		public TextView mProjectTextView;
		public TextView mMileTextView;
		public TextView mTimeTextView;
		public TextView mCheckTextView;
		public Map<String,String> checkObj;
	}
	
}
