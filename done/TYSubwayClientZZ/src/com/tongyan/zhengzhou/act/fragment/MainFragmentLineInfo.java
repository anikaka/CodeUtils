package com.tongyan.zhengzhou.act.fragment;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tongyan.zhengzhou.act.MainAct;
import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.act.line.LineInfoDetailAct;
import com.tongyan.zhengzhou.common.afinal.MFinalFragment;
import com.tongyan.zhengzhou.common.db.DBService;
import com.tongyan.zhengzhou.common.entities.LineTreeNode;
import com.tongyan.zhengzhou.common.utils.Constants;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * @Title: MainFragmentLineInfo.java
 * @author Rubert
 * @date 2015-3-2 PM 02:11:01
 * @version V1.0
 * @Description: 线路信息-该模块的逻辑为-使用LineTreeNode实体类， 在初始化线路数据时，把所有需要显示的数据都封装在mRootNode中，
 *                        显示下级一级菜单的时候，会在mRootNode中寻找是否有同级别的数据，如果有
 * 
 * 
 */
public class MainFragmentLineInfo extends MFinalFragment{

	private TextView mTitle;
	private LinearLayout mBackBtn;
	private ListView mLineListView;
	private Context mContext;
	private List<LineTreeNode> mAllTreesList = new ArrayList<LineTreeNode>();
	private LineInfoAdapter mAdapter;
	
	
	public static MainFragmentLineInfo newInstance(Context context) {
		MainFragmentLineInfo mMainFragmentLine = new MainFragmentLineInfo();
		mMainFragmentLine.mContext = context;
		return mMainFragmentLine;
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_fragmentline, container, false);
		mTitle = (TextView) view.findViewById(R.id.title_content);
		mBackBtn = (LinearLayout) view.findViewById(R.id.title_back_btn);
		mLineListView = (ListView) view.findViewById(R.id.subway_info_listview);
		mTitle.setText("地铁线路信息");
		mBackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(MainAct.mSlidingMenu != null) {
					MainAct.mSlidingMenu.showMenu();
				}
			}
		});
		mAdapter = new LineInfoAdapter(mContext, R.layout.line_info_list_item, mAllTreesList);
		mLineListView.setAdapter(mAdapter);
		mLineListView.setOnItemClickListener(mItemListener);
		refreshNodeList();
		return view;
	}
	
	
	public void refreshNodeList() {
		mAllTreesList.clear();
		new Thread(new Runnable(){
			@Override
			public void run() {
				//构造数据
				//跟目录
				LineTreeNode mRootNode = new LineTreeNode("天津轻轨");
				mRootNode.setmNodeCode("1");
				ArrayList<HashMap<String, String>> mLineInfoList = new DBService(mContext).getLineInfoByPMetroLineId("-1");
				if(mLineInfoList != null) {
					for(HashMap<String, String> m : mLineInfoList) {
						//线路
						LineTreeNode mNode = new LineTreeNode(m.get("MetroLineName"), m.get("MetroLineId"), true, false, 2);
						ArrayList<HashMap<String, String>> mSubInfoList = new DBService(mContext).getLineInfoByPMetroLineId(m.get("MetroLineId"));
						for(HashMap<String, String> mm : mSubInfoList) {
							//主线路
							LineTreeNode mSubNode = new LineTreeNode(mm.get("MetroLineName"), mm.get("MetroLineId"), true, false, 3);
							ArrayList<HashMap<String, String>> mSubTernInfoList = new DBService(mContext).getLineInfoByPMetroLineId(mm.get("MetroLineId"));
							for(HashMap<String, String> mmm : mSubTernInfoList) {
								//1期工程
								LineTreeNode mSubTermNode = new LineTreeNode(mmm.get("MetroLineName"), mmm.get("MetroLineId"), true, false, 4);
								ArrayList<HashMap<String, String>> mObjectInfoList = new DBService(mContext).getLineInfoByObject(mmm.get("MetroLineId"), "1") ;
								for(HashMap<String, String> mmmm : mObjectInfoList) {
									LineTreeNode mObjectNode = new LineTreeNode(mmmm.get("MetroLineName"), mmmm.get("CheckObjectCode"), true, false, 5);
									ArrayList<HashMap<String, String>> mStationInfoList =  new DBService(mContext).getLineStationById(mmmm.get("ID"));
									for(HashMap<String, String> mmmmm : mStationInfoList) {
										LineTreeNode mStationNode = new LineTreeNode(mmmmm.get("MetroLineName"), mmmmm.get("CheckObjectCode"), false, false, 7);
										mObjectNode.addChild(mStationNode);
									}
									mSubTermNode.addChild(mObjectNode);
								}
								mSubNode.addChild(mSubTermNode);
							}
							mNode.addChild(mSubNode);
						}
						mRootNode.addChild(mNode);
					}
				}
				mAllTreesList.add(mRootNode);
				sendMessage(Constants.MSG_0x2001);
			}
		}).start();
	}
	
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constants.MSG_0x2001:
			mAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	};
	
	
	
	
	/**
	 * 点击事件*/
	OnItemClickListener mItemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			if (!mAllTreesList.get(position).isHasChild()) {
				//Toast.makeText(mContext, mAllTreesList.get(position).getCaption(),Toast.LENGTH_SHORT).show();
				return;
			}
			if (mAllTreesList.get(position).isExpanded()) {
				mAllTreesList.get(position).setExpanded(false);
				LineTreeNode element = mAllTreesList.get(position);
				ArrayList<LineTreeNode> temp = new ArrayList<LineTreeNode>();

				for (int i = position + 1; i < mAllTreesList.size(); i++) {
					if (element.getmNodeLevel() >= mAllTreesList.get(i).getmNodeLevel()) {
						break;
					}
					temp.add(mAllTreesList.get(i));
				}
				mAllTreesList.removeAll(temp);
				mAdapter.notifyDataSetChanged();
			} else {
				LineTreeNode obj = mAllTreesList.get(position);
				obj.setExpanded(true);
				int level = obj.getmNodeLevel();
				int nextLevel = level + 1;

				ArrayList<LineTreeNode> tempList = obj.getmChildList();

				for (int i = tempList.size() - 1; i > -1; i--) {
					LineTreeNode element = tempList.get(i);
					element.setmNodeLevel(nextLevel);
					element.setExpanded(false);
					mAllTreesList.add(position + 1, element);
				}
				mAdapter.notifyDataSetChanged();
		}
		}
	};
	
	
	/**
	 * @Title: MainFragmentLineInfo.java 
	 * @author Rubert
	 * @date 2015-3-19 AM 09:20:19 
	 * @version V1.0 
	 * @Description: TODO
	 */
	class LineInfoAdapter extends BaseAdapter {
		private Context mContext;
		private int mResourceId;
		private List<LineTreeNode> mTreeList;
		private LayoutInflater mInflater;
		public final static int LINE_OPEN_BUTTON = R.drawable.line_info_open_btn;
		public final static int LINE_CLOSE_BUTTON = R.drawable.line_info_close_btn;
		
		public LineInfoAdapter(Context context, int resuorceId, List<LineTreeNode> treeList) {
			this.mContext =  context;
			this.mResourceId = resuorceId;
			this.mTreeList = treeList;
			this.mInflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			if(mTreeList == null) {
				return 0;
			}
			return mTreeList.size();
		}

		@Override
		public Object getItem(int position) {
			return mTreeList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			convertView = mInflater.inflate(mResourceId, null);
			holder = new ViewHolder();
			holder.mInfoText = (TextView) convertView.findViewById(R.id.line_text);
			holder.mOperationBtn = (Button) convertView.findViewById(R.id.line_close_btn);
			holder.mSpaceLayout = (LinearLayout) convertView.findViewById(R.id.horizontal_space);
			holder.mMoveLayout = (LinearLayout) convertView.findViewById(R.id.move_to_next_container);

			convertView.setTag(holder);
			final LineTreeNode treeElement = mTreeList.get(position);
			int level = treeElement.getmNodeLevel();
			if (level == 1) {// 根节点

			} else {
				ArrayList<Integer> spaceList = treeElement.getmSpaceList();
				// 绘制前面的组织架构线条
				for (int i = 0; i < spaceList.size(); i++) {
					ImageView img = new ImageView(mContext);
					img.setImageResource(spaceList.get(i));
					holder.mSpaceLayout.addView(img);
				}
			}
			if (treeElement.isHasChild()) {
				if (treeElement.isExpanded()) {
					holder.mOperationBtn.setBackgroundResource(LINE_OPEN_BUTTON);
				} else {
					holder.mOperationBtn.setBackgroundResource(LINE_CLOSE_BUTTON);
				}
			} else {
				holder.mOperationBtn.setVisibility(View.GONE);
			}
			
			
			holder.mMoveLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					toNextAct(treeElement);
				}
			});
			
			holder.mInfoText.setText(treeElement.getmNodeName());// 设置标题
			return convertView;
		}
		
		class ViewHolder {
			private LinearLayout mSpaceLayout;
			private Button mOperationBtn;
			private TextView mInfoText;
			private LinearLayout mMoveLayout;
		}
		
	}
	
	public void toNextAct(LineTreeNode lineTreeNode) {
		Intent intent = new Intent(mContext, LineInfoDetailAct.class);
		intent.putExtra("nodeCode", lineTreeNode.getmNodeCode());
		intent.putExtra("nodeLevel", lineTreeNode.getmNodeLevel());
		startActivity(intent);
	}
}
