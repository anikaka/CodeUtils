package com.tongyan.zhengzhou.act.fragment.proillegalinfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.act.adapter.ProtectProInfoAdapter;
import com.tongyan.zhengzhou.act.fragment.proillegalinfo.IllegalPhotoFragment.ImageAdapter;
import com.tongyan.zhengzhou.act.fragment.proillegalinfo.IllegalPhotoFragment.ImageAdapter.ViewHolder;
import com.tongyan.zhengzhou.act.illegal.IllegalImagePagerActivity;
import com.tongyan.zhengzhou.common.db.DBService;
import com.tongyan.zhengzhou.common.utils.CommonUtils;
import com.tongyan.zhengzhou.common.utils.Constants;
import com.tongyan.zhengzhou.common.utils.ImageUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class IllegalVedioFragment extends Fragment{

	private ArrayList<String> vedioList = new ArrayList<String>();
	private Context mContext;
	private GridView gridView;
	private String illegalID = "";
	private ImageAdapter.ViewHolder  viewHolder;
	private String originCameraImgPath;
	
	public static IllegalVedioFragment getInstance(ArrayList<String> list,String illegalId,Context context){
		IllegalVedioFragment fragment = new IllegalVedioFragment();
		if(list != null){
			fragment.vedioList.addAll(list);
		}
		fragment.mContext = context;
		fragment.illegalID = illegalId;
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pro_illegal_photo_info,null, false);
		gridView = (GridView) view.findViewById(R.id.gridview);

		gridView.setAdapter(new ImageAdapter());
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				viewHolder=(ImageAdapter.ViewHolder)arg1.getTag();
				viewHolder.vedioPath = new DBService(mContext).getVedioFilePath(viewHolder.remotePath);
				if(viewHolder.vedioPath != null && !"".endsWith(viewHolder.vedioPath)){
					Intent it = new Intent(Intent.ACTION_VIEW);
					it.setDataAndType(Uri.parse(viewHolder.vedioPath),"video/mp4");
					startActivity(it);
				}else{
					final Dialog dialog = new Dialog(mContext);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.common_sure_cancel_dialog);
					TextView title = (TextView)dialog.findViewById(R.id.prompt_content);
					title.setText("是否下载？");
					Button sureBtn = (Button)dialog.findViewById(R.id.sure_btn);
					Button cancleBtn = (Button)dialog.findViewById(R.id.cancel_btn);
					sureBtn.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {//TODO  如果是历史这里不是真的删除，是重新插入一条记录，并修改状态。如果不是历史就直接删除
							dialog.dismiss();
							downLoadVedioFile(viewHolder.remotePath,viewHolder.imageView);
						}
					});
					cancleBtn.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					dialog.show();
				}
			}
		});
		return view;
	}
	private void downLoadVedioFile(String remotePath,ImageView imageView) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			 Toast.makeText(mContext, "SD卡不可用", Toast.LENGTH_SHORT).show();
           return;
        }
		final String fileDir = Environment.getExternalStorageDirectory().getPath() + Constants.PATH_OF_ILLEGAL_VEDIO;
	    File file = new File(fileDir);
	    if(!file.exists()) {
	        file.mkdirs();
	     }
	   
	    new Thread(
	    		new Runnable() {
					public void run() {
						String url;
						try {
							url = java.net.URLEncoder.encode(CommonUtils.getRoute(mContext)+viewHolder.remotePath,"utf-8");
							url =  url.replaceAll("%2F", "/");
							url =  url.replaceAll("%3A", ":");
							String fileName = url.substring(url.lastIndexOf("/") + 1);
							originCameraImgPath = fileDir + fileName;
							 boolean isDownLoad = downLoadFile(url,fileDir);
							 if(isDownLoad){
								 new DBService(mContext).insertPicPath(illegalID,originCameraImgPath, viewHolder.remotePath);
								 Message msg = new Message();
								 msg.arg1 = Constants.SUCCESS;
								 handler.sendMessage(msg);
							 }
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}	
					}
				}).start();
	}
	/**
	 * 下载指定url下的文件到指定的目录下
	 * @param url 地址
	 * @param targetPath 目的文件的路径
	 * @return boolean   如果返回true下载成功，否则下载失败
	 * */
	public boolean downLoadFile(String url, String targetPath){
		
		String fileName = null;
		if (fileName == null || "".equals(fileName))
			fileName = url.substring(url.lastIndexOf("/") + 1);
		
		URL Url = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			Url = new URL(url);
			conn = (HttpURLConnection)Url.openConnection();
			//conn.connect();
			is = conn.getInputStream();
			
			int fileSize = conn.getContentLength();// 根据响应获取文件大小
			if (fileSize <= 0) { 	// 获取内容长度为0
				throw new RuntimeException("无法获知文件大小");
			}
			
			fos = new FileOutputStream(targetPath + "/" + fileName);
			byte buf[] = new byte[1024];
			int numread;
			while ((numread = is.read(buf)) != -1) {
				fos.write(buf, 0, numread);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}finally{
			try {
				if(is != null){
					is.close();
				}
				if(fos != null){
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	
	}
	
	public class ImageAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return vedioList.size();
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
				holder.vedioPath = "";
				holder.remotePath = "";
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			holder.progressBar.setVisibility(view.INVISIBLE);
			holder.remotePath = vedioList.get(position);
			holder.vedioPath = new DBService(mContext).getVedioFilePath(vedioList.get(position));
			if(holder.vedioPath != null && !"".endsWith(holder.vedioPath)){
				Bitmap bitmap = new ImageUtil().getVideoThumbnail(holder.vedioPath, 90, 95, Images.Thumbnails.MINI_KIND);
				if(bitmap != null){
					holder.imageView.setImageBitmap(bitmap);
				}
			}else{
				holder.imageView.setImageResource(R.drawable.ic_empty);
			}
		

			return view;
		}

		class ViewHolder {
			ImageView imageView;
			ProgressBar progressBar;
			String vedioPath;
			String remotePath;
		}
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case Constants.SUCCESS:
				 Bitmap bitmap = new ImageUtil().getVideoThumbnail(originCameraImgPath, 90, 95, Images.Thumbnails.MINI_KIND);
					if(bitmap != null){
						viewHolder.imageView.setImageBitmap(bitmap);
					}
				break;

			default:
				break;
			}
			
		};
	};
}
