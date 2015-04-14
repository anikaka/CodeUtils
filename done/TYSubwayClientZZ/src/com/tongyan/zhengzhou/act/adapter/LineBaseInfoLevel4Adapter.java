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

/**'
 * 区间信息适配器
 * @author ChenLang
 */
public class LineBaseInfoLevel4Adapter  extends BaseAdapter{

	private ArrayList<HashMap<String, String>> mArrayList;
	private int mResoureId;
	private LayoutInflater mInflater;
	private ViewHolder mViewHolder;
	
	public LineBaseInfoLevel4Adapter(Context context,ArrayList<HashMap<String, String>> arrayList,int resoureId){
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
			mViewHolder.mTxtIntervalName=(TextView)convertView.findViewById(R.id.txtLineLevel4ItemIntervalName);
			mViewHolder.mTxtInterValCode=(TextView)convertView.findViewById(R.id.txtLineLevel4ItemIntervalCode);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder=(ViewHolder)convertView.getTag();
		}
		HashMap<String, String> map=mArrayList.get(position);
		mViewHolder.mTxtIntervalName.setText(map.get("intervalName"));
		mViewHolder.mTxtInterValCode.setText(map.get("intervalCode"));
		return convertView; 
	}

	private class ViewHolder{
		private TextView mTxtIntervalName;
		private TextView mTxtInterValCode;
	}
}
