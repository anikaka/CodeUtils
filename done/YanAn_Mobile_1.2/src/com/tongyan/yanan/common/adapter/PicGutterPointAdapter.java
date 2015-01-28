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
 * 盲沟
 * @author ChenLang
 *
 */
public class PicGutterPointAdapter  extends BaseAdapter{

	private Context mContext;
	private ArrayList<HashMap<String, String>> mArrayList;
	private LayoutInflater mInflater;
	private int mResoureId;
	private ViewHolderPicGutterPoint mViewHolder;
	
	public PicGutterPointAdapter(Context context,ArrayList<HashMap<String,String>> list,int resoureId){
		this.mContext=context;
		this.mArrayList=list;
		this.mInflater=LayoutInflater.from(context);
		this.mResoureId=resoureId;
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
				 convertView=mInflater.inflate(mResoureId, null);
				 mViewHolder=new ViewHolderPicGutterPoint();
				 mViewHolder.mTxtGutterPointName=(TextView)convertView.findViewById(R.id.txtName_picGutterPoint);
				 mViewHolder.mTxtGutterPointStart=(TextView)convertView.findViewById(R.id.txtStart);
				 mViewHolder.mTxtGutterPointEnd=(TextView)convertView.findViewById(R.id.txtEnd);
				 convertView.setTag(mViewHolder);
			}else{
				mViewHolder=(ViewHolderPicGutterPoint)convertView.getTag();
				}
			HashMap<String, String> mMap=mArrayList.get(position);
			if(mMap.size()>0){
				mViewHolder.mTxtGutterPointName.setText("名称: "+mMap.get("typeName"));
				mViewHolder.mTxtGutterPointStart.setText("起点: x:"+mMap.get("startPointX")+",y:"+mMap.get("startPointY")+",z:"+mMap.get("startPointZ"));
				mViewHolder.mTxtGutterPointEnd.setText("终点: x:"+mMap.get("endPointX")+",y: "+mMap.get("endPointY")+",z:"+mMap.get("endPointZ"));
				mViewHolder.mMapPicGutterPoint=mMap;
			}
		return convertView;
	}
	
	public class ViewHolderPicGutterPoint{
		private TextView mTxtGutterPointName;
		private TextView mTxtGutterPointStart;
		private TextView mTxtGutterPointEnd;
		public HashMap<String, String> mMapPicGutterPoint;
	}
}
