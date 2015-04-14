package com.tongyan.zhengzhou.act.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.zhengzhou.act.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 第六级隧道附加信息
 * @author ChenLang
 *
 */
public class LineBaseInfoLevel6SandSoilAdapter  extends BaseAdapter{

	private ArrayList<HashMap<String, String>> mArrayList;
	private  int mResoureId;
	private LayoutInflater mInflater;
	private ViewHolder mViewHolder;
	
	public LineBaseInfoLevel6SandSoilAdapter(Context context,ArrayList<HashMap<String, String>> arrayList,int resoureId){
		this.mArrayList=arrayList;
		this.mResoureId=resoureId;
		this.mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
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
			mViewHolder.mTxtFacilityName=(TextView)convertView.findViewById(R.id.lineBaseInfoLevel6FacilityName);
			mViewHolder.mTxtFacilityCode=(TextView)convertView.findViewById(R.id.lineBaseInfoLevel6FacilityCode);
			mViewHolder.mTxtStartMile=(TextView)convertView.findViewById(R.id.lineBaseInfoLevel6StartMile);
			mViewHolder.mTxtEndMile=(TextView)convertView.findViewById(R.id.lineBaseInfoLevel6EndMile);
			convertView.setTag(mViewHolder);
		}else{
		   mViewHolder=(ViewHolder)convertView.getTag();
		}
		HashMap<String, String>  map=mArrayList.get(position);
		if(map!=null){
			mViewHolder.mTxtFacilityName.setText("信息类型:"+map.get("facilityName"));
			mViewHolder.mTxtFacilityCode.setText("编号:"+map.get("facilityCode"));
			mViewHolder.mTxtStartMile.setText("起始里程:"+map.get("startMile"));
			mViewHolder.mTxtEndMile.setText("终止里程:"+map.get("endMile"));
		}
		return convertView;
	}

	private class ViewHolder{ 
		private TextView mTxtFacilityName;  //设施名称
		private TextView mTxtFacilityCode;    //设施编号
		private TextView mTxtStartMile;        //开始里程
		private TextView mTxtEndMile; 		//结束里程
		
	}
}
