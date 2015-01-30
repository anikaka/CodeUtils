package com.tygeo.highwaytunnel.adpter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tygeo.highwaytunnel.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 检查设备适配器
 * @author ChenLang
 */

public class HideCheckDeviceAdapter extends  BaseAdapter {


	private Context mContext;
	private ArrayList<HashMap<String, String>> mArrayList;
	private int mResourId;
	private LayoutInflater mInflater;
	private ViewHolder mViewHolder;
	
	public HideCheckDeviceAdapter(Context context,ArrayList<HashMap<String, String>> mArrayList,int resoureId){
		this.mContext=context;
		this.mArrayList=mArrayList;
		this.mResourId=resoureId;
		this.mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			mViewHolder=new ViewHolder();
			convertView=mInflater.inflate(mResourId, null);
			mViewHolder.mTxtProjectName=(TextView)convertView.findViewById(R.id.txtCheckDeviceProjectName);
			mViewHolder.mTxtSection=(TextView)convertView.findViewById(R.id.txtCheckDeviceSection);
			mViewHolder.mTxtCheckPerson=(TextView)convertView.findViewById(R.id.txtCheckDevicePerson);
			mViewHolder.mTxtCheckDate=(TextView)convertView.findViewById(R.id.txtCheckDeviceDate);
			mViewHolder.mTxtUploadState=(TextView)convertView.findViewById(R.id.txtCheckDeviceUploadState);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder=(ViewHolder)convertView.getTag();
		}
		HashMap<String, String> map=mArrayList.get(position);
		mViewHolder.mTxtProjectName.setText("项目名称:"+map.get("projectName"));
		mViewHolder.mTxtSection.setText("施工标段:"+map.get("section"));
		mViewHolder.mTxtCheckPerson.setText("检查人:"+map.get("checkPerson"));
		mViewHolder.mTxtCheckDate.setText("检查日期:"+map.get("checkDate"));
		if("1".equals(map.get("uploadState"))){
			mViewHolder.mTxtUploadState.setText("已上传");
		}else{
			mViewHolder.mTxtUploadState.setText("未上传");
		}
		mViewHolder.map=map;
		
		return convertView;
	}

	public class ViewHolder{
		private TextView  mTxtProjectName;  //项目名称
		private TextView  mTxtSection;           //施工标段
		private TextView  mTxtCheckPerson;   //检查人
		private TextView  mTxtCheckDate;      //检查日期
		private TextView 	 mTxtUploadState;    //上传状态
		public   HashMap<String, String> map;
	}
}
