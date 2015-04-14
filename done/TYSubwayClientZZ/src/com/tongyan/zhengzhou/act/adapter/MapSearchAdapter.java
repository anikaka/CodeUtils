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
 * 
 * @Title: MapSearchAdapter.java 
 * @author Rubert
 * @date 2015-3-26 PM 05:40:13 
 * @version V1.0 
 * @Description: TODO
 */
public class MapSearchAdapter extends BaseAdapter {
	
	private Context mContext;
	private ArrayList<HashMap<String,String>> mArrayList;
	private LayoutInflater mInflater;
	
	
	public MapSearchAdapter(Context context, ArrayList<HashMap<String,String>> list) {
		this.mContext = context;
		this.mArrayList = list;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.map_search_item, parent, false);
			holder = new ViewHolder();
			holder.mTextView = (TextView)convertView.findViewById(R.id.map_search_item_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		HashMap<String,String> map = mArrayList.get(position);
		if(map != null) {
			if("1".equals(map.get("Type"))) {
				holder.mTextView.setText("违规建筑：" + map.get("Name"));
			} else {
				holder.mTextView.setText("监护项目：" + map.get("Name"));
			}
			holder.mCacheData = map;
		}
		return convertView;
	}
	
	public class ViewHolder {
		public TextView mTextView;
		public HashMap<String, String> mCacheData;
	}
	
	
}
