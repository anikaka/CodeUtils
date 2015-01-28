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
 * 原地貌点适配器
 * @author ChenLang
 * @versionYanAn 1.0
 */
public class PicOriginalPointAdapter  extends BaseAdapter{

	private Context mContext;
	private ArrayList<HashMap<String, String>> mArrayList;
	private LayoutInflater mInflater;
	private int mResoureId;
	private ViewHolderPicPoint mViewHolder;
	
	public PicOriginalPointAdapter(Context context,ArrayList<HashMap<String, String>> list,int resoureId){
		this.mContext=context;
		this.mArrayList=list;
		this.mInflater=LayoutInflater.from(mContext);
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
			mViewHolder=new ViewHolderPicPoint();
			mViewHolder.mTxtName=(TextView)convertView.findViewById(R.id.txtName);
			mViewHolder.mTxtPoint=(TextView)convertView.findViewById(R.id.txtPoint_pic);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder=(ViewHolderPicPoint)convertView.getTag();
		}
		HashMap<String, String> mMap=mArrayList.get(position);
		if(mMap.size()>0){
			if("".equals(mMap.get("text"))){
				mViewHolder.mTxtName.setText(mMap.get("名称:"));
			}
			mViewHolder.mTxtName.setText("名称:"+mMap.get("text"));
			mViewHolder.mTxtPoint.setText("x:"+mMap.get("pointX")+",y:"+mMap.get("pointY")+",z:"+mMap.get("pointZ"));
		}
		mViewHolder.mMapPicPoint=mMap;
		return  convertView;
	}
	public class  ViewHolderPicPoint{
		private TextView mTxtName;
		private TextView mTxtPoint;
		public   HashMap<String, String> mMapPicPoint;
	}
}
