package com.tongyan.yanan.common.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.act.progress.completion.WeekCompletionListAct;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @category月计划列表适配器
 * @author ChenLang
 * @date 2014/07/10
 * @version YanAn1.0
 */
public class CommonListViewAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, String>> mArrayList;
	private int mResoureId;
	private LayoutInflater mInflater;
	private HolderViewProgressMonthProject mHolderView;
	private Class mClazz;
	
	public CommonListViewAdapter(Context context, ArrayList<HashMap<String, String>> list, int resoureId, Class clazz) {
		this.mContext = context;
		this.mArrayList = list;
		this.mResoureId = resoureId;
		this.mInflater = LayoutInflater.from(context);
		this.mClazz = clazz;
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
			mHolderView = new HolderViewProgressMonthProject();
			convertView = mInflater.inflate(mResoureId, null);
			mHolderView.mTxtMonthDate=(TextView)convertView.findViewById(R.id.txtMonthDate);
			mHolderView.mTxtMonthState=(TextView)convertView.findViewById(R.id.txtMonthDateState);
			convertView.setTag(mHolderView);
		} else {
		 	 mHolderView = (HolderViewProgressMonthProject)convertView.getTag();
		}
		
		 HashMap<String, String> mMap=mArrayList.get(position);
		 if(mMap!=null){
			 String mMonthDate= mMap.get("CommonInfo");
			 String  mMonthState = mMap.get("State");
			 if(mClazz == WeekCompletionListAct.class) {
				 mMonthDate = mMonthDate + "周";
			 }
			 mHolderView.mTxtMonthDate.setText(mMonthDate);
			 if(!"".equals(mMonthState)){
				 if("0".equals(mMonthState)){
					 mHolderView.mTxtMonthState.setText("未完成");
				 }else if("1".equals(mMonthState)){
					 mHolderView.mTxtMonthState.setText("已完成");
				 }else if("2".equals(mMonthState)){
					 mHolderView.mTxtMonthState.setText("已提交");
				 }
			 }
			 mHolderView.mMapProgressMonth= mMap;
		 }
		return convertView;
	}

	 public class  HolderViewProgressMonthProject{
		  private TextView  mTxtMonthDate;
		  private TextView mTxtMonthState;
		  public  HashMap<String, String> mMapProgressMonth;
	 }
}
