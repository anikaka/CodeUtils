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
 * @category月计划列表适配器
 * @author ChenLang
 * @date 2014/07/10
 * @version YanAn1.0
 */
public class CommonSingleLineListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, String>> mArrayList;
	private int mResoureId;
	private LayoutInflater mInflater;
	
	public CommonSingleLineListAdapter(Context context, ArrayList<HashMap<String, String>> list, int resoureId) {
		this.mContext = context;
		this.mArrayList = list;
		this.mResoureId = resoureId;
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
		HolderView mHolderView = null;
		if(convertView==null){
			mHolderView = new HolderView();
			convertView = mInflater.inflate(mResoureId, null);
			mHolderView.mCommonText=(TextView)convertView.findViewById(R.id.common_single_line);
			convertView.setTag(mHolderView);
		} else {
		 	 mHolderView = (HolderView)convertView.getTag();
		}
		 HashMap<String, String> mMap=mArrayList.get(position);
		 if(mMap != null && mMap.size() > 0){
			 mHolderView.mCommonText.setText(mMap.get("DName"));
			 mHolderView.mMap = mMap;
		 }
		return convertView;
	}

	 public class HolderView {
		  private TextView  mCommonText;
		  public  HashMap<String, String> mMap;
	 }
}
