package com.tongyan.zhengzhou.act.fragment.proillegalinfo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.act.illegal.IllegalImagePagerActivity;
import com.tongyan.zhengzhou.common.utils.CommonUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

public class IllegalPhotoFragment extends Fragment{

	private ArrayList<String> photoList = new ArrayList<String>();
	private GridView gridView;
	private DisplayImageOptions options;
	private Context mContext;
	private Bitmap bitmap;
	
	
	/**
	 * @param list
	 * @param context
	 * @return
	 */
	public static IllegalPhotoFragment getInstance(ArrayList<String> list,Context context){
		IllegalPhotoFragment fragment = new IllegalPhotoFragment();
		if(list != null){
			fragment.photoList.addAll(list);
		}
		fragment.mContext = context;
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pro_illegal_photo_info,null, false);
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_stub)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();	
		gridView = (GridView) view.findViewById(R.id.gridview);
		gridView.setAdapter(new ImageAdapter());
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				Intent intent = new Intent(mContext, IllegalImagePagerActivity.class);
				intent.putExtra("list", photoList);
				intent.putExtra("position", arg2);
				startActivity(intent);
			}
		});
		return view;
	}
	
	public class ImageAdapter extends BaseAdapter {
		
		@Override
		public int getCount() {
			return photoList.size();
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
			final ViewHolder holder;
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = LayoutInflater.from(getActivity());
				view = inflater.inflate(R.layout.item_grid_image, parent, false);
				holder = new ViewHolder();
				holder.imageView = (ImageView) view.findViewById(R.id.image);
				holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			String url = null;
			try {
				url = java.net.URLEncoder.encode(CommonUtils.getRoute(mContext)+photoList.get(position),"utf-8");
				url =  url.replaceAll("%2F", "/");
				url =  url.replaceAll("%3A", ":");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			ImageLoader.getInstance().displayImage(url, holder.imageView, options, new SimpleImageLoadingListener() {
										 @Override
										 public void onLoadingStarted(String imageUri, View view) {
											 holder.progressBar.setProgress(0);
											 holder.progressBar.setVisibility(View.VISIBLE);
										 }

										 @Override
										 public void onLoadingFailed(String imageUri, View view,FailReason failReason) {
											 String message = null;
												switch (failReason.getType()) {
													case IO_ERROR:
														message = "Input/Output error";
														break;
													case DECODING_ERROR:
														message = "Image can't be decoded";
														break;
													case NETWORK_DENIED:
														message = "Downloads are denied";
														break;
													case OUT_OF_MEMORY:
														message = "Out Of Memory error";
														break;
													case UNKNOWN:
														message = "Unknown error";
														break;
												}
												Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
											 holder.progressBar.setVisibility(View.GONE);
										 }

										 @Override
										 public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
											 holder.progressBar.setVisibility(View.GONE);
//											 bitmap = new ImageUtil().ResizeBitmap(loadedImage,110);
//											 holder.imageView.setImageBitmap(bitmap);
										 }
									 }, 
									 new ImageLoadingProgressListener() {
										 @Override
										 public void onProgressUpdate(String imageUri, View view, int current,
												 int total) {
											 holder.progressBar.setProgress(Math.round(100.0f * current / total));
										 }
									 }
			);

			return view;
		}

		class ViewHolder {
			ImageView imageView;
			ProgressBar progressBar;
		}
	}
	
	@Override
	public void onDestroy() {
		if(bitmap != null){
			bitmap.recycle();
			bitmap = null;
		}
		super.onDestroy();
	}
}
