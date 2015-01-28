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
 * @category收文适配器
 * @author ChenLang
 * @date  	2014/07/23
 *  @version Yan An 1.0
 */
public class OaReciveTextAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<HashMap<String, String>> mArraylist=new ArrayList<HashMap<String,String>>();
	private LayoutInflater mInflater;
	private int mResoureId;
	private ViewHolderReceiveText mViewHolder;
	
	public OaReciveTextAdapter(Context context,ArrayList<HashMap<String, String>> list,int resoureId){
		this.mContext=context;
		this.mArraylist=list;
		mInflater=LayoutInflater.from(context);
		this.mResoureId=resoureId;
	}
	
	@Override
	public int getCount() {
		return mArraylist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			mViewHolder=new ViewHolderReceiveText();
			convertView=mInflater.inflate(mResoureId, null);
			mViewHolder.mTxtTitle=(TextView)convertView.findViewById(R.id.txtTitle_receiveText);
			mViewHolder.mTxtTheme=(TextView)convertView.findViewById(R.id.txtTheme_receiveText);
			mViewHolder.mTxtTime=(TextView)convertView.findViewById(R.id.txtTime_receiveText);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder=(ViewHolderReceiveText)convertView.getTag();
		}
		HashMap<String, String>	mMap=mArraylist.get(position);
		if(mMap!=null && mMap.size()>0){
			mViewHolder.mTxtTitle.setText("[收 文]");
			mViewHolder.mTxtTheme.setText(mMap.get("theme"));
			mViewHolder.mTxtTime.setText(mMap.get("createTime"));
			mViewHolder.mMapViewHolderReceiveText=mMap;
		}
		return convertView;
	}

	public class ViewHolderReceiveText{
		private TextView mTxtTitle;
		private TextView mTxtTheme;
		private TextView mTxtTime;
		public   HashMap<String, String> mMapViewHolderReceiveText;
	}
}
