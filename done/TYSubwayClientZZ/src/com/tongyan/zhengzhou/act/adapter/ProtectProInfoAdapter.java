package com.tongyan.zhengzhou.act.adapter;

import java.util.HashMap;
import java.util.LinkedList;

import com.tongyan.zhengzhou.act.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 安保区监护工程信息适配器
 * @author ChenLang
 *
 */

public class ProtectProInfoAdapter extends BaseAdapter {

	private Context  mContext;
	private LinkedList<HashMap<String, Object>> mLinkedList;
	private int mResoureId; 
	private LayoutInflater mInflater;
	private ViewHolder mViewholder;
	
	public ProtectProInfoAdapter(Context context,LinkedList<HashMap<String, Object>>  linkedList,int resoureId){
		this.mContext=context;
		this.mLinkedList=linkedList;
		this.mResoureId=resoureId;
		mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {

		return mLinkedList.size();
	}

	@Override
	public Object getItem(int position) {

		return null;
	}

	@Override
	public long getItemId(int position) {
		return	position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			mViewholder=new ViewHolder();
			convertView=mInflater.inflate(mResoureId, null);
			mViewholder.mTxtProjectName=(TextView)convertView.findViewById(R.id.projectName);
			mViewholder.mTxtProjectRank=(TextView)convertView.findViewById(R.id.projectRank);
			convertView.setTag(mViewholder);
		}else{
			mViewholder=(ViewHolder)convertView.getTag();
		}
		HashMap<String, Object> map= mLinkedList.get(position);
		if(map!=null){
			HashMap<String, String> mBaseInfo = (HashMap<String, String>)map.get("BaseInfo");
			if(mBaseInfo != null) {
				mViewholder.mTxtProjectName.setText("项目名称:"+mBaseInfo.get("projectName"));
				mViewholder.mTxtProjectRank.setText("项目等级:"+mBaseInfo.get("projectClass"));
			}
			mViewholder.map=map;
		}
		return convertView;
	}

	public class ViewHolder{
		
		private TextView mTxtProjectName; //项目名称
		private TextView  mTxtProjectRank;   //项目等级
		public   HashMap<String, Object> map;
	}
}
