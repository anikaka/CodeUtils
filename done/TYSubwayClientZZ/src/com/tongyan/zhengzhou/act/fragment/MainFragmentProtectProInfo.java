package com.tongyan.zhengzhou.act.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.tongyan.zhengzhou.act.MainAct;
import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.act.adapter.ProtectProInfoAdapter;
import com.tongyan.zhengzhou.act.pro.ProProtectDetailInfoAct;
import com.tongyan.zhengzhou.common.afinal.MFinalFragment;
import com.tongyan.zhengzhou.common.utils.CommonUtils;
import com.tongyan.zhengzhou.common.utils.Constants;
import com.tongyan.zhengzhou.common.utils.JSONParseUtils;
import com.tongyan.zhengzhou.common.utils.WebServiceUtils;
import com.tongyan.zhengzhou.common.widgets.widgetspullrefresh.PullToRefreshBase.OnRefreshListener;
import com.tongyan.zhengzhou.common.widgets.widgetspullrefresh.PullToRefreshListView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 *安保区监护工程信息
 * @author ChenLang
 *
 */
public class MainFragmentProtectProInfo extends MFinalFragment  implements OnItemClickListener{

	/**上下文*/
	private Context mContext;
	/**每一次下拉刷新的数据条数 */
	public static   final  String  PAGE_SIZE="15";
	/**当前页数 */
	private  int  mCurrentPage=0;
	/** 基础数据容器*/
	private HashMap<String, Object> mMap=new HashMap<String, Object>();
	private ArrayList<HashMap<String,Object>>  mArrayListT;
	private LinkedList<HashMap<String, Object>> mLinkedListBaseInfo=new LinkedList<HashMap<String,Object>>();
	private ListView mListViewProtectProInfo;
	private PullToRefreshListView mPullToRefreshListView;
	private ProtectProInfoAdapter mAdapter;
	private LinearLayout mBackBtn;
	
	/** 基础信息 Key {@link #addEmptyBaseInfo()} */
	public static final String MAP_KEY_BASEINFO = "baseInfo";
	/** 设计文件 Key  {@link #addEmptyBaseInfo()} */
	public static final String MAP_KEY_DESIGNFILES = "designFiles";
	/** 勘察文件 key {@link #addEmptyBaseInfo()}*/
	public static final String MAP_KEY_RECONNACEFILES = "reconnanceFiles";
	/** 实施文件 key {@link #addEmptyBaseInfo()}*/
	public static final String MAP_KEY_IMPLEMENTFILES = "implementFiles";
	/** 照片文件 key {@link #addEmptyBaseInfo()}*/
	public static final String MAP_KEY_PHOTOFILES = "photoFiles";

	/**停止刷新*/
	public static final int REFRESH_STOP = 0;
	/**上拉刷新*/
	public static final int   REFRESH_UP = 1;
	/** 下拉刷新*/
	public static final int   REFRESH_DOWN = 2;
	private Dialog mDialog = null;
	
	public static MainFragmentProtectProInfo newewInstance(Context context){
		MainFragmentProtectProInfo fragment=new MainFragmentProtectProInfo();
		fragment.mContext=context;
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			View view=inflater.inflate(R.layout.main_fragmentprotectproinfo, null, false);
			mPullToRefreshListView=(PullToRefreshListView)view.findViewById(R.id.listViewProtectProInfo);
			mBackBtn=(LinearLayout)view.findViewById(R.id.title_back_btn);
			mBackBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(MainAct.mSlidingMenu != null) {
						MainAct.mSlidingMenu.showMenu();
					}
				}
			});
			((TextView)view.findViewById(R.id.title_content)).setText(getResources().getString(R.string.protect_info_management));
			mListViewProtectProInfo=mPullToRefreshListView.getRefreshableView();
			mPullToRefreshListView.setOnRefreshListener(onRefreshListener);
			//addEmptyBaseInfo();
			mAdapter=new ProtectProInfoAdapter(mContext, mLinkedListBaseInfo, R.layout.main_fragment_protectproinfo_item);
			mListViewProtectProInfo.setAdapter(mAdapter);
			mListViewProtectProInfo.setOnItemClickListener(this);
			getProjectInfo();
			return view;
	}
	
	
	/**
	 * 添加一个空的集合*/
