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
 * 合同中间计量单详情适配器
 * @author ChenLang
 * @date  2014/11/03
 */
public class MidMeasureRecordOptionListViewApdater  extends BaseAdapter{

	private Context mContext;
	private ArrayList<HashMap<String, String>> mArrayList;
	private LayoutInflater mInflater;
	private int mResoureId;
	
	public MidMeasureRecordOptionListViewApdater(Context context,ArrayList<HashMap<String, String>> arrayList,int resoureId){
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
		ViewHolder mViewHolder=new ViewHolder();
		if(convertView==null){
			
			convertView=mInflater.inflate(mResoureId, null);
			mViewHolder.mTextViewProjectName=(TextView)convertView.findViewById(R.id.recordFormProjectName);
			mViewHolder.mTextViewBillNo=(TextView)convertView.findViewById(R.id.recordFormBillNo);
			mViewHolder.mTextViewBillName=(TextView)convertView.findViewById(R.id.recordFormBillName);
			mViewHolder.mTextViewCount=(TextView)convertView.findViewById(R.id.recordFormCount);
			mViewHolder.mTextViewDeclareCount=(TextView)convertView.findViewById(R.id.recordFormDeclareCount);
			mViewHolder.mTextViewDeclareScale=(TextView)convertView.findViewById(R.id.recordFormDeclareScale);
			mViewHolder.mTextViewVerifyCount=(TextView)convertView.findViewById(R.id.recordFormVerifyCount);
			mViewHolder.mTextViewVerifyScale=(TextView)convertView.findViewById(R.id.recordFormVerifyScale);
			mViewHolder.mTextViewApproveCount=(TextView)convertView.findViewById(R.id.recordFormApproveCount);
			mViewHolder.mTextViewCurrentSurplusCount=(TextView)convertView.findViewById(R.id.recordFormCurrentSurplusCount);
			mViewHolder.mTextViewCurrentSurplusScale=(TextView)convertView.findViewById(R.id.recordFormCurrentSurplusScale);
			mViewHolder.mTextViewPriorPeriodCount=(TextView)convertView.findViewById(R.id.recordFormPriorPeriodCount);
			mViewHolder.mTextViewPriorPeriodScale=(TextView)convertView.findViewById(R.id.recordFormPriorPeriodScale);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder = (ViewHolder)convertView.getTag();
		}
		HashMap<String, String> map=mArrayList.get(position);
		if(map!=null){
			mViewHolder.mTextViewProjectName.setText("工程名称:"+map.get("projectName"));
			mViewHolder.mTextViewBillNo.setText("清单编号:"+map.get("billNo"));
			mViewHolder.mTextViewBillName.setText("清单名称:"+map.get("billName"));
			mViewHolder.mTextViewCount.setText("数量:"+map.get("count"));
			mViewHolder.mTextViewDeclareCount.setText("申报数量:"+map.get("declareCount"));
			mViewHolder.mTextViewDeclareScale.setText("申报比例(100%):"+map.get("declarerateScale"));
			mViewHolder.mTextViewVerifyCount.setText("核定数量:"+map.get("verifyCount"));
			mViewHolder.mTextViewVerifyScale.setText("核定比例(100%):"+map.get("verifyScale"));
			mViewHolder.mTextViewApproveCount.setText("审核数量:"+map.get("approveCount"));
			mViewHolder.mTextViewCurrentSurplusCount.setText("本期末剩余量:"+map.get("currentSurplusCount"));
			mViewHolder.mTextViewCurrentSurplusScale.setText("本期末剩余比例(100%):"+map.get("currentSurplusScale"));
			mViewHolder.mTextViewPriorPeriodCount.setText("上期末剩余量:"+map.get("priorPeriodCount"));
			mViewHolder.mTextViewPriorPeriodScale.setText("上期剩余比例(100%):"+map.get("priorPeriodScale"));
		}
		return convertView;
	}

 private class ViewHolder{
	 TextView mTextViewProjectName,  		  //项目名称
	 				mTextViewBillNo,  	   			  //清单编号
	 				mTextViewBillName, 			  //清单名称
	 				mTextViewCount,					  //数量
	 				mTextViewDeclareCount,	      //申报数量
	 				mTextViewDeclareScale,		  //申报比例
	 				mTextViewVerifyCount,			  //核定数量
	 				mTextViewVerifyScale,			  //核定比例
	 				mTextViewApproveCount,      //审批数量
	 				mTextViewCurrentSurplusCount, //本期剩余量
	 				mTextViewCurrentSurplusScale,   //本期剩余比例
	 				mTextViewPriorPeriodCount, 	   //上期剩余量
	 				mTextViewPriorPeriodScale;         //上期剩余比例
 }
}
