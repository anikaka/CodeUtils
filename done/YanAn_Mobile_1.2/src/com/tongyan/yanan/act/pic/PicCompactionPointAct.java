package com.tongyan.yanan.act.pic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.TypeInfo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.PicCompactionPointListViewAdapter;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.common.widgets.view.MDateNoDayPickerDialog;
import com.tongyan.yanan.common.widgets.view.PullDownView;
import com.tongyan.yanan.common.widgets.view.PullDownView.OnPullDownListener;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;

/**
 * 强夯点获取
 * @author ChenLang
 */

public class PicCompactionPointAct extends FinalActivity implements OnClickListener,OnItemClickListener,OnPullDownListener{

	@ViewInject(id=R.id.editSearch,click="clickContent")
	EditText  mEditSerach;
	//搜索按钮
	@ViewInject(id=R.id.butSearch)
	Button mButSearch;
	//标题
	@ViewInject(id=R.id.txtTitle_pic_compaction_point)
	TextView mTxtTitle;
	//列表
	@ViewInject(id=R.id.listView_pic_compaction_point)
    PullDownView mPullDownView;
	
	private ListView mListView;
	private Context mContext=this;
	private int mPageSize=1;
	private Bundle mBundle;
	private String mLotId;
	private String mNewId;
	private SharedPreferences mSP;
	private PicCompactionPointListViewAdapter mAdapter;
	ArrayList<HashMap<String, String>> mArrayList=new ArrayList<HashMap<String,String>>();
	ArrayList<HashMap<String, String>> mArrayListJson=new ArrayList<HashMap<String,String>>();
	private Dialog mProgressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pic_compaction_point);
		mSP=PreferenceManager.getDefaultSharedPreferences(mContext);
		if(getIntent().getExtras()!=null){
			mBundle=getIntent().getExtras();
			mTxtTitle.setText(mBundle.getString("name"));
			mLotId=mBundle.getString("lotId");
			mNewId=mBundle.getString("newId");
		}
		//设置编辑框光标隐藏
		mEditSerach.setCursorVisible(false);
//		mEditSerach.setInputType(InputType.TYPE_NULL);
		mPullDownView.setOnPullDownListener(this);
		mListView=mPullDownView.getListView();
		mListView.setCacheColorHint(0);
		mListView.setSelector(getResources().getDrawable(R.drawable.selector_common_listview));
		//构造适配器
		mAdapter=new PicCompactionPointListViewAdapter(mContext, mArrayList, R.layout.pic_compaction_point_listview_item);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mButSearch.setOnClickListener(this);
		mPullDownView.enableAutoFetchMore(true,1);
		mPullDownView.notifyDidLoad();// 第一次加载隐藏头部
		mProgressBar=new  Dialog(mContext, R.style.dialog);
		mProgressBar.setContentView(R.layout.common_normal_progressbar);
		mProgressBar.show();
		requestCompactionInfo("");
	}
	
	public  void  requestCompactionInfo(final String mSerachContent){
		new Thread(new Runnable() {
			@Override
			public void run() {
				HashMap<String, String> params =new HashMap<String, String>();
				params.put("method", Constants.METHOD_OF_SINGLE_COMPACTION);
 				params.put("key",Constants.PUBLIC_KEY);
 				params.put("userId", mSP.getString(Constants.PREFERENCES_INFO_USERID, ""));
 				params.put("lotId",mLotId );
 				params.put("search", mSerachContent);
 				params.put("newId", mNewId); //类型Id
 				params.put("fieldList", "NewId,CompactionName,CType,Area");
 				params.put("pageNum", String.valueOf(mPageSize));
 				params.put("pageSize", "20");
 				String mStream=null;
 				try {
 					mStream=HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_COMPACTION, params, mContext));
 					if(JsonTools.getCommonResult(mStream)){ 						
 						mArrayListJson=JsonTools.getCompactionPoint(mStream);
 						if(mArrayListJson.size()>0){
 							sendFMessage(Constants.SUCCESS);
 						}else{
 							sendFMessage(Constants.COMMON_MESSAGE_1);
 						}
 					}else{
 						sendFMessage(Constants.COMMON_MESSAGE_2);
 					}
 				} catch (IOException e) {
 					sendFMessage(Constants.ERROR);
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void refersh(){
		if(mArrayListJson.size()>0){
			mArrayList.addAll(mArrayListJson);
		}
		mArrayListJson.clear();
		mAdapter.notifyDataSetChanged();
		if(mPageSize>1 ){
			mPullDownView.notifyDidMore();
		}else{			
			mPullDownView.notifyDidLoad();
		}
	}
	
	
	@Override
	public void onClick(View v) {
		mEditSerach.setFocusableInTouchMode(true);
		//圆形进度条
//		mProgressBar=new  Dialog(mContext, R.style.dialog);
//		mProgressBar.setContentView(R.layout.common_normal_progressbar);
		mProgressBar.show();
		mPageSize=1;
		if("".equals(mEditSerach.getText().toString())){
			Toast.makeText(mContext, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
		}else{
			mEditSerach.setCursorVisible(false);
			mArrayList.clear();
			requestCompactionInfo(mEditSerach.getText().toString());
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		PicCompactionPointListViewAdapter.ViewHolderPicCompaction mViewHolder=(PicCompactionPointListViewAdapter.ViewHolderPicCompaction)view.getTag();
		HashMap<String, String> mMap=mViewHolder.mMapPicComPaction;
		Intent intent=new Intent(mContext,PicPhotoAct.class);
		mBundle.putString("newId", mMap.get("newId"));
		intent.putExtras(mBundle);
		startActivity(intent);
	}
	
	@Override
	protected void handleOtherMessage(int flag) {
		super.handleOtherMessage(flag);
		switch (flag) {
		case Constants.SUCCESS:
				refersh();
				mProgressBar.cancel();
				mPageSize++;	 
			break;
		case Constants.ERROR:
			break;
		case  Constants.WHAT_DID_REFRESH:
			mAdapter.notifyDataSetChanged();
			// 告诉它更新完毕
			mPullDownView.notifyDidRefresh();
			mProgressBar.cancel();
		break;
		case Constants.COMMON_MESSAGE_1:
			Toast.makeText(mContext, "无数据显示", Toast.LENGTH_SHORT).show();
			mProgressBar.cancel();
			break;
		case Constants.COMMON_MESSAGE_2:
			refersh();
			mProgressBar.cancel();
			Toast.makeText(mContext, "数据已经全部更新", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	@Override
	public void onRefresh() {
		sendFMessage(Constants.WHAT_DID_REFRESH);
	}

	@Override
	public void onMore() {
		requestCompactionInfo(mEditSerach.getText().toString());
	}
	
}
