package com.tongyan.yanan.act.oa;

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
import com.tongyan.yanan.common.adapter.CommonSingleLineListAdapter;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;
/**
 * 
 * @className OaContactsMenuAct
 * @author wanghb
 * @date 2014-7-29 PM 02:01:12
 * @Desc TODO
 */
public class OaContactsMenuAct extends FinalActivity {
	
	@ViewInject(id=R.id.title_common_content) TextView mTitleContent;
	@ViewInject(id=R.id.listView_data) ListView mListView;
	
	private Dialog mDialog = null;
	private Context mContext = this;
	private SharedPreferences mPreferences;
	private String mUserid = null;
	private CommonSingleLineListAdapter mAdapter;
	private ArrayList<HashMap<String, String>> mDataList = new ArrayList<HashMap<String, String>>();
	private String mIntentType = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		mUserid = mPreferences.getString(Constants.PREFERENCES_INFO_USERID, "");
		ArrayList<HashMap<String, String>> list = new DBService(mContext).getDepartmentNatureList(mUserid);
		if(list == null || list.size() == 0) {
			getDptNature();
		} else {
			mDataList.addAll(list);
		}
		setContentView(R.layout.common_listview_layout);
		mTitleContent.setText("单位性质");
		mListView.setOnItemClickListener(mItemClickListener);
		mAdapter = new CommonSingleLineListAdapter(mContext, mDataList, R.layout.common_single_line_listview_item);
		mListView.setAdapter(mAdapter);
		Bundle mBundle = getIntent().getExtras();
		if(mBundle != null) {
			mIntentType = mBundle.getString("IntentType");//OaScheduleAddAct, 
		}
	}
	
	OnItemClickListener mItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			CommonSingleLineListAdapter.HolderView mHolderView = (CommonSingleLineListAdapter.HolderView)arg1.getTag();
			if(mHolderView != null) {
				HashMap<String, String> map = mHolderView.mMap;
				if(map != null) {
					Intent intent = null;
					if("OaScheduleAddAct".equals(mIntentType)) {
						intent = new Intent(mContext, OaContactsSelectAct.class);
						intent.putExtra("NewId", map.get("NewId"));
						startActivityForResult(intent, 1234);
					} else {
						intent = new Intent(mContext, OaContactsAct.class);
						intent.putExtra("NewId", map.get("NewId"));
						startActivity(intent);
					}
					
				}
			}
		}
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1234) {
			if(resultCode == 123) {
				setResult(9897);
				finish();
			} 
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	public void getDptNature() {
		mDialog=new Dialog(mContext, R.style.dialog);
		mDialog.show();
		mDialog.setContentView(R.layout.common_normal_progressbar);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				HashMap<String, String>  mParams=new HashMap<String, String>();
				 mParams.put("method", Constants.METHOD_OF_GETDPTNATURE);
				 mParams.put("key", Constants.PUBLIC_KEY);
				 mParams.put("userId", mUserid);
				 mParams.put("fieldList", "");
				 try{
					String mStrJson = HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_OA, mParams, mContext));
					//{"s":"ok","v":[
					//{"NewId":"5cb02d35-5375-4539-9f3d-7b2a3d8e828d","SysId":null,"DType":"单位性质","DName":"业主单位","DValue":"1","DSort":1},
					//{"NewId":"f1b17d7b-78e7-483d-9067-d793439b0147","SysId":null,"DType":"单位性质","DName":"监理单位","DValue":"2","DSort":2},
					//{"NewId":"89650402-e317-4f5d-b29b-d7368f7e7673","SysId":null,"DType":"单位性质","DName":"施工单位","DValue":"3","DSort":3},
					//{"NewId":"85546b84-03b6-4764-8ac2-70f21f4fe41d","SysId":null,"DType":"单位性质","DName":"测量单位","DValue":"4","DSort":4},
					//{"NewId":"5db750bd-1b1f-4cad-9f1c-bad91650aad5","SysId":null,"DType":"单位性质","DName":"检测单位","DValue":"5","DSort":5}]}
					if(!"".equals(mStrJson) && mStrJson!=null){		
						ArrayList<HashMap<String, String>> list = JsonTools.getDptNature(mStrJson);
						if(list != null) {
							if(mDataList != null) {
								mDataList.clear();
								if(list != null && list.size() > 0) {
									if(new DBService(mContext).delDepartmentNature(mUserid)) {
										new DBService(mContext).insertDepartmentNature(list);
									}
									mDataList.addAll(list);
								}
							}
							sendFMessage(Constants.SUCCESS);
						} else {
							sendFMessage(Constants.ERROR);
						}
					}
				 }catch(Exception e){
					 e.printStackTrace();
					 sendFMessage(Constants.CONNECTION_TIMEOUT);
				 }
				
			}
		}).start();
	}
	
	
	protected void handleOtherMessage(int flag) {
		switch(flag){
		case Constants.SUCCESS:
				if(mDialog!=null){				
					mDialog.dismiss();
				}
				mAdapter.notifyDataSetChanged();
			break;
		case Constants.ERROR:
			if(mDialog!=null){				
				mDialog.dismiss();
			}
			Toast.makeText(mContext, "获取数据失败", Toast.LENGTH_SHORT).show();
			break;
		case Constants.CONNECTION_TIMEOUT:
			if(mDialog!=null){				
				mDialog.dismiss();
			}
			Toast.makeText(mContext, "访问超时,请检查网络", Toast.LENGTH_SHORT).show();
			break;
		}
	};
	
	
	
}	
