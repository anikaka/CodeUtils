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
 * 定点拍照适配器
 * @author ChenLang
 */

public class PicPointPhotoAdapter extends BaseAdapter{

	private Context mContext;
	private  ArrayList<HashMap<String, String>> mArrayList;
	private LayoutInflater mInflater;
	private int mResoureId;
	private ViewHolderPicPointPhoto mViewHolder;
	
	public PicPointPhotoAdapter(Context context,ArrayList<HashMap<String, String>> arrayList,int resoureId){
		this.mContext=context;
		this.mArrayList=arrayList;
		this.mResoureId=resoureId;
		this.mInflater=LayoutInflater.from(context);
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
			mViewHolder=new ViewHolderPicPointPhoto();
			convertView=mInflater.inflate(mResoureId, null);
			mViewHolder.mTxtName=(TextView)convertView.findViewById(R.id.txtPointName);
			mViewHolder.mTxtPoint=(TextView)convertView.findViewById(R.id.txtPoint_picPointPhoto);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder=(ViewHolderPicPointPhoto)convertView.getTag();
		}
		HashMap<String, String> mMap=mArrayList.get(position);
		if(mMap.size()>0){
			mViewHolder.mTxtName.setText(mMap.get("pointName"));
			mViewHolder.mTxtPoint.setText("x:"+mMap.get("pointX")+",y:"+mMap.get("pointY")+",z:"+mMap.get("pointZ"));
			mViewHolder.mMapPicPointPhoto=mMap;
		}
		return convertView;
	}

	 public class  ViewHolderPicPointPhoto{
		 private TextView mTxtName;
		 private TextView mTxtPoint;
		 public HashMap<String, String> mMapPicPointPhoto;
	 }
}
