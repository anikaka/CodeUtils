package com.tongyan.activity.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.tongyan.activity.R;
import com.tongyan.common.entities._Check;


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
public class SuperviceAdapter extends BaseAdapter {
	
	private LayoutInflater layoutInflater;
	private List<_Check> checkList;
	
	public SuperviceAdapter(Context context, List<_Check> checkList) {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.supervice_list_item, null);
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
		_Check check = checkList.get(position);
		if (check != null) {
			String currdate = "";
			try {
				Date date = new SimpleDateFormat("yyyy-MM-dd").parse(check.getUpTime());
				currdate = new SimpleDateFormat("yyyy年MM月dd日").format(date);
			} catch (ParseException e) {
				currdate = check.getUpTime();
				e.printStackTrace();
			}
			if ("0".equals(check.getIsUpdate())) {// 0:未检查,1:已检查,2:已上传
				holder.mCheckTextView.setText("未检查");
			} else if("1".equals(check.getIsUpdate())) {
				holder.mCheckTextView.setText("已检查");
			} else if("2".equals(check.getIsUpdate())) {
				holder.mCheckTextView.setText("已上传");
			} else {
				holder.mCheckTextView.setText("未检查");
			}
			holder.mItemTextView.setText(check.getaItemName());
			holder.mSectionTextView.setText(check.getaSecName());
			holder.mProjectTextView.setText(check.getaProName());
			holder.mMileTextView.setText(check.getaStartMile() + "~" + check.getaEndMile());
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
		public _Check checkObj;
	}
	
}
