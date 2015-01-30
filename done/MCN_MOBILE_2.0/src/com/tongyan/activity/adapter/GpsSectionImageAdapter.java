package com.tongyan.activity.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.activity.R;
import com.tongyan.widget.view.AsyncImageView;
import com.tongyan.widget.view.AsyncImageView.OnImageViewLoadListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 
 * @ClassName P38_RiskNoticeAdapter.java
 * @Author wanghb
 * @Date 2013-9-11 pm 01:46:13
 * @Desc 工程查阅模块-照片
 */
public class GpsSectionImageAdapter extends BaseAdapter {
	
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	
	private ArrayList<HashMap<String, String>> mPicList;
	
	
	
	public GpsSectionImageAdapter(Context mContext, ArrayList<HashMap<String, String>> mPicList) {
		mLayoutInflater = LayoutInflater.from(mContext);
		this.mPicList = mPicList;
		this.mContext = mContext;
	}
	
	
	@Override
	public int getCount() {
		return mPicList == null ? 0 : mPicList.size();
	}

	@Override
	public Object getItem(int position) {
		if (mPicList != null) {
			return mPicList.get(position);
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
			convertView = mLayoutInflater.inflate(R.layout.gps_section_show_picture_item, null);
			holder = new ViewHolder();
			holder.mContainer = (AsyncImageView)convertView.findViewById(R.id.gps_section_iamge_view);
			holder.mImageName = (TextView)convertView.findViewById(R.id.gps_section_iamge_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.mContainer.clear();
		
		final HashMap<String, String> map = mPicList.get(position);
		String mPhotoName = map.get("photo_name");
		if(mPhotoName != null && mPhotoName.contains(".jpg")) {
			mPhotoName = mPhotoName.replaceAll(".jpg", "");
		}
		
		String mLocalPath = map.get("local_img_path");
		holder.mContainer.setDefaultImageResource(R.drawable.p00_ing_picture);
		if(mLocalPath != null) {
			//holder.mContainer.setPath(mLocalPath, null, mLocalPath);
			mPhotoName = mPhotoName + "(未上传)";
		} 
		holder.mContainer.setDefaultImageResource(R.drawable.p00_ing_picture);
		holder.mImageName.setText(mPhotoName);
		/*holder.mContainer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imageDialog(map);
			}
		});*/
		holder.mItemData = map;
		holder.mContainer.setOnImageViewLoadListener(new OnImageViewLoadListener(){

			@Override
			public void onLoadingStarted(AsyncImageView imageView) {
				imageView.setDefaultImageResource(R.drawable.p00_ing_picture);
			}

			@Override
			public void onLoadingEnded(AsyncImageView imageView, Bitmap image) {
				
			}

			@Override
			public void onLoadingFailed(AsyncImageView imageView, Throwable throwable) {
				imageView.setDefaultImageResource(R.drawable.p00_no_picture);
			}});
		
		return convertView;
	}
	
	
	
	public class ViewHolder {
		public AsyncImageView mContainer;
		public TextView mImageName;
		public HashMap<String, String> mItemData;
	}
}
