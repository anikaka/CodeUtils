package com.tongyan.yanan.common.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.act.quality.QualityWaveVelocityAddLayerAct;
import com.tongyan.yanan.act.quality.QualityWaveVelocityLayerDetailsAct;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @Title: QualityWaveVelocityLayerAdapter.java 
 * @author Rubert
 * @date 2014-8-19 PM 03:04:04 
 * @version V1.0 
 * @Description: 
 */
public class QualityWaveVelocityLayerAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<HashMap<String, String>>  mListSubside;
	private Class mClazz;
	
	public QualityWaveVelocityLayerAdapter(Context context,ArrayList<HashMap<String, String>> list,Class clazz){
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView  holderView = null;;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.quality_wave_inject_layer_list_item, null);
			holderView = new HolderView();
			holderView.titleContainer = (RelativeLayout) convertView.findViewById(R.id.quality_wave_layer_title_container);
			holderView.titleTv = (TextView) convertView.findViewById(R.id.qquality_wave_layer_title);
			holderView.titleAdd = (Button) convertView.findViewById(R.id.qquality_wave_layer_add_btn);
			
			holderView.mContentLinearLayout = (LinearLayout) convertView.findViewById(R.id.quality_wave_layer_content_contianer); 
			holderView.contentSpeed = (TextView) convertView.findViewById(R.id.quality_wave_layer_content_speed);
			holderView.contentDeep = (TextView) convertView.findViewById(R.id.quality_wave_layer_content_deep);
			
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView)convertView.getTag();
		}
		final HashMap<String, String> map = mListSubside.get(position);
		if (map != null) {
			if ("title".equals(map.get("attribute"))) {
				holderView.titleContainer.setVisibility(View.VISIBLE);
				holderView.mContentLinearLayout.setVisibility(View.GONE);
				holderView.titleTv.setText("编号：" + map.get("PointNums") + " x：" +map.get("PointX")+ " y：" +map.get("PointY")+ " z：" +map.get("PointZ"));
			} else {
				holderView.titleContainer.setVisibility(View.GONE);
				holderView.mContentLinearLayout.setVisibility(View.VISIBLE);
				holderView.contentSpeed.setText("波速：" + map.get("DeepStartValue") + "-" + map.get("DeepEndValue"));
				holderView.contentDeep.setText("深度：" + map.get("WaveVelocityValue"));
			}
			holderView.mItemData = map;
		}
		
		holderView.titleAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				QualityWaveVelocityAddLayerAct.showDialog(mContext, map);
			}
		});
		
		if(mClazz == QualityWaveVelocityLayerDetailsAct.class) {
			holderView.titleAdd.setVisibility(View.GONE);
		}
	    return convertView;
	}

	public class HolderView {
		public RelativeLayout titleContainer;
		public TextView titleTv;
		public Button titleAdd;
		
		public LinearLayout mContentLinearLayout;
		public TextView contentDeep;
		public TextView contentSpeed;
		
		public HashMap<String, String> mItemData;
	}
}
