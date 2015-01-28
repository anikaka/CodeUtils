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
 * @category收文附件适配器
 * @author ChenLang
 * @date 2014/07/25
 * @version YanAn 1.0
 */
public class OaFileAdapter  extends BaseAdapter{

	private Context mContext;
	private ArrayList<HashMap<String, String>> mArrayList;
	private LayoutInflater mInflater;
	private int mResoureId;
	private ViewHolderOaReceiveTextFire mViewHolder;
	
	public OaFileAdapter(Context context,ArrayList<HashMap<String, String>> list,int resoureId){
		this.mContext=context;
		this.mArrayList=list;
		this.mResoureId=resoureId;
		this.mInflater=LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return mArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				mViewHolder=new ViewHolderOaReceiveTextFire();
				convertView=mInflater.inflate(mResoureId, null);
				mViewHolder.mTxtFileName=(TextView)convertView.findViewById(R.id.txtFileName_oaReceive);
				convertView.setTag(mViewHolder);
			}else{
				mViewHolder=(ViewHolderOaReceiveTextFire)convertView.getTag();
			}
			HashMap<String, String> mMap=mArrayList.get(position);
			if(mMap!=null && mMap.size()>0){
				mViewHolder.mTxtFileName.setText(mMap.get("fileName"));
				mViewHolder.mMapReciveFile=mMap;
			}
		return convertView;
	}
	
	public class ViewHolderOaReceiveTextFire{
		private TextView mTxtFileName;
		public   HashMap<String, String> mMapReciveFile;
	}
}
