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
 * @category计划上报集合适配器
 * @author Administrator
 * @date 2014/07/07
 * @version YanAn 1.0
 */
public class ProgressProjectUploadListViewAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, String>> mArraryList;
	private LayoutInflater mInflater;
	private int mResoureId;
	private ViewHolderProgressProjectUpload mViewHoldler;
	public ProgressProjectUploadListViewAdapter(Context context,
			ArrayList<HashMap<String, String>> list, int resoureId) {
		this.mContext = context;
		this.mArraryList = list;
		this.mResoureId = resoureId;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mArraryList.size();
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
			convertView =mInflater.inflate(mResoureId, null);
			mViewHoldler=new ViewHolderProgressProjectUpload();
			mViewHoldler.mTxtTitleItem_project_upload=(TextView)convertView.findViewById(R.id.txtTitleItem_project_upload);
			mViewHoldler.mTxtStartTime_project_upload=(TextView)convertView.findViewById(R.id.txtStartTime_project_upload);
			mViewHoldler.mTxtTimeFlag_project_upload=(TextView)convertView.findViewById(R.id.txtCenterTime_project_upload);
			mViewHoldler.mTxtEndTime_project_upload=(TextView)convertView.findViewById(R.id.txtEndTime_project_upload);
			mViewHoldler.mTxtUploadState_project_upload=(TextView)convertView.findViewById(R.id.txtUploadState_project_upload);
			convertView.setTag(mViewHoldler);
		}else{
			mViewHoldler =(ViewHolderProgressProjectUpload)convertView.getTag();
		}
			if(mArraryList.size()>0){
				HashMap<String, String>  map=mArraryList.get(position);
				 if(map != null){
					 mViewHoldler.mTxtTitleItem_project_upload.setText("第"+map.get("CommonInfo")+"周");
					 mViewHoldler.mTxtStartTime_project_upload.setText(map.get("startDate"));
					 mViewHoldler.mTxtEndTime_project_upload.setText(map.get("endDate"));
					 String mState = map.get("State");
					 if("1".equals(mState)) {
						 mViewHoldler.mTxtUploadState_project_upload.setText("已完成");
					 } else if("2".equals(mState)){
						 mViewHoldler.mTxtUploadState_project_upload.setText("已提交");
					 } else if("0".equals(mState)){
						 mViewHoldler.mTxtUploadState_project_upload.setText("未完成");
					 }else {
						 mViewHoldler.mTxtUploadState_project_upload.setText("");
					 }
				 }
				 mViewHoldler.mMapProgressProjectUpload=map;
			}
		return convertView;
	}

	public class ViewHolderProgressProjectUpload {
		private TextView  mTxtTitleItem_project_upload;
		private TextView  mTxtStartTime_project_upload;
		private TextView  mTxtTimeFlag_project_upload;
		private TextView  mTxtEndTime_project_upload;
		private TextView  mTxtUploadState_project_upload;
		public HashMap<String, String> mMapProgressProjectUpload;
	}
}
