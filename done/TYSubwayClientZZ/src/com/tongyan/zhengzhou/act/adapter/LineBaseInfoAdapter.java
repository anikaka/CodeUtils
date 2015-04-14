package com.tongyan.zhengzhou.act.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.tongyan.zhengzhou.act.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LineBaseInfoAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<HashMap<String, String>> mArrayList;
	private int mResoureId;
	private LayoutInflater mInflater;
	private ViewHolder  mViewHolder;
	
	public LineBaseInfoAdapter(Context context,ArrayList<HashMap<String, String>> arrayList,int resoureId){
		this.mContext=context;
		this.mArrayList=arrayList;
		this.mResoureId=resoureId;
		mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			convertView=mInflater.inflate(mResoureId, null );
			mViewHolder.itemNameText=(TextView)convertView.findViewById(R.id.itemName);
			mViewHolder.itemValueText=(TextView)convertView.findViewById(R.id.itemValue);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder=(ViewHolder)convertView.getTag();
		}
		HashMap<String, String> map=mArrayList.get(position);
		if(map!=null){
			Iterator iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				HashMap.Entry entry = (HashMap.Entry) iterator.next();
				String itemName = entry.getKey().toString();
				String itemValue = entry.getValue().toString();
				mViewHolder.itemNameText.setText(itemName);
				mViewHolder.itemValueText.setText(itemValue);
			}
		}
		return convertView;
	}

	public class ViewHolder{
		private TextView itemNameText; //条目名称
		private  TextView itemValueText;//条目值
	}
}
