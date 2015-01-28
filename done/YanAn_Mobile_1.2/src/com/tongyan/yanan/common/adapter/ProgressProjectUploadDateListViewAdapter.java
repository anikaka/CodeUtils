package com.tongyan.yanan.common.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

import com.tongyan.yanan.act.R;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @category日期选择适配器
 * @author ChenLang
 * @date 2014/07/08
 * @version YanAn 1.0
 */
public class ProgressProjectUploadDateListViewAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, String>>  mArraList;
	private LayoutInflater mInlfater;
	private int mResoureId;
	private HolderViewProgressProjectUploadDateListViewAdapter mHolderView;
	
	public ProgressProjectUploadDateListViewAdapter(Context context,ArrayList<HashMap<String, String>> list,int resoureId){
		this.mContext=context;
		this.mArraList=list;
		this.mInlfater=LayoutInflater.from(context);
		this.mResoureId=resoureId;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArraList.size();
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
			mHolderView=new HolderViewProgressProjectUploadDateListViewAdapter();
			convertView=mInlfater.inflate(mResoureId, null);
			mHolderView.mTxtDate=(TextView)convertView.findViewById(R.id.txtItem_Date);
			mHolderView.mAccomplishDate=(TextView)convertView.findViewById(R.id.txtItem_AccomplishState);
			convertView.setTag(mHolderView);
		}else{
			mHolderView=(HolderViewProgressProjectUploadDateListViewAdapter)convertView.getTag();
		}
		if(mArraList!=null && mArraList.size()>0){
			HashMap<String, String> mMap=mArraList.get(position);
			 if(mMap.size()>0){
				 mHolderView.mTxtDate.setText(mMap.get("dayDate"));
				 if("1".equals(mMap.get("state"))){
					 mHolderView.mAccomplishDate.setText("已完成");
					 mHolderView.mAccomplishDate.setVisibility(View.VISIBLE);
				 }else{
					 mHolderView.mAccomplishDate.setVisibility(View.INVISIBLE);
				 }
			 }
			 mHolderView.mMapDate = mMap;
		}
		
		return convertView;
	}

	  public class HolderViewProgressProjectUploadDateListViewAdapter {
		 private TextView  mTxtDate;
		 private TextView  mAccomplishDate;
		 public   HashMap<String, String> mMapDate;
	  }
}
