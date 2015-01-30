package com.tongyan.activity.adapter;

import java.util.List;

import com.tongyan.activity.R;
import com.tongyan.common.entities._HoleFace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 
 * @ClassName P38_RiskNoticeAdapter.java
 * @Author wanghb
 * @Date 2013-9-11 pm 01:46:13
 * @Desc TODO
 */
public class RiskNoticeAdapter extends BaseAdapter {
	
	//private Context mContext;
	private LayoutInflater mLayoutInflater;
	
	private List<_HoleFace> mRiskNoticeList;
	
	public RiskNoticeAdapter(Context mContext, List<_HoleFace> mRiskNoticeList) {
		mLayoutInflater = LayoutInflater.from(mContext);
		this.mRiskNoticeList = mRiskNoticeList;
		//this.mContext = mContext;
	}
	
	
	@Override
	public int getCount() {
		return mRiskNoticeList == null ? 0 : mRiskNoticeList.size();
	}

	@Override
	public Object getItem(int position) {
		if (mRiskNoticeList != null) {
			return mRiskNoticeList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.risk_showing_list_item, null);
			holder = new ViewHolder();
			holder.mHoleTextView = (TextView)convertView.findViewById(R.id.p39_risk_notice_hole_text);
			holder.mDegreeTextView = (TextView)convertView.findViewById(R.id.p39_risk_degree_content_text);
			holder.mMileTextView = (TextView)convertView.findViewById(R.id.p39_risk_notice_mile_text);
			holder.mPoposeTextView = (TextView)convertView.findViewById(R.id.p39_risk_popose_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		_HoleFace _mHoleFace = mRiskNoticeList.get(position);
		if(_mHoleFace != null) {
			holder.mHoleTextView.setText(_mHoleFace.getHole());
			holder.mDegreeTextView.setText("风险等级：" + _mHoleFace.getRiskDegree());
			holder.mMileTextView.setText("里程：" + _mHoleFace.getCurrMile());
			holder.mPoposeTextView.setText("风险处理意见：" + _mHoleFace.getRiskHSuggest());
			holder._mHoleFace = _mHoleFace;
		}
		return convertView;
	}
	
	public class ViewHolder {
		public TextView mHoleTextView;
		public TextView mDegreeTextView;
		public TextView mMileTextView;
		public TextView mPoposeTextView;
		public _HoleFace _mHoleFace;
	}
}
