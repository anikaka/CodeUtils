package com.tongyan.activity.adapter;

import java.util.List;

import com.tongyan.activity.R;
import com.tongyan.widget.view.AsyncImageView;
import com.tongyan.widget.view.AsyncImageView.OnImageViewLoadListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
/**
 * 
 * @ClassName P38_RiskNoticeAdapter.java
 * @Author wanghb
 * @Date 2013-9-11 pm 01:46:13
 * @Desc TODO
 */
public class RiskPictureAdapter extends BaseAdapter {
	
	//private Context mContext;
	private LayoutInflater mLayoutInflater;
	
	private List<String> mRiskPicList;
	
	public RiskPictureAdapter(Context mContext, List<String> mRiskPicList) {
		mLayoutInflater = LayoutInflater.from(mContext);
		this.mRiskPicList = mRiskPicList;
		//this.mContext = mContext;
	}
	
	
	@Override
	public int getCount() {
		return mRiskPicList == null ? 0 : mRiskPicList.size();
	}

	@Override
	public Object getItem(int position) {
		if (mRiskPicList != null) {
			return mRiskPicList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.risk_show_picture_item, null);
			holder = new ViewHolder();
			holder.mContainer = (AsyncImageView)convertView.findViewById(R.id.p43_risk_show_picture_container);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		String path = mRiskPicList.get(position);
		if(null != path && !"".equals(path)) {
			String nPath = "";
			try {
			String mMPath = path.substring(0, path.lastIndexOf("."));
			String mNPath = path.substring(path.lastIndexOf("."));
			nPath = mMPath + "_s" + mNPath;
			}catch(IndexOutOfBoundsException e) {/*NO TODo*/}
			holder.mPath = path;
			holder.mContainer.setPath(nPath, null, nPath);
		} else {
			holder.mPath = null;
			holder.mContainer.setDefaultImageResource(R.drawable.p00_no_picture);
		}
		holder.mContainer.setOnImageViewLoadListener(new OnImageViewLoadListener(){

			@Override
			public void onLoadingStarted(AsyncImageView imageView) {
				imageView.setDefaultImageResource(R.drawable.p00_ing_picture);
			}

			@Override
			public void onLoadingEnded(AsyncImageView imageView, Bitmap image) {
				
			}

			@Override
			public void onLoadingFailed(AsyncImageView imageView,
					Throwable throwable) {
				imageView.setDefaultImageResource(R.drawable.p00_no_picture);
			}});
		return convertView;
	}
	
	public class ViewHolder {
		public AsyncImageView mContainer;
		public String mPath;
	}
}
