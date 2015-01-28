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
 * 盲沟类型
 * @author ChenLang
 *
 */
public class PicGutterAdapter  extends BaseAdapter{

	private Context mContext;
	private ArrayList<HashMap<String, String>> mArrayList;
	private LayoutInflater mInflater;
	private int mResoureId;
	private ViewHolderPicGutter mViewHolder;
	
	public PicGutterAdapter(Context context,ArrayList<HashMap<String,String>> list,int resoureId){
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
				 mViewHolder=new ViewHolderPicGutter();
				 mViewHolder.mTxtGutterName=(TextView)convertView.findViewById(R.id.txtGutterName);
				 convertView.setTag(mViewHolder);
			}else{
				mViewHolder=(ViewHolderPicGutter)convertView.getTag();
				}
			HashMap<String, String> mMap=mArrayList.get(position);
			if(mMap.size()>0){
				mViewHolder.mTxtGutterName.setText(mMap.get("text"));
				mViewHolder.mMapPicGutter=mMap;
			}
		return convertView;
	}
	public class ViewHolderPicGutter{
		private TextView mTxtGutterName;
		public HashMap<String, String> mMapPicGutter;
	}
}
