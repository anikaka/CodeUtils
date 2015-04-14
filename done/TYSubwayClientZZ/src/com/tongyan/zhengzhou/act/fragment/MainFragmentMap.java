package com.tongyan.zhengzhou.act.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.model.LatLng;
import com.readystatesoftware.viewbadger.BadgeView;
import com.tongyan.zhengzhou.act.MainAct;
import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.act.adapter.MFragmentPagerAdapter;
import com.tongyan.zhengzhou.act.adapter.MapSearchAdapter;
import com.tongyan.zhengzhou.act.fragment.patro.PatroMapLineSelectFragment;
import com.tongyan.zhengzhou.act.fragment.patro.PatroMapRedLineSelectFragment;
import com.tongyan.zhengzhou.act.patro.PatroDangerBuildingDetailAct;
import com.tongyan.zhengzhou.act.patro.PatroInfoDetailAct;
import com.tongyan.zhengzhou.common.afinal.MFinalFragment;
import com.tongyan.zhengzhou.common.db.DBService;
import com.tongyan.zhengzhou.common.entities.LineTreeNode;
import com.tongyan.zhengzhou.common.utils.Constants;
import com.tongyan.zhengzhou.common.utils.JSONParseUtils;
import com.tongyan.zhengzhou.common.utils.MapUtils;
import com.tongyan.zhengzhou.common.utils.WebServiceUtils;
import com.tongyan.zhengzhou.common.widgets.view.EasingType;
import com.tongyan.zhengzhou.common.widgets.view.ExpoInterpolator;
import com.tongyan.zhengzhou.common.widgets.view.Panel;
import com.tongyan.zhengzhou.common.widgets.view.Panel.OnPanelListener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @Title: MainFragment1.java 
 * @author Rubert
 * @date 2015-3-2 PM 02:11:01 
 * @version V1.0 
 * @Description: Fragment类名暂用MainFragment1
 */
public class MainFragmentMap extends MFinalFragment  implements OnClickListener{
	
	private MapView  mMapView;
	private ImageButton  mImgBtnMapZoom,      //地图放大
									   mImgBtnMapDecrease, //地图缩小
									   mImgBtnSliding, imgBtnSwitch;
	private Button imgBtnSignal;            //
	private LinearLayout mLineSelectContainer, mLineSelectBtn, mBadger;
	private TextView mChangeLineText, mLineName;
	private EditText mEditText;
	
	public static final int BAIDU_LOGO=1;
	private BaiduMap mBaiduMap;
	private Context mContext;
	//private LocationClient mLocClient;
	//private MyLocationListenner myListener = new MyLocationListenner();
	private String mStartStation = "380";
	private String mEndStation = "389";
	private boolean isOpenLineSelectDialog = false;// 是否首次定位
	private ArrayList<LatLng> mUpPointList;
	
	private ViewPager mPagerView;
	private ArrayList<Fragment> fragmentsList;
	private MFragmentPagerAdapter mPageFragment;
	private SharedPreferences mPreferences;
	
	private String mCurrentLineId = "";
	private ArrayList<HashMap<String,Object>> protectList = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String,Object>> illegalList = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> protectWarningList = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> illegalWarningList = new ArrayList<HashMap<String,Object>>();
	private boolean isIllegal = false;
	private BadgeView badgeView;
	
	private ListView mSearchListView;
	private MapSearchAdapter mMapSearchAdapter;
	private ArrayList<HashMap<String, String>> mSearchList = new ArrayList<HashMap<String, String>>();
	private boolean isShowSearch = false;
	private Panel panel;
	private monitorInfoAdapter mAdapter;
	private List<LineTreeNode> mAllTreesList = new ArrayList<LineTreeNode>();
	private ListView mLineListView;
	
