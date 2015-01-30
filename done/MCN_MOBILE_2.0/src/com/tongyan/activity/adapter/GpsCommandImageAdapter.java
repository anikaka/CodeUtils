package com.tongyan.activity.adapter;


import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedList;


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
 * 监控指令 网格照片适配器
 * @author ChenLang
 * @Date  2014/11/25
 */

public class GpsCommandImageAdapter  extends BaseAdapter  {

	private  Context mContext;
	private LinkedList<HashMap<String, String>> mLinkedListImg;
	private LayoutInflater mInflater;
	private int mResoureId;
	private ViewHolder mViewHolder;
	private HashMap<String, SoftReference<Bitmap>>  mMapImageCache=new HashMap<String, SoftReference<Bitmap>>();
	public GpsCommandImageAdapter(Context context,LinkedList<HashMap<String, String>> linkedList,int resoureId){
		mContext=context;
		mLinkedListImg=linkedList;
		mResoureId=resoureId;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {

		return mLinkedListImg.size();
	}

	@Override
	public Object getItem(int position) {

		return mLinkedListImg.get(position);
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
		HashMap<String, String> map=mLinkedListImg.get(position);
		if("false".equals(map.get("type"))){
			if(mMapImageCache.containsKey(map.get("filePath"))){
				SoftReference<Bitmap>  sr=mMapImageCache.get(map.get("filePath"));
				if(sr.get()!=null){
					mViewHolder.mImageView.setImageBitmap(sr.get());					
				}
			}else{
				//图片压缩
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize=2;
				options.inPurgeable = true;
				options.inJustDecodeBounds=false;
				final float scale=mContext.getResources().getDisplayMetrics().density;
				Bitmap bitmap=BitmapFactory.decodeFile(map.get("filePath"), options);
				Bitmap	b=ThumbnailUtils.extractThumbnail(bitmap, (int)(110*scale+0.5f), (int)(115*scale+0.5f));
//			   Bitmap bitmap=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+map.get("filePath"));
				mMapImageCache.put(map.get("filePath"), new SoftReference<Bitmap>(b));
				mViewHolder.mImageView.setImageBitmap(b);
			}
		}else{			
			mViewHolder.mImageView.setImageResource(R.drawable.add_img);	
		}
		//设置监听
//		if("false".equals(map.get("type"))){
//			mViewHolder.mImageView.setOnLongClickListener(new imgLongClickListener());
//		}
		mViewHolder.mapImg=map;
		return convertView;	
	}
		public class  ViewHolder{
			private ImageView mImageView;
			public HashMap<String, String>  mapImg;
		}
		
		/** 图片缓存*/
		public   HashMap<String,SoftReference<Bitmap>> getCache(){
			return mMapImageCache;
		}
}
