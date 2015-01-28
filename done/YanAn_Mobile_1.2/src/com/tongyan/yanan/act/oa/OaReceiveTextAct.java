package com.tongyan.yanan.act.oa;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.OaReciveTextAdapter;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.common.widgets.view.PullDownView;
import com.tongyan.yanan.common.widgets.view.PullDownView.OnPullDownListener;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpRequest;
import com.tongyan.yanan.tfinal.https.HttpUtils;

/**
 * @category收文界面
 * @author ChenLang
 * @date 	2014/07/23
 *	@version Yan An 1.0
 */

public class OaReceiveTextAct extends FinalActivity implements OnPullDownListener {


	private ListView mListView;
	@ViewInject(id=R.id.pull_down_view)
	private PullDownView mPullDownView;
	private int pageSize=1;
	private OaReciveTextAdapter  mAdapter;
	private Context mContext=this;
	private SharedPreferences mSP;
	private  ArrayList<HashMap<String, String>>  mList=new ArrayList<HashMap<String,String>>();
	private  ArrayList<HashMap<String, String>>  mJsonList=new ArrayList<HashMap<String,String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oa_receivetext);
		mSP=PreferenceManager.getDefaultSharedPreferences(mContext);

		 // 1.使用PullDownView 2.设置OnPullDownListener 3.从mPullDownView里面获取ListView
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mListView.setCacheColorHint(0);
		mListView.setSelector(getResources().getDrawable(R.drawable.selector_common_listview));
		//构造适配器
		mAdapter=new OaReciveTextAdapter(mContext, mList, R.layout.oa_receivetext_listview_item);
		mListView.setAdapter(mAdapter);
		mPullDownView.enableAutoFetchMore(true, 1);
		requestReceiveText();
		mListView.setOnItemClickListener(listener);
	}
	
	/** listview 监听*/
	OnItemClickListener listener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
				 OaReciveTextAdapter.ViewHolderReceiveText  mViewHolder=(OaReciveTextAdapter.ViewHolderReceiveText)view.getTag();
				 	HashMap<String, String> mMap=mViewHolder.mMapViewHolderReceiveText;
				 Intent  intent=new Intent(mContext,OaReceiveTextSingleAct.class);
				 if(mMap!=null){					 
					 intent.putExtra("newId", mMap.get("newId"));
				 }
				startActivity(intent);
			
		}
	};
	
	/**收文信息请求*/
	public void requestReceiveText(){
		new Thread(){
			public void run(){
				 HashMap<String, String> params=new HashMap<String, String>();
				params.put("method", Constants.METHOD_OF_OA_RECEIVETEXT);
				params.put("key", Constants.PUBLIC_KEY);
				params.put("userId", mSP.getString(Constants.PREFERENCES_INFO_USERID, ""));
				params.put("type", "false");
				params.put("pageNum", String.valueOf(pageSize)); //当前第几页
				params.put("pageSize", "15");
				params.put("fieldList", "NewId,Theme,CreateTime");
				String mStream=null;
				try {
					mStream=HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_OA, params, mContext));
					if(mStream!=""){
							if(JsonTools.getCommonResult(mStream)){								
								//解析Json
								mJsonList=JsonTools.getReceiveText(mStream);
								sendFMessage(Constants.SUCCESS);
							}else{
								sendFMessage(Constants.COMMON_MESSAGE_2);
							}
					}else{
						sendFMessage(Constants.COMMON_MESSAGE_2);
					}
				} catch (IOException e) {
					e.printStackTrace();
					sendFMessage(Constants.ERROR);
				}
			}
		}.start();
	}
	
	/**更新数据*/
	public void  refresh(){
		if(mJsonList!=null && mJsonList.size()>0){
			mList.addAll(mJsonList);
		}
		mJsonList.clear();
		mAdapter.notifyDataSetChanged();
		if(pageSize>1){
			mPullDownView.notifyDidMore();
		}else{			
			mPullDownView.notifyDidLoad();
		}
	}
	
	@Override
	protected void handleOtherMessage(int flag) {
		super.handleOtherMessage(flag);
		switch(flag){
			case  Constants.SUCCESS:
						refresh();
						pageSize++;
				break;
			case  Constants.ERROR:
					Toast.makeText(mContext, "网络出现异常", Toast.LENGTH_SHORT).show();
				break;
			case  Constants.WHAT_DID_REFRESH:
					mAdapter.notifyDataSetChanged();
					// 告诉它更新完毕
					mPullDownView.notifyDidRefresh();
				break;
			case Constants.WHAT_DID_MORE:
					refresh();
				break;
			case Constants.COMMON_MESSAGE_1:
					refresh();
					Toast.makeText(mContext, "数据已经全部更新", Toast.LENGTH_SHORT).show();
					break;
			case Constants.COMMON_MESSAGE_2:
					refresh();
					Toast.makeText(mContext, "数据已经全部更新", Toast.LENGTH_SHORT).show();
					break;
		}
	}

	@Override
	public void onRefresh() {
		sendFMessage(Constants.WHAT_DID_REFRESH);
	}

	@Override
	public void onMore() {
		requestReceiveText();
	}
}
