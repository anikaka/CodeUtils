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
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.PicCompactionListViewAdapter;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;

/**
 * 强夯类型
 * @author ChenLang
 */

public class PicCompactionAct  extends FinalActivity implements OnItemClickListener{

	@ViewInject(id=R.id.listView_pic_compaction)
	ListView mListView;
	private Context mContext=this;
	private Bundle mBundle;
	private SharedPreferences mSP;
	private ArrayList<HashMap<String, String>>mArrayList=new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String, String>> mArrayListJson=new ArrayList<HashMap<String,String>>();
	private PicCompactionListViewAdapter mAdapter;
	private Dialog mProgressBar;
 	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pic_compaction);
		if(getIntent().getExtras()!=null){
			mBundle=getIntent().getExtras();
		}
		 mSP=PreferenceManager.getDefaultSharedPreferences(mContext);
		 mAdapter=new PicCompactionListViewAdapter(mContext, mArrayList, R.layout.pic_compaction_listview_item);
	   	 mListView.setAdapter(mAdapter);
	   	 mListView.setOnItemClickListener(this);
		//圆形进度条
		mProgressBar=new  Dialog(mContext, R.style.dialog);
		mProgressBar.setContentView(R.layout.common_normal_progressbar);
		mProgressBar.show();
		request();
	}

 	/** 
 	 *数据刷新
 	 */
 	public void refersh(){
 		if(mArrayListJson!=null){
 			mArrayList.addAll(mArrayListJson);
 			mArrayListJson.clear();
 		}
 		mAdapter.notifyDataSetChanged();
 	}
 	
	/** 强夯信息处理*/
	public void request(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
			HashMap<String, String>  params=new HashMap<String, String>();
			params.put("method", Constants.METHOD_OF_ALL_COMPACTION);
			params.put("key", Constants.PUBLIC_KEY);
			params.put("userId", mSP.getString(Constants.PREFERENCES_INFO_USERID, ""));
			params.put("fieldList", "");
			String mStream=null;
			try {
				mStream=HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_COMPACTION, params, mContext));
				mArrayListJson=JsonTools.getCompaction(mStream);
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		PicCompactionListViewAdapter.ViewHolderPicCompaction 	mViewholder=(PicCompactionListViewAdapter.ViewHolderPicCompaction)view.getTag();
		HashMap<String, String> mMap=mViewholder.mMapPicComPaction;
		if(mMap!=null && mMap.size()>0){
			 Intent intent=new Intent(mContext,PicPactAct.class);
			 mBundle.putString("newId",mMap.get("newId"));
			 mBundle.putString("name", mMap.get("text"));
			 intent.putExtras(mBundle);
			 startActivity(intent);
		}
	}	

	@Override
	protected void handleOtherMessage(int flag) {
		super.handleOtherMessage(flag);
		switch(flag){
		case Constants.SUCCESS:
				refersh();
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
