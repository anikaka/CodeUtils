package com.tongyan.yanan.act.pic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.PicPointPhotoAdapter;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;

/**
 * 定点拍照
 * @author ChenLang
 */

public class PicPointPhotoAct extends FinalActivity implements OnItemClickListener{

	@ViewInject(id=R.id.listView_picPointPhoto)
	ListView mListView;
	private Context mContext=this;
	private SharedPreferences mSP;
	private ArrayList<HashMap<String, String>> mArrayList=new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String, String>>  mArrayListJson=new ArrayList<HashMap<String,String>>();
	private PicPointPhotoAdapter mAdapter;
	private Bundle mBundle;
	private String mLotId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	   setContentView(R.layout.pic_pointphoto);
	   mSP=PreferenceManager.getDefaultSharedPreferences(mContext);
	   if(getIntent().getExtras()!=null){
		   mBundle=getIntent().getExtras();
		   mLotId=mBundle.getString("lotId");
	   }
	   mAdapter=new PicPointPhotoAdapter(mContext, mArrayList, R.layout.pic_pointphoto_listview_item);
	   mListView.setAdapter(mAdapter);
	   mListView.setOnItemClickListener(this);
	   requestPointPhoto();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		PicPointPhotoAdapter.ViewHolderPicPointPhoto mViewHolder=(PicPointPhotoAdapter.ViewHolderPicPointPhoto)view.getTag();
		HashMap<String, String> mMap=mViewHolder.mMapPicPointPhoto;
		Intent intent=new Intent(mContext, PicPhotoAct.class);
		mBundle.putString("newId",mMap.get("newId"));
		intent.putExtras(mBundle);
		startActivity(intent);
	}

	/** 定点拍照获取*/
	public void requestPointPhoto(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HashMap<String, String> params=new HashMap<String, String>();
				params.put("method", Constants.METHO_OF_POINTPHOTO_ALL);
				params.put("key",Constants.PUBLIC_KEY);
				params.put("userId", mSP.getString(Constants.PREFERENCES_INFO_USERID, ""));
//				params.put("startTime", "");
//				params.put("endTime", "");
				params.put("lotId", mLotId);
				params.put("fieldList", "");
				String mStream=null;
				try {
					mStream=HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_POINTPHOTO, params,mContext));
			    	mArrayListJson=JsonTools.getPointPhoto(mStream);
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
	
	/** 数据刷新*/
	public void refresh(){
		if(mArrayListJson.size()>0){
			mArrayList.addAll(mArrayListJson);
			mArrayListJson.clear();
		}
		mAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void handleOtherMessage(int flag) {
		super.handleOtherMessage(flag);
		switch(flag){
		case Constants.SUCCESS:
			refresh();
			break;
		case Constants.ERROR:
			Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constants.COMMON_MESSAGE_1:
				Toast.makeText(mContext, "无数据显示", Toast.LENGTH_SHORT).show();
			break;
		}
	}
}
