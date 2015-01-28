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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.OANoticeAdapter;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.common.widgets.view.PullDownView;
import com.tongyan.yanan.common.widgets.view.PullDownView.OnPullDownListener;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;
/**
 * 
 * @className OaNoticeAct
 * @author wanghb
 * @date 2014-7-14 AM 08:18:39
 * @Desc 公告通知
 */
public class OaNoticeAct extends FinalActivity  implements OnPullDownListener,OnItemClickListener{
	
	@ViewInject(id=R.id.title_common_content)  
	TextView  mTitleName;

	@ViewInject(id=R.id.pullDownView_notice) 
	private PullDownView mPullDownView_notice;

	private ListView mListView;
	private int pageSize = 1;
	private Context mContext = this;
	private SharedPreferences mSP;
	private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, String>> mJsonList = new ArrayList<HashMap<String, String>>();
	private OANoticeAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oa_notice);
		mSP=PreferenceManager.getDefaultSharedPreferences(mContext);
		mTitleName.setText(getResources().getString(R.string.announcement_notice));//设置标题
		mListView=mPullDownView_notice.getListView();
		mListView.setOnItemClickListener(this);
		//构造适配器,与公告适配器连用
		mAdapter=new OANoticeAdapter(mContext, mList, R.layout.oa_notice_listview_item);
		mListView.setAdapter(mAdapter);
		mListView.setCacheColorHint(0);
		mListView.setSelector(getResources().getDrawable(R.drawable.selector_common_listview));
		mPullDownView_notice.enableAutoFetchMore(true, 1);
		requestNoticeData();
	}

	@Override
	public void onRefresh() {
		sendFMessage(Constants.WHAT_DID_REFRESH);
	}

	@Override
	public void onMore() {
		requestNoticeData();
	}
	
	public void requestNoticeData(){
		new Thread(){
			public void run(){
				 HashMap<String, String> params=new HashMap<String, String>();
				 params.put("method", Constants.METHOD_OF_OA_NOTICE);
				 params.put("key", Constants.PUBLIC_KEY);
				 params.put("userId", mSP.getString(Constants.PREFERENCES_INFO_USERID, ""));
				 params.put("pageNum",String.valueOf(pageSize));
				 params.put("pageSize", "15");
				 params.put("fieldList", "");
				 String mStream=null;
				 try {
					 mStream=HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_OA, params, mContext));
					 if(mStream!=null && !"".equals(mStream)){
						if(JsonTools.getCommonResult(mStream)){							
							//解析信息
							mJsonList=JsonTools.getNotice(mStream);
							sendFMessage(Constants.SUCCESS);
						}else{
							sendFMessage(Constants.COMMON_MESSAGE_1);
						}
					 }else{
						 sendFMessage(Constants.COMMON_MESSAGE_1);
					 }
				} catch (IOException e) {
					sendFMessage(Constants.ERROR);
					e.printStackTrace();
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
			mPullDownView_notice.notifyDidMore();
		}else{			
			mPullDownView_notice.notifyDidLoad();
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
				mPullDownView_notice.notifyDidRefresh();
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
				 Intent intent=new Intent(mContext,OaNoticeSingleAct.class);
				 intent.putExtra("newId", mMap.get("newId"));
				 startActivity(intent);
			 }
	}
}
