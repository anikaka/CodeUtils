package com.tongyan.activity.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 计量模块 流程查看适配器
 * @author ChenLang
 * @date 2014/11/10
 */

public class MyTaskLookAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, String>> mArrayList;
	private LayoutInflater mInflater;
	private int mResoureId;
	private ViewHolder mViewHolder;
	
	public MyTaskLookAdapter(Context context,ArrayList<HashMap<String, String>> arrayList,int  resoureId){
		this.mContext=context;
		this.mArrayList=arrayList;
		this.mInflater=LayoutInflater.from(mContext);
		this.mResoureId=resoureId;
	}
	
	@Override
	public int getCount() {
		return mArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return mArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			mViewHolder=new ViewHolder();
			convertView=mInflater.inflate(mResoureId, null);
			mViewHolder.mTextViewFlowName=(TextView)convertView.findViewById(R.id.textViewManageLink);
			mViewHolder.mTextViewApprovePerson=(TextView)convertView.findViewById(R.id.textViewManageTime);
			mViewHolder.mTextViewApproveTime=(TextView)convertView.findViewById(R.id.textViewManagePerson);
			mViewHolder.mTextViewApproveSuggestion=(TextView)convertView.findViewById(R.id.textViewManageSuggestion);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder=(ViewHolder)convertView.getTag();
		}
		 HashMap<String, String>  map=mArrayList.get(position);
		 mViewHolder.mTextViewFlowName.setText(map.get("flowName"));
		 if("null".equals(map.get("approvePerson")) || "".equals(map.get("approvePerson"))){
			 mViewHolder.mTextViewApprovePerson.setText(map.get(""));
		 }else{
			 mViewHolder.mTextViewApprovePerson.setText(map.get("approvePerson"));			 
		 }
		 if("null".equals(map.get("approveTime")) || "".equals(map.get("approveTime"))){			 
			 mViewHolder.mTextViewApproveTime.setText(map.get(""));
		 }else{
			 mViewHolder.mTextViewApproveTime.setText(map.get("approveTime"));
		 }
		 if("null".equals(map.get("approveSuggestion")) || "".equals(map.get("approveSuggestion"))){
			 mViewHolder.mTextViewApproveSuggestion.setText((map.get("")));
		 }else{
			 mViewHolder.mTextViewApproveSuggestion.setText((map.get("approveSuggestion")));
			 
		 }
		 
		return convertView;
	}

	private class ViewHolder{
		private TextView  mTextViewFlowName,         //步骤名称
									 mTextViewApprovePerson,  //审批人
									 mTextViewApproveTime,      //审批时间,
									 mTextViewApproveSuggestion;			//审批意见
	}
}
