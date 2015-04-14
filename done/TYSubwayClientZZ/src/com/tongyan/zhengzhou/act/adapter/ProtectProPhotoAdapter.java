package com.tongyan.zhengzhou.act.adapter;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.common.utils.FileOpen;

/**
 * 照片列表适配器
 * @author ChenLang
 */

public class ProtectProPhotoAdapter  extends BaseAdapter{

	private Context mContext;
	private LayoutInflater mInflater;
	private int mResoureId;
	private ViewHolder mViewHolder;
	private HashMap<String, SoftReference<Bitmap>> mImageCache=new HashMap<String, SoftReference<Bitmap>>();
	private ArrayList<HashMap<String, String>>  mArrayList;
	private String[] mArrImgName;
	
	public ProtectProPhotoAdapter(Context context,ArrayList<HashMap<String, String>>  arraylist, String[] arrImgName,int resoureId){
		  mContext=context;
		  mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		  mArrayList=arraylist;
		  mResoureId=resoureId;
		  mArrImgName=arrImgName;
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
				mViewHolder=new ViewHolder();
				convertView=mInflater.inflate(mResoureId, null);
				mViewHolder.img=(ImageView)convertView.findViewById(R.id.protectProImg);
				convertView.setTag(mViewHolder);
			}else{
				mViewHolder=(ViewHolder)convertView.getTag();
			}
			final HashMap<String, String> map=mArrayList.get(position);
			if(!"".equals(map.get("filePath"))){
				if(mImageCache.containsKey(mArrImgName[position])){
					SoftReference<Bitmap> sr=mImageCache.get(mArrImgName[position]);
					if(sr.get()!=null){
					  mViewHolder.img.setImageBitmap(sr.get());
					}
				}else{
					//图片压缩
					final float scale=mContext.getResources().getDisplayMetrics().density;
					Bitmap bitmap=BitmapFactory.decodeFile(map.get("filePath"));
					Bitmap	b=ThumbnailUtils.extractThumbnail(bitmap, (int)(120*scale+0.5f), (int)(120*scale+0.5f));
					mImageCache.put(mArrImgName[position], new SoftReference<Bitmap>(b));
					mViewHolder.img.setImageBitmap(b);
				}
				mViewHolder.img.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mContext.startActivity(FileOpen.getImageFileIntent(new File(map.get("filePath"))));
					}
				});
			}
		return convertView;
	}
	
	
	public HashMap<String, SoftReference<Bitmap>> getImgCache(){
		return mImageCache;
	}
	
	public class ViewHolder{
		private ImageView img;
	}
}
