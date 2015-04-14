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
 * 安保区监护工程信息设计文件Adapter
 * @author ChenLang
 */

public class ProtectProDesignFlieAdapter  extends BaseAdapter{

	private Context mContext;
	private ArrayList<HashMap<String,String>> mArrayListFile;
	private int mResoureId;
	private LayoutInflater mInflater;
	private  ViewHolder mViewHolder;
	
	public ProtectProDesignFlieAdapter(Context context,ArrayList<HashMap<String,String>> arrayListFile,int resoureId){
		this.mContext=context;
		this.mArrayListFile=arrayListFile;
		this.mResoureId=resoureId;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return mArrayListFile.size();
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
			mViewHolder.mTxtFileName=(TextView)convertView.findViewById(R.id.txtFileName);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder=(ViewHolder)convertView.getTag();
		}
		HashMap<String,String> map=mArrayListFile.get(position);
		if(map!=null){
			mViewHolder.mTxtFileName.setText(map.get("fileName"));
			mViewHolder.map=map;
		}
		
		return convertView;
	}


	public class ViewHolder{
		private TextView mTxtFileName; 
		public HashMap<String, String> map;
	}

}
