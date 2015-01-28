package com.tongyan.yanan.common.adapter;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.tongyan.yanan.act.R;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 照片列表适配器
 * @author ChenLang
 */
public class PicPhotoLevelAdapter  extends BaseAdapter{

	private Context mContext;
	private ArrayList<HashMap<String, String>> mArrayList;
	private LayoutInflater mInflater;
	private int mResoureId;
	private ViewHolderPicPhotoLevel mViewHolder;
	//缓存池
	public  HashMap<String,SoftReference<Bitmap>> mImageCache = new  HashMap<String,SoftReference<Bitmap>>();
	public PicPhotoLevelAdapter(Context context,ArrayList<HashMap<String, String>> list,int resoureId){
		this.mContext=context;
		this.mArrayList=list;
		this.mResoureId=resoureId;
		this.mInflater=LayoutInflater.from(context);
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
			mViewHolder=new ViewHolderPicPhotoLevel();
			convertView=mInflater.inflate(mResoureId, null);
			mViewHolder.mImg=(ImageView)convertView.findViewById(R.id.img);
			mViewHolder.mTxtPicName=(TextView)convertView.findViewById(R.id.txtPicName);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder=(ViewHolderPicPhotoLevel)convertView.getTag();
		}
		if(mArrayList.size()>0){
			HashMap<String, String> mMap=mArrayList.get(position);
			//图片压缩
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize=2;
			options.inPurgeable = true;
			options.inJustDecodeBounds=false;
			final float scale=mContext.getResources().getDisplayMetrics().density;
			Bitmap bit=BitmapFactory.decodeFile(mMap.get("picPath"), options);
			Bitmap	b2=ThumbnailUtils.extractThumbnail(bit, (int)(80*scale+0.5f), (int)(60*scale+0.5f));
			//图片缓存
			SoftReference<Bitmap> bmp = new SoftReference<Bitmap>(b2);
			if(mImageCache.containsKey(mMap.get("picPath"))){
				SoftReference<Bitmap> softReference=mImageCache.get(mMap.get("picPath"));
				if(softReference.get()!=null){
					mViewHolder.mImg.setImageBitmap(softReference.get());
				}
			}else{
				mImageCache.put(mMap.get("picPath"), bmp);
				mViewHolder.mImg.setImageBitmap(b2);
			}
			mViewHolder.mTxtPicName.setText(mMap.get("picName")+".jpg");
			mViewHolder.mMapPicPhotoLevel=mMap;
		}
		return convertView;
	}
	
	public class ViewHolderPicPhotoLevel{
		private ImageView mImg;
		private TextView 	   mTxtPicName;
		public  HashMap<String, String > mMapPicPhotoLevel;
	}
	
	public   HashMap<String,SoftReference<Bitmap>> getCache(){
		return mImageCache;
	}
}
