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
 * 照片时间列表适配器
 * @author ChenLang
 */
public class PicPhotoAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<HashMap<String, String>> mArrayList;
	private LayoutInflater mInflater;
	private int mResoureId;
	private ViewHolderPicPhoto mViewHolder;
	public PicPhotoAdapter(Context context,ArrayList<HashMap<String, String>> list,int resoureId){
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
				mViewHolder=new ViewHolderPicPhoto();
				convertView=mInflater.inflate(mResoureId, null);
				mViewHolder.txtDate=(TextView)convertView.findViewById(R.id.txtDate_picPhoto);
				mViewHolder.txtNumber=(TextView)convertView.findViewById(R.id.txtPicNumber);
				mViewHolder.txtState=(TextView)convertView.findViewById(R.id.txtState_picPhoto);
				convertView.setTag(mViewHolder);
			}else{
				mViewHolder=(ViewHolderPicPhoto)convertView.getTag();
			}
			HashMap<String, String> mMap=mArrayList.get(position);
			if(mMap.size()>0){
				mViewHolder.txtDate.setText(mMap.get("picDate"));
				mViewHolder.txtNumber.setText("数量: "+mMap.get("picNumber"));
				if("1".equals(mMap.get("picState"))){
					mViewHolder.txtState.setVisibility(View.VISIBLE);
					mViewHolder.txtState.setText("已上传");
				}else{
					mViewHolder.txtState.setVisibility(View.GONE);
				}
				mViewHolder.mMapPicPhoto=mMap;
			}
		return convertView;
	}

	public class ViewHolderPicPhoto{
		private TextView txtDate;
		private TextView txtNumber;
		private TextView txtState;
		public HashMap<String, String> mMapPicPhoto;
	}
}
