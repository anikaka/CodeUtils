package com.tongyan.yanan.act.oa;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.OaContactsSelectorAdapter;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;
/**
 * 
 * @Title: OaContactsSelectAct.java 
 * @author Rubert
 * @date 2014-8-5  PM 03:13:06 
 * @version V1.0 
 * @Description: 人员选择
 */
public class OaContactsSelectAct extends FinalActivity {
	
	@ViewInject(id = R.id.title_common_content)
	TextView mTitleName;
	
	@ViewInject(id = R.id.common_button_container, click = "finishListener")
	RelativeLayout mFinishBtn;
	
	@ViewInject(id = R.id.title_common_sure_view)
	TextView mFinishBtnView;
	
	@ViewInject(id = R.id.listView_data)
	ListView mListView;
	
	private Context mContext = this;
	private static OaContactsSelectorAdapter mAdapter;
	private static ArrayList<HashMap<String, String>> mDataList = new ArrayList<HashMap<String, String>>();
	private static String mDptId = "";
	private SharedPreferences mPreferences;
	private Dialog mDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_listview_layout);
		
		mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		mFinishBtnView.setVisibility(View.VISIBLE);
		mFinishBtn.setVisibility(View.VISIBLE);
		mTitleName.setText("人员选择");
		Bundle mBundle = getIntent().getExtras();
		mDataList.clear();
		if(mBundle != null) {
			mDptId = mBundle.getString("NewId");//f1b17d7b-78e7-483d-9067-d793439b0147
			ArrayList<HashMap<String, String>> list = new DBService(mContext).getContactsList(mDptId, "UserNamePinyin");
			if(list == null || list.size() == 0) {
				getContacts(mDptId);	
			} else {
				mDataList.addAll(list);
			}
		}
		mAdapter = new OaContactsSelectorAdapter(mContext, mDataList);
		mListView.setAdapter(mAdapter);
	}
	
	
	public static void refreshListView(Context context) {
		if(mDataList != null) {
			mDataList.clear();
			ArrayList<HashMap<String, String>> list = new DBService(context).getContactsList(mDptId, "UserNamePinyin");
			if(list != null) {
				mDataList.addAll(list);
			}
		}
		mAdapter.notifyDataSetChanged();
	}
	
	public void getContacts(final String mDptNatureId) {
		mDialog=new Dialog(mContext, R.style.dialog);
		mDialog.setContentView(R.layout.common_normal_progressbar);
		new Thread(new Runnable() {
			@Override
			public void run() {
				HashMap<String, String>  mParams = new HashMap<String, String>();
				 mParams.put("method", Constants.METHOD_OF_GETCONTACTS);
				 mParams.put("key", Constants.PUBLIC_KEY);
				 mParams.put("userId", mPreferences.getString(Constants.PREFERENCES_INFO_USERID, ""));
				 mParams.put("DptNatureId", mDptNatureId);
				 mParams.put("fieldList", "");
				 try{
					String mStrJson = HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_OA, mParams, mContext));
					if(!"".equals(mStrJson) && mStrJson!=null){		
						ArrayList<HashMap<String, String>> list = JsonTools.getContacts(mStrJson);
						if(list != null) {
							if(mDataList != null) {
								mDataList.clear();
								if(list != null && list.size() > 0) {
									if(new DBService(mContext).delContacts(mDptId)) {
										new DBService(mContext).insertContacts(list, mDptId);
									}
									mDataList.addAll(list);
								} 
							}
							sendFMessage(Constants.SUCCESS);
						} else {
							sendFMessage(Constants.ERROR);
						}
					}
				 } catch (Exception e){
					 e.printStackTrace();
					 sendFMessage(Constants.CONNECTION_TIMEOUT);
				 }
			}
		}).start();
	}
	
	public void finishListener(View v) {
		setResult(123);
		finish();
	}
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constants.SUCCESS:
			if (mDialog != null) {
				mDialog.dismiss();
			}
			refreshListView(mContext);
			break;
		case Constants.ERROR:
			if (mDialog != null) {
				mDialog.dismiss();
			}
			Toast.makeText(mContext, "获取数据失败", Toast.LENGTH_SHORT).show();
			break;
		case Constants.CONNECTION_TIMEOUT:
			if (mDialog != null) {
				mDialog.dismiss();
			}
			Toast.makeText(mContext, "访问超时,请检查网络", Toast.LENGTH_SHORT).show();
			break;
		}
	}
}
