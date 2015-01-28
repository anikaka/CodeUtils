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
 * @categoryOA公告通知适配器
 * @author ChenLang
 *
 */
public class OANoticeAdapter extends BaseAdapter{
	
	private ArrayList<HashMap<String, String>> mArrayList;
	private Context mContext;
	private LayoutInflater mInflater;
	private int mResoureId;
	private ViewHolderNotice mViewHolder;
	
	public OANoticeAdapter(Context context,ArrayList<HashMap<String, String>> list,int resoureId){
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
		// TODO Auto-generated method stub
		if(convertView==null){
			mViewHolder=new ViewHolderNotice();
			convertView=mInflater.inflate(mResoureId, null);
			mViewHolder.mTxtTitle=(TextView)convertView.findViewById(R.id.txtTitle_notice);
			mViewHolder.mTxtContent=(TextView)convertView.findViewById(R.id.txtContent_notice);
			mViewHolder.mTxtTime=(TextView)convertView.findViewById(R.id.txtTime_notice);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder=(ViewHolderNotice)convertView.getTag();
		}
		HashMap<String, String> mMap=mArrayList.get(position);
			if(mMap!=null && mMap.size()>0){
				mViewHolder.mTxtTitle.setText(mMap.get("title"));
				mViewHolder.mTxtContent.setText(mMap.get("content"));
				mViewHolder.mTxtTime.setText(mMap.get("time"));
				mViewHolder.mMapNotice=mMap;
			}
		return convertView;
	}
	
	public  class ViewHolderNotice{
		private TextView mTxtTitle;
		private TextView mTxtContent;
		private TextView mTxtTime;
		public HashMap<String, String> mMapNotice;
	}
}
