package com.tongyan.activity.risk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.R;
import com.tongyan.activity.adapter.RiskNoticeAdapter;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.entities._HoleFace;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.WebServiceUtils;
/**
 * 
 * @ClassName P39_RiskHolefaceAct.java
 * @Author wanghb
 * @Date 2013-10-14 am 08:57:31
 * @Desc TODO
 */
public class P39_RiskHolefaceAct extends AbstructCommonActivity {
	
	private Context mContext = this;
	private TextView mNoListTextView;
	private ListView mListView;
	private TextView mImageView;
	private List<_HoleFace> mRiskNoticeList = new ArrayList<_HoleFace>();
	private List<_HoleFace> mRemoteNoticeList = null;
	
	private RiskNoticeAdapter mAdapter;
	
	private MyApplication mApplication;
	private Dialog mDialog;
	private _User mLocalUser;
	private String mIsSucc;
	private String mIntentType;//0:风险展示，1：风险提示，2：监督监理
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessM();
	}
	
	private void initPage() {
		setContentView(R.layout.risk_holeface_list);
		mNoListTextView = (TextView)findViewById(R.id.p39_risk_showing_no_listview);
		mListView = (ListView)findViewById(R.id.p39_risk_showing_listview);
		mImageView = (TextView)findViewById(R.id.p39_tiltle_words);
		
	}
	
	private void setClickListener() {
		mListView.setOnItemClickListener(mItemClickListener);
	}
	
	private void businessM(){
		mApplication = ((MyApplication)getApplication());
		mApplication.addActivity(this);
		mLocalUser = mApplication.localUser;
		
		mAdapter = new RiskNoticeAdapter(this, mRiskNoticeList);
		mListView.setAdapter(mAdapter);
		if(getIntent() != null && getIntent().getExtras() != null) {//from P38_RiskShowingAct
			 getRemoteNotice(getIntent().getExtras().getString("checkid"));
			 mIntentType = getIntent().getExtras().getString("mIntentType");
			 if("0".equals(mIntentType)) {
				 //mImageView.setBackgroundResource(R.drawable.p42_title_image_words);//风险展示
				 mImageView.setText(getResources().getString(R.string.main_risk_showing_text));
			 }
		} else {
			refeshList();
		}
	}
	public void getRemoteNotice(final String checkid) {
		mDialog = new AlertDialog.Builder(this).create();
		mDialog.show();
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		
		 new Thread(new Runnable() {
				@Override
				public void run() {
					String params = "{checkid:'" +checkid +"'}";
					try {
						String str = WebServiceUtils.getRequestStr(mLocalUser.getUsername(), mLocalUser.getPassword(), null, null, "GetRiskList", params, Constansts.METHOD_OF_GETLISTNOPAGE,mContext);
						
						Map<String,Object> mR = new Str2Json().checkHoleList(str);
							if(mR != null) {
								mIsSucc = (String)mR.get("s");
								if("ok".equals(mIsSucc)) {
									mRemoteNoticeList = (List<_HoleFace>)mR.get("v");
									if("1".equals(mIntentType)) {
										String paramsJson = "{emp_id:'" +mLocalUser.getUserid() +"', alarmid:'" + checkid +"'}";
										WebServiceUtils.getRequestStr(mLocalUser.getUsername(), mLocalUser.getPassword(), null, null, "RiskRead", paramsJson, Constansts.METHOD_OF_UPDATE,mContext);//to target the record is read
									}
									sendMessage(Constansts.SUCCESS);
								} else {
									sendMessage(Constansts.ERRER);
								}
							} else {
								sendMessage(Constansts.CONNECTION_TIMEOUT);
							}
					} catch (Exception e) {
						sendMessage(Constansts.CONNECTION_TIMEOUT);
						e.printStackTrace();
					} 
				};
			}).start(); 
	}
	
	public void refeshList() {
		if(mRemoteNoticeList != null) {
			if(mRiskNoticeList != null) {
				mRiskNoticeList.clear();
			}
			mRiskNoticeList.addAll(mRemoteNoticeList);
		}
		mAdapter.notifyDataSetChanged();
		if(mRiskNoticeList == null || mRiskNoticeList.size() == 0) {
			mNoListTextView.setVisibility(View.VISIBLE);
		} else {
			mNoListTextView.setVisibility(View.INVISIBLE);
		}
	}
	
	OnItemClickListener mItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			RiskNoticeAdapter.ViewHolder mViewHolder = (RiskNoticeAdapter.ViewHolder)arg1.getTag();
			Intent intent = new Intent(mContext, P40_RiskDetailAct.class);
			intent.putExtra("checkid", mViewHolder._mHoleFace.get_id());
			intent.putExtra("maincheckid", getIntent().getExtras().getString("checkid"));
			intent.putExtra("position", mViewHolder._mHoleFace.getHole());
			intent.putExtra("mIntentType", mIntentType);
			startActivity(intent);
		}
	};
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			if(mDialog != null)
				mDialog.dismiss();
			refeshList();
			break;
		case Constansts.ERRER:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(mContext, mIsSucc, Toast.LENGTH_SHORT).show();
			break;
		case Constansts.NET_ERROR:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.CONNECTION_TIMEOUT:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(mContext, "连接超时", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
	
}
