package com.tongyan.zhengzhou.act.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.zhengzhou.act.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 线路基本信息
 * @author ChenLang
 */
public class LineBaseInfoLevel2Adapter  extends BaseAdapter{

	private ArrayList<HashMap<String, String>> mArrayList;
	private int mResoureId;
	private LayoutInflater mInflater;
	private ViewHolder mViewHolder;
	
	public LineBaseInfoLevel2Adapter(Context context,ArrayList<HashMap<String, String>> arrayList,int resoureId){
	   this.mArrayList=arrayList;
	   this.mResoureId=resoureId;
		mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			mViewHolder=new ViewHolder();
			convertView=mInflater.inflate(mResoureId, null);
			mViewHolder.mTxtBranchName=(TextView)convertView.findViewById(R.id.lineBaseInfoLevel2BranchName);
			mViewHolder.mTxtLeftStartMile=(TextView)convertView.findViewById(R.id.lineBaseInfoLevel2LeftStartMile);
			mViewHolder.mTxtLeftEndMile=(TextView)convertView.findViewById(R.id.lineBaseInfoLevel2LeftEndMile);
			mViewHolder.mTxtRightStartMile=(TextView)convertView.findViewById(R.id.lineBaseInfoLevel2RightStartMile);
			mViewHolder.mTxtRightEndMile=(TextView)convertView.findViewById(R.id.lineBaseInfoLevel2RightEndMile);
			mViewHolder.mTxtStartStationName=(TextView)convertView.findViewById(R.id.lineBaseInfoLevel2StartStationName);
			mViewHolder.mTxtEndStationName=(TextView)convertView.findViewById(R.id.lineBaseInfoLevel2EndStationName);
			convertView.setTag(mViewHolder);
		}else{
		   mViewHolder=(ViewHolder)convertView.getTag();
		}
		HashMap<String, String> map=mArrayList.get(position);
		mViewHolder.mTxtBranchName.setText("分支类型:"+map.get("branchName"));
		mViewHolder.mTxtLeftStartMile.setText("上行线起始里程:"+map.get("leftStartMile"));
		mViewHolder.mTxtLeftEndMile.setText("上行终止里程:"+map.get("leftEndMile"));
		mViewHolder.mTxtRightStartMile.setText("下行起始里程"+map.get("rightStartMile"));
		mViewHolder.mTxtRightEndMile.setText("下行开始里程:"+map.get("rightEndMile"));
		mViewHolder.mTxtStartStationName.setText("起始站名:"+map.get("startStationName"));
		mViewHolder.mTxtEndStationName.setText("终止站名:"+map.get("endStationName"));
		return convertView;
	}

	private class ViewHolder{
		TextView  mTxtBranchName;
		TextView mTxtLeftStartMile;//上行开始里程
		TextView mTxtLeftEndMile;//上行终止里程
		TextView mTxtRightStartMile;//下行开始里程
		TextView mTxtRightEndMile;//下行终止里程
		TextView  mTxtStartStationName;//起始站名
		TextView  mTxtEndStationName;//终止站名
		
	}
}
