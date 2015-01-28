package com.tongyan.yanan.act.pic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.PicOriginalListViewAdapter;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;

/**
 * 原地貌
 * @author ChenLang
 */

public class PicOriginalAct  extends FinalActivity implements OnItemClickListener{
	
	//标题
	@ViewInject(id=R.id.txtTitle_pic)
	TextView mTxtTitle;
	//列表
	@ViewInject(id=R.id.listView_pic_original)
	ListView mListView;
	
	private Context mContext=this;
	private SharedPreferences mSP;
	private  ArrayList<HashMap<String, String>>  mArrayListOriginalInfoJson=new ArrayList<HashMap<String,String>>();
	private  ArrayList<HashMap<String, String>>  mArrayListOriginalInfo=new ArrayList<HashMap<String,String>>();
	private PicOriginalListViewAdapter mAdapter;
	private String mType;//0 原地貌 1
	private Bundle mBundle;
	private Dialog mProgressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pic_original);
		mSP=PreferenceManager.getDefaultSharedPreferences(mContext);
		//构建适配器
		mAdapter=new PicOriginalListViewAdapter(mContext, mArrayListOriginalInfo, R.layout.pic_original_listview_item);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		if(getIntent().getExtras()!=null){
			mBundle=getIntent().getExtras();
			mType=mBundle.getString("type");
			if("0".equals(mType)){
				mTxtTitle.setText("原地貌");
			}
		}
		//圆形进度条
		mProgressBar=new Dialog(mContext,R.style.dialog);
		mProgressBar.setContentView(R.layout.common_normal_progressbar);
		mProgressBar.show();
		requestOriginalInfo();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		PicOriginalListViewAdapter.ViewHoderPic  mViewHolder=(PicOriginalListViewAdapter.ViewHoderPic)view.getTag();
		HashMap<String, String> mMap=mViewHolder.mMapPic;
		if(mMap.size()>0){
			Intent intent=new Intent(mContext,PicPactAct.class);
			mBundle.putString("newId", mMap.get("newId"));
			mBundle.putString("displayName", mMap.get("displayName"));
			intent.putExtras(mBundle);
			startActivity(intent);
		}
 	}
	
	/** 原地貌类型数据请求*/
	public void  requestOriginalInfo(){
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				HashMap<String, String> params=new HashMap<String, String>();
				params.put("method", Constants.METHOD_OF_ALLORIGINAL);
				params.put("key", Constants.PUBLIC_KEY);
				params.put("userId", mSP.getString(Constants.PREFERENCES_INFO_USERID, ""));
				params.put("fieldList", "");
				String mStream=null;
				try {
					mStream=HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_ORIGINAL, params, mContext));
					mArrayListOriginalInfoJson=JsonTools.getAllOriginal(mStream);
					if(mArrayListOriginalInfoJson.size()>0){
						sendFMessage(Constants.SUCCESS);
					}else{
						sendFMessage(Constants.COMMON_MESSAGE_1);
					}
				} catch (IOException e) {
					sendFMessage(Constants.ERROR);
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void refershData(){
		if(mArrayListOriginalInfoJson.size()>0){
			mArrayListOriginalInfo.addAll(mArrayListOriginalInfoJson);
			mArrayListOriginalInfoJson.clear();
		}
		mAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void handleOtherMessage(int flag) {
		super.handleOtherMessage(flag);
		switch(flag){
		 case Constants.SUCCESS:
			 refershData();
			 mProgressBar.cancel();
			 break;
		 case Constants.ERROR:
			 Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
			 mProgressBar.cancel();
			 break;
		 case Constants.COMMON_MESSAGE_1:
			 Toast.makeText(mContext, "无数据显示", Toast.LENGTH_SHORT).show();
			 mProgressBar.cancel();
			 break;
		}
	}
	
}
