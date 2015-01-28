package com.tongyan.yanan.act.subside;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;

import android.R.color;
import android.R.style;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.act.R.id;
import com.tongyan.yanan.act.R.layout;
import com.tongyan.yanan.common.adapter.SubsideTermpartPactAdapter;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.http.AsyncHttpClient;
import com.tongyan.yanan.tfinal.http.AsyncHttpResponseHandler;
import com.tongyan.yanan.tfinal.http.RequestParams;
import com.tongyan.yanan.tfinal.https.HttpUtils;

/**
 * @category标段选择界面
 * @author ChenLang
 * @versionYan An 1.0
 * 
 */
public class SubsideTermPartPactAct extends FinalActivity {

	@ViewInject (id=R.id.txtTitle_pactselect) TextView mTxtTitle;
	@ViewInject (id=R.id.listView_subside_pactselect) ListView  mListView;
	
	private String mMonitorTypeName;
	private String mMonitorProjectName;
    private SubsideTermpartPactAdapter  mSubsidePactSelectListViewAdapter;
    private String mTypeId;// 子选项Id用来获取每个合同段的信息条数
    private Context mContext=this;
    private SharedPreferences mSP;
    Dialog  mDialog;
    private ArrayList<HashMap<String, String>> mArrayListLotNumber=new ArrayList<HashMap<String,String>>();
    private ArrayList<HashMap<String, String>> mArrayListlotNumberJson=new ArrayList<HashMap<String,String>>();
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.layout_subside_pactselect);
		mSP=PreferenceManager.getDefaultSharedPreferences(mContext);
		//进度条
		mDialog=new Dialog(mContext, R.style.dialog);
		mDialog.setContentView(R.layout.common_normal_progressbar);
		setProgressBarIndeterminateVisibility(true);
		mDialog.show();
		if(getIntent() != null && getIntent().getExtras() != null) {
			final HashMap<String, String> map = (HashMap<String, String>)getIntent().getExtras().get("MonitorType");
			if(map != null) {
				mMonitorTypeName=map.get("MonitorTypeName");
				mMonitorProjectName=map.get("monitorProjectTypeName");
				mTypeId=map.get("NewId");
				mTxtTitle.setText(mMonitorTypeName);
			}
			getLotNumber();
			ArrayList<HashMap<String, String>> mAllList = new DBService(this).getTermPartPact();
			//设置适配器
			mSubsidePactSelectListViewAdapter =new SubsideTermpartPactAdapter(mContext, mAllList, R.layout.main_subside_paceselect_listview_item,
					mArrayListLotNumber);
			mListView.setAdapter(mSubsidePactSelectListViewAdapter);
			mDialog.cancel();
			mListView.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					SubsideTermpartPactAdapter.HolderViewPactSelect mHolderView = (SubsideTermpartPactAdapter.HolderViewPactSelect)arg1.getTag();
					if(mHolderView != null && mHolderView.mItemMap != null) {
						Intent intent = new Intent(SubsideTermPartPactAct.this,SubsidePointAct.class);
						intent.putExtra("monitorProjectTypeName", mMonitorProjectName);
						intent.putExtra("monitorTypeName", mMonitorTypeName);
						intent.putExtra("ItemMap", mHolderView.mItemMap);
						intent.putExtra("TypeMap", map);
						startActivity(intent);
					}
				}
			});
		}
	}
	
	/** 获取每个合同段信息的数目*/
	public void getLotNumber(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HashMap<String, String> params=new HashMap<String, String>();
				params.put("method", Constants.METHOD_OF_LOTNUMBER);
				params.put("key", Constants.PUBLIC_KEY);
				params.put("userId", mSP.getString(Constants.PREFERENCES_INFO_USERID, ""));
				params.put("typeName", "cjjc");
				params.put("typeId", mTypeId);
				String mStream="";
				try {
					mStream=HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_PACTSELECT, params, mContext));
					if(!"".equals(mStream)){
						mArrayListlotNumberJson=JsonTools.getLotNumber(mStream);
						sendFMessage(Constants.SUCCESS);
					}else{

					}
				} catch (IOException e) {
					sendFMessage(Constants.ERROR);
					e.printStackTrace();
				}
			}
		}).start();
	}
	public void refresh(){
		if(mArrayListlotNumberJson.size()>0){
			mArrayListLotNumber.clear();
			mArrayListLotNumber.addAll(mArrayListlotNumberJson);
			mArrayListlotNumberJson.clear();
		}
		mSubsidePactSelectListViewAdapter.notifyDataSetChanged();
	}
	
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constants.ERROR:
			Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constants.SUCCESS:
				refresh();
			break;
		default:
			break;
		}
	};

}
