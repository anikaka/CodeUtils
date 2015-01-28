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
 * @Title: QualityHardingAddListAdapter.java 
 * @author Rubert
 * @date 2014-8-14 PM 03:48:04 
 * @version V1.0 
 * @Description: TODO
 */
public class QualityHardingAddListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private int  mResoureLayout;
	private ArrayList<HashMap<String, String>>  mListSubside;

	HolderView  holderView;
	public QualityHardingAddListAdapter(Context context,ArrayList<HashMap<String, String>> list,int resoureLayout){
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
		return mListSubside.get(position);
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
			holderView.titleTv = (TextView) convertView.findViewById(R.id.txtMonthDate);
			holderView.contentTv = (TextView) convertView.findViewById(R.id.txtMonthDateState);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView)convertView.getTag();
		}
		HashMap<String, String> map = mListSubside.get(position);
		if (map != null) {
			
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
