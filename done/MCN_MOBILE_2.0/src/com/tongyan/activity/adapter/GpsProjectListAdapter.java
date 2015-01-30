package com.tongyan.activity.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 
 * @ClassName P06_MsgAdapter 
 * @author wanghb
 * @date 2013-7-16 AM 11:20:37
 */
public class GpsProjectListAdapter extends BaseAdapter {
	
	private int resource;//绑定条目界面
	private ArrayList<HashMap<String, Object>> mProjectList;
	private LayoutInflater inflater;
	
	public GpsProjectListAdapter(Context context, ArrayList<HashMap<String, Object>> mProjectList, int resource) {
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
			holder.mContent = (TextView) convertView.findViewById(R.id.project_item_content);
			holder.mContentCode = (TextView) convertView.findViewById(R.id.project_item_project_code);
			holder.mDistance = (TextView) convertView.findViewById(R.id.project_item_distance);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		HashMap<String, Object> mProject = mProjectList.get(position);
		if(mProject != null) {
			holder.mContent.setText((String)mProject.get("subName"));
			holder.mDistance.setText("距离：" +mProject.get("distance"));
			holder.mContentCode.setText("编号：" + mProject.get("subCode"));
			holder.mProject = mProject;
		}
		return convertView;
	}
	
	public final class ViewHolder {
		public TextView mContent;
		public TextView mContentCode;
		public TextView mDistance;
		public HashMap<String,Object> mProject;
	}
	
}
