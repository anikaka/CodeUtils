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
import com.tongyan.yanan.common.adapter.PicGutterPointAdapter;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;

/**
 * 盲沟类型点获取
 * @author ChenLang
 */
public class PicGutterPointAct  extends FinalActivity implements OnItemClickListener{

	@ViewInject(id=R.id.txtTitle_picGutterPoint)
	TextView mTxtTitle;
	
	@ViewInject(id=R.id.listView_picGutterPoint)
	ListView mListView;
	
	private Context mContext=this;
	private SharedPreferences mSP;
	private Bundle mBundle;
	private String mLotId;//合同段Id
	private String mNewId;//盲沟类型Id
	private ArrayList<HashMap<String, String>> mArrayList=new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String, String>> mArrayListJson=new ArrayList<HashMap<String,String>>();
	private PicGutterPointAdapter  mAdapter;
	private Dialog mProgressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pic_gutter_point);
		mSP=PreferenceManager.getDefaultSharedPreferences(mContext);
		if(getIntent().getExtras()!=null){
			mBundle=getIntent().getExtras();
			mLotId=mBundle.getString("lotId");
			mNewId=mBundle.getString("newId");
			mTxtTitle.setText(mBundle.getString("typeName"));
		}
		//构造适配器
		mAdapter=new PicGutterPointAdapter(mContext, mArrayList, R.layout.pic_gutter_point_listview_item);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		//圆形进度条
		mProgressBar=new  Dialog(mContext, R.style.dialog);
		mProgressBar.setContentView(R.layout.common_normal_progressbar);
		mProgressBar.show();
		requestGutterPoint();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		PicGutterPointAdapter.ViewHolderPicGutterPoint mViewHolder=(PicGutterPointAdapter.ViewHolderPicGutterPoint)view.getTag();
		HashMap<String, String> mMap=mViewHolder.mMapPicGutterPoint;
		Intent intent=new Intent(mContext,PicPhotoAct.class);
		mBundle.putString("newId", mMap.get("newId"));
		intent.putExtras(mBundle);
		startActivity(intent);
	}
	
	/** 盲沟数据点请求*/
	public void requestGutterPoint(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HashMap<String, String> params=new HashMap<String, String>();
				params.put("method", Constants.METHOD_OF_GETGUTTER);
				params.put("key", Constants.PUBLIC_KEY);
				params.put("userId", mSP.getString(Constants.PREFERENCES_INFO_USERID, ""));
				params.put("lotId", mLotId);
				params.put("newId", mNewId);
				params.put("fieldList", "NewId,GutterName,TypeName,StartPointX,StartPointY,StartPointZ,EndPointX,EndPointY,EndPointZ");
				String mStream; 
				try {
					mStream=HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_GUTTER, params, mContext));
					mArrayListJson=JsonTools.getGutter(mStream);
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
	public void  refresh(){
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
				mProgressBar.cancel();
				break;
			case Constants.ERROR:
				mProgressBar.cancel();
				Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
				break;
			case Constants.COMMON_MESSAGE_1:
				mProgressBar.cancel();
				Toast.makeText(mContext, "无数据显示", Toast.LENGTH_SHORT).show();
				break;
		}
	}
	
}
