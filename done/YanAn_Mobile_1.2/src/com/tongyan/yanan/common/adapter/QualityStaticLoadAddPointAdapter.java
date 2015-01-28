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
 * 检测点数据适配器
 * 
 * @author ChenLang
 * 
 */
public class QualityStaticLoadAddPointAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, String>> arrayList;
	private LayoutInflater mInflater;
	private int mReoureId;
	private ViewHolderQualityStaticLoadAddPoint mViewHolder;

	public QualityStaticLoadAddPointAdapter(Context context,
			ArrayList<HashMap<String, String>> list, int resoureId) {
		this.mContext = context;
		this.arrayList = list;
		this.mInflater = LayoutInflater.from(mContext);
		this.mReoureId = resoureId;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			mViewHolder = new ViewHolderQualityStaticLoadAddPoint();
			convertView = mInflater.inflate(mReoureId, null);
			mViewHolder.mTxtPoint = (TextView) convertView
					.findViewById(R.id.txtPoint);
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolderQualityStaticLoadAddPoint) convertView.getTag();
		}
		HashMap<String, String> mMap = arrayList.get(position);
		if (mMap.size() > 0) {
			mViewHolder.mTxtPoint.setText("点号: "+mMap.get("pointName")+","+"x:"+mMap.get("pointX")+
					"  y:"+mMap.get("pointY")+"  z:"+mMap.get("pointZ"));
			mViewHolder.mMapExaminePoint=mMap;
		}
		return convertView;
	}

	public class ViewHolderQualityStaticLoadAddPoint {
		private TextView mTxtPoint;
		public HashMap<String, String> mMapExaminePoint;
	}
}
