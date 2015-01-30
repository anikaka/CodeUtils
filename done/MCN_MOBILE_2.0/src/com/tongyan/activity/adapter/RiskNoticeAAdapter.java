package com.tongyan.activity.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import com.tongyan.activity.R;
import com.tongyan.common.entities._RiskNotice;

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
public class RiskNoticeAAdapter extends BaseAdapter {
	
	//private Context mContext;
	private LayoutInflater mLayoutInflater;
	
	private LinkedList<_RiskNotice> mRiskNoticeList;
	
	public RiskNoticeAAdapter(Context mContext, LinkedList<_RiskNotice> mRiskNoticeList) {
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
			convertView = mLayoutInflater.inflate(R.layout.risk_showing_list_item_a, null);
			holder = new ViewHolder();
			holder.mTitleTextView = (TextView)convertView.findViewById(R.id.p38_risk_notice_title_text);
			holder.mDegreeTextView = (TextView)convertView.findViewById(R.id.p37_risk_degree_content_text);
			holder.mCheckerTextView = (TextView)convertView.findViewById(R.id.p37_risk_checker);
			holder.mCheckDateTextView = (TextView)convertView.findViewById(R.id.p37_risk_check_date_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		_RiskNotice mRiskNotice = mRiskNoticeList.get(position);
		
		if(mRiskNotice != null) {
			holder.mTitleTextView.setText(mRiskNotice.getSectionName() + mRiskNotice.getUnitName());
			holder.mDegreeTextView.setText(mRiskNotice.getAlarmClass());
			holder.mCheckerTextView.setText(mRiskNotice.getChecker());
			String currdate = "";
			try {
				Date date = new SimpleDateFormat("yyyy-MM-dd").parse(mRiskNotice.getaDate());
				currdate = new SimpleDateFormat("yyyy年MM月dd日").format(date);
			} catch (ParseException e) {
				currdate = mRiskNotice.getaDate();
				e.printStackTrace();
			}
			holder.mCheckDateTextView.setText(currdate);
			holder._mRiskNotice = mRiskNotice;
		}
		return convertView;
	}
	
	public class ViewHolder {
		public TextView mTitleTextView;
		public TextView mDegreeTextView;
		public TextView mCheckerTextView;
		public TextView mCheckDateTextView;
		public _RiskNotice _mRiskNotice;
	}
}
