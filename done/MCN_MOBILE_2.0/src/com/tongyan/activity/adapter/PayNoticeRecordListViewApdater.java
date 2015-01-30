package com.tongyan.activity.adapter;


import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 付款通知单适配器
 * @author ChenLang
 */
public class PayNoticeRecordListViewApdater extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String,String>> mListView;
	private LayoutInflater mInflater;
	private int  mResoureId;
	private ViewHolder mViewHolder;
	
	public PayNoticeRecordListViewApdater(Context context,ArrayList<HashMap<String,String>> listView,int resoureId){
		this.mContext=context;	
		this.mListView=listView;
		this.mInflater=LayoutInflater.from(mContext);
		this.mResoureId=resoureId;
	}
	
	@Override
	public int getCount() {
		return mListView.size();
	}

	@Override
	public Object getItem(int position) {
		return mListView.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 if(convertView==null){
			   convertView=mInflater.inflate(mResoureId, null);
			   mViewHolder=new ViewHolder();
			   mViewHolder.mTextViewProjectName=(TextView)convertView.findViewById(R.id.payNoticeFormItem_ProjectName);
			   mViewHolder.mTextViewUpCountPrice=(TextView)convertView.findViewById(R.id.payNoticeFormItem__UpCountPrice);
			   mViewHolder.mTextViewCurrentPrice=(TextView)convertView.findViewById(R.id.payNoticeFormItem_CurrentPrice);
			   mViewHolder.mTextViewCountPrice=(TextView)convertView.findViewById(R.id.payNoticeFormItem_CountPrice);
			   convertView.setTag(mViewHolder);
		 }else{
			 mViewHolder=(ViewHolder)convertView.getTag();
		 }
		 if(mListView!=null && mListView.size()>0){			 
			// for(int i=0;i<mListView.size();i++){
				 HashMap<String, String> map=mListView.get(position);
				 mViewHolder.mTextViewProjectName.setText(map.get("projectName"));
				 mViewHolder.mTextViewUpCountPrice.setText(map.get("upCountMoney"));
				 mViewHolder.mTextViewCurrentPrice.setText(map.get("currentPeriodMoney"));
				 mViewHolder.mTextViewCountPrice.setText(map.get("countMoney"));
			// }
		 }
		return convertView;
	}

	private class ViewHolder{
		 TextView mTextViewProjectName,   //项目名称
		 				mTextViewUpCountPrice,  //上期累计金额
		 				mTextViewCurrentPrice,    //本期金额
		 				mTextViewCountPrice;      //累计金额
	}
}
