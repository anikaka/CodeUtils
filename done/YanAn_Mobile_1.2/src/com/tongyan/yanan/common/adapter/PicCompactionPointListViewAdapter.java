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
 *	强夯点适配器
 * @author ChenLang
 */

public class PicCompactionPointListViewAdapter extends BaseAdapter{
	
	private Context mContext;
	private  ArrayList<HashMap<String, String>> mArrayList;
	private LayoutInflater mInflater;
	private int mResoureId;
	private ViewHolderPicCompaction mViewHolder;
	
	public PicCompactionPointListViewAdapter(Context context,ArrayList<HashMap<String,String>> list,int resoureId){
		this.mContext=context;
		this.mArrayList=list;
		this.mInflater=LayoutInflater.from(context);
		this.mResoureId=resoureId;
	}
	
	@Override
	public int getCount() {
		return mArrayList.size();
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
		if(convertView==null){
			mViewHolder=new ViewHolderPicCompaction();
			convertView=mInflater.inflate(mResoureId, null);
			mViewHolder.mTxtName=(TextView)convertView.findViewById(R.id.txtType_pic_compaction);
//			mViewHolder.mTxtArea=(TextView)convertView.findViewById(R.id.txtArea_pic_compaction);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder=(ViewHolderPicCompaction)convertView.getTag();
		}
		HashMap<String, String> mMap=mArrayList.get(position);
		if(mMap!=null){
			mViewHolder.mTxtName.setText(mMap.get("text"));
//			mViewHolder.mTxtArea.setText(mMap.get("area"));
			mViewHolder.mMapPicComPaction=mMap;
		}
		return convertView;
	}

	public class ViewHolderPicCompaction{
		private TextView mTxtName;
//		private TextView mTxtArea;
		public   HashMap<String, String> mMapPicComPaction;
	}
}
