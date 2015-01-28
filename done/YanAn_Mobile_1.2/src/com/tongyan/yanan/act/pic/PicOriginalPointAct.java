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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.PicOriginalPointAdapter;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;

/**
 * 原地貌点
 * @author ChenLang
 */

public class PicOriginalPointAct  extends FinalActivity implements OnItemClickListener{
 
	//标题
	@ViewInject(id=R.id.txtTitle_picOriginal_point)
	TextView  mTxtTitle;
	//点列表
	@ViewInject(id=R.id.listView_picPoint)
	ListView mListView;
	
	private Context mContext=this;
	private SharedPreferences mSP;
	private String mNewId; //类型Id
	private Bundle mBundle;
	private String mLotId;//合同段Id
	private ArrayList<HashMap<String, String>> mArraylist=new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String, String>>  mArrayListJson=new ArrayList<HashMap<String,String>>();
	private PicOriginalPointAdapter mAdapter;
	private  Dialog mProgressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pic_original_point);
		
		mSP=PreferenceManager.getDefaultSharedPreferences(mContext);
		//构造适配器
		mAdapter=new PicOriginalPointAdapter(mContext, mArraylist,R.layout.pic_original_point_listview_item);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		if(getIntent().getExtras()!=null){
			mBundle=getIntent().getExtras();
			mNewId=mBundle.getString("newId");
			mLotId=mBundle.getString("lotId");
			mTxtTitle.setText(mBundle.getString("displayName"));
		}
		//圆形进度条
		mProgressBar=new  Dialog(mContext, R.style.dialog);
		mProgressBar.setContentView(R.layout.common_normal_progressbar);
		mProgressBar.show();
		requestPoint();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		PicOriginalPointAdapter.ViewHolderPicPoint mViewHolder=(PicOriginalPointAdapter.ViewHolderPicPoint)view.getTag();
		   HashMap<String, String> mMap=mViewHolder.mMapPicPoint;
			Intent intent=new Intent(mContext,PicPhotoAct.class);
			mBundle.putString("newId", mMap.get("newId"));
			intent.putExtras(mBundle);
			startActivity(intent);
	}
	
	/** 数据刷新*/
	public void refershData(){
		if(mArrayListJson.size()>0){
			mArraylist.addAll(mArrayListJson);
			mArrayListJson.clear();
		}
		mAdapter.notifyDataSetChanged();
	}
	
	/** 获取单条原地貌信息*/
	public void requestPoint(){
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				HashMap<String, String> params=new HashMap<String, String>();
				params.put("method", Constants.METHOD_OF_ORIGINAL);
				params.put("key",Constants.PUBLIC_KEY);
				params.put("userId", mSP.getString(Constants.PREFERENCES_INFO_USERID, ""));
				params.put("originalTypeId", mNewId);
				params.put("lotId", mLotId);
				params.put("fieldList", "");
				String mStream=null;
				try {
					mStream=HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_ORIGINAL, params, mContext));
					mArrayListJson=JsonTools.getOriginalSingle(mStream);
					if(mArrayListJson.size()>0){
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
	
	@Override
	protected void handleOtherMessage(int flag) {
		super.handleOtherMessage(flag);
		switch(flag){
		case Constants.SUCCESS:
			refershData();
			mProgressBar.cancel();
			break;
		case Constants.ERROR:
			Toast.makeText(mContext,"网络异常", Toast.LENGTH_SHORT).show();
			mProgressBar.cancel();
			break;
		case Constants.COMMON_MESSAGE_1:
			Toast.makeText(mContext,"无数据显示", Toast.LENGTH_SHORT).show();
			mProgressBar.cancel();
			break;
		}
	}

}

