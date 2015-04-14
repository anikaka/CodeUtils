package com.tongyan.zhengzhou.act.adapter;

import java.util.ArrayList;
import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.common.utils.CommonUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LineBaseInfoTunnelAdapter extends BaseAdapter{

	private ArrayList<HashMap<String, String>> mArrayList;
	private int mResoureId;
	private LayoutInflater mInflater;
	private ViewHolder mViewHolder;

	
	public LineBaseInfoTunnelAdapter(Context context,ArrayList<HashMap<String, String>> arrayList,int resoureId){
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
			mViewHolder.mTxtFacilityName=(TextView)convertView.findViewById(R.id.txtTunnelListItemFacilityName);
			mViewHolder.mTxtFacilityCode=(TextView)convertView.findViewById(R.id.txtTunnelListItemFacilityCode);
			mViewHolder.mTxtStartCircle=(TextView)convertView.findViewById(R.id.txtTunnelListItemStartCircle);
			mViewHolder.mTxtEndCircle=(TextView)convertView.findViewById(R.id.txtTunnelListItemEndCircle);
			mViewHolder.mTxtStartMileage=(TextView)convertView.findViewById(R.id.txtTunnelListItemStartMileage);
			mViewHolder.mTxtEndMileage=(TextView)convertView.findViewById(R.id.txtTunnelListItemEndMileage);
			mViewHolder.mTxtRemark=(TextView)convertView.findViewById(R.id.txtTunnelListItemRemark);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder=(ViewHolder)convertView.getTag();
		}
		HashMap<String, String > map=mArrayList.get(position);
		mViewHolder.mTxtFacilityName.setText("信息类型:"+map.get("facilityName"));
		mViewHolder.mTxtFacilityCode.setText("编号:"+map.get("facilityCode"));
		mViewHolder.mTxtStartCircle.setText("开始环号:"+map.get("startCircle"));
		mViewHolder.mTxtEndCircle.setText("结束换号:"+map.get("endCircle"));
		mViewHolder.mTxtStartMileage.setText("开始里程:"+map.get("startMile"));
		mViewHolder.mTxtEndMileage.setText("结束里程:"+map.get("endMile"));
		mViewHolder.mTxtRemark.setText("备注"+CommonUtils.textIsNull(map.get("remark")));
		return convertView;
	}

	private class ViewHolder{
		private TextView mTxtFacilityName; // 信息类型
		private TextView mTxtFacilityCode;// 编号
		private TextView mTxtStartMileage;// 开始里程
		private TextView mTxtEndMileage; // 终止里程
		private TextView mTxtStartCircle; // 开始环号
		private TextView mTxtEndCircle;// 结束环号
		private TextView mTxtRemark; // 备注
	}
}
