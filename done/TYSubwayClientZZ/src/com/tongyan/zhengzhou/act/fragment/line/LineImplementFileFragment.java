package com.tongyan.zhengzhou.act.fragment.line;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.act.adapter.ProtectProDesignFlieAdapter;
import com.tongyan.zhengzhou.common.service.DownloadFileService;
import com.tongyan.zhengzhou.common.service.DownloadService;
import com.tongyan.zhengzhou.common.service.DownloadFileService.IFileCallbackResult;
import com.tongyan.zhengzhou.common.service.DownloadFileService.MFileBinder;
import com.tongyan.zhengzhou.common.service.DownloadService.ICallbackResult;
import com.tongyan.zhengzhou.common.service.DownloadService.MBinder;
import com.tongyan.zhengzhou.common.utils.Constants;
import com.tongyan.zhengzhou.common.utils.FileOpen;
import com.tongyan.zhengzhou.common.utils.JSONParseUtils;
import com.tongyan.zhengzhou.common.utils.WebServiceUtils;
import com.tongyan.zhengzhou.common.widgets.widgetspullrefresh.PullToRefreshListView;
import com.tongyan.zhengzhou.common.widgets.widgetspullrefresh.PullToRefreshBase.OnRefreshListener;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 实施文件
 * @author ChenLang
 *
 */
public class LineImplementFileFragment extends Fragment implements OnItemClickListener{

	private static LineImplementFileFragment sInstance=new LineImplementFileFragment();

	private String mNodeCode;
	private int mNodeLevel;
	private Context mContext;
	private ArrayList<HashMap<String, String>>  mArrayList=new ArrayList<HashMap<String,String>>();
	private int   mCurrentPage; //当前页数
	private PullToRefreshListView mPullToRefreshListView;
	private ListView mListView;
	private MyAsnycTask  mMyAsnycTask;
	private ProtectProDesignFlieAdapter  mAdapter;
	
	public static LineImplementFileFragment getInstance(String nodeCode,int nodeLevel){
		sInstance.mNodeCode=nodeCode;
		sInstance.mNodeLevel=nodeLevel;
		return sInstance;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			View view=inflater.inflate(R.layout.line_designfile, null,false);
			mContext=getActivity();
			mPullToRefreshListView=(PullToRefreshListView)view.findViewById(R.id.listViewLineDesignFile);
			mListView=mPullToRefreshListView.getRefreshableView();
			mPullToRefreshListView.setOnRefreshListener(onRefreshListener);
			mMyAsnycTask=new MyAsnycTask();
			mMyAsnycTask.execute();
			mAdapter=new ProtectProDesignFlieAdapter(mContext, mArrayList, R.layout.protectpro_designfile_item);
			mListView.setAdapter(mAdapter);
			return view;
	}
	
	
	OnRefreshListener onRefreshListener=new OnRefreshListener() {

		@Override
		public void onRefresh() {
			if(mMyAsnycTask==null){
				mMyAsnycTask=new MyAsnycTask(mPullToRefreshListView.getRefreshType());
			}else{
				if(!mMyAsnycTask.isCancelled())
					mMyAsnycTask.cancel(true);
			}
			mMyAsnycTask=new MyAsnycTask(mPullToRefreshListView.getRefreshType());
			mMyAsnycTask.execute();
		}
	};

	
	
	public class  MyAsnycTask extends  AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>{
		
		private int pullType;
		public MyAsnycTask(){
		}
		
		public MyAsnycTask(int pullType){
			this.pullType=pullType;
		}
		
		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(
				Void... params) {
			if(pullType == 0) {
				mCurrentPage = 1;
				if(mArrayList.size()>0){
					mArrayList.clear();
				}
			} 
			if(pullType == 1) {
				mCurrentPage = 1;
				if(mArrayList.size()>0){
					mArrayList.clear();
				}
			} 
			if(pullType == 2) {//向上
				mCurrentPage ++;
			} 
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("metroCode ", mNodeCode);
			map.put("documentType ", "1");
			map.put("metroLevel ", String.valueOf(mNodeLevel)); 
			map.put("currentPage ", String.valueOf(mCurrentPage));
			map.put("pageCount ", "15");
			String  stream=null;
			try {
				stream=WebServiceUtils.requestM(mContext, map, Constants.METHOD_OF_CLIENT_GETRELATEDFILES);
				if(stream!=null){
					return new JSONParseUtils().getLineFiles(stream);
				}
			}catch(Exception e){
				e.printStackTrace();
				handler.sendEmptyMessage(Constants.CONNECTION_TIMEOUT);
			}
			return null;
	}

	@Override
	protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
		if(result != null && result.size() > 0) {
			mArrayList.addAll(result);
			if( pullType == 0) {
        		mAdapter.notifyDataSetChanged();
        	} else {
        		mPullToRefreshListView.onRefreshComplete();
        	}
		}else{
			mPullToRefreshListView.onRefreshComplete();
			mAdapter.notifyDataSetChanged();
		}
		super.onPostExecute(result);
		}
	}
	
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
	private boolean mIsBinder;
	private MBinder binder;
	
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



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		   ProtectProDesignFlieAdapter.ViewHolder  mViewHolder=(ProtectProDesignFlieAdapter.ViewHolder)view.getTag();
		   HashMap<String, String> map=mViewHolder.map;
		   if(map!=null){
			   String fileSuffix=map.get("filePath").substring(map.get("filePath").lastIndexOf("."),map.get("filePath").length());
			   String filePath=Constants.PATH_OF_FILE+ map.get("fileName")+fileSuffix;
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
						e.printStackTrace();
					}
			   }else{
				   showDownloadFileDialog(map);
			   }
		   } 
	}
	
	private Handler handler=new Handler(){
		 public void handleMessage(android.os.Message msg) {
			 switch(msg.what){
			 case  Constants.CONNECTION_TIMEOUT:
				 Toast.makeText(mContext,"网络连接超时",Toast.LENGTH_SHORT).show();
				 break;
			 }
		 };	
		};
}
