package com.tongyan.yanan.common.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.yanan.act.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author ChenLang
 * 沉降界面 ListView适配器
 */
public class SubsideTypeAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private int  mResoureLayout;
	private ArrayList<HashMap<String, String>>  mListSubside;

	HolderView  holderView;
	public SubsideTypeAdapter(Context context,ArrayList<HashMap<String, String>> list,int resoureLayout){
		this.mContext=context;
		this.mListSubside=list;
		this.mResoureLayout=resoureLayout;
		mInflater=LayoutInflater.from(mContext);
	} 
	@Override
	public int getCount() {
		return mListSubside.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(mResoureLayout, null);
			holderView = new HolderView();
			holderView.layoutTitle = (LinearLayout) convertView.findViewById(R.id.ll_title);
			holderView.layoutContent = (LinearLayout) convertView.findViewById(R.id.ll_content);
			holderView.titleTv = (TextView) convertView.findViewById(R.id.txt_item_title);
			holderView.contentTv = (TextView) convertView.findViewById(R.id.txt_item_content);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView)convertView.getTag();
		}
		HashMap<String, String> map = mListSubside.get(position);
		if (map != null) {
			if ("title".equals(map.get("attribute"))) {
				holderView.titleTv.setText(map.get("MonitorTypeName"));
				holderView.layoutTitle.setVisibility(View.VISIBLE);
				holderView.layoutContent.setVisibility(View.GONE);
			} else {
				holderView.contentTv.setText(map.get("MonitorTypeName"));
				holderView.layoutContent.setVisibility(View.VISIBLE);
				holderView.layoutTitle.setVisibility(View.GONE);
			}
			holderView.mMonitorType = map;
		}
	    return convertView;
	}

	public class HolderView {
		public LinearLayout layoutTitle;
		public LinearLayout layoutContent;
		public TextView titleTv;
		public TextView contentTv;
		public HashMap<String, String> mMonitorType;
	}
}
