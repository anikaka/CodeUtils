package com.tongyan.activity.gps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.R;
import com.tongyan.activity.adapter.GpsSectionProgressAdapter;
import com.tongyan.activity.adapter.GpsSectionRecordAdapter;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.WebServiceUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @className SectionIndexDetailAct
 * @author wanghb
 * @date 2014-7-24 PM 08:06:33
 * @desc 工程获取进度和计量信息
 */
public class SectionIndexDetailAct extends AbstructCommonActivity {
	
	private static Dialog mDialog = null;
	private Context mContext = this;
	private TextView mTitleContent;
	private ListView mListView;
	private _User localUser;
	private MyApplication mApplication;
	
	private String mIsSucc;
	
	private ArrayList<HashMap<String, String>> mIndexList = new ArrayList<HashMap<String, String>>();
	
	private GpsSectionProgressAdapter mProgressAdapter;
	private GpsSectionRecordAdapter mRecordAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_listview_layout);
		showDialog();
		mTitleContent = (TextView)findViewById(R.id.titleContent);
		mListView = (ListView)findViewById(R.id.commont_listview);
		
		mApplication = ((MyApplication)getApplication());
		mApplication.addActivity(this);
		localUser = mApplication.localUser;
		
		Bundle mBundle = getIntent().getExtras();		
		if(mBundle != null) {
			String mIntentType = mBundle.getString("type");
			String mSubId = mBundle.getString("subId");
			if("measure".equals(mIntentType)) {//计量
				mTitleContent.setText("计量详情");
				getRecordInfo(mSubId);
				mRecordAdapter = new GpsSectionRecordAdapter(mContext, mIndexList, R.layout.gps_project_record_info_item);
				mListView.setAdapter(mRecordAdapter);
			} else {//进度
				mTitleContent.setText("进度详情");
				getProgressInfo(mSubId);
				mProgressAdapter = new GpsSectionProgressAdapter(mContext, mIndexList, R.layout.gps_project_progress_info_item);
				mListView.setAdapter(mProgressAdapter);
			}
		}
		
	}
	
	/**
	 * 获取工程清单信息列表
	 */
	public void getRecordInfo(final String mSubId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String params = "{sectionSubItemId:'"+ mSubId +"'}";
				Map<String,Object> mR = null;
				try {
					String str = WebServiceUtils.getRequestStr(localUser.getUsername(), localUser.getPassword(), null, null, "GetBillsInfoList", params, Constansts.METHOD_OF_GETLISTNOPAGE, mContext);
					//GetListNoPageResponse{GetListNoPageResult={"s":"ok","v":{"rows":0,"list":[{"code":"420-1-a-7","lastapprove":0.000000,
					//"sumapprove":0.000000,"remain":51.500000,"total":51.500000,"price":0.0000}]}}; }
					mR = new Str2Json().getGpsRecordListNoPage(str);
					if(mR != null) {
						mIsSucc = (String)mR.get("s");
						if("ok".equals(mIsSucc)) {
							if(mIndexList != null) {
								mIndexList.clear();
							}
							ArrayList<HashMap<String, String>> mList = (ArrayList<HashMap<String, String>>)mR.get("v");
							if(mList != null) {
								mIndexList.addAll(mList);
							}
							sendMessage(Constansts.MES_TYPE_1);
						} else {
							sendMessage(Constansts.ERRER);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}).start();
	}
	/**
	 * 获取进度清单信息列表
	 * @param mSubId
	 */
	public void getProgressInfo(final String mSubId) {
		new Thread(new Runnable(){
			@Override
			public void run() {
				String params = "{rowId:'"+ mSubId +"'}";
				Map<String,Object> mR = null;
				try {
					String str = WebServiceUtils.getRequestStr(localUser.getUsername(), localUser.getPassword(), null, null, "GetProcessInfoList", params, Constansts.METHOD_OF_GETLISTNOPAGE, mContext);
					//GetListNoPageResponse{GetListNoPageResult={"s":"ok","v":{"rows":0,"list":[{"code":"420-1-a-7","SsiName":"钢筋混凝土盖板涵，3m×3.5m",
					//"afterchgnum":51.50,"munit":"m","todayfinishrate":0.00,"todayNum":0.00,"todayfinishrateJE":0.00}]}}; }
					mR = new Str2Json().getGpsProgressListNoPage(str);
					if(mR != null) {
						mIsSucc = (String)mR.get("s");
						if("ok".equals(mIsSucc)) {
							if(mIndexList != null) {
								mIndexList.clear();
							}
							ArrayList<HashMap<String, String>> mList = (ArrayList<HashMap<String, String>>)mR.get("v");
							if(mList != null) {
								mIndexList.addAll(mList);
							}
							sendMessage(Constansts.MES_TYPE_2);
						} else {
							sendMessage(Constansts.ERRER);
						}
					}
				} catch (Exception e) {
					sendMessage(Constansts.CONNECTION_TIMEOUT);
					e.printStackTrace();
				} 
			}
		}).start();
		
	}
	
	public void showDialog() {
		mDialog = new AlertDialog.Builder(mContext).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
	}
	
	public void closeDialog() {
		if(mDialog != null) {
			mDialog.dismiss();
		}
	}
	
	
	@Override
	protected void handleOtherMessage(int index) {
		switch (index) {
		case Constansts.MES_TYPE_1:
			closeDialog();
			if(mRecordAdapter != null) {
				mRecordAdapter.notifyDataSetChanged();
			}
			break;
		case Constansts.MES_TYPE_2:
			closeDialog();
			if(mProgressAdapter != null) {
				mProgressAdapter.notifyDataSetChanged();
			}
			break;
		case Constansts.ERRER:
			closeDialog();
			Toast.makeText(mContext, "加载数据失败", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.CONNECTION_TIMEOUT:
			closeDialog();
			Toast.makeText(mContext, "请求超时", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
	
}
