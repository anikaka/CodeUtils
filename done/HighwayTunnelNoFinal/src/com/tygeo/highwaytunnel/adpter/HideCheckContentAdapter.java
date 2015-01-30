package com.tygeo.highwaytunnel.adpter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tygeo.highwaytunnel.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * “˛ªº≈≈≤ÈºÏ≤Èƒ⁄»›  ≈‰∆˜
 * @author ChenLang
 */

public class HideCheckContentAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<HashMap<String, String>> mArrayList;
	private LayoutInflater mInflater;
	private int mResoureId;
	private ViewHolder mViewHolder;
	
	public HideCheckContentAdapter(Context context,ArrayList<HashMap<String, String>> arrayList,int resoureId){
		this.mContext=context;
		this.mArrayList=arrayList;
		this.mResoureId=resoureId;
		mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		
		if (convertView == null) {
			mViewHolder=new ViewHolder();
			convertView=mInflater.inflate(mResoureId, null);
			mViewHolder.mTxtCheckContent =(TextView)convertView.findViewById(R.id.txtCheckContentItem);
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder=(ViewHolder)convertView.getTag();
		}
			HashMap<String, String> map=mArrayList.get(position);
			if(map!=null){
				mViewHolder.mTxtCheckContent.setText(map.get("content"));
				if("true".equals(map.get("isCheck"))){
					mViewHolder.mTxtCheckContent.setBackgroundResource(R.drawable.blue);
				}else{
					mViewHolder.mTxtCheckContent.setBackgroundResource(R.drawable.maybehs);
				}
				mViewHolder.map=map;
			}
			
		return convertView;
	}

	public class ViewHolder {
		private TextView  mTxtCheckContent;
		public   HashMap<String, String> map;
	}
	
}
