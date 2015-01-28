package com.tongyan.yanan.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.act.subside.SubsideTermPartPactAct;
import com.tongyan.yanan.common.adapter.SubsideTypeAdapter;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.tfinal.https.HttpUtils;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * @category沉降界面 
 * @author ChenLang
 * @date   2014/06/18
 * @version YanAn1.0
 * @LastMT  2014/06/27 wanghb
 */
public class SubsideFragement  extends BaseFragement {
	
	private ArrayList<HashMap<String,String>> mSubsideList = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String,String>> mSubsideRemoteList = null;
	private SubsideTypeAdapter mSubsideAdpater;
	private ListView  mListView;
	private SharedPreferences mPreferences;
	private Dialog mDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		getTypeInfo();
		View mView=inflater.inflate(R.layout.layout_subside, null, false);
		mListView = (ListView) mView.findViewById((R.id.listView_subside));
		mSubsideAdpater = new SubsideTypeAdapter(getActivity(), mSubsideList, R.layout.main_subside_fragement_listivew_item);
		mListView.setAdapter(mSubsideAdpater);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
				SubsideTypeAdapter.HolderView mHolderView = (SubsideTypeAdapter.HolderView)view.getTag();
				if(mHolderView != null && mHolderView.mMonitorType != null) {
					Intent intent = new Intent(getActivity(),SubsideTermPartPactAct.class);
					intent.putExtra("MonitorType", mHolderView.mMonitorType);
					//每条子选项Id
					getActivity().startActivity(intent);
				} else {
					sendFragmentMessage(Constants.OPERATION_ERROR);
				}
			}
		});
		return mView;
	}
	
	/**  监测类型数据请求 */
	public void getTypeInfo(){
		//加载进度条
		mDialog=new Dialog(getActivity(), R.style.dialog);
		mDialog.setContentView(R.layout.common_normal_progressbar);
		getActivity().setProgressBarIndeterminateVisibility(true);
		mDialog.show();
		//参数上传
		new Thread(new Runnable() {
			@Override
			public void run() {
				HashMap<String, String> params=new HashMap<String, String>();
				params.put("method", Constants.METHOD_OF_SUBSIDE_TYPE);
				params.put("key", Constants.PUBLIC_KEY);
				params.put("userId", mPreferences.getString(Constants.PREFERENCES_INFO_USERID, ""));
				params.put("fieldList", ""); //fieldList
				try {
					String mResponseBody = HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_SUBSIDE_POINT, params, getActivity()));
					
					mSubsideRemoteList = JsonTools.getTypeInfo(mResponseBody);
					if (mSubsideRemoteList != null && mSubsideRemoteList.size() > 0) {
						sendFragmentMessage(Constants.SUCCESS);
					} else {
						sendFragmentMessage(Constants.ERROR);
					}
				} catch (Exception e) {
					if (e instanceof IOException) {
						sendFragmentMessage(Constants.CONNECTION_TIMEOUT);
					} else if (e instanceof JSONException) {
						sendFragmentMessage(Constants.OPERATION_ERROR);//字符解析失败//TODO
					} else if(e instanceof NullPointerException){
						sendFragmentMessage(Constants.OPERATION_ERROR);//返回数据为空//TODO
					} else {
						sendFragmentMessage(Constants.OPERATION_ERROR);
					}
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	
	
	
	public void refreshListView() {
		
		if(mSubsideRemoteList != null) {
			if(mSubsideList != null) {
				mSubsideList.clear();
			}
			mSubsideList.addAll(mSubsideRemoteList);
		}
		mSubsideAdpater.notifyDataSetChanged();
	}
	
	public void closeMDialog() {
		if(mDialog != null) {
			mDialog.dismiss();
		}
	}
	
	
	@Override
	protected void handleOtherMessage(int index) {
		switch (index) {
		case Constants.SUCCESS :
			closeMDialog();
			refreshListView();
			break;
		case Constants.ERROR : 
			closeMDialog();
			Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
			break;
		case Constants.CONNECTION_TIMEOUT :
			closeMDialog();
			Toast.makeText(getActivity(), "网络连接超时", Toast.LENGTH_SHORT).show();
			break;
		case Constants.OPERATION_ERROR : 
			closeMDialog();
			Toast.makeText(getActivity(), "操作失败", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
	
}
