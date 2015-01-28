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

public class ProgressMonthProjectWeeksListViewAdapter extends BaseAdapter {

	/**
	 * @category周计划周期数适配器
	 * @author ChenLang
	 * @date 2014/07/14
	 * @version YanAn 1.0
	 */
	
   private  ArrayList<HashMap<String, String>> mArrayList;
   private  LayoutInflater mInflater;
   private  Context mContext;
   private  int  mResoureId;
   private ViewHolderProgressMonthProjectWeeksListViewAdapter mViewHolder;
   public ProgressMonthProjectWeeksListViewAdapter(Context context,ArrayList<HashMap<String, String>> list,int resoureId){
	   this.mContext=context;
	   this.mArrayList=list;
	   this.mResoureId=resoureId;
	   this.mInflater=LayoutInflater.from(context);
   }
   
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
			convertView=mInflater.inflate(mResoureId, null);
			mViewHolder=new ViewHolderProgressMonthProjectWeeksListViewAdapter();
			mViewHolder.mTxtWeek=(TextView)convertView.findViewById(R.id.txtMonthDate);
			mViewHolder.mTxtState=(TextView)convertView.findViewById(R.id.txtMonthDateState);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder=(ViewHolderProgressMonthProjectWeeksListViewAdapter)convertView.getTag();
		}
		HashMap<String, String> mMap=mArrayList.get(position);
		if(mMap.size()>0 && mMap!=null){
			if(!"1".equals(mMap.get("dataType"))) {
				mViewHolder.mTxtWeek.setText(mMap.get("weekDate"));
			} else {
				//mViewHolder.mTxtWeek.setText(String.format("第%s周", mMap.get("weekDate")));
				mViewHolder.mTxtWeek.setText(mMap.get("weekDate"));
			}
			
			if("0".equals(mMap.get("state"))){
				mViewHolder.mTxtState.setText("未完成");
			}else if("1".equals(mMap.get("state"))){
				mViewHolder.mTxtState.setText("已完成");
			}else if("2".equals(mMap.get("state"))){
				mViewHolder.mTxtState.setText("已提交");
			}
			mViewHolder.mMapWeekOfMonth=mMap;
		}
		return convertView;
	}

	public  class ViewHolderProgressMonthProjectWeeksListViewAdapter{
		private TextView  mTxtWeek;
		private TextView  mTxtState;
		public  HashMap<String, String>  mMapWeekOfMonth;
	}
}
