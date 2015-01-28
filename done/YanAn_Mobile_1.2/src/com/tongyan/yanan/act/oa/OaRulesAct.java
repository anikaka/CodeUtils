package com.tongyan.yanan.act.oa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.OANoticeAdapter;
import com.tongyan.yanan.common.adapter.OaReciveTextAdapter;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.common.widgets.view.PullDownView;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;

/**
 * 	规章制度
 * 
 * @author ChenLang
 * @date 2014/07/30
 * @version YanAn 1.0
 */
public class OaRulesAct extends FinalActivity  implements OnItemClickListener{

@ViewInject(id=R.id.pullDownView_rules)	 
PullDownView  mPullDownView_rules;
private ListView mListView;
private int pageSize=1;
private OANoticeAdapter  mAdapter;
private Context mContext=this;
private SharedPreferences mSP;
private  ArrayList<HashMap<String, String>>  mList=new ArrayList<HashMap<String,String>>();
private  ArrayList<HashMap<String, String>>  mJsonList=new ArrayList<HashMap<String,String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oa_rules);
		mSP=PreferenceManager.getDefaultSharedPreferences(mContext);
		mListView=mPullDownView_rules.getListView();
		mAdapter=new OANoticeAdapter(mContext, mList, R.layout.oa_notice_listview_item);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mListView.setCacheColorHint(0);
		mListView.setSelector(getResources().getDrawable(R.drawable.selector_common_listview));
		mPullDownView_rules.enableAutoFetchMore(true, 1);
		requestRulesData();
	}
	
	/** 获取规章制度信息*/
	public void requestRulesData(){
		new Thread(){
			public void run(){
				 HashMap<String, String> params=new HashMap<String, String>();
				 params.put("method", Constants.METHOD_OF_OA_RULES);
				 params.put("key", Constants.PUBLIC_KEY);
				 params.put("userId", mSP.getString(Constants.PREFERENCES_INFO_USERID, ""));
				 params.put("pageNum",String.valueOf(pageSize));
				 params.put("pageSize", "15");
				 params.put("fieldList", "");
				 String mStream=null;
				 try {
					 mStream=HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_OA, params,mContext));
					 if(mStream!=null && !"".equals(mStream)){
						if(JsonTools.getCommonResult(mStream)){							
							//解析信息
							mJsonList=JsonTools.getRules(mStream);
							sendFMessage(Constants.SUCCESS);
						}else{
							sendFMessage(Constants.COMMON_MESSAGE_1);
						}
					 }else{
						 sendFMessage(Constants.COMMON_MESSAGE_1);
					 }
				} catch (IOException e) {
					e.printStackTrace();
					sendFMessage(Constants.ERROR);
				}
			}
		}.start();
	}

	public void  refresh(){
		if(mJsonList!=null && mJsonList.size()>0){
			mList.addAll(mJsonList);
		}
		mJsonList.clear();
		mAdapter.notifyDataSetChanged();
		if(pageSize>1){
			mPullDownView_rules.notifyDidMore();
		}else{			
			mPullDownView_rules.notifyDidLoad();
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
				mPullDownView_rules.notifyDidRefresh();
			break;
		case Constants.WHAT_DID_MORE:
				refresh();
			break;
		case Constants.COMMON_MESSAGE_1:
				Toast.makeText(mContext, "服务器忙,请稍候再试..", Toast.LENGTH_SHORT).show();
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
			  OANoticeAdapter.ViewHolderNotice  mViewHolder=(OANoticeAdapter.ViewHolderNotice)view.getTag();
			 HashMap<String, String> mMap= mViewHolder.mMapNotice;
			 if(mMap!=null && mMap.size()>0){
				 Intent intent=new Intent(mContext,OaRulesSingleAct.class);
				 intent.putExtra("newId", mMap.get("newId"));
				 startActivity(intent);
			 }
	}
}
