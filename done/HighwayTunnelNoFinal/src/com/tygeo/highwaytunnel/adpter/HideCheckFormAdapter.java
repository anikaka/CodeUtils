package com.tygeo.highwaytunnel.adpter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.DBhelper.DB_Provider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 隐患排查表单适配器
 * @author ChenLang
 */
public class HideCheckFormAdapter  extends BaseAdapter{

	private Context mContext;
	private int 	mResoureId;
	private LayoutInflater mInflater;
	private ArrayList<HashMap<String, String>> mArrayList;
	private ViewHolder mViewHolder;
	
	public HideCheckFormAdapter(Context context,ArrayList<HashMap<String, String>> arrayList,int resoureId){
		this.mContext=context;
		this.mArrayList=arrayList;
		this.mResoureId=resoureId;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
				convertView=mInflater.inflate(mResoureId, null);
				mViewHolder.mTunnelName=(TextView)convertView.findViewById(R.id.textViewTunnelName);
				mViewHolder.mMileage=(TextView)convertView.findViewById(R.id.textViewMileage);
				mViewHolder.mDate=(TextView)convertView.findViewById(R.id.textViewDate);
				mViewHolder.mUploadState=(TextView)convertView.findViewById(R.id.checkFormUpdateState);
				convertView.setTag(mViewHolder);
			}else{
				mViewHolder=(ViewHolder)convertView.getTag();
			}
			HashMap<String, String> map=mArrayList.get(position);
//			mViewHolder.mTunnelName.setText(DB_Provider.getTunnelName(map.get("projectId"))+":"+map.get("tunnelName"));
			mViewHolder.mTunnelName.setText(map.get("projectName")+":"+map.get("tunnelName"));
     		mViewHolder.mMileage.setText("起始桩号:"+map.get("startMileage")+"/"+map.get("endMileage"));
			mViewHolder.mDate.setText("日期:"+map.get("date"));
			if("1".equals(map.get("uploadState"))){
				mViewHolder.mUploadState.setText("已上传");
			}else{
				mViewHolder.mUploadState.setText("未上传");
			}
			mViewHolder.map=map;
		return convertView;
	}

	public class ViewHolder{
		private TextView mTunnelName;
		private TextView mMileage;
		private TextView mDate;
		private TextView mUploadState;
		public   HashMap<String, String> map;
	}
}
