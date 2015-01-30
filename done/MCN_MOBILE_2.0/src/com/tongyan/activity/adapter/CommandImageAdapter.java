package com.tongyan.activity.adapter;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.activity.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * 图片加载适配器
 * @author ChenLang
 */

public class CommandImageAdapter  extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private int mResoureId;
	private ArrayList<HashMap<String, Bitmap>> mArrayList;
	private String[]  mUrl;
	private HashMap<String, SoftReference<Bitmap>> mImageCache=new HashMap<String, SoftReference<Bitmap>>();;
	private ViewHolder mViewHolder;
	
	public CommandImageAdapter(Context context,ArrayList<HashMap<String, Bitmap>> arrayList,String[] url,int soureId){
		mContext=context;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mArrayList=arrayList;
		this.mUrl=url;
		this.mResoureId=soureId;
	}
	
	@Override
	public int getCount() {
		return mArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return mArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			mViewHolder=new ViewHolder();
			convertView=mInflater.inflate(mResoureId, null);
			mViewHolder.mImageView=(ImageView)convertView.findViewById(R.id.commandImage);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder=(ViewHolder)convertView.getTag();
		}
		HashMap<String, Bitmap> map=mArrayList.get(position);
		if(map!=null){
			if(mImageCache.containsKey(mUrl[position])){
				SoftReference<Bitmap> sr=mImageCache.get(mUrl[position]);
				if(sr.get()!=null){
				  mViewHolder.mImageView.setImageBitmap(sr.get());
				}
			}else{
				//图片压缩
//				BitmapFactory.Options options = new BitmapFactory.Options();
//				options.inSampleSize=1;
//				options.inPurgeable = true;
//				options.inJustDecodeBounds=false;
				final float scale=mContext.getResources().getDisplayMetrics().density;
				Bitmap bitmap=map.get(mUrl[position]);
				Bitmap	b=ThumbnailUtils.extractThumbnail(bitmap, (int)(120*scale+0.5f), (int)(120*scale+0.5f));
//			   Bitmap bitmap=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+map.get("filePath"));
				mImageCache.put(mUrl[position], new SoftReference<Bitmap>(b));
				mViewHolder.mImageView.setImageBitmap(b);
			}
	//		mViewHolder.mImageView.setImageBitmap(map.get(mUrl[position]));
			mViewHolder.mBitmap=map.get(mUrl[position]);
			
		}
		return convertView;
	}

	public class ViewHolder{
		private ImageView  mImageView;
		public 	 Bitmap mBitmap;
	}
	
	/** 图片缓存*/
	public  HashMap<String, SoftReference<Bitmap>>  getImageCache(){
		return mImageCache;
	}
}