	public static MainFragmentMap newInstance(Context context) {
		MainFragmentMap mMainFragment = new MainFragmentMap();
		mMainFragment.mContext = context;
		return mMainFragment;
	}
	
	
	private  AnimationDrawable getFrameAnimMenuMenuMenu(){
			AnimationDrawable mFrameAnimMenuMenuMenu=new AnimationDrawable();
			mFrameAnimMenuMenuMenu.addFrame(getResources().getDrawable(R.drawable.sliding_menu01), 50);
			mFrameAnimMenuMenuMenu.addFrame(getResources().getDrawable(R.drawable.sliding_menu02), 50);
			mFrameAnimMenuMenuMenu.addFrame(getResources().getDrawable(R.drawable.sliding_menu03), 50);
			mFrameAnimMenuMenuMenu.addFrame(getResources().getDrawable(R.drawable.sliding_menu04), 50);
			mFrameAnimMenuMenuMenu.addFrame(getResources().getDrawable(R.drawable.sliding_menu05), 50);
			mFrameAnimMenuMenuMenu.addFrame(getResources().getDrawable(R.drawable.sliding_menu06), 50);
			mFrameAnimMenuMenuMenu.addFrame(getResources().getDrawable(R.drawable.sliding_menu07), 50);
			mFrameAnimMenuMenuMenu.addFrame(getResources().getDrawable(R.drawable.sliding_menu08), 50);
			mFrameAnimMenuMenuMenu.addFrame(getResources().getDrawable(R.drawable.sliding_menu09), 50);
		return mFrameAnimMenuMenuMenu;
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//SDKInitializer.initialize(mContext); 
		View view=inflater.inflate(R.layout.main_fragmentmap, null, false);
		mMapView=(MapView)view.findViewById(R.id.baiduMap);
		mImgBtnSliding=(ImageButton)view.findViewById(R.id.imgBtnSliding);
		mImgBtnMapZoom=(ImageButton)view.findViewById(R.id.imgBtnMapZoom);
		mImgBtnMapDecrease=(ImageButton)view.findViewById(R.id.imgBtnMapDecrease);
		imgBtnSwitch = (ImageButton)view.findViewById(R.id.imgBtnSwitch);
		imgBtnSignal = (Button) view .findViewById(R.id.panelHandle);
		mLineSelectContainer = (LinearLayout)view.findViewById(R.id.line_select_container);
		mLineSelectBtn = (LinearLayout)view.findViewById(R.id.line_select_btn);
		mChangeLineText = (TextView)view.findViewById(R.id.change_line_text);
		mLineName = (TextView)view.findViewById(R.id.line_name);
		panel = (Panel) view.findViewById(R.id.rightPanel);
		
		badgeView=new BadgeView(mContext,imgBtnSignal);
		badgeView.setTextSize(8);
		
		//地图搜索功能
		mSearchListView = (ListView)view.findViewById(R.id.search_list);
		mEditText = (EditText)view.findViewById(R.id.txtSearch);
		mMapSearchAdapter = new MapSearchAdapter(mContext, mSearchList);
		mSearchListView.setAdapter(mMapSearchAdapter);
		
		mEditText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isShowSearch) {
					mSearchListView.setVisibility(View.GONE);
					isShowSearch = false;
				} else {
					refreshSearchList();
					mSearchListView.setVisibility(View.VISIBLE);
					isShowSearch = true;
				}
			}
		});
		
		
		mLineListView = (ListView) view.findViewById(R.id.panelContent);
		mAdapter = new monitorInfoAdapter(mContext, R.layout.monitor_illegal_project_info, mAllTreesList);
		mLineListView.setOnItemClickListener(itemSelectedListener);
		mLineListView.setAdapter(mAdapter);
		
		panel.setOnPanelListener(new OnPanelListener() {
			
			@Override
			public void onPanelOpened(Panel panel) {
				mAllTreesList.clear();
				ArrayList<HashMap<String, Object>> allalaist = new ArrayList<HashMap<String,Object>>();
				HashMap<String, Object> proMap = new HashMap<String, Object>();
				proMap.put("name", "监护预警项目");
				proMap.put("list", protectWarningList);
				proMap.put("isIllegal", false);
				allalaist.add(proMap);
				HashMap<String, Object> illMap = new HashMap<String, Object>();
				illMap.put("name", "违规预警项目");
				illMap.put("list", illegalWarningList);
				illMap.put("isIllegal", true);
				allalaist.add(illMap);
				
				if(allalaist != null){
					for(int i = 0; i < allalaist.size(); i++){
						LineTreeNode mRootNode = new LineTreeNode((String) allalaist.get(i).get("name"));
						ArrayList<HashMap<String,Object>> mBackMapList = (ArrayList<HashMap<String, Object>>) allalaist.get(i).get("list");
						isIllegal = (Boolean) allalaist.get(i).get("isIllegal");
						if(mBackMapList != null ){
							for(HashMap<String,Object> mMap : mBackMapList){
								LineTreeNode mNode = null;
								HashMap<String, String> map = (HashMap<String, String>) mMap.get("BaseInfo");
								if(map != null){
									if(isIllegal){			
										mNode = new LineTreeNode(map.get("IllegalName"), map.get("IllegalID"), false, false, 2);
									}else{
										mNode = new LineTreeNode(map.get("projectName"), map.get("projectId"), false, false, 2);
									}	
								}
								mRootNode.addChild(mNode);
							}
						}
						mAllTreesList.add(mRootNode);
					}
					mAdapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void onPanelClosed(Panel panel) {
				
			}
		});
		panel.setInterpolator(new ExpoInterpolator(EasingType.OUT));
		//
		mSearchListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MapSearchAdapter.ViewHolder mViewHolder = (MapSearchAdapter.ViewHolder)view.getTag();
				if(mViewHolder.mCacheData != null) {
					LatLng point = new LatLng(Double.parseDouble(mViewHolder.mCacheData.get("y")), Double.parseDouble(mViewHolder.mCacheData.get("x")));
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(point);
					mBaiduMap.animateMapStatus(u);
					mSearchListView.setVisibility(View.GONE);
				}
			}
		});
		
		mImgBtnSliding.setOnClickListener(this);
		mImgBtnMapZoom.setOnClickListener(this);
		mImgBtnMapDecrease.setOnClickListener(this);
		imgBtnSwitch.setOnClickListener(this);
