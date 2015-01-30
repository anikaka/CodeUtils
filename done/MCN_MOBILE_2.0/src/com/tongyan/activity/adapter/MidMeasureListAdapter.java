package com.tongyan.activity.adapter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.tongyan.activity.R;
import com.tongyan.activity.MainAct.MyOnClickListener;
import com.tongyan.activity.measure.measure.MidMeasureRecordAct;
import com.tongyan.utils.CommonUtils;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
/**
 * 
 * @Title: MidMeasureListAdapter.java 
 * @author Rubert
 * @date 2014-10-21 下午08:11:36 
 * @version V1.0 
 * @Description: 中间计量单
 */

public class MidMeasureListAdapter extends BaseAdapter {
	
	private LayoutInflater layoutInflater;
	private LinkedList<Map<String,String>> mDataList;
	private int mApproveState;
	
	public MidMeasureListAdapter(Context context, LinkedList<Map<String,String>> mDataList,int approveState) {
		layoutInflater = LayoutInflater.from(context);
		this.mDataList = mDataList;
		this.mApproveState=approveState;
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
			convertView = layoutInflater.inflate(R.layout.measure_mid_record_item, null);
			holder = new ViewHolder();
			holder.mMeasureRecordCode = (TextView)convertView.findViewById(R.id.measure_record_code);
			holder.mMeasureRecordName = (TextView)convertView.findViewById(R.id.measure_record_name);
			holder.mMeasurePosition = (TextView)convertView.findViewById(R.id.measure_position);
			holder.mMeasureMapNumber = (TextView)convertView.findViewById(R.id.measure_map_number);
			holder.mMeasureStakeNumber = (TextView)convertView.findViewById(R.id.measure_stake_number);
			holder.mCreater = (TextView)convertView.findViewById(R.id.creater);
			holder.mCreateDate = (TextView)convertView.findViewById(R.id.create_date);
			holder.mMeasureApplyState = (TextView)convertView.findViewById(R.id.measure_apply_statues);
			holder.mCheckBox=(CheckBox)convertView.findViewById(R.id.approveCheckBox);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		Map<String,String> mDataMap = mDataList.get(position);
		holder.mMeasureRecordCode.setText("中间计量单号 :" + CommonUtils.toHandlerString(mDataMap.get("imCode")));
		holder.mMeasureRecordName.setText("计量名称 :" + CommonUtils.toHandlerString(mDataMap.get("imName")));
		holder.mMeasurePosition.setText("部位 :" + CommonUtils.toHandlerString(mDataMap.get("imPart")));
		holder.mMeasureMapNumber.setText("图号:" + CommonUtils.toHandlerString(mDataMap.get("imMapNum")));
		holder.mMeasureStakeNumber.setText("桩号 :" + CommonUtils.toHandlerString(mDataMap.get("imMile")));
		
		holder.mMeasureMapNumber.setText("图号:" + CommonUtils.toHandlerString(mDataMap.get("imMapNum")));
		holder.mMeasureStakeNumber.setText("桩号 :" + CommonUtils.toHandlerString(mDataMap.get("imMile")));
		
		holder.mCreater.setText("制表人 :" + CommonUtils.toHandlerString(mDataMap.get("imTabulator")));
		holder.mCreateDate.setText("制表日期 :" + CommonUtils.toHandlerString(mDataMap.get("imTabDate")));
		
		holder.mMeasureApplyState.setText("状态 :" + CommonUtils.toHandlerString(mDataMap.get("flowStatus")));
		if(mApproveState!=MidMeasureRecordAct.DECLARED &&  "未申报".equals(mDataMap.get("flowStatus"))){
			holder.mCheckBox.setVisibility(View.GONE);
		}else{	
			if("0".equals(mDataMap.get("checkBoxState"))){
				holder.mCheckBox.setChecked(false);
			}else{
				holder.mCheckBox.setChecked(true);
			}
			holder.mCheckBox.setOnClickListener(new MyOnClickListener(position,holder.mCheckBox));
		}
		holder.mDataMap=mDataMap;
		return convertView;
	}
	
  private  class MyOnClickListener implements View.OnClickListener{
	  private CheckBox mCheckBox;
	  private int mPosition;
	  private MidMeasureRecordAct mMidMeasureRecordAct= new MidMeasureRecordAct();
	  private LinkedList<Map<String,String>> mLinkedList=mMidMeasureRecordAct.mDataList;
	  
	  public MyOnClickListener(int position,CheckBox checkBox){
		 this.mPosition=position;
		 this.mCheckBox=checkBox;
	  }
	  
	@Override
	public void onClick(View v) {
			 for(int i=0;i<mLinkedList.size();i++){
				 if(i==mPosition){
					 Map<String, String> map=mLinkedList.get(i);
						if(mCheckBox.isChecked()){
							map.put("checkBoxState", "1");
						}else{
							map.put("checkBoxState", "0");
						}
			 }
		}
	}
  }
  
	public final class ViewHolder {
		public TextView mMeasureRecordCode;
		public TextView mMeasureRecordName;
		public TextView mMeasurePosition;
		public TextView mMeasureMapNumber;
		public TextView mMeasureStakeNumber;
		public TextView mCreater;
		public TextView mCreateDate;
		public TextView mMeasureApplyState;
		public  CheckBox mCheckBox;
		public Map<String,String> mDataMap;
	}
	
}
