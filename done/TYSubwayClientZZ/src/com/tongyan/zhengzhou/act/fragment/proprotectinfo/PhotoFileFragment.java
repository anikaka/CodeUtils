package com.tongyan.zhengzhou.act.fragment.proprotectinfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.act.adapter.ProtectProPhotoAdapter;
import com.tongyan.zhengzhou.common.utils.Constants;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;


/**
 * 照片文件
 * @author ChenLang
 */

public class PhotoFileFragment extends Fragment {

	private static PhotoFileFragment instance=new PhotoFileFragment();
	private ArrayList<HashMap<String, String>>  mArrayList=new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String, String>> mArrayListBitmap=new ArrayList<HashMap<String,String>>();
	private GridView  mGridView;
	private String[]  mArrImgName;
	
	
	/**图片缓存路径*/
	private  String   mImgPath=Environment.getExternalStorageDirectory().getPath()+"/zz/img/protectpro/";
	private ProtectProPhotoAdapter mPhotoAdapter;
	//private ExecutorService  mExecutorService=Executors.newFixedThreadPool(3); 
	
	public static PhotoFileFragment getInstance(ArrayList<HashMap<String, String>> mArrayList){
		if(mArrayList!=null && mArrayList.size()>0){
			if(instance.mArrayList.size()>0){
				instance.mArrayList.clear();
				instance.mArrayListBitmap.clear();
			}
			instance.mArrayList.addAll(mArrayList);
		}
		return instance;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			View view=inflater.inflate(R.layout.protectpro_photofile, null);
			addDefaultImg();
			mGridView=(GridView)view.findViewById(R.id.gridViewPhotoFile);
			mPhotoAdapter=new ProtectProPhotoAdapter(getActivity(),mArrayListBitmap,mArrImgName,R.layout.protectpro_photofile_item);
			mGridView.setAdapter(mPhotoAdapter);
			for(int i=0;i<mArrayList.size();i++){
				HashMap<String, String> map=mArrayList.get(i);
				downFile(map.get("filePath"), map.get("fileName"));
			}		
			return view;
	}
	
	
	/**
	 *添加默认的Bitmap对象*/
	private void addDefaultImg(){
		mArrImgName=new  String[mArrayList.size()];
		for(int i=0;i<mArrayList.size();i++){
			HashMap<String, String> map=mArrayList.get(i);
			HashMap<String, String> mapFilePath=new HashMap<String, String>();
			mapFilePath.put("filePath", "");
			mapFilePath.put("fileName", map.get("fileName"));
			mArrayListBitmap.add(mapFilePath);
			mArrImgName[i]=map.get("fileName");
		}
	}
	
	
	/** 
	 * 从网咯下载图片后替换原来默认的bitmap对象*/
	private void setFilePath(String fileName){
		for(int i=0;i<mArrayListBitmap.size();i++){
			HashMap<String, String> map=mArrayListBitmap.get(i);
			if(map.containsValue(fileName)){
				map.put("filePath", mImgPath+fileName);
				return ;
			}
		}
	}

	/**
	 * */
	private void   downFile(final String imgUrl,final String fileName){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				  if(!new File(mImgPath).exists()){
					  new File(mImgPath).mkdirs();
				    }
					File  file=new File(mImgPath,fileName);
					try {
						if(!file.exists()) {
							file.createNewFile();
						}
						String fileUrl= imgUrl.replace("\\", "/");
						fileUrl = URLEncoder.encode(fileUrl, "utf-8");
						fileUrl =  fileUrl.replaceAll("%2F", "/");
						fileUrl="http://"+Constants.SERVER_URL_IP_PORT+fileUrl;
						URL  url=new URL(fileUrl);
						URLConnection conn=url.openConnection();
						conn.connect();
						InputStream is=conn.getInputStream();
						FileOutputStream fos=new FileOutputStream(file);
						byte[] b=new byte[1024];
						int len = -1;
						while((len=is.read(b))!=-1){
							fos.write(b,0, len);
						}
						fos.flush();
						fos.close();
						is.close();
						Message msg=Message.obtain();
						Bundle bundle=new Bundle();
						bundle.putString("fileName", fileName);
						msg.setData(bundle);
						msg.what=Constants.SUCCESS;
						handler.sendMessage(msg);
					} catch (IOException e) {
						e.printStackTrace();
						handler.sendEmptyMessage(Constants.CONNECTION_TIMEOUT);
					}
				}
		}).start();
	}
	
	
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case Constants.SUCCESS:
				String fileName=msg.getData().getString("fileName");
				setFilePath(fileName);
				mPhotoAdapter.notifyDataSetChanged();
				break;
			case Constants.CONNECTION_TIMEOUT:
				Toast.makeText(getActivity(), "网络连接超时，请重试", Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};
	
	@Override
	public void onDestroy() {
		HashMap<String, SoftReference<Bitmap>> map=mPhotoAdapter.getImgCache();
		Iterator  iterator=map.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry entry=(Map.Entry)iterator.next();
			SoftReference<Bitmap> bmp =(SoftReference<Bitmap>)entry.getValue();
			if(bmp.get()!=null){					
				bmp.get().recycle();
				bmp.clear();
				System.gc();
			}		
		}
		super.onDestroy();
	}
	
}
