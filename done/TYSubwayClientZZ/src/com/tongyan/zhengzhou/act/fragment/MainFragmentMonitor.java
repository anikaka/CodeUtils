package com.tongyan.zhengzhou.act.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tongyan.zhengzhou.act.MainAct;
import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.act.monitor.MonitorStructureAct;
import com.tongyan.zhengzhou.common.afinal.MFinalFragment;
import com.tongyan.zhengzhou.common.entities.LineTreeNode;
import com.tongyan.zhengzhou.common.utils.CommonUtils;
import com.tongyan.zhengzhou.common.utils.Constants;
import com.tongyan.zhengzhou.common.utils.JSONParseUtils;
import com.tongyan.zhengzhou.common.utils.WebServiceUtils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainFragmentMonitor extends MFinalFragment{
	
	private ListView mLineListView;
	private List<LineTreeNode> mAllTreesList = new ArrayList<LineTreeNode>();
	private Context mContext;
	private monitorInfoAdapter mAdapter;
	private Dialog mDialog = null;
	
	public static MainFragmentMonitor newInstance(Context context){
		MainFragmentMonitor fragmentMonitor = new MainFragmentMonitor();
		fragmentMonitor.mContext = context;
		return fragmentMonitor;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_fragmentmonitor, null, false);
		mLineListView = (ListView) view.findViewById(R.id.subway_info_listview);
		((TextView) view.findViewById(R.id.title_content)).setText(getResources().getString(R.string.monitor_management));
		
		LinearLayout mBackBtn = (LinearLayout)view.findViewById(R.id.title_back_btn);
		mBackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(MainAct.mSlidingMenu != null) {
					MainAct.mSlidingMenu.showMenu();
				}
			}
		});
		
		mAdapter = new monitorInfoAdapter(getActivity(), R.layout.line_info_list_item, mAllTreesList);
		mLineListView.setAdapter(mAdapter);
		mLineListView.setOnItemClickListener(itemSelectedListener);
		getCommonPro();
		return view;
	}
	
	private void getCommonPro() {
		mDialog = CommonUtils.creatLoadingDialog(mContext);
		mAllTreesList.clear();
		new Thread(new Runnable() {
			@Override
			public void run() {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("LoginAccount", "1");
				String stream=null;
				try{
					stream = WebServiceUtils.requestM(getActivity(), map, Constants.METHOD_OF_CLIENT_GETMONITORPROJECTLIST);
					HashMap<String,Object> mBackMap = new JSONParseUtils().getCommonProInfo(stream);
					if(mBackMap != null){
						LineTreeNode mRootNode = new LineTreeNode((String) mBackMap.get("MonitorName"));
						ArrayList<HashMap<String,Object>> mBackMapList = (ArrayList<HashMap<String, Object>>) mBackMap.get("MonitorList");
						if(mBackMapList != null && mBackMapList.size() > 0){
							for(HashMap<String,Object> mMap : mBackMapList){
								LineTreeNode mNode = new LineTreeNode((String)mMap.get("MonitorName"), (String)mMap.get("MonitorCode"), true, false, 2);
								ArrayList<HashMap<String, String>> list = (ArrayList<HashMap<String, String>>) mMap.get("MonitorList");
								if(list != null){
									for(HashMap<String, String> mapLast : list){
										LineTreeNode mSubNode = new LineTreeNode(mapLast.get("MonitorName"), mapLast.get("MonitorCode"), false, false, 3);
										mNode.addChild(mSubNode);
									}
								}
								mRootNode.addChild(mNode);
							}
							mAllTreesList.add(mRootNode);
							sendMessage(Constants.SUCCESS);
						} else {
							sendMessage(Constants.ERROR);
						}
					} else {
						sendMessage(Constants.ERROR);
					}
				}catch (Exception e) {
					e.printStackTrace();
					sendMessage(Constants.CONNECTION_TIMEOUT);
				}	
			}

		}).start();
	}
	/**
	 * 点击监听事件
	 */
	OnItemClickListener itemSelectedListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			if (!mAllTreesList.get(arg2).isHasChild()) {
				Intent intent = new Intent(getActivity(),MonitorStructureAct.class);
				intent.putExtra("MonitorCode", mAllTreesList.get(arg2).getmNodeCode());
				startActivity(intent);
				return;
			}
			if(mAllTreesList.get(arg2).isExpanded()){
				mAllTreesList.get(arg2).setExpanded(false);
				
				ArrayList<LineTreeNode> arrayList = new ArrayList<LineTreeNode>();
				for(int i = arg2 +1; i < mAllTreesList.size() ;i++){
					if(mAllTreesList.get(arg2).getmNodeLevel() < mAllTreesList.get(i).getmNodeLevel()){
						arrayList.add(mAllTreesList.get(i));					
					}
				}
				
				mAllTreesList.removeAll(arrayList);
				mAdapter.notifyDataSetChanged();
			}else{
				mAllTreesList.get(arg2).setExpanded(true);
				int level = mAllTreesList.get(arg2).getmNodeLevel()+1;
				ArrayList<LineTreeNode> lineTreeNodes = mAllTreesList.get(arg2).getmChildList();
				
				if(lineTreeNodes != null ){
					for(int i = lineTreeNodes.size() - 1; i > -1;i--){
						LineTreeNode node = lineTreeNodes.get(i);
						node.setmNodeLevel(level);
						node.setExpanded(false);
						mAllTreesList.add(arg2+1,node);
					}
					mAdapter.notifyDataSetChanged();
				}
			}
		}
	};
	/**
	 * 关闭加载的dialog
	 */
	private void closeMDialog() {
		if(mDialog != null) {
			mDialog.cancel();
			mDialog = null;
		}
	}
	
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constants.SUCCESS:
			closeMDialog();
			mAdapter.notifyDataSetChanged();
			break;
		case Constants.CONNECTION_TIMEOUT:
			closeMDialog();
			Toast.makeText(mContext, "加载失败，请重试", Toast.LENGTH_SHORT).show();
			break;	
		case Constants.ERROR:
			closeMDialog();
			Toast.makeText(mContext, "加载失败，请重试", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	};
	
	class monitorInfoAdapter extends BaseAdapter{
		
		private Context mContext;
		private int mResourceId;
		private List<LineTreeNode> mTreeList;
		private LayoutInflater mInflater;
		
		public monitorInfoAdapter(Context context, int resuorceId, List<LineTreeNode> treeList){
			this.mContext = context;
			this.mResourceId = resuorceId;
			this.mTreeList = treeList;
			this.mInflater = LayoutInflater.from(getActivity());
		}

		@Override
		public int getCount() {
			return mTreeList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mTreeList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			ViewHolder  mViewHolder;
			mViewHolder = new ViewHolder();
			arg1 = mInflater.inflate(mResourceId, null );
			mViewHolder.mInfoText = (TextView) arg1.findViewById(R.id.line_text);
			mViewHolder.mOperationBtn = (Button) arg1.findViewById(R.id.line_close_btn);
			mViewHolder.mSpaceLayout = (LinearLayout) arg1.findViewById(R.id.horizontal_space);
			mViewHolder.mMoveLayout = (LinearLayout) arg1.findViewById(R.id.move_to_next_container);
			arg1.setTag(mViewHolder);	
			LineTreeNode treeElement = mTreeList.get(arg0);
			int level = treeElement.getmNodeLevel();
			if(1 == level){
				
			}else{
				ArrayList<Integer> spaceList = treeElement.getmSpaceList();
				// 绘制前面的组织架构线条
				for (int i = 0; i < spaceList.size(); i++) {
					ImageView img = new ImageView(mContext);
					img.setImageResource(spaceList.get(i));
					mViewHolder.mSpaceLayout.addView(img);
				}
			}
			
			if (treeElement.isHasChild()) {
				if (treeElement.isExpanded()) {
					mViewHolder.mOperationBtn.setBackgroundResource(R.drawable.line_info_open_btn);
				} else {
					mViewHolder.mOperationBtn.setBackgroundResource(R.drawable.line_info_close_btn);
				}
			} else {
				mViewHolder.mOperationBtn.setVisibility(View.GONE);
			}
			
			mViewHolder.mMoveLayout.setVisibility(View.GONE);
			mViewHolder.mInfoText.setText(treeElement.getmNodeName());
			return arg1;
		}
		
		class ViewHolder {
			private LinearLayout mSpaceLayout;
			private Button mOperationBtn;
			private TextView mInfoText;
			private LinearLayout mMoveLayout;
		}
		
	}
}
