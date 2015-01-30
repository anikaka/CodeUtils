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
 * 监理指令 适配器
 *@author ChenLang
 */

public class GpsCommandAdapter  extends BaseAdapter{

	private Context mContext;
	private ArrayList<HashMap<String, String>> arrayList;
	private LayoutInflater mInflater;
	private int  mResourId;
	private ViewHolder mViewHodler;
	
	public GpsCommandAdapter(Context context,ArrayList<HashMap<String, String>> arrayList,int resoureId){
		this.mContext=context;
		this.arrayList=arrayList;
		this.mResourId=resoureId;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			mViewHodler=new ViewHolder();
			convertView=mInflater.inflate(mResourId, null);
			mViewHodler.mTextViewCommandContent=(TextView)convertView.findViewById(R.id.textViewCommandContent);
			mViewHodler.mTextViewCommandDate=(TextView)convertView.findViewById(R.id.textViewCommandDate);
			mViewHodler.mTextViewCommandProcedureState=(TextView)convertView.findViewById(R.id.textViewCommandProcedure);
			convertView.setTag(mViewHodler);
		
		}else{
			mViewHodler=(ViewHolder)convertView.getTag();
		}
		HashMap<String, String> map=arrayList.get(position);
		mViewHodler.mTextViewCommandContent.setText(map.get("content"));
		mViewHodler.mTextViewCommandDate.setText(map.get("saveDate"));
		if("0".equals(map.get("isStart"))){
			mViewHodler.mTextViewCommandProcedureState.setText("未启动");
		}else if("1".equals(map.get("isStart"))){
			mViewHodler.mTextViewCommandProcedureState.setText("已启动");
		}
		mViewHodler.mapCommand=map;
		return convertView; 
	}
		
	 public class ViewHolder{
		private TextView mTextViewCommandDate; // 标段名称
		private TextView  mTextViewCommandContent; //指令内容 Procedure
		private TextView  mTextViewCommandProcedureState; //指令状态 是否已经启动流程
		public HashMap<String, String>  mapCommand;
	 }
}