//	private void  addEmptyBaseInfo(){
//		mMap.put(MAP_KEY_BASEINFO, mLinkedListBaseInfo);
//		mMap.put(MAP_KEY_DESIGNFILES, new LinkedList<HashMap<String,String>>());
//		mMap.put(MAP_KEY_RECONNACEFILES, new LinkedList<HashMap<String,String>>());
//		mMap.put(MAP_KEY_IMPLEMENTFILES, new LinkedList<HashMap<String,String>>());
//		mMap.put(MAP_KEY_PHOTOFILES, new LinkedList<HashMap<String,String>>());
//	}

	/**
	 * 更新数据*/
	public void  refreshMap(){
		if(mArrayListT!=null && mArrayListT.size()>0){
			//mArrayListT.clear();
//			mMap.put(MAP_KEY_BASEINFO, mArrayListT.get(MAP_KEY_BASEINFO));
//			mMap.put(MAP_KEY_DESIGNFILES, mArrayListT.get(MAP_KEY_DESIGNFILES));
//			mMap.put(MAP_KEY_RECONNACEFILES, mArrayListT.get(MAP_KEY_RECONNACEFILES));
//			mMap.put(MAP_KEY_IMPLEMENTFILES, mArrayListT.get(MAP_KEY_IMPLEMENTFILES));
//			mMap.put(MAP_KEY_PHOTOFILES, mArrayListT.get(MAP_KEY_PHOTOFILES));
			mLinkedListBaseInfo.addAll(mArrayListT);
			mAdapter.notifyDataSetChanged();
		}
	}

	OnRefreshListener onRefreshListener=new OnRefreshListener() {

		@Override
		public void onRefresh() {
			if(mPullToRefreshListView.getRefreshType()==REFRESH_STOP){
				mMap.clear();
				mLinkedListBaseInfo.clear();
				mCurrentPage=1;
			}else if(mPullToRefreshListView.getRefreshType()==REFRESH_UP){
				mCurrentPage=1;
				mLinkedListBaseInfo.clear();
			}else if(mPullToRefreshListView.getRefreshType()==REFRESH_DOWN){
				mCurrentPage= mCurrentPage + 1;
			}
			getProjectInfo();
		}
	};
	
	

	/** 
	 * 获取监护工程信息*/
	public void  getProjectInfo(){
		mDialog = CommonUtils.creatLoadingDialog(mContext);
		new Thread(new Runnable() {
			@Override
			public void run() {
				HashMap<String, String> parameters = new HashMap<String, String>();
				parameters.put("pageSize", PAGE_SIZE);
				parameters.put("currentPage", String.valueOf( mCurrentPage));
				parameters.put("MetroLineID", "");
				String stream = null;
				try {
					stream = WebServiceUtils.requestM(mContext, parameters, Constants.METHOD_OF_CLIENT_PROTECTPROJECTINFOLIST);
					if(stream!=null){
						mArrayListT=new JSONParseUtils().getProtectProInfo(stream);
						if(mArrayListT.size()>0){
							sendMessage(Constants.SUCCESS);
						}else{
							sendMessage(Constants.ERROR);
						}
					}else{
						sendMessage(Constants.ERROR);
					}
				} catch (Exception e) {
					sendMessage(Constants.CONNECTION_TIMEOUT);
					e.printStackTrace();
				}	
			}
		}).start();
	}
	
	/**
	 * 关闭加载的dialog
	 */
	private void closeMDialog() {
		if(mDialog != null) {
			mDialog.cancel();
			mDialog = null;
		}
	}
	
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constants.SUCCESS:
			closeMDialog();
			refreshMap();
        	if( mPullToRefreshListView.getRefreshType() == REFRESH_STOP) {
        		mAdapter.notifyDataSetChanged();
        	} else {
        		mPullToRefreshListView.onRefreshComplete();
        	}
			break;
		case Constants.ERROR:
			closeMDialog();
			if( mPullToRefreshListView.getRefreshType() == REFRESH_STOP) {
        		mAdapter.notifyDataSetChanged();
        	} else {
        		mPullToRefreshListView.onRefreshComplete();
        	}
			break;
		case Constants.CONNECTION_TIMEOUT:
			closeMDialog();
				Toast.makeText(mContext, "网络连接超时", Toast.LENGTH_SHORT).show();
				if( mPullToRefreshListView.getRefreshType() == REFRESH_STOP) {
	        		mAdapter.notifyDataSetChanged();
	        	} else {
	        		mPullToRefreshListView.onRefreshComplete();
	        	}
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
			  ProtectProInfoAdapter.ViewHolder  viewHolder=(ProtectProInfoAdapter.ViewHolder)view.getTag();
			  Intent intent=new Intent(mContext, ProProtectDetailInfoAct.class);
			  intent.putExtra("protectProInfo",viewHolder.map);
			  startActivity(intent);
	}

	
}
