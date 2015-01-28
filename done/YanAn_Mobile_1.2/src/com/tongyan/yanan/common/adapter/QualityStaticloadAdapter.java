package com.tongyan.yanan.common.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.yanan.act.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 平板静电荷试验适配器
 * @author ChenLang
 *
 */
public class QualityStaticloadAdapter extends BaseAdapter{
	private Context mContext;
	private ArrayList<HashMap<String, String>> arrayList;
	private LayoutInflater mInflater;
	private int mResoureId;
	private ViewHolderQualityStaticLoad mViewHolder;
	
	public QualityStaticloadAdapter(Context context,ArrayList<HashMap<String, String>> list,int resoureId){
		this.mContext=context;
		this.arrayList=list;
		this.mResoureId=resoureId;
		this.mInflater=LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			mViewHolder=new ViewHolderQualityStaticLoad();
			convertView=mInflater.inflate(mResoureId, null);
			mViewHolder.mTxtNo=(TextView)convertView.findViewById(R.id.txtNo);
			mViewHolder.mTxtDate=(TextView)convertView.findViewById(R.id.txtDate);
			mViewHolder.mTxtState=(TextView)convertView.findViewById(R.id.txtState);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder=(ViewHolderQualityStaticLoad)convertView.getTag();
		}
		if(arrayList.size()>0){
			HashMap<String, String> mMap = arrayList.get(position);
			mViewHolder.mTxtNo.setText("编号 :"+mMap.get("no"));
			mViewHolder.mTxtDate.setText("日期 :"+mMap.get("date"));
			if("2".equals(mMap.get("state"))){
				mViewHolder.mTxtState.setText("已上传");
			}else if("1".equals(mMap.get("state"))) {
				mViewHolder.mTxtState.setText("已完成");
			} else {
				mViewHolder.mTxtState.setText("未完成");
			}
			mViewHolder.mMapQualityStaticLoad=mMap;
		}
		return convertView;
	}

	public class ViewHolderQualityStaticLoad{
		private TextView mTxtNo; //编号
		private TextView mTxtDate; //日期
		private TextView mTxtState;//上传状态
		public HashMap<String, String> mMapQualityStaticLoad;
	}
}