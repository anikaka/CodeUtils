package com.tongyan.zhengzhou.act.fragment.proprotectinfo;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.act.adapter.ProtectProDesignFlieAdapter;
import com.tongyan.zhengzhou.common.service.DownloadFileService;
import com.tongyan.zhengzhou.common.service.DownloadFileService.IFileCallbackResult;
import com.tongyan.zhengzhou.common.service.DownloadFileService.MFileBinder;
import com.tongyan.zhengzhou.common.service.DownloadService;
import com.tongyan.zhengzhou.common.service.DownloadService.ICallbackResult;
import com.tongyan.zhengzhou.common.utils.Constants;
import com.tongyan.zhengzhou.common.utils.FileOpen;
import com.tongyan.zhengzhou.common.service.DownloadService.MBinder;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 *设计文件
 * @author ChenLang
 *
 */
public class DesignFileFragment  extends Fragment implements OnItemClickListener{

	private static DesignFileFragment  instance=new DesignFileFragment();
	private ProtectProDesignFlieAdapter mAdapter;
	private ArrayList<HashMap<String, String>> mArrayListFile=new ArrayList<HashMap<String,String>>();
	private ListView mListView;
	private Context mContext;
	
	public static DesignFileFragment getInstance(ArrayList<HashMap<String, String>> arrayList){
		if(arrayList.size()>0){
			if(instance.mArrayListFile.size()>0){
				instance.mArrayListFile.clear();
			}
			instance.mArrayListFile.addAll(arrayList);
		}
		return instance;
}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			 mContext=getActivity();
			 View view=inflater.inflate(R.layout.protectpro_designfile, null, false);
			 mListView=(ListView)view.findViewById(R.id.listViewDesignFlie);
			 mListView.setOnItemClickListener(this);
			 mAdapter=new ProtectProDesignFlieAdapter(getActivity(), mArrayListFile, R.layout.protectpro_designfile_item);
			 mListView.setAdapter(mAdapter);
		return view;
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
			   ProtectProDesignFlieAdapter.ViewHolder  mViewHolder=(ProtectProDesignFlieAdapter.ViewHolder)view.getTag();
			   HashMap<String, String> map=mViewHolder.map;
			   if(map!=null){
				   String fileSuffix = map.get("filePath").substring(map.get("filePath").lastIndexOf("."), map.get("filePath").length());
				   String filePath = Constants.PATH_OF_FILE + map.get("fileName") + fileSuffix;
				   File file=new File(filePath);
				   if(file.exists()){
						try {
							PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo("cn.wps.moffice_eng", 0);
							if(packageInfo==null){
								//下载WPS
								   showDownloadWPSDialog();
							}else{
								//打开文件
								startActivity(FileOpen.getWordFileIntent(file));
							}
						} catch (NameNotFoundException e) {
							showDownloadWPSDialog();
							e.printStackTrace();
						}
				   }else{
					   showDownloadFileDialog(map);
				   }
			   } 
	}
	

	ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mIsBinder = false;
		}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			binder = (MBinder) service;
			// 开始下载
			binder.addCallback(callback);
			binder.start();
		}
	};
	
	private ICallbackResult callback = new ICallbackResult() {
		@Override
		public void onBackResult(Object result) {
			if ("finish".equals(result)) {
				if(conn != null && mIsBinder) {
					//unbindService(conn);
					//conn = null;
				}
				return;
			} else if("error".equals(result)) {
				if(conn != null && mIsBinder) {
					//unbindService(conn);
					//conn = null;
				}
			}
		}
	};
		
	private MBinder binder;
	private boolean mIsBinder;
	
	/** 
	 * 显示WPS应用下载对话框*/
	private  void showDownloadWPSDialog(){
		AlertDialog.Builder alertDialog=new AlertDialog.Builder(mContext);
		alertDialog.setMessage("WPS应用未下载,是否下载?");
		alertDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent  intent=new Intent(getActivity(), DownloadService.class);
				intent.putExtra("filePath", Constants.WPS_PATH);
				intent.putExtra("downloadContent", "WPS下载");
				mContext.bindService(intent, conn, Context.BIND_AUTO_CREATE);
			}
		});
		
		alertDialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		alertDialog.create().show();
	}
	
	private MFileBinder mFileBinder;
	
	ServiceConnection connFile = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mIsBinder = false;
		}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mFileBinder = (MFileBinder) service;
			// 开始下载
			mFileBinder.addCallback(callbackFile);
			mFileBinder.start();
		}
	};
	
	
	private IFileCallbackResult callbackFile = new IFileCallbackResult() {
		@Override
		public void onBackResult(Object result) {
			if ("finish".equals(result)) {
				if(conn != null && mIsBinder) {
					//unbindService(conn);
					//conn = null;
				}
				return;
			} else if("error".equals(result)) {
				if(conn != null && mIsBinder) {
					//unbindService(conn);
					//conn = null;
				}
			}
		}
	};
	
	/**
	 *文件下载对话框提示 */
	private void showDownloadFileDialog(final HashMap<String, String> map){
		AlertDialog.Builder alertDialog=new AlertDialog.Builder(mContext);
		alertDialog.setMessage("文件未下载,是否下载?");
		alertDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent  intent=new Intent(getActivity(), DownloadFileService.class);
				intent.putExtra("filePath", map.get("filePath"));
				intent.putExtra("fileName", map.get("fileName"));
				mContext.bindService(intent, connFile, Context.BIND_AUTO_CREATE);
			}
		});
		
		alertDialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		alertDialog.create().show();
	}
	
}
