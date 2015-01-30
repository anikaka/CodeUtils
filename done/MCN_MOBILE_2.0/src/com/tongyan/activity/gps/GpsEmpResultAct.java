package com.tongyan.activity.gps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.R;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.entities._GpsEmp;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.WebServiceUtils;

import android.content.Context;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @ClassName P04_GpsEmpResultAct.java
 * @Author wanghb
 * @Date 2013-9-25 am 09:56:38
 * @Desc 在岗查询列表
 */
public class GpsEmpResultAct extends AbstructCommonActivity {
	
	private MapView mMapView = null; 
	private Context mContext = this;
	private Button mRefreshBtn;//zoomAddBtn,zoomPlusBtn,
	
	LocationListener mLocationListener = null;//onResume时注册此listener，onPause时需要Remove
	
	private _User localUser;
	private String isSucc;
	private MyApplication myApp;
	
	List<_GpsEmp> gpsEmpList = null;
	
	//结点索引
	private Button mBtnPre,mBtnNext;
	int nodeIndex = 0;//节点索引,供浏览节点时使用
	private TextView  popupText = null;//泡泡view
	private View viewCache = null;
	
	private String mTimeHide, mEmpId;
	
	private BaiduMap mBaiduMap;
	
	BitmapDescriptor mStart = BitmapDescriptorFactory.fromResource(R.drawable.map_icon_start);
	BitmapDescriptor mNode = BitmapDescriptorFactory.fromResource(R.drawable.map_icon_node);
	BitmapDescriptor mEnd = BitmapDescriptorFactory.fromResource(R.drawable.map_icon_end);
	