//		imgBtnSignal.setOnClickListener(this);
		
		
		
		hideDeafalutZoom();
		mMapView.removeViewAt(BAIDU_LOGO);
		
		mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		mCurrentLineId = mPreferences.getString(Constants.PREFERENCES_LINE_NUMBER, "");
		if("".equals(mCurrentLineId)) {
			Toast.makeText(getActivity(), "请选择线路", Toast.LENGTH_SHORT).show();
		}
		mLineName.setText(mPreferences.getString(Constants.PREFERENCES_LINE_NAME, ""));
		
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(19.0f));
		mBaiduMap.setMyLocationEnabled(true);
		/*mLocClient = new LocationClient(mContext);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();*/
		mBaiduMap.setOnMarkerClickListener(listener);
		mPagerView = (ViewPager)view.findViewById(R.id.vPager);
		fragmentsList = new ArrayList<Fragment>();
		//http://blog.csdn.net/cshxql/article/details/22788343
		mPageFragment = new MFragmentPagerAdapter(getChildFragmentManager(), fragmentsList);
		refreshFragment();
		mPagerView.setAdapter(mPageFragment);
        mPagerView.setCurrentItem(0);
        mPagerView.setOnPageChangeListener(new MyOnPageChangeListener());
        mLineSelectBtn.setOnClickListener(new MyOnClickListener());
        mLineSelectBtn.setTag(Integer.valueOf(0));
		new Thread(new Runnable(){
			@Override
			public void run() {
				getProtectPro();//获取监护项目数据
				getIllegalPro();//获取违规建筑信息
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				sendMessage(Constants.MSG_0x2001);
			}
		}).start();
		
		return view;
	}
	public void refreshFragment() {
		if(fragmentsList != null) {
			fragmentsList.clear();
		}
		Fragment mRedLineSelectFragment = PatroMapRedLineSelectFragment.newinstance();
		Fragment mLineSelectFragment = PatroMapLineSelectFragment.newinstance();
		//添加的顺序不能颠倒
		fragmentsList.add(mRedLineSelectFragment);
		fragmentsList.add(mLineSelectFragment);
		mPageFragment.notifyDataSetChanged();
	}
	
	
	public class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
        	if(v.getTag() == Integer.valueOf(0)) {
        		mPagerView.setCurrentItem(1);
        		v.setTag(Integer.valueOf(1));
        	} else if(v.getTag() == Integer.valueOf(1)){
        		mPagerView.setCurrentItem(0);
        		v.setTag(Integer.valueOf(0));
        	} else {
        		mPagerView.setCurrentItem(0);
        		v.setTag(Integer.valueOf(0));
        	}
        }
    };

	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageSelected(int arg0) {
			if(arg0 == 0) {
				mChangeLineText.setText("切换线路");
			} else {
				mChangeLineText.setText("切换红线");
			}
		}
	};
	/**
	 * 线路刷新
	 */
	public void refreshSearchList() {
		if(mSearchList != null) {
			mSearchList.clear();
		} else {
			mSearchList = new ArrayList<HashMap<String, String>>();
		}
		
		if(illegalList != null && illegalList.size() > 0) {
			for(HashMap<String,Object> illegalMap : illegalList){
				ArrayList<HashMap<String, String >> pointList = (ArrayList<HashMap<String, String>>) illegalMap.get("IllegalPoints");
				if(pointList != null && pointList.size() > 0){
					HashMap<String, String> pointMap = pointList.get(0);
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("x", pointMap.get("x"));
					map.put("y", pointMap.get("y"));
					map.put("Type", "1");
					map.put("Name", String.valueOf(pointMap.get("IllegalName")));
					mSearchList.add(map);
				}
			}
		}
		if(protectList != null && protectList.size() > 0) {
			for(HashMap<String,Object> mContainerMap : protectList){
				if(mContainerMap != null){
					HashMap<String, String> baseMap = (HashMap<String, String>) mContainerMap.get("BaseInfo");
					if(baseMap != null && baseMap.size() > 0){
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("x", baseMap.get("x"));
						map.put("y", baseMap.get("y"));
						map.put("Type", "2");
						map.put("Name", String.valueOf(baseMap.get("projectName")));
						mSearchList.add(map);
					}
				}
			}
		}
		mMapSearchAdapter.notifyDataSetChanged();
	}
	
	
	/**
	 * 获取监护项目数据
	 */
	public void getProtectPro() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("pageSize", "1000");
		map.put("currentPage", "1");
		map.put("MetroLineID", mCurrentLineId);
		String stream=null;
		try {
			stream = WebServiceUtils.requestM(getActivity(), map, Constants.METHOD_OF_CLIENT_PROTECTPROJECTINFOLIST);
			protectList = new JSONParseUtils().getProtectProInfo(stream);
			if(protectList != null && protectList.size() > 0){
				for(HashMap<String,Object> mContainerMap : protectList){
					if(mContainerMap != null && mContainerMap.size() > 0){
						HashMap<String, String>  baseMap = (HashMap<String, String>) mContainerMap.get("BaseInfo");
						if(baseMap != null && baseMap.size() > 0){
							double x=Double.valueOf(baseMap.get("x"));
						    double y=Double.valueOf(baseMap.get("y"));
							LatLng point = new LatLng(y, x);
							drawPoint(point,R.drawable.map_danger_dot_blue);
							if("1".equals(baseMap.get("IsWaring"))){
								protectWarningList.add(mContainerMap);
							}
						}
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取违规建筑数据
	 */
	public void getIllegalPro(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("MetroLineID", mCurrentLineId);
		String stream=null;
		try{
			stream = WebServiceUtils.requestM(getActivity(), map, Constants.METHOD_OF_CLIENT_GETDANGERPROJECTLIST);
			illegalList = new JSONParseUtils().getIllegalProInfo(stream);
			if(illegalList != null && illegalList.size() > 0){
				for(HashMap<String,Object> illegalMap : illegalList){
					ArrayList<HashMap<String, String >> pointList = (ArrayList<HashMap<String, String>>) illegalMap.get("IllegalPoints");
					if(pointList != null && pointList.size() > 0){
						HashMap<String, String> pointMap = pointList.get(0);
						double x=Double.valueOf(pointMap.get("x"));
					    double y=Double.valueOf(pointMap.get("y"));
					    LatLng point = new LatLng(y, x);
						drawPoint(point,R.drawable.map_danger_dot_red);	
					}
					HashMap<String, String>  baseMap = (HashMap<String, String>) illegalMap.get("BaseInfo");
					if(baseMap != null && baseMap.size() > 0){
						if("1".equals(baseMap.get("IsWaring"))){
							illegalWarningList.add(illegalMap);
						}
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 在地图上添加覆盖物
	 * @param point
	 */
	public void drawPoint(LatLng point,int drawable){
		//构建Marker图标  
		BitmapDescriptor bitmap = null;
		bitmap = BitmapDescriptorFactory.fromResource(drawable);
		
		//构建MarkerOption，用于在地图上添加Marker  
		OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);  
		//在地图上添加Marker，并显示  
		 mBaiduMap.addOverlay(option);
	}
	
	/**
	 * 刷新红线轮廓、隧道轮廓、车站轮廓
	 */
	public void refreshView() {
		badgeView.setText(String.valueOf(illegalWarningList.size()+protectWarningList.size()));
		badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView.show();
		if(mPreferences.getBoolean(Constants.PREFERENCES_INTERVAL, false)) {
			drawTunnelLine();
		}
		if(mPreferences.getBoolean(Constants.PREFERENCES_STATION, false)) {
			drawStationLine();
		}
		if(mPreferences.getBoolean(Constants.PREFERENCES_RED_LINE, false)) {
			drawRedLine();
		}
	}
	
	/**
	 * 将地铁线路显示在地图上
	 */
	public void drawTunnelLine() {
//		String metroLineID = mTaskDetail.get("MetroLineID");	//地铁id
		String metroLineID = mCurrentLineId;	//地铁id
		int startStation = 0;	//起始车站
		int endStation = 0;		//结束车站
		
//		if(metroLineID == null){
//			return;
//		}
		try {
			startStation = Integer.parseInt(mStartStation);
			endStation = Integer.parseInt(mEndStation);
		} catch(Exception e) {
			return;
		}
		if(startStation > endStation) {
			int temp = startStation;
			startStation = endStation;
			endStation = temp;
		}
		
		ArrayList<String> stationList = new DBService(getActivity()).getStationList(metroLineID, startStation, endStation);//按照站台顺序存放所以需要画红线的站台ID
		//绘制上行的站台以及区间的红线
		if(stationList != null && stationList.size() > 0) {
			int len = stationList.size();
			
			for(int i = 0; i < len; i ++){
				String mStation = stationList.get(i);
				if(i == len - 1) {
					break;
				}
				String mEndStation = stationList.get(i + 1);
				
				HashMap<String, Object> mGroupLeftTunnelPointList = new DBService(getActivity()).getTunnelsList(metroLineID, mStation, mEndStation);
				HashMap<String, Object> mGroupRightTunnelPointList = new DBService(getActivity()).getTunnelsList(metroLineID, mEndStation, mStation);
				
				ArrayList<ArrayList<HashMap<String, String>>>  leftPointList=(ArrayList<ArrayList<HashMap<String, String>>>)mGroupLeftTunnelPointList.get("Shape");
				ArrayList<ArrayList<HashMap<String, String>>>  rightPointList=(ArrayList<ArrayList<HashMap<String, String>>>)mGroupRightTunnelPointList.get("Shape");
				if(leftPointList != null && leftPointList.size() > 0) {
					for(int j = 0;j < leftPointList.size();j++){
						ArrayList<HashMap<String, String>> list = leftPointList.get(j);
						if(list != null && list.size() > 0){
							List<LatLng> points = new ArrayList<LatLng>();
							for(int k=0;k<list.size();k++) {
								HashMap<String, String> pointMap=list.get(k);
								if(pointMap != null) {
										double x=Double.valueOf(pointMap.get("x"));
									    double y=Double.valueOf(pointMap.get("y"));
									    LatLng p= new LatLng(y,x);
									    points.add(p);
								}
							}
							mBaiduMap.addOverlay(MapUtils.drawLine(0xAA00FF00,0xAA00FF00, points));
						}
					}	
				}
//				
				if(rightPointList != null && rightPointList.size() > 0) {
					for(int j = 0;j < rightPointList.size();j++){
						ArrayList<HashMap<String, String>> list = rightPointList.get(j);
						if(list != null && list.size() > 0){
							List<LatLng> points = new ArrayList<LatLng>();
							for(int k=0;k<list.size();k++) {
								HashMap<String, String> pointMap=list.get(k);
								if(pointMap != null) {
										double x=Double.valueOf(pointMap.get("x"));
									    double y=Double.valueOf(pointMap.get("y"));
									    LatLng p= new LatLng(y,x);
									    points.add(p);
								}
							}
							mBaiduMap.addOverlay(MapUtils.drawLine(0xAA00FF00,0xAA00FF00, points));
						}
					}
				}
				
			}
		}
	}
	/**
	 * 将车站信息显示在地图上
	 */
	public void drawStationLine() {
		//as the query,it can get one
//		String metroLineID = mTaskDetail.get("MetroLineID");	//地铁id
		String metroLineID = mCurrentLineId;	//地铁id
		int startStation = 0;	//起始车站
		int endStation = 0;		//结束车站
		
//		if(metroLineID==null){
//			return;
//		}
		try {
			startStation = Integer.parseInt(mStartStation);
			endStation = Integer.parseInt(mEndStation);
		} catch(Exception e) {
			return;
		}
		if(startStation > endStation) {
			int temp = startStation;
			startStation = endStation;
			endStation = temp;
		}
		
		ArrayList<String> stationList = new DBService(getActivity()).getStationList(metroLineID, startStation, endStation);//按照站台顺序存放所以需要画红线的站台ID
		//绘制上行的站台以及区间的红线
		if(stationList != null && stationList.size() > 0) {
			int len = stationList.size();
			for(int i = 0; i < len; i ++){
				String mStation = stationList.get(i);
				HashMap<String, Object> mGroupLeftStationPointList = new DBService(getActivity()).getStationPointList(metroLineID, mStation);
				ArrayList<ArrayList<HashMap<String, String>>>  leftPointList=(ArrayList<ArrayList<HashMap<String, String>>>)mGroupLeftStationPointList.get("shape");
				if(leftPointList != null && leftPointList.size() > 0) {
					for(int j = 0;j < leftPointList.size();j++){
						ArrayList<HashMap<String, String>> list = leftPointList.get(j);
						if(list != null && list.size() > 0){
							List<LatLng> points = new ArrayList<LatLng>();
							for(int k=0;k < list.size();k++) {
								HashMap<String, String> pointMap = list.get(k);
								if(pointMap != null) {
									double x=Double.valueOf(pointMap.get("x"));
									double y=Double.valueOf(pointMap.get("y"));
									LatLng p= new LatLng(y,x);
								    points.add(p);
								}
							}
							mBaiduMap.addOverlay(MapUtils.drawLine(0xAA000000, points));
						}
					}					
				}
			}
		}
	}
	/**
	 * 绘制红线
	 * 
	 * */
	public void drawRedLine() {
		//红线
//		String metroLineID = mTaskDetail.get("MetroLineID");	//地铁id
		String metroLineID = mCurrentLineId;	//地铁id
		int startStation = 0;	//起始车站
		int endStation = 0;		//结束车站
		
//		if(metroLineID==null){
//			Toast.makeText(mContext, "地铁信息有误，无法显示红色警戒线", Toast.LENGTH_SHORT).show();
//			return;
//		}
		try {
			startStation = Integer.parseInt(mStartStation);
			endStation = Integer.parseInt(mEndStation);
		} catch(Exception e) {
			return;
		}
		if(startStation > endStation) {
			int temp = startStation;
			startStation = endStation;
			endStation = temp;
		}
		if(mUpPointList != null) {
			mUpPointList.clear();
		} else {
			mUpPointList = new ArrayList<LatLng>();
		}
		
		ArrayList<String> stationList = new DBService(getActivity()).getStationList(metroLineID, startStation, endStation);//按照站台顺序存放所以需要画红线的站台ID
		//绘制上行的站台以及区间的红线
		if(stationList != null && stationList.size() > 0) {
			int len = stationList.size();
			
			for(int i = 0; i < len; i ++){
				String mStation = stationList.get(i);
				if(i == len - 1) {
					//最后一个车站
					HashMap<String, Object> upStationPoints0 = new DBService(getActivity()).getRedLineStationList(metroLineID, "0", mStation);
					addPoints((ArrayList<HashMap<String, String>>)upStationPoints0.get("shape"));
					break;
				}
				String mEndStation = stationList.get(i + 1);
				
				if(mStation != null) {
					HashMap<String, Object> upStationPoints0 = new DBService(getActivity()).getRedLineStationList(metroLineID, "0", mStation);
					addPoints((ArrayList<HashMap<String, String>>)upStationPoints0.get("shape"));
					
					HashMap<String, Object> upLinePoints0 = new DBService(getActivity()).getRedLineList(metroLineID, "0", mStation, mEndStation);//上行
					addPoints((ArrayList<HashMap<String, String>>)upLinePoints0.get("shape"));
				}
			}
			
			
			for(int i = len - 1; i > -1; i --){
				String mStation = stationList.get(i);
				if(i == 0) {
					//最后一个车站
					HashMap<String, Object> upStationPoints0 = new DBService(getActivity()).getRedLineStationList(metroLineID, "1", mStation);
					addPoints((ArrayList<HashMap<String, String>>)upStationPoints0.get("shape"));
					break;
				}
				String mEndStation = stationList.get(i - 1);
				
				HashMap<String, Object> upStationPoints0 = new DBService(getActivity()).getRedLineStationList(metroLineID, "1", mStation);
				addPoints((ArrayList<HashMap<String, String>>)upStationPoints0.get("shape"));
				
				HashMap<String, Object> upLinePoints0 = new DBService(getActivity()).getRedLineList(metroLineID, "1", mStation, mEndStation);//上行
				addPoints((ArrayList<HashMap<String, String>>)upLinePoints0.get("shape"));
			}
		}
		
		if(mUpPointList != null && mUpPointList.size() > 0) {
			mUpPointList.add(mUpPointList.get(0));
		}
		OverlayOptions mOptions = MapUtils.drawLine(0xAAFF0000, mUpPointList);
		if(mOptions != null)
		mBaiduMap.addOverlay(mOptions);
	}
	/**
	 *  添加点到集合当中（把stationPointsList的点转换后放到pointList集合当中）
	 * @param stationPointsList
	 */
	private void addPoints(ArrayList<HashMap<String, String>> stationPointsList){
		if(stationPointsList != null && stationPointsList.size() > 0) {
			for(HashMap<String,String> mPoint : stationPointsList) {
				double x=Double.valueOf(mPoint.get("x"));
			    double y=Double.valueOf(mPoint.get("y"));
			    LatLng point =new LatLng(y,x);
        		mUpPointList.add(point);
        	}
		}	
	}
	
	 /** 
	  * 地图 Marker 覆盖物点击事件监听函数 
	  * @param marker 被点击的 marker 
	  */ 
	OnMarkerClickListener listener = new OnMarkerClickListener() {  
	    
	    public boolean onMarkerClick(Marker marker){
	    	LatLng point = marker.getPosition();
	    	String lon = String.valueOf(point.longitude);
	    	String lat = String.valueOf(point.latitude);
	    	
	    	if(protectList != null && protectList.size() > 0){
	    		for(HashMap<String,Object> mContainerMap : protectList){
					if(mContainerMap != null && mContainerMap.size() > 0){
						HashMap<String, String>  baseMap = (HashMap<String, String>) mContainerMap.get("BaseInfo");
						if(baseMap != null && baseMap.size() > 0){
							if(lat.equals(baseMap.get("y")) && lon.equals(baseMap.get("x"))){
								Intent intent = new Intent(getActivity(),PatroInfoDetailAct.class);
								intent.putExtra("protectProInfo",mContainerMap);
								startActivity(intent);
							}
						}
					}
				}
	    	}
	    	
	    	if(illegalList != null && illegalList.size() > 0){
				for(HashMap<String,Object> illegalMap : illegalList){
					ArrayList<HashMap<String, String >> pointList = (ArrayList<HashMap<String, String>>) illegalMap.get("IllegalPoints");
					if(pointList != null && pointList.size() > 0){
						HashMap<String, String> pointMap = pointList.get(0);
						if(lat.equals(pointMap.get("y")) && lon.equals(pointMap.get("x"))){
							Intent intent = new Intent(getActivity(),PatroDangerBuildingDetailAct.class);
							intent.putExtra("illegalProInfo",illegalMap);
							startActivity(intent);
							return true;
						}
					}
				}
			}
	    	return true;
	    }  
	};
	
	/** 隐藏默认的缩放图片*/
	private void hideDeafalutZoom(){
		mMapView.showZoomControls(false);
	}
	
	/**
	 * 在百度地图上显示当前位置
	 * @author ylq
	 */
	/*public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null){
				return;
			}
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}*/
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constants.MSG_0x2001:
			refreshView();
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBtnSliding:		//返回菜单
				if(MainAct.mSlidingMenu != null) {
						MainAct.mSlidingMenu.showMenu();
						AnimationDrawable mAnimationDrawable = getFrameAnimMenuMenuMenu();
						mImgBtnSliding.setBackgroundDrawable(mAnimationDrawable);
						mAnimationDrawable.start();
				}
				break;
		case R.id.imgBtnMapZoom: 
				mMapView.getMap().setMapStatus(MapStatusUpdateFactory.zoomIn());
			break;
		case R.id.imgBtnMapDecrease:
				mMapView.getMap().setMapStatus(MapStatusUpdateFactory.zoomOut());
			break;
		case R.id.imgBtnSwitch:
			  if(isOpenLineSelectDialog) {
				  mLineSelectContainer.setVisibility(View.GONE);
				  isOpenLineSelectDialog = false;
				  //mRedLineSelectedI.selectedRedLine();
				  sendMessage(Constants.MSG_0x2001);
			  } else {
				  mLineSelectContainer.setVisibility(View.VISIBLE);
				  isOpenLineSelectDialog = true;
			  }
			break;
		default:
			break;
		}
	}
	/**
	 * 点击监听事件
	 */
	OnItemClickListener itemSelectedListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			if (!mAllTreesList.get(arg2).isHasChild()) {
//				Intent intent = new Intent(mContext,MonitorStructureAct.class);
//				intent.putExtra("MonitorCode", mAllTreesList.get(arg2).getmNodeCode());
//				startActivity(intent);
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
	
class monitorInfoAdapter extends BaseAdapter{
		
		private Context mContext;
		private int mResourceId;
		private List<LineTreeNode> mTreeList;
		private LayoutInflater mInflater;
		
		public monitorInfoAdapter(Context context, int resuorceId, List<LineTreeNode> treeList){
			this.mContext = context;
			this.mResourceId = resuorceId;
			this.mTreeList = treeList;
			this.mInflater = LayoutInflater.from(context);
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
			mViewHolder.mBadger=(LinearLayout)arg1.findViewById(R.id.badger_container);
			mViewHolder.badgeView=new BadgeView(mContext,mViewHolder.mBadger);
			mViewHolder.badgeView.setTextSize(10);
			arg1.setTag(mViewHolder);	
			LineTreeNode treeElement = mTreeList.get(arg0);
			int level = treeElement.getmNodeLevel();
			if(1 == level){
				if(isIllegal){
					mViewHolder.badgeView.setText(String.valueOf(illegalWarningList.size()));
				}else{
					mViewHolder.badgeView.setText(String.valueOf(protectWarningList.size()));
				}			
				mViewHolder.badgeView.show();
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
				if(level > 1){
					mViewHolder.mOperationBtn.setVisibility(View.GONE);
				}
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
			private BadgeView badgeView;
			private LinearLayout mBadger;
		}
		
	}
	
	
}
