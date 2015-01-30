package com.tongyan.activity.gps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.R;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.db.DBSectionService;
import com.tongyan.common.db.DBService;
import com.tongyan.common.entities._User;
import com.tongyan.service.DownloadBService;
import com.tongyan.service.DownloadBService.IBCallbackResult;
import com.tongyan.service.DownloadBService.MbBinder;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.WebServiceUtils;
import com.tongyan.utils.threadpool.ThreadPoolManager;
import com.tongyan.utils.threadpool.ThreadPoolTask;
import com.tongyan.utils.threadpool.ThreadPoolTask.TaskCallBack;
import com.tongyan.widget.view.AnimationButton;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @className SectionUpdateListAct
 * @author wanghb
 * @date 2014-7-21 AM 11:16:36
 * @Desc 工程标段下载、更新界面
 */
public class GpsSectionUpdateListAct extends AbstructCommonActivity implements TaskCallBack{
	
	private Button mRefreshBtn;
	private ListView mListView;
	
	private static GpsSectionUpdateAdapter mAdapter;
	private Context mContext = this;
	
	private static ArrayList<HashMap<String, String>> mSectionList = new ArrayList<HashMap<String, String>>();
	
	private ThreadPoolManager mThreadPoolManager = null;
	
	private _User mLocalUser;
	private MyApplication mApplication;
	
	private static Dialog mDialog = null;
	
	
	private boolean mIsLoading = false;
	
