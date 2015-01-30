package com.tongyan.activity.gps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.R;
import com.tongyan.activity.adapter.GpsProjectListAdapter;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.db.DBSectionService;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.MapUtils;
import com.tongyan.utils.WebServiceUtils;
/**
 * 
 * @className P26_ProjectListAct
 * @author wanghb
 * @date 2014-5-5 AM 09:10:29
 * @Desc 工程订阅
 */
public class GpsProjectLocAct extends AbstructCommonActivity {
	
	private Context mContext = this;
	
	private MapView mMapView;
	private Button mListBtn;
	private Button  mSectionBtn;
	
	private _User mLocalUser;
	private String mIsSucc;
	private Dialog mDialog;
	private MyApplication mApplication;
	private ArrayList<HashMap<String, Object>> mProjectList = new ArrayList<HashMap<String, Object>>();
	//百度地图自带的定位
	private LocationClient mLocClient = null;
	private MLocalListenner mMLocalListener = new MLocalListenner();
	//手机带的GPS定位
	private LocationManager mGpsManager;
	private Location mLocation;
	private double mLongitude;//经度
	private double mLatitude;//纬度
	private LatLng mGeoPoint = null;
	//
	private String mSelectMethod = "1"; // GPS : 1/GPRS : 2
	private Timer mTimer;
	private boolean mShowRight = false;
	private BaiduMap mBaiduMap;
	private ListView mListView;
	private GpsProjectListAdapter mAdapter;
	