	private InfoWindow mInfoWindow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();  
		businessM();
	}
	
	
	public void initPage() {
		//注意：请在试用setContentView前初始化BMapManager对象，否则会报错  
		setContentView(R.layout.gps_emp_result_route);
		mMapView=(MapView)findViewById(R.id.p25_gps_bmapsView);
		//zoomAddBtn = (Button)findViewById(R.id.p25_gps_zoom_controls_add_btn);
		//zoomPlusBtn = (Button)findViewById(R.id.p25_gps_zoom_controls_plus_btn);
		mRefreshBtn = (Button)findViewById(R.id.p25_gps_click_refresh);
		
		mBtnPre = (Button)findViewById(R.id.p25_gps_route_controls_pre_btn);
		mBtnNext = (Button)findViewById(R.id.p25_gps_route_controls_next_btn);
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		
		viewCache = getLayoutInflater().inflate(R.layout.gps_time_pop, null);
        popupText =(TextView) viewCache.findViewById(R.id.p25_gps_current_time_text);
	}
	
	
	
	public void setClickListener() {
		//zoomAddBtn.setOnClickListener(addZoomListener);
		//zoomPlusBtn.setOnClickListener(plusZoomListener);
		mRefreshBtn.setOnClickListener(mRefreshBtnListener);
		
		mBtnPre.setOnClickListener(mBtnPreListener);
		mBtnNext.setOnClickListener(mBtnNextListener);
	}
	
	public void businessM() {
		//获取标段数据
		myApp = ((MyApplication)getApplication());
		myApp.addActivity(this);
		localUser = myApp.localUser;
		mBaiduMap = mMapView.getMap();
		LatLng point =new LatLng(Constansts.mLat, Constansts.mLon);
		mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(point));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(10.0f));//(new BaiduMapOptions().mapStatus(new MapStatus.Builder().target(point).build()));
		if(getIntent() != null && getIntent().getExtras() != null) {
			mTimeHide = (String) getIntent().getExtras().get("mTimeHide");
			mEmpId = (String) getIntent().getExtras().get("mEmpId");
			getEmpRoutes(mEmpId, mTimeHide);
		} 
	}
	
	public void animateToRoute(){
		if(gpsEmpList != null && gpsEmpList.size() > 0) {
			mBaiduMap.clear();
			List<LatLng> mPointList = new ArrayList<LatLng>();
			for(int i = 0,len = gpsEmpList.size(); i < len; i ++) {
				_GpsEmp emp = gpsEmpList.get(i);
				if(emp != null) {
					LatLng mLatLng = new LatLng(Double.valueOf(emp.getLat()), Double.valueOf(emp.getLng()));
					if(i == 0) {
						mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(mLatLng));
						mBaiduMap.addOverlay(new MarkerOptions().position(mLatLng).icon(mStart).zIndex(9).draggable(true));
					} else if(i == len - 1) {
						mBaiduMap.addOverlay(new MarkerOptions().position(mLatLng).icon(mEnd).zIndex(9).draggable(true));
					} else {
						mBaiduMap.addOverlay(new MarkerOptions().position(mLatLng).icon(mNode).zIndex(9).draggable(true));
					}
					mPointList.add(mLatLng);
				}
			}
			
			OverlayOptions ooPolyline = new PolylineOptions().width(10).color(0xAAFF0000).points(mPointList);
			mBaiduMap.addOverlay(ooPolyline);
			//setDragListener();
			mBtnPre.setVisibility(View.VISIBLE);
			mBtnNext.setVisibility(View.VISIBLE);
		} else {
			mBaiduMap.clear();
			mBtnPre.setVisibility(View.INVISIBLE);
			mBtnNext.setVisibility(View.INVISIBLE);
			Toast.makeText(this, "无路径信息", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void getEmpRoutes(final String mEmpId,final String beginTime) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String params = "{sdate:'" + beginTime + "',edate:'" + beginTime + "', emp_id:'" + mEmpId + "'}";
					String str = WebServiceUtils.getRequestStr(localUser.getUsername(), localUser.getPassword(), null, null, "GetEmpGpsRoad", params,
							Constansts.METHOD_OF_GETLISTNOPAGE, mContext);
					Map<String,Object> mR = new Str2Json().getListNoPage(str);
					if (mR != null) {
						isSucc = (String)mR.get("s");
						if ("ok".equals(isSucc)) {
							gpsEmpList = (List<_GpsEmp>)mR.get("v");
							sendMessage(Constansts.SUCCESS);
						} else {
							sendMessage(Constansts.ERRER);
						}
					} else {
						sendMessage(Constansts.CONNECTION_TIMEOUT);
					}
				} catch (Exception e) {
					sendMessage(Constansts.CONNECTION_TIMEOUT);
					e.printStackTrace();
				}
			};
		}).start();
	}
	
	public void mNodeClick(View v){
		if (mBtnPre.equals(v)){
			nodeIndex--;
		}
		if (mBtnNext.equals(v)){
			nodeIndex++;
		}
        if (gpsEmpList != null && nodeIndex <= 0) {
        	nodeIndex = gpsEmpList.size() - 1;
        } else if(gpsEmpList != null && nodeIndex >= gpsEmpList.size() - 1) {
        	nodeIndex = 0;
        }
		//上一个节点
		_GpsEmp emp = gpsEmpList.get(nodeIndex);
		if (emp != null) {
			showRouteInfo(emp);
		}
	}
	
	
	public void showRouteInfo(_GpsEmp emp) {
		popupText.setText(emp.getGpsDate());
		LatLng mLatLng = new LatLng(Double.valueOf(emp.getLat()), Double.valueOf(emp.getLng()));
		viewCache.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mInfoWindow = new InfoWindow(viewCache, mLatLng, null);
		mBaiduMap.showInfoWindow(mInfoWindow);
		mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(mLatLng));
	}
	
	OnClickListener mRefreshBtnListener = new OnClickListener() {
		public void onClick(View v) {
			getEmpRoutes(mEmpId,mTimeHide);
		}
	};
	
	OnClickListener mBtnPreListener = new OnClickListener() {
		public void onClick(View v) {
			mNodeClick(v);
		}
	};
	
	OnClickListener mBtnNextListener = new OnClickListener() {
		public void onClick(View v) {
			mNodeClick(v);
		}
	};
	
	@Override
	protected void onDestroy() {
		if(mStart != null) {
			mStart.recycle();
			mStart = null;
		}
		if(mNode != null) {
			mNode.recycle();
			mNode = null;
		}
		if(mEnd != null) {
			mEnd.recycle();
			mEnd = null;
		}
		if(mMapView != null)
			mMapView.onDestroy();
		super.onDestroy();
	}
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			animateToRoute();
			break;
		case Constansts.ERRER:
			Toast.makeText(this, isSucc, Toast.LENGTH_SHORT).show();
			break;
		case Constansts.NET_ERROR:
			Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.CONNECTION_TIMEOUT:
			Toast.makeText(this, "连接超时", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
}