	private boolean mIsBinderB;
	private MbBinder mBBinder;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gps_section_update_list);
		
		new DBService(mContext).updateALLSectionUpdate("0");
		
		mRefreshBtn = (Button)findViewById(R.id.gps_section_update_all);
		mListView = (ListView)findViewById(R.id.gps_section_update);
		
		mThreadPoolManager = new ThreadPoolManager(ThreadPoolManager.TYPE_FIFO, 3);
		
		mApplication = ((MyApplication)getApplication());
		mApplication.addActivity(this);
		mLocalUser = mApplication.localUser;
		
		mAdapter = new GpsSectionUpdateAdapter(mSectionList, R.layout.gps_section_update_list_item, mThreadPoolManager);
		mListView.setAdapter(mAdapter);
		refreshListView(mContext);
		
		mRefreshBtn.setOnClickListener(mClickListener);
	}
	
	/**
	 * 刷新列表
	 */
	public static void refreshListView(Context mContext) {
		ArrayList<HashMap<String, String>> list = new DBService(mContext).getSectionList();
		if(mSectionList != null) {
			mSectionList.clear();
			if(list != null) {
				mSectionList.addAll(list);
				list = null;
			}
			mAdapter.notifyDataSetChanged();
		}
	}
	
	
	
	private String mSqiltePath = null;
	private String mSqliteVersion = null;
	/**
	 * 
	 */
	OnClickListener mClickListener = new  OnClickListener() {
		@Override
		public void onClick(View v) {
			if(mIsLoading) {
				Toast.makeText(mContext, "正在加载所有项，请耐心等待", Toast.LENGTH_SHORT).show();
				return;
			}
			mIsLoading = true;
			
			/*new DBService(mContext).updateALLSectionUpdateTime(null);
			refreshListView(mContext);
			if(mSectionList != null && mSectionList.size() > 0) {
				mThreadPoolManager.start();
				new DBSectionService().delAll("SectionBaseInfo");
				for(HashMap<String, String> map : mSectionList) {
					if(map != null) {
						String sid = map.get("sid");
						if(mThreadPoolManager != null) {
							mThreadPoolManager.addAsyncTask(new ThreadPoolTask(mContext, mLocalUser.getUsername(), mLocalUser.getPassword(), 0, sid, null, GpsSectionUpdateListAct.this, ThreadPoolTask.REQUEST_COUNT, false) );
							mIsLoading = true;
						} 
					}
				}
			}*/
			
			
			new Thread(new Runnable(){
				@Override
				public void run() {
					try {
					Map<String, String> propert = new HashMap<String, String>();
					propert.put("publicKey", Constansts.PUBLIC_KEY);
					String strJson = WebServiceUtils.requestM(propert, Constansts.METHOD_OF_GETSECTIONITEM, mContext);
					Map<String,String> mR2 = new Str2Json().getNewVersion(strJson,"GetSectionItemResult");
					if(mR2 != null) {
						mSqliteVersion = mR2.get("version");
						mSqiltePath = mR2.get("path");
						sendMessage(Constansts.MES_TYPE_3);
					} else {
						sendMessage(Constansts.ERRER);
					}
				} catch (Exception e) {
					e.printStackTrace();
					sendMessage(Constansts.ERRER);
				}
					
					
				}
				
			}).start();
			
		}
	};
	
	
	
	
	
	
	@Override
	public void onTaskCallBack(int mRequestType, int mPageNum, String mSectionId, View mItemView, TaskCallBack mCallback, int back, boolean mIsLastTimes) {
		if(ThreadPoolTask.REQUEST_COUNT == mRequestType) {//获取总数返回
			
			//Log.i("GpsSectionUpdateListAct", "获取总数返回数:" + back + ",mSectionId:" + mSectionId);
			if(back > 0) {
				int times = back/Constansts.SECTION_PAGE_SIZE;
				if(back % Constansts.SECTION_PAGE_SIZE > 0) {
					times = times + 1;
				}
				boolean isLastTimes = false;
				for(int i = 1; i <= times; i++) {
					if(i == times) {
						isLastTimes = true;
					}
					mThreadPoolManager.addAsyncTask(new ThreadPoolTask(mContext, mLocalUser.getUsername(), mLocalUser.getPassword(), i, mSectionId, mItemView, mCallback, ThreadPoolTask.REQUEST_TASK, isLastTimes));
				}
			} else if(back == -2){//request time out
				if(mItemView != null) {
					((AnimationButton)mItemView).stopAnimation();
					sendMessage(Constansts.CONNECTION_TIMEOUT);
				} else {
					new DBService(mContext).updateSectionUpdateById(mSectionId, "0");
					sendMessage(Constansts.MES_TYPE_1);
				}
			} else if(back == 0){
				if(mItemView != null) {
					((AnimationButton)mItemView).stopAnimation();
					sendMessage(Constansts.MES_TYPE_2);
				} else {
					new DBService(mContext).updateSectionUpdateById(mSectionId, "0");
					sendMessage(Constansts.MES_TYPE_1);
				}
			} 
		} else {//获取单个任务返回
			//Log.i("GpsSectionUpdateListAct", "获取单个任务返回数:" + back + ",mSectionId:" + mSectionId + ",最后1个返回:" + mIsLastTimes);
			if(back == -2 || back == -1) {
				mThreadPoolManager.addAsyncTask(new ThreadPoolTask(mContext, mLocalUser.getUsername(), mLocalUser.getPassword(), mPageNum, mSectionId, mItemView, mCallback, ThreadPoolTask.REQUEST_TASK, true));
			} else {
				if (mIsLastTimes) {
					new DBService(mContext).updateSectionUpdateById(mSectionId, "0");
					new DBService(mContext).updateSectionUpdateTimeById(mSectionId);
					sendMessage(Constansts.MES_TYPE_1);
				} else {
					new DBService(mContext).updateALLSectionUpdate("0");
					if(!new DBService(mContext).getSectionById(mSectionId)) {
						new DBService(mContext).updateSectionUpdateById(mSectionId, "1");
					}
					sendMessage(Constansts.MES_TYPE_1);
				}
			}
		}
	}
	
	
	/**
	 * 更新
	 */
	public static void showDialog(Context context) {
		mDialog = new AlertDialog.Builder(context).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mThreadPoolManager != null && mThreadPoolManager.isRunning()) {
			mThreadPoolManager.stop();
			mThreadPoolManager = null;
		}
		
	}
	public  void closeDialog() {
		if(mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
	}
	
	ServiceConnection connB = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mIsBinderB = false;
		}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBBinder = (MbBinder) service;
			// 开始下载
			mBBinder.addCallback(callbackB);
			mBBinder.start();
		}
	};
	
	private IBCallbackResult callbackB = new IBCallbackResult() {
		@Override
		public void onBackResult(Object result) {
			if ("finish".equals(result)) {
				if(connB != null && mIsBinderB) {
					unbindService(connB);
					connB = null;
					mIsBinderB = false; 
				}
				finish();
				return;
			} else if("error".equals(result)) {
				if(connB != null && mIsBinderB) {
					unbindService(connB);
					connB = null;
					mIsBinderB = false;
				}
			}
		}
	};
	
	
	@Override
	protected void handleOtherMessage(int index) {
		switch (index) {
		case Constansts.SUCCESS:
			closeDialog();
			break;
		case Constansts.ERRER:
			closeDialog();
			break;
		case Constansts.CONNECTION_TIMEOUT:
			Toast.makeText(mContext, "请求超时，请重试", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.MES_TYPE_1:
			refreshListView(mContext);
			break;
		case Constansts.MES_TYPE_2:
			Toast.makeText(mContext, "该标段下无项目工程", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.MES_TYPE_3:
			Intent it = new Intent(mContext, DownloadBService.class);
			it.putExtra("DownloadContent", "标段工程目录数据 正在下载...");
			it.putExtra("DownloadPath", mSqiltePath);
			it.putExtra("DownloadIcon", R.drawable.download_soft);
			it.putExtra("DownloadParams", mSqliteVersion);	
			mIsBinderB = bindService(it, connB, Context.BIND_AUTO_CREATE);
			sendMessage(Constansts.ERRER);
			break;	
		default:
			break;
		}
	}
	
	
	/**
	 * @className GpsSectionUpdateAdapter
	 * @author wanghb
	 * @date 2014-7-21 PM 04:00:22
	 * @Desc TODO
	 */
	public class GpsSectionUpdateAdapter extends BaseAdapter {
		
		private int resource;//绑定条目界面
		private ArrayList<HashMap<String, String>> mSectionList;
		private LayoutInflater inflater;
		private ThreadPoolManager mThreadPoolManager;
		
		public GpsSectionUpdateAdapter(ArrayList<HashMap<String, String>> mSectionList, int resource,ThreadPoolManager mThreadPoolManager) {
			this.mSectionList = mSectionList;
			this.resource = resource;
			inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.mThreadPoolManager = mThreadPoolManager;
		}
		
		@Override
		public int getCount() {
			return mSectionList.size();
		}

		@Override
		public Object getItem(int position) {
			return mSectionList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(resource, null);
				holder = new ViewHolder();
				holder.mSectionName = (TextView) convertView.findViewById(R.id.section_name);
				holder.mUpdateTime = (TextView) convertView.findViewById(R.id.section_update_time);
				holder.mIsUpdateBtn = (AnimationButton) convertView.findViewById(R.id.section_update_btn);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			final HashMap<String, String> map = mSectionList.get(position);
			if(map != null) {
				holder.mSectionName.setText(map.get("sContent"));	
				holder.mUpdateTime.setText(map.get("UpdateTime"));
				if("0".equals(map.get("IsUpdate"))) {
					holder.mIsUpdateBtn.stopAnimation();
				} else {
					holder.mIsUpdateBtn.startAnimation();
				}
				holder.mViewMap = map;
			}
			
			holder.mIsUpdateBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mThreadPoolManager != null && new DBSectionService().deleteSectionBaseInfo(map.get("sid"))) {
						new DBService(mContext).updateALLSectionUpdateTime(map.get("sid"));
						mThreadPoolManager.start();
						mThreadPoolManager.addAsyncTask(new ThreadPoolTask(mContext, mLocalUser.getUsername(), mLocalUser.getPassword(), 0, map.get("sid"), v, GpsSectionUpdateListAct.this, ThreadPoolTask.REQUEST_COUNT, false) );
						((AnimationButton)v).startAnimation();
					} else {
						Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT).show();
					}
				}
			});
			return convertView;
		}
		
		public class ViewHolder {
			public TextView mSectionName;
			public TextView mUpdateTime;
			public AnimationButton mIsUpdateBtn;
			public HashMap<String, String> mViewMap;
		}
	}
	
}
