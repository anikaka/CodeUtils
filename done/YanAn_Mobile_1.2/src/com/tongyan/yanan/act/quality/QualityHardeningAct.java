package com.tongyan.yanan.act.quality;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.QualityHardingAdapter;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;
/**
 * 
 * @Title: QualityHardeningAct.java 
 * @author Rubert
 * @date 2014-8-14 PM 01:21:06 
 * @version V1.0 
 * @Description: 地基土压实度
 */
public class QualityHardeningAct extends FinalActivity {
	
	@ViewInject(id=R.id.listView_data) ListView mListView;
	@ViewInject(id=R.id.title_common_content) TextView mTitleContent;
	
	private Context mContext = this;
	private SharedPreferences mPreferences = null;
	private String mPactId = null;
	
	private QualityHardingAdapter mAdapter = null;
	
	private ArrayList<HashMap<String, String>> mQualityList = new ArrayList<HashMap<String, String>>();
	
	private Dialog mDialog = null;
	
	private HashMap<String, String> mPactMap = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_listview_layout);
		mTitleContent.setText("地基土压实度");
		mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		
		mAdapter = new QualityHardingAdapter(mContext, mQualityList, R.layout.quality_harding_of_ground_list_item );
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(onItemClickListener);
		if(getIntent() != null && getIntent().getExtras() != null) {
			mPactMap = (HashMap<String, String>)getIntent().getExtras().get("ItemMap");
			if(mPactMap != null) {
				mPactId = mPactMap.get("NewId");//合同段编号
				//mPactName=mPactMap.get("LotName");
				//mTxtPactName.setText(mPactName);
				getRemote();
			}
		}
	}
	
	OnItemClickListener onItemClickListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			QualityHardingAdapter.HolderView mHolderView = (QualityHardingAdapter.HolderView)arg1.getTag();
			HashMap<String, String> map = mHolderView.mItemData;
			if(map != null) {
				if("title".equals(map.get("attribute"))) {
					getRemoteById(map.get("NewId"));
				} else {
					Intent intent = new Intent(mContext, QualityHardingListAct.class);
					intent.putExtra("PactMap", mPactMap);
					intent.putExtra("QualityMap", map);
					startActivity(intent);
				}
			}
		}
	};
	
	
	
	
	public void getRemote() {
		openDialog();
		new Thread(new Runnable(){
			@Override
			public void run() {
				HashMap<String, String>  mParams = new HashMap<String, String>();
				 mParams.put("method", Constants.METHOD_OF_GETDETECTIONAREA);
				 mParams.put("key", Constants.PUBLIC_KEY);
				 mParams.put("userId", mPreferences.getString(Constants.PREFERENCES_INFO_USERID, ""));
				 mParams.put("LotId", mPactId);
				 try{
					String mStrJson = HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_QUALITY, mParams, mContext));
					ArrayList<HashMap<String, String>> list = JsonTools.getDetectionArea(mStrJson);
					if(list != null && list.size() > 0) {
						if(mQualityList != null) {
							mQualityList.clear();
							mQualityList.addAll(list);
						}
						sendFMessage(Constants.SUCCESS);
					} else {
						sendFMessage(Constants.ERROR);
					}
				 } catch(Exception e) {
					 e.printStackTrace();
					 sendFMessage(Constants.CONNECTION_TIMEOUT);
				 }
			}
			
		}).start();
		
	}
	
	public void getRemoteById(final String mAreaChangesId) {
		openDialog();
		new Thread(new Runnable(){
			@Override
			public void run() {
				HashMap<String, String>  mParams = new HashMap<String, String>();
				 mParams.put("method", Constants.METHOD_OF_GETDETECTIONAREABYID);
				 mParams.put("key", Constants.PUBLIC_KEY);
				 mParams.put("userId", mPreferences.getString(Constants.PREFERENCES_INFO_USERID, ""));
				 mParams.put("AreaChangesId", mAreaChangesId);
				 try{
					String mStrJson = HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_QUALITY, mParams, mContext));
					ArrayList<HashMap<String, String>> list = JsonTools.getDetectionSubArea(mStrJson);
					if(list != null && list.size() > 0) {
						ArrayList<HashMap<String, String>> mCacheList = new ArrayList<HashMap<String, String>>();
						if(mQualityList != null && mQualityList.size() > 0) {
							for(HashMap<String, String> m : mQualityList) {
								if(m != null) {
									if("title".equals(m.get("attribute"))) {
										mCacheList.add(m);
										if(mAreaChangesId.equals(m.get("NewId"))) {
											for(HashMap<String, String> ms : list) {
												if(ms != null) {
													mCacheList.add(ms);
												}
											}
										}
									}
								}
							}
							mQualityList.clear();
							mQualityList.addAll(mCacheList);
							sendFMessage(Constants.SUCCESS);
						}
					} else{
						sendFMessage(Constants.ERROR);
					}
				 } catch(Exception e) {
					 e.printStackTrace();
					 sendFMessage(Constants.CONNECTION_TIMEOUT);
				 }
			}
			
		}).start();
		
	}
	
	
	public void openDialog() {
		mDialog=new Dialog(mContext, R.style.dialog);
		mDialog.setContentView(R.layout.common_normal_progressbar);
	}
	
	public void closeDialog() {
		if(mDialog!=null){				
			mDialog.dismiss();
		}
	}
	
	@Override
	protected void handleOtherMessage(int flag) {
		 switch(flag){
			case Constants.SUCCESS:
				closeDialog();
				mAdapter.notifyDataSetChanged();
				break;
			case Constants.ERROR:
				closeDialog();
				Toast.makeText(mContext, "获取数据失败", Toast.LENGTH_SHORT).show();
				break;
			case Constants.CONNECTION_TIMEOUT:
				closeDialog();
				Toast.makeText(mContext, "访问超时,请检查网络", Toast.LENGTH_SHORT).show();
				break;
			}
	}
	
	
}
