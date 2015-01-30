package com.tongyan.activity.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.tongyan.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 监理指令 附件适配器
 *@author ChenLang
 */

public class GpsCommandFileAdapter  extends BaseAdapter{

	private Context mContext;
	private LinkedList<HashMap<String, String>> mArrayListFile;
	private LayoutInflater mInflater;
	private int mResoureId;
	private ViewHoloder mViewHolder;
	public GpsCommandFileAdapter(Context context,LinkedList<HashMap<String, String>> linkedListFile,int resoureId){
		this.mContext=context;
		this.mArrayListFile=linkedListFile;
		this.mResoureId=resoureId;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return mArrayListFile.size();
	}

	@Override
	public Object getItem(int position) {
		return mArrayListFile.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			mViewHolder=new ViewHoloder();
			convertView=mInflater.inflate(mResoureId, null);
			mViewHolder.mTextViewFile=(TextView)convertView.findViewById(R.id.monitorCommandFileName);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder= (ViewHoloder)convertView.getTag();
		}
		HashMap<String, String> map=mArrayListFile.get(position);
		mViewHolder.mTextViewFile.setText(map.get("fileName"));
		mViewHolder.mapFile=map;
		return convertView;
	}

	public class  ViewHoloder{
		private TextView mTextViewFile;
		public   HashMap<String, String> mapFile;
	}
}