	// 初始化全局 bitmap 信息，不用时及时 recycle
	private BitmapDescriptor mLocalPoint = BitmapDescriptorFactory.fromResource(R.drawable.map_dot_orange);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPageView();
		 businessM();
	}
	
	public void initPageView() {
		setContentView(R.layout.gps_project_loc);
		mMapView = (MapView)findViewById(R.id.p26_gps_bmapsView);
		mListBtn = (Button)findViewById(R.id.p26_title_list_btn);
		mSectionBtn = (Button)findViewById(R.id.p26_title_menu_btn);
		mListView = (ListView)findViewById(R.id.p27_project_loc_listview);
		mAdapter = new GpsProjectListAdapter(mContext, mProjectList, R.layout.gps_project_list_item);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(mListViewClick);
		mListBtn.setOnClickListener(mListClick);
		mSectionBtn.setOnClickListener(mSectionsLinstener);
	}
	
	public void businessM() {
		mApplication = ((MyApplication)getApplication());
		mApplication.addActivity(this);
		mLocalUser = mApplication.localUser;
		mBaiduMap = mMapView.getMap();
		LatLng point =new LatLng(Constansts.mLat,Constansts.mLon);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(10.0f));
		refreshMapView(MapStatusUpdateFactory.newLatLng(point));
		initMap();
		//showMDialog();
	}
	
	OnItemClickListener mListViewClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			GpsProjectListAdapter.ViewHolder mViewHolder = (GpsProjectListAdapter.ViewHolder)arg1.getTag();
			if(mViewHolder != null && mViewHolder.mProject != null) {
				int index = Integer.parseInt((String)mViewHolder.mProject.get("index"));
				refreshMapView(index);
				mListView.setVisibility(View.GONE);
				mShowRight = false;
			}
		}
	};
	
	OnClickListener mSectionsLinstener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext, SectionIndexAct.class);
			startActivity(intent);
		}
	};
	
	/**
	 * 首次进入界面，开启dialog
	 *//*
	public void showMDialog() {
		final Dialog mGpsSettingDialog =  new Dialog(mContext, R.style.dialog);
		mGpsSettingDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mGpsSettingDialog.setContentView(R.layout.common_checking_gps_select_dialog);
		mGpsSettingDialog.setCanceledOnTouchOutside(false);
		Button mSureBtn = (Button)mGpsSettingDialog.findViewById(R.id.checking_gps_selectd_sure_btn);
		Button mCancelBtn = (Button)mGpsSettingDialog.findViewById(R.id.checking_gps_selectd_cancel_btn);
		final CheckBox mGprsCheckBox = (CheckBox)mGpsSettingDialog.findViewById(R.id.gprs_checkbox);
		final CheckBox mGpsCheckBox = (CheckBox)mGpsSettingDialog.findViewById(R.id.gps_checkbox);
		//判断是否开启GPS功能，否则开启该功能。目前均采用GPS
		final LocationManager alm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE );
		if (!alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			mGpsCheckBox.setChecked(false);
			mGprsCheckBox.setChecked(true);
			mSelectMethod = "2";
		} else {
			mGpsCheckBox.setChecked(true);
			mGprsCheckBox.setChecked(false);
			mSelectMethod = "1";
		}
		
		mSureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(null != mGpsSettingDialog) {
					mGpsSettingDialog.dismiss();
				}
				if("GPS".equals(mSelectMethod) && !alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
					Toast.makeText(mContext, "请打开GPS", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivityForResult(intent, 123);
				}
				initMap();
			}
		});
		mCancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(null != mGpsSettingDialog) {
					mGpsSettingDialog.dismiss();
				}
			}
		});
		
		
		mGprsCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					mGprsCheckBox.setChecked(true);
					mGpsCheckBox.setChecked(false);
					mSelectMethod = "2";
				} else {
					mGprsCheckBox.setChecked(false);
					mGpsCheckBox.setChecked(true);
					mSelectMethod = "1";
				}
			}
		});
		
		mGpsCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					mGprsCheckBox.setChecked(false);
					mGpsCheckBox.setChecked(true);
					mSelectMethod = "1";
				} else {
					mGprsCheckBox.setChecked(true);
					mGpsCheckBox.setChecked(false);
					mSelectMethod = "2";
				}
			}
		});
	}
	
	*//**
	 * 根据选择来初始化地图-所有项
	 */
	public void initMap() {
		mDialog = new AlertDialog.Builder(this).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		mTimer = new Timer();
		final LocationManager alm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE );
		if(!alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			Toast.makeText(mContext, "GPS未开启，将会用基站定位", Toast.LENGTH_SHORT).show();
			mSelectMethod = "2";
		}
		
		if("1".equals(mSelectMethod)) {
			mGpsManager  = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE); 
			//从可用的位置提供器中，匹配以上标准的最佳提供器
			final String provider = mGpsManager.getBestProvider(MapUtils.getCriteria(), true);
			if(mGpsManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || mGpsManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				mGpsManager.requestLocationUpdates(provider, 1000, 1, locationListener);
				mLocation = mGpsManager.getLastKnownLocation(provider);
				mTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						mLocation = mGpsManager.getLastKnownLocation(provider);
						if(mLocation != null) {
							mLatitude = mLocation.getLatitude();
							mLongitude = mLocation.getLongitude();
							if(mLatitude != 0 & mLongitude != 0) {
								refeshRequest();
								this.cancel();
							}
						} 
					}
				},1000, 2000);
			} else {
				mGprsLocation();
			}
		} else {
			mGprsLocation();
		}
	}
	
	public void mGprsLocation() {
		mSelectMethod = "2";
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(mMLocalListener);
		setLocationOption();
        mLocClient.start();
        mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(mLatitude != 0 & mLongitude != 0) {
					this.cancel();
					refeshRequest();
				} else {
					if(mLocClient != null) {
						mLocClient.requestLocation();
					}
				}
			}
		},5000,3000);
	}
	
	
	
	public void refeshRequest() {
		if("1".equals(mSelectMethod)) {
			mGeoPoint = MapUtils.getGeoPointByGps(mLatitude, mLongitude);//WGS84 --> bd00ll
		} else {
			mGeoPoint = MapUtils.getGeoPointByBaidu(mLatitude, mLongitude);
		}
		new Thread(new Runnable(){
			@Override
			public void run() {
				Map<String,Object> mR = null;
				try {
					//mLatitude = 28.052324;
					//mLongitude = 115.861751;
					String params = "{Lng:'"+ mGeoPoint.longitude +"',Lat:'"+ mGeoPoint.latitude +"',Gtype:'"+ mSelectMethod +"'}";
					String str = WebServiceUtils.getRequestStr(mLocalUser.getUsername(), mLocalUser.getPassword(), null, null, "GetNearBySubitem", params, Constansts.METHOD_OF_GETLISTNOPAGE, mContext);
					mR = new Str2Json().getProjectLocList(str);
					if(mR != null) {
						mIsSucc = (String)mR.get("s");
						if("ok".equals(mIsSucc)) {
							if(mProjectList != null) {
								mProjectList.clear();
							}
							ArrayList<HashMap<String, Object>> mList = (ArrayList<HashMap<String, Object>>)mR.get("v");
							if(mList != null && mList.size() > 0) {
								mProjectList.addAll(mList);
								sendMessage(Constansts.SUCCESS);
							} else {
								sendMessage(Constansts.MES_TYPE_1);
							}
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
			}
		}).start();
		
	}
	
	public void refreshMapView(int index) {
		if(mBaiduMap!=null){			
			mBaiduMap.clear();
		}
		if (mGeoPoint != null) {
			if(mLocalPoint == null) {
				mLocalPoint = BitmapDescriptorFactory.fromResource(R.drawable.map_dot_orange);
			}
			OverlayOptions ooA = new MarkerOptions().position(mGeoPoint).icon(mLocalPoint);
			addMapOverlay(ooA);
		}
		if(mProjectList != null && mProjectList.size() > 0) {
			HashMap<String, Object> map = mProjectList.get(index);
			ArrayList<LatLng> mGeoPointList = new ArrayList<LatLng>();
			if(map != null) {
				ArrayList<HashMap<String, String>> coordinatesList = (ArrayList<HashMap<String, String>>)map.get("latlngs");
				if(coordinatesList != null && coordinatesList.size() > 0) {
					for(int i = 0, len = coordinatesList.size(); i < len; i ++) {
						HashMap<String, String> m = coordinatesList.get(i);
						if(m != null) {
							String mLat = m.get("Lat");
							String mLong = m.get("Lng");
							LatLng mPoint = MapUtils.getGeoPointByBaidu(Double.parseDouble(mLat), Double.parseDouble(mLong));
							mGeoPointList.add(mPoint);
							OverlayOptions ooDot = new DotOptions().center(mPoint).radius(6).color(0xFF0000FF);
							addMapOverlay(ooDot);
							if(i == len - 1) {
								refreshMapView(MapStatusUpdateFactory.newLatLng(mPoint));
							}
						}
					}
				}
				//将工程坐标展示在地图上
				OverlayOptions ooPolyline = new PolylineOptions().width(10).color(0xAAFF0000).points(mGeoPointList);
				addMapOverlay(ooPolyline);
				String s = (String)map.get("subId");
				String secId = (String)map.get("secId");
				if(s != null) {
					if(new DBSectionService().getSectionIsParent(s, secId) == 0) {
						showDetailDialog(map);//非更多
					} else {
						showDetailMoreDialog(map);//更多
					}
				}
			}
		}
		if(mDialog != null) {
			mDialog.dismiss();
		}
	}
	
	public void addMapOverlay(OverlayOptions mOptions) {
		try {
			mBaiduMap.addOverlay(mOptions);//防止出sdk内的异常，避免程序奔溃
		} catch(Exception e) {
			
		}
		
	}
	
	public void refreshMapView(MapStatusUpdate mStatus) {
		try {
			mBaiduMap.animateMapStatus(mStatus);
		} catch(Exception e) {
			
		}
	}
	
	
	
	public void showDetailMoreDialog(final HashMap<String, Object> map) {
		Dialog mDialog = new AlertDialog.Builder(this).create();
	    Window window = mDialog.getWindow(); 
	    window.setWindowAnimations(R.style.dialog_anim_bottom_top);  //添加动画  
		mDialog.show();
		//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.gps_section_detail_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		TextView NameText = (TextView)mDialog.findViewById(R.id.p26_gps_project_name);
		TextView CodeText = (TextView)mDialog.findViewById(R.id.p26_gps_project_code);
		NameText.setText((String)map.get("subName"));
		CodeText.setText("编号:" + map.get("subCode"));
		
		Button ProgressBtn = (Button)mDialog.findViewById(R.id.p26_gps_project_info_progress);
		Button MeasureBtn = (Button)mDialog.findViewById(R.id.p26_gps_project_info_measure);
		Button PhotographBtn = (Button)mDialog.findViewById(R.id.p26_gps_project_info_photograph);
		Button btnCommand=(Button)mDialog.findViewById(R.id.p26_gps_supervisor_command_btn);
		ProgressBtn.setVisibility(View.INVISIBLE);
		PhotographBtn.setVisibility(View.INVISIBLE);
		btnCommand.setVisibility(View.GONE);
		MeasureBtn.setText("更多");
		
		MeasureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, SectionIndexAct.class);
				intent.putExtra("MoreDetail", map);
				startActivity(intent);
			}
		});
	}
	
	
	/**
	 * 
	 */
	private Dialog mDetailDialog = null;
	public void showDetailDialog(final HashMap<String, Object> map) {
			mDetailDialog = new AlertDialog.Builder(this).create();
		    Window window = mDetailDialog.getWindow(); 
		    window.setWindowAnimations(R.style.dialog_anim_bottom_top);  //添加动画  
		    mDetailDialog.show();
			//注意此处要放在show之后 否则会报异常
		    mDetailDialog.setContentView(R.layout.gps_section_detail_dialog);
		    mDetailDialog.setCanceledOnTouchOutside(false);
			TextView NameText = (TextView)mDetailDialog.findViewById(R.id.p26_gps_project_name);
			TextView CodeText = (TextView)mDetailDialog.findViewById(R.id.p26_gps_project_code);
			NameText.setText((String)map.get("subName"));
			CodeText.setText("编号:" + map.get("subCode"));
			final String sid = (String)map.get("subId");
			
			
			Button ProgressBtn = (Button)mDetailDialog.findViewById(R.id.p26_gps_project_info_progress);
			Button MeasureBtn = (Button)mDetailDialog.findViewById(R.id.p26_gps_project_info_measure);
			Button PhotographBtn = (Button)mDetailDialog.findViewById(R.id.p26_gps_project_info_photograph);
			Button btnCommand=(Button)mDetailDialog.findViewById(R.id.p26_gps_supervisor_command_btn);
			
			ProgressBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, SectionIndexDetailAct.class);
					intent.putExtra("subId", sid);
					intent.putExtra("type", "progress");
					startActivity(intent);
				}
			});
			
			MeasureBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, SectionIndexDetailAct.class);
					intent.putExtra("subId", sid);
					intent.putExtra("type", "measure");
					startActivity(intent);
				}
			});
			
			PhotographBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, SectionCameraAct.class);
					intent.putExtra("CacheMap", map);
					startActivity(intent);
				}
			});
			
			btnCommand.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//TODO
					final Dialog d = new AlertDialog.Builder(mContext).create();
					if(d!=null){
						d.show();
						//注意此处要放在show之后 否则会报异常
						d.setContentView(R.layout.common_supervisor_command_select_dialog);
						d.setCanceledOnTouchOutside(false);
					}
					final CheckBox   monitorOrder =(CheckBox)d.findViewById(R.id.gps_checkbox); //监控指令
			        final CheckBox   projectOfficeOrder=(CheckBox)d.findViewById(R.id.gprs_checkbox); //项目办指令
			        final Button btnConfirm=(Button)d.findViewById(R.id.checking_gps_selectd_sure_btn);  //确认按钮
			        final Button btnCancel=(Button)d.findViewById(R.id.checking_gps_selectd_cancel_btn);  //取消按钮
			        		        
			        monitorOrder.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							projectOfficeOrder.setChecked(false);
						}
					});
			        
			        
			        projectOfficeOrder.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							monitorOrder.setChecked(false);
							
						}
					});
			        
			        btnConfirm.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Bundle bundle=new Bundle();
							bundle.putString("newId", map.get("subId").toString());//工程编号Id
							bundle.putString("sectionId", map.get("secId").toString()); //标段Id
							if(monitorOrder.isChecked()){
								bundle.putString("commandType","0");//指令类型 //监控指令0  项目办指令1
							}
							if(projectOfficeOrder.isChecked()){
								bundle.putString("commandType","1");
							}
							if(monitorOrder.isChecked() || projectOfficeOrder.isChecked()){		
								if(d!=null){
									d.cancel();
								}
								Intent intent=new Intent(mContext, CommandAct.class);
								intent.putExtra("commandBundle",bundle); //工程编号Id
								startActivity(intent);
							}
						}
					});
			        
			        btnCancel.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							d.dismiss();
						}
					});
				}
			});
	}
	
	@Override
	protected void onDestroy() {
		if (mLocClient != null) {
			mLocClient.stop();
			mLocClient = null;
		}
		if(mLocalPoint != null) {
			mLocalPoint.recycle();
			mLocalPoint = null;
		}
		if(mDetailDialog != null) {
			mDetailDialog.dismiss();
		}
		if(mLocalPoint != null) {
			mLocalPoint.recycle();
			mLocalPoint = null;
		}
		
		mMapView.onDestroy();
		super.onDestroy();
	}
	
	OnClickListener mListClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(mProjectList == null || mProjectList.size() == 0) {
				Toast.makeText(mContext, "无周边工程数据", Toast.LENGTH_SHORT).show();
				return;
			}
			if(mShowRight) {
				mListView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.appear_top_right_out));
				mListView.setVisibility(View.GONE);
				mShowRight = false;
			} else {
				mListView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.appear_top_right_in));
				mListView.setVisibility(View.VISIBLE);
				mShowRight = true;
			}
		}
	};
	
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && mShowRight ) {
			mListView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.appear_top_right_out));
			mListView.setVisibility(View.GONE);
			mShowRight = false;
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 通过GPS定位
	 */
	private LocationListener locationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {//位置信息变化时触发
			if(location != null)
				mLocation = location;
			else 
				mLocation = null;
		}
		@Override
		public void onProviderDisabled(String provider) {}//GPS禁用时触发
		@Override
		public void onProviderEnabled(String provider) {}// GPS开启时触发
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}// GPS状态变化时触发
	};
	
	/**
	 * @className MLocalListenner
	 * @Desc 通过基站定位
	 */
	public class MLocalListenner implements BDLocationListener {
		public void onReceiveLocation(BDLocation location) {
			if (location == null || mMapView == null)
				return;
			mLongitude = location.getLongitude();
			mLatitude = location.getLatitude();
			
			LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			refreshMapView(u);
		}
		public void onReceivePoi(BDLocation arg0) {}
	}
	
	private void setLocationOption(){
 		LocationClientOption option = new LocationClientOption();
 		option.setOpenGps(true);				//打开gps
 		option.setCoorType("bd09ll");		//设置坐标类型
 		//option.setScanSpan(5000);	//设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
 		mLocClient.setLocOption(option);
 	}
	
	public void closeDialog() {
		if(mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
	}
	
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			closeDialog();
			refreshMapView(0);
			if(mAdapter != null) {
				mAdapter.notifyDataSetChanged();
			}
			break;
		case Constansts.ERRER:
			closeDialog();
			Toast.makeText(this, mIsSucc, Toast.LENGTH_SHORT).show();
			break;
		case Constansts.NET_ERROR:
			closeDialog();
			Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.CONNECTION_TIMEOUT:
			closeDialog();
			Toast.makeText(this, "连接超时", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.MES_TYPE_1:
			closeDialog();
			Toast.makeText(this, "暂无周边工程", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	};
	
}
