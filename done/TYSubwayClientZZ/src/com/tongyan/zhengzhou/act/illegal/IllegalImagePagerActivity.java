package com.tongyan.zhengzhou.act.illegal;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tongyan.zhengzhou.act.MApplication;
import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.common.afinal.annotation.view.ViewInject;
import com.tongyan.zhengzhou.common.utils.CommonUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.graphics.Bitmap;

public class IllegalImagePagerActivity extends Activity {
	
	@ViewInject(id = R.id.pager) ViewPager pager;
	private DisplayImageOptions options;
	private ArrayList<String> picList;
	private int pagerPosition;
	private Context mContext = this;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pro_illegal_photo_pager);
		MApplication.getInstance().addActivity(this); 
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			picList = bundle.getStringArrayList("list");
			pagerPosition = bundle.getInt("position", 0);
		}
		
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.resetViewBeforeLoading(true)
		.cacheOnDisc(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.considerExifParams(true)
		.displayer(new FadeInBitmapDisplayer(300))
		.build();

		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(new ImagePagerAdapter(picList));
		pager.setCurrentItem(pagerPosition);
	}
	
	private class ImagePagerAdapter extends PagerAdapter {

		private ArrayList<String> images;
		private LayoutInflater inflater;

		ImagePagerAdapter(ArrayList<String> images) {
			this.images = images;
			inflater = getLayoutInflater();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
			ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
			final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
			String url = null;
			try {
				url = java.net.URLEncoder.encode(CommonUtils.getRoute(mContext)+images.get(position),"utf-8");
				url =  url.replaceAll("%2F", "/");
				url =  url.replaceAll("%3A", ":");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			ImageLoader.getInstance().displayImage(url, imageView, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					spinner.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
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
					Toast.makeText(IllegalImagePagerActivity.this, message, Toast.LENGTH_SHORT).show();

					spinner.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					spinner.setVisibility(View.GONE);
				}
			});

			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}
}
