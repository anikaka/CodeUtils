package com.tongyan.yanan.act.quality;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.SubsideTermpartPactAdapter;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;

/**
 * @category 标段选择界面
 * @author ChenLang
 * @versionYan An 1.0
 * 
 */
public class QualityTermPartPactAct extends FinalActivity {

	@ViewInject (id=R.id.txtTitle_pactselect) TextView mTxtTitle;
	@ViewInject (id=R.id.listView_subside_pactselect) ListView  mListView;
	private Context  mContext=this;
	private String mIntentType = null;
	private SharedPreferences mSP;
	private SubsideTermpartPactAdapter mAdapter;
	private ArrayList<HashMap<String, String>> mArrayListLotNumber = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, String>> mArrayListlotNumberJson=new  ArrayList<HashMap<String,String>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.layout_subside_pactselect);
		mSP=PreferenceManager.getDefaultSharedPreferences(mContext);
		if(getIntent() != null && getIntent().getExtras() != null) {
			mIntentType = getIntent().getExtras().getString("IntentType");
			ArrayList<HashMap<String, String>> mAllList = new DBService(this).getTermPartPact();
			//设置适配器
			mAdapter =new SubsideTermpartPactAdapter(this, mAllList, R.layout.main_subside_paceselect_listview_item,mArrayListLotNumber);
			mListView.setAdapter(mAdapter);
			if("pad_load".equals(mIntentType)){
				refresh();
			}else{
				getLotNumber();
			}
//			refresh();
//			getLotNumber();
			mListView.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					SubsideTermpartPactAdapter.HolderViewPactSelect mHolderView = (SubsideTermpartPactAdapter.HolderViewPactSelect)arg1.getTag();
					if(mHolderView != null && mHolderView.mItemMap != null) {
						/*Intent intent = new Intent(QualityTermPartPactAct.this,SubsidePointAct.class);
						intent.putExtra("monitorProjectTypeName", mMonitorProjectName);
						intent.putExtra("monitorTypeName", mMonitorTypeName);
						intent.putExtra("ItemMap", mHolderView.mItemMap);
						intent.putExtra("TypeMap", map);
						startActivity(intent);*/
						intentNextClazz(mHolderView.mItemMap);
					}
				}
			});
		}
	}
	
	public void  refresh(){
		if("pad_load".equals(mIntentType)){
			mArrayListLotNumber.clear();
			mArrayListlotNumberJson=new DBService(mContext).queryTableStaticLod(mSP.getString(Constants.PREFERENCES_INFO_USERID, ""));
			mArrayListLotNumber.addAll(mArrayListlotNumberJson);
		}else{
			mArrayListLotNumber.clear();
			mArrayListLotNumber.addAll(mArrayListlotNumberJson);
		}
		mArrayListlotNumberJson.clear();
		mAdapter.notifyDataSetChanged();
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
				params.put("typeName", "ysd");
				params.put("typeId", "");
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

	


	public void intentNextClazz(HashMap<String, String> map) {
		Class clazz = null;
		if("hardening_of_ground".equals(mIntentType)) {
			clazz = QualityHardeningAct.class;
		} else if("pad_load".equals(mIntentType)) {
			clazz = QualityStaticLoad.class;
		} else if("wave_testing".equals(mIntentType)) {
			clazz = QualityWaveVelocityListAct.class;
		} else if("inject_testing".equals(mIntentType)) {
			clazz = QualityWaveVelocityListAct.class;
		}
		Intent intent = new Intent(this, clazz);
		intent.putExtra("ItemMap", map);
		if("wave_testing".equals(mIntentType)) {
			intent.putExtra("IntentType", "wave_testing");
		} else if("inject_testing".equals(mIntentType)) {
			intent.putExtra("IntentType", "inject_testing");
		}
		startActivity(intent);
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
