package com.tongyan.activity.gps;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.MainAct;
import com.tongyan.activity.R;
import com.tongyan.activity.adapter.GpsEmpStateAdapter;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.entities._LocationInfo;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.MDialog;
import com.tongyan.utils.WebServiceUtils;
import com.tongyan.widget.pullrefresh.PullToRefreshListView;
import com.tongyan.widget.pullrefresh.PullToRefreshBase.OnRefreshListener;
/**
 * 
 * @ClassName P04_GpsEmpListAct.java
 * @Author wanghb
 * @Date 2013-9-23 pm 01:37:06
 * @Desc 在岗查询列表
 */
public class GpsSearchEmpListAct extends AbstructCommonActivity {
	
	private Button mHomeBtn;
	private Context mContext = this;
	private String mTimeHide,mDistance,mProjectId,mProjectName,mSectionName,mItemName;
	private TextView mDescTextView;
	private ListView mListView;
	private PullToRefreshListView mPullRefreshListView;
	
	private _User mLocalUser;
	private String mIsSucc;
	private LinkedList<_LocationInfo> mLocationInfoList = new LinkedList<_LocationInfo>();
	private int mPage = 0;
	private Dialog mDialog;
	private GetDataTask mGetDataTask;
	
	private GpsEmpStateAdapter mP04GpsEmpStateAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessM();
	}
	
	private void initPage() {
		setContentView(R.layout.gps_emp_list_state);
		mHomeBtn = (Button)findViewById(R.id.p04_gps_station_list_home_btn);
		mDescTextView = (TextView)findViewById(R.id.p04_gps_emp_list_description);
		mPullRefreshListView = (PullToRefreshListView)findViewById(R.id.p04_gps_emp_list);
		mListView = mPullRefreshListView.getRefreshableView();
	}
	
	private void setClickListener() {
		mHomeBtn.setOnClickListener(homeBtnListener);
		mListView.setOnItemClickListener(mOnItemListener);
		mPullRefreshListView.setOnRefreshListener(frshListener);
	}
	
	private void businessM(){
		MyApplication mApplication = ((MyApplication)getApplication());
		mApplication.addActivity(this);
		mLocalUser = mApplication.localUser;
		
		if(getIntent() != null && getIntent().getExtras() != null) {
			mTimeHide = (String) getIntent().getExtras().get("mTimeHide");
			mItemName = (String) getIntent().getExtras().get("mItemName");
			mSectionName = (String) getIntent().getExtras().get("mSectionName");
			mProjectId = (String) getIntent().getExtras().get("mProjectId");
			mProjectName = (String) getIntent().getExtras().get("mProjectName");
			mDistance = (String) getIntent().getExtras().get("mDistance");
		}
		//mSectionName = mSectionName.replaceAll("\\[", "").replaceAll("\\]", "");
		mDescTextView.setText(mTimeHide + "  " + mItemName + "项目" + mSectionName + "标段" + mProjectName + "工程");
		
		mP04GpsEmpStateAdapter = new GpsEmpStateAdapter(this, mLocationInfoList);
		mListView.setAdapter(mP04GpsEmpStateAdapter);
		loadingData();
	}
	
	public void loadingData() {
		mDialog = new AlertDialog.Builder(this).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		mGetDataTask = new GetDataTask();
		mGetDataTask.execute();
	}
	int mPullType;
	private class GetDataTask extends AsyncTask<Void, Void, List<_LocationInfo>> {
		
		public GetDataTask() {
			
		}
		public GetDataTask(int pullType) {
			mPullType = pullType;
		}
        @Override
        protected List<_LocationInfo> doInBackground(Void... params) {
        	if(mPullType == 0) {
            	if(mLocationInfoList != null) {
            		mLocationInfoList.clear();
            	}
            	mPage = 1;
            } 
            if(mPullType == 1) {//向下拉刷新数据
            	if(mLocationInfoList != null) {
            		mLocationInfoList.clear();
            	}
            	mPage = 1;
            } 
            if(mPullType == 2) {//向上拉刷新数据
            	mPage = mPage + 1;
            } 
			String paramsStr = "{date:'"+mTimeHide+"',unit_id:'"+ mProjectId + "',distance:'" + mDistance +"'}";
			try {
				//Log.i(TAG, "paramsStr" + paramsStr);
				String jsonStr = WebServiceUtils.getRequestStr(mLocalUser.getUsername(), mLocalUser.getPassword(), String.valueOf(Constansts.PAGE_SIZE), String.valueOf(mPage), "GetEmpAttendance", paramsStr, Constansts.METHOD_OF_GETLIST,mContext);
				//Log.i(TAG, jsonStr);
				Map<String,Object> mR = new Str2Json().getEmpStateList(jsonStr);
				if(mR != null) {
					mIsSucc = (String)mR.get("s");
					if("ok".equals(mIsSucc)) {
						List<_LocationInfo> mLocInfoR = (List<_LocationInfo>)mR.get("v");
						if(mLocInfoR ==null || mLocInfoR.size() == 0) {
							sendMessage(Constansts.NO_DATA);
						}
						return mLocInfoR;
					} else {
						sendMessage(Constansts.ERRER);
					}
				} else {
					sendMessage(Constansts.NET_ERROR);
				}
			}  catch (Exception e) {
				sendMessage(Constansts.CONNECTION_TIMEOUT);
				e.printStackTrace();
			}
            return null;
        }

        @Override
        protected void onPostExecute(List<_LocationInfo> result) {
        	if(result != null && result.size() > 0) {
        		for(int i = 0; i < result.size(); i ++) {
        			mLocationInfoList.addLast(result.get(i));
        		}
        	}
        	if(mDialog != null)
				mDialog.dismiss();
        	if( mPullType == 0) {
        		if(mLocationInfoList == null || mLocationInfoList.size() == 0) {
        			showDialog();
        		}
        		mP04GpsEmpStateAdapter.notifyDataSetChanged();
        	} else {
        		mPullRefreshListView.onRefreshComplete();
        	}
        }
        
    }
	
	public void showDialog() {
		final MDialog dialogCategory = new MDialog(mContext, R.style.dialog);
		dialogCategory.createDialog(R.layout.gps_nolist_dialog, 0.95, 0.3, getWindowManager());
		Button mIKnow = (Button)dialogCategory.findViewById(R.id.p04_gps_nolist_btn);
		
		mIKnow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(dialogCategory != null) {
					dialogCategory.dismiss();
				}
				finish();
			}
		});
	}
	
	//=========================================================
	// part of listener
	//=========================================================
	//下拉刷新
	OnRefreshListener frshListener = new OnRefreshListener(){
		@Override
		public void onRefresh() {
			if(mGetDataTask == null) {
				mGetDataTask = new GetDataTask(mPullRefreshListView.getRefreshType());
			} else {
				if(!mGetDataTask.isCancelled())
					mGetDataTask.cancel(true);
				mGetDataTask = new GetDataTask(mPullRefreshListView.getRefreshType());
			}
			mGetDataTask.execute();
		}
	};
	
	OnItemClickListener mOnItemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			GpsEmpStateAdapter.ViewHolder holder = (GpsEmpStateAdapter.ViewHolder)arg1.getTag();
			if(holder != null) {
				if(null != holder.mLocatinInfo && !"0".equals(holder.mLocatinInfo.getNum())) {
					Intent intent = new Intent(mContext,GpsEmpResultAct.class);
					intent.putExtra("mTimeHide", mTimeHide);
					intent.putExtra("mEmpId", holder.mLocatinInfo.getEmp_id());
					startActivity(intent);
				} 
			}
		}
	};
	
	OnClickListener homeBtnListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(mContext,MainAct.class);
			startActivity(intent);
		}
	};
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			if(mDialog != null)
				mDialog.dismiss();
			break;
		case Constansts.ERRER:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, mIsSucc, Toast.LENGTH_SHORT).show();
			break;
		case Constansts.NET_ERROR:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.NO_DATA:
			if(mDialog != null)
				mDialog.dismiss();
			if(mPage != 1) 
				Toast.makeText(this, "无最新数据", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.CONNECTION_TIMEOUT :
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, "网络连接超时", Toast.LENGTH_SHORT).show();
			break;
		default:
			if(mDialog != null)
				mDialog.dismiss();
			break;
		}
	}
	
}
