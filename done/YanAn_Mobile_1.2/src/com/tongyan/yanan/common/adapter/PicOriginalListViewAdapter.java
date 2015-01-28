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
 * 照片上传 原地貌和强夯适配器
 * @author ChenLang
 * @version YanAn 1.0
 */
public class PicOriginalListViewAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, String>> mArrayList;
	private LayoutInflater mInflater;
	private int 	mResoureId;
	private ViewHoderPic mViewHolder;
	
	public PicOriginalListViewAdapter(Context context,ArrayList<HashMap<String, String>> list,int resoureId){
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
			convertView=mInflater.inflate(mResoureId, null);
			mViewHolder=new ViewHoderPic();
			mViewHolder.mTxtContent= (TextView)convertView.findViewById(R.id.txtType_original);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder=(ViewHoderPic)convertView.getTag();
			}
		HashMap<String, String> mMap=mArrayList.get(position);
		if(mMap!=null){
			mViewHolder.mTxtContent.setText(mMap.get("displayName"));
			mViewHolder.mMapPic=mMap;
		}
		return convertView;
	}

	public class ViewHoderPic{
		private TextView mTxtContent;
		public HashMap<String, String> mMapPic;
	}
}
