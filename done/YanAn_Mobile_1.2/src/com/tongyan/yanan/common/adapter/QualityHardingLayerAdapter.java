package com.tongyan.yanan.common.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.act.quality.QualityHardingAddLayerAct;
import com.tongyan.yanan.act.quality.QualityHardingLayerDetailAct;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @Title: QualityHardingAdapter.java 
 * @author Rubert
 * @date 2014-8-19 PM 03:04:04 
 * @version V1.0 
 * @Description: 
 */
public class QualityHardingLayerAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<HashMap<String, String>>  mListSubside;
	private Class mClazz;
	
	HolderView  holderView;
	public QualityHardingLayerAdapter(Context context,ArrayList<HashMap<String, String>> list,Class clazz){
		this.mContext=context;
		this.mListSubside=list;
		this.mClazz = clazz;
		mInflater = LayoutInflater.from(mContext);
	} 
	@Override
	public int getCount() {
		return mListSubside.size();
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
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.quality_harding_layer_list_item, null);
			holderView = new HolderView();
			holderView.titleContainer = (RelativeLayout) convertView.findViewById(R.id.quality_harding_layer_title_container);
			holderView.titleTv = (TextView) convertView.findViewById(R.id.qquality_harding_layer_title);
			holderView.contentTv = (TextView) convertView.findViewById(R.id.quality_harding_layer_content);
			holderView.titleAdd = (Button) convertView.findViewById(R.id.qquality_harding_layer_add_btn);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView)convertView.getTag();
		}
		final HashMap<String, String> map = mListSubside.get(position);
		if (map != null) {
			if ("title".equals(map.get("attribute"))) {
				holderView.titleContainer.setVisibility(View.VISIBLE);
				holderView.contentTv.setVisibility(View.GONE);
				holderView.titleTv.setText(map.get("LayerNum"));
			} else {
				holderView.titleContainer.setVisibility(View.GONE);
				holderView.contentTv.setVisibility(View.VISIBLE);
				holderView.contentTv.setText(map.get("Content"));
			}
			holderView.mItemData = map;
		}
		
		holderView.titleAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				QualityHardingAddLayerAct.showDialog(mContext, map.get("LayerNum"));
			}
		});
		
		if(mClazz == QualityHardingLayerDetailAct.class) {
			holderView.titleAdd.setVisibility(View.GONE);
		}
		
	    return convertView;
	}

	public class HolderView {
		public RelativeLayout titleContainer;
		public TextView titleTv;
		public TextView contentTv;
		public Button titleAdd;
		public HashMap<String, String> mItemData;
	}
}
