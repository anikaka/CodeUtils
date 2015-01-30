package com.tongyan.activity.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * @ClassName P06_MsgAdapter 
 * @author wanghb
 * @date 2013-7-16 AM 11:20:37
 */
public class GpsSectionProgressAdapter extends BaseAdapter {
	
	private int resource;//绑定条目界面
	private ArrayList<HashMap<String, String>> mProjectList;
	private LayoutInflater inflater;
	
	public GpsSectionProgressAdapter(Context context, ArrayList<HashMap<String, String>> mProjectList, int resource) {
		this.mProjectList = mProjectList;
		this.resource = resource;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return mProjectList.size();
	}

	@Override
	public Object getItem(int position) {
		return mProjectList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(resource, null);
			holder = new ViewHolder();
			holder.mCode = (TextView) convertView.findViewById(R.id.progress_code);
			holder.mSsiName = (TextView) convertView.findViewById(R.id.SsiName);
			holder.mAfterchgnum = (TextView) convertView.findViewById(R.id.afterchgnum);
			holder.mMunit = (TextView) convertView.findViewById(R.id.munit);
			holder.mTodayfinishrate = (TextView) convertView.findViewById(R.id.todayfinishrate);
			holder.mTodayNum = (TextView) convertView.findViewById(R.id.todayNum);
			holder.mTodayfinishrateJE = (TextView) convertView.findViewById(R.id.todayfinishrateJE);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		HashMap<String, String> mItem = mProjectList.get(position);
		if(mItem != null) {
			holder.mCode.setText("清单细目编号：" + mItem.get("code"));
			holder.mSsiName.setText("名称：" + mItem.get("SsiName"));
			holder.mAfterchgnum.setText("分解量：" + mItem.get("afterchgnum"));
			holder.mMunit.setText("单位：" + mItem.get("munit"));
			holder.mTodayfinishrate.setText("至今日完成百分比：" + mItem.get("todayfinishrate"));
			holder.mTodayNum.setText("至今完成数量：" + mItem.get("todayNum"));
			holder.mTodayfinishrateJE.setText("至今日完成(元)：" + mItem.get("todayfinishrateJE"));
			holder.mData = mItem;
		}
		return convertView;
	}
	
	public final class ViewHolder {
		public TextView mCode;
		public TextView mSsiName;
		public TextView mAfterchgnum;
		public TextView mMunit;
		public TextView mTodayfinishrate;
		public TextView mTodayNum;
		public TextView mTodayfinishrateJE;
		public HashMap<String,String> mData;
	}
	
}
