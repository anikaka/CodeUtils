package com.tongyan.activity.adapter;

import java.util.List;
import java.util.Map;

import com.tongyan.activity.R;
import com.tongyan.utils.CommonUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 
 * @Title: MidMeasureListAdapter.java 
 * @author Rubert
 * @date 2014-10-21 下午08:11:36 
 * @version V1.0 
 * @Description: 中期支付证书汇总表
 */

public class MeasurePayCertificateListAdapter extends BaseAdapter {
	
	private LayoutInflater layoutInflater;
	private List<Map<String,String>> mDataList;
	
	public MeasurePayCertificateListAdapter(Context context, List<Map<String,String>> mDataList) {
		layoutInflater = LayoutInflater.from(context);
		this.mDataList = mDataList;
	}
	
	@Override
	public int getCount() {
		return mDataList.size();
	}

	@Override
	public Object getItem(int position) {
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.measure_pay_certificate_item, null);
			holder = new ViewHolder();
			holder.mMeasureChapertCode = (TextView)convertView.findViewById(R.id.measure_chapter_code);
			holder.mMeasureProjectName = (TextView)convertView.findViewById(R.id.measure_project_name);
			holder.mOriginalContactSum = (TextView)convertView.findViewById(R.id.original_contact_sum);
			holder.mModifiedSum = (TextView)convertView.findViewById(R.id.modify_sum);
			holder.mChangedSum = (TextView)convertView.findViewById(R.id.changed_sum);
			
			holder.mCurrentTermFinishSum = (TextView)convertView.findViewById(R.id.current_term_finish_sum);
			holder.mLastTermFinishSum = (TextView)convertView.findViewById(R.id.last_term_finish_sum);
			
			holder.mCurrentPay = (TextView)convertView.findViewById(R.id.current_pay);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		Map<String,String> mDataMap = mDataList.get(position);
		
		String mChapter = mDataMap.get("verse");
		if(mChapter == null || "".equals(mChapter)) {
			holder.mMeasureChapertCode.setVisibility(View.GONE);
		} else {
			holder.mMeasureChapertCode.setVisibility(View.VISIBLE);
			holder.mMeasureChapertCode.setText("章节号 : " + CommonUtils.toHandlerString(mDataMap.get("verse")));
		}
		holder.mMeasureProjectName.setText("项目名称 :" + CommonUtils.toHandlerString(mDataMap.get("pitem")));
		holder.mOriginalContactSum.setText("原合同金额(元) : " + CommonUtils.toHandlerString(mDataMap.get("contactAmount")));
		holder.mModifiedSum.setText("清单核查后金额(元) : " + CommonUtils.toHandlerString(mDataMap.get("afterRevamount")));
		holder.mChangedSum.setText("变更后金额(元) : " + CommonUtils.toHandlerString(mDataMap.get("afterChangeAmount")));
		
		holder.mCurrentTermFinishSum.setText("本期末累计支付(元) : " + CommonUtils.toHandlerString(mDataMap.get("nowAmount")));
		holder.mCurrentPay.setText("本期支付(元) : " + CommonUtils.toHandlerString(mDataMap.get("thisAmount")));
		holder.mLastTermFinishSum.setText("上期末累计支付(元) : " + CommonUtils.toHandlerString(mDataMap.get("lastAmount")));
		return convertView;
	}
	
	public final class ViewHolder {
		public TextView mMeasureChapertCode;
		public TextView mMeasureProjectName;
		public TextView mOriginalContactSum;
		public TextView mModifiedSum;
		public TextView mChangedSum;
	
		public TextView mCurrentTermFinishSum;
		public TextView mLastTermFinishSum;
		public TextView mCurrentPay;
		
		public Map<String,String> mDataMap;
	}
	
}
