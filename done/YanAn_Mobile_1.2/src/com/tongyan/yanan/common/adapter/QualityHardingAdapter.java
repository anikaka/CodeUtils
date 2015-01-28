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
 * @Title: QualityHardingAdapter.java 
 * @author Rubert
 * @date 2014-8-14 PM 03:48:04 
 * @version V1.0 
 * @Description: TODO
 */
public class QualityHardingAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private int  mResoureLayout;
	private ArrayList<HashMap<String, String>>  mListSubside;

	HolderView  holderView;
	public QualityHardingAdapter(Context context,ArrayList<HashMap<String, String>> list,int resoureLayout){
		this.mContext=context;
		this.mListSubside=list;
		this.mResoureLayout=resoureLayout;
		mInflater = LayoutInflater.from(mContext);
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
			holderView.titleTv = (TextView) convertView.findViewById(R.id.quality_harding_of_ground_list_item_title);
			holderView.contentTv = (TextView) convertView.findViewById(R.id.quality_harding_of_ground_list_item_content);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView)convertView.getTag();
		}
		HashMap<String, String> map = mListSubside.get(position);
		if (map != null) {
			if ("title".equals(map.get("attribute"))) {
				holderView.titleTv.setVisibility(View.VISIBLE);
				holderView.contentTv.setVisibility(View.GONE);
				holderView.titleTv.setText(map.get("ChangeName"));
			} else {
				holderView.titleTv.setVisibility(View.GONE);
				holderView.contentTv.setVisibility(View.VISIBLE);
				holderView.contentTv.setText(map.get("AreaName"));
			}
			holderView.mItemData = map;
		}
	    return convertView;
	}

	public class HolderView {
		public TextView titleTv;
		public TextView contentTv;
		public HashMap<String, String> mItemData;
	}
}
