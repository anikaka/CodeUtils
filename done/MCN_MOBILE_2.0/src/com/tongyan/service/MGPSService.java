package com.tongyan.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.baidu.mapapi.model.LatLng;
import com.tongyan.activity.MyApplication;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.db.DBService;
import com.tongyan.common.entities._LocalMsg;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.MapUtils;
import com.tongyan.utils.WebServiceUtils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
/**
 * 
 * @ClassName GPSService 
 * @author wanghb
 * @date 2013-7-26 am 10:00:15
 * @desc 用于GPS定位
 * 
 */
public class MGPSService extends Service {
	
	private Timer mTimer;
	private _User mLocalUser;
	private Map<String,String> mProperties;
	private double mLongitude;//经度
	private double mLatitude;//纬度
	private MyApplication mApplication;
	private LocationManager mGpsManager;
	private Criteria criteria;
	private Location mLocation;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mGpsManager  = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE); 
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mApplication = ((MyApplication)getApplication());
		if(mApplication.mIsLogin) {
			mLocalUser = mApplication.localUser;
			mProperties = new HashMap<String,String>();
			mProperties.put("publicKey", Constansts.PUBLIC_KEY);
			if(mLocalUser.getUserid() == null || "".equals(mLocalUser.getUserid())) {
				reGetCurrentUser();
			}
			mProperties.put("userName", mLocalUser.getUsername());
			mProperties.put("Password", mLocalUser.getPassword());
			mProperties.put("type", "AddGpsData");
			if(mGpsManager == null) {
				mGpsManager  = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE); 
			}
			if(criteria == null) {
				criteria = MapUtils.getCriteria();
			}
			//从可用的位置提供器中，匹配以上标准的最佳提供器
			final String provider = mGpsManager.getBestProvider(criteria, true); 
			if(provider != null) {
				mLocation = mGpsManager.getLastKnownLocation(provider);
				mGpsManager.requestLocationUpdates(provider, 60000, 1, locationListener);
				mTimer = new Timer();
				mTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						if(mLocalUser.getUserid() == null || "".equals(mLocalUser.getUserid())) {
							reGetCurrentUser();
						} 
						if(mLocation != null) {
							/*GeoPoint mG = getGeoPoint(mLocation.getLatitude(),mLocation.getLongitude());
							double mLa = mG.getLatitudeE6()/1E6;
							double mLo = mG.getLongitudeE6()/1E6;*/
							mLatitude = mLocation.getLatitude();
							mLongitude = mLocation.getLongitude();
							if(mLatitude != 0 && mLongitude != 0) {
								refeshRequest();
								//mLocation.reset();//Clears the contents of the location.
							}  /*else {
								Log.i("MGPSService", "mLatitude is null");
							}*/
						} else {
							mLocation = mGpsManager.getLastKnownLocation(provider);
						}
					}
				},3000, Constansts.BAIDU_MAP_TIME * 60 * 1000);//Constansts.BAIDU_MAP_TIME * 60 * 1000
				}
		}
		return Service.START_REDELIVER_INTENT;
	}
	
	String params = "";
	String exception = "";
	public void refeshRequest() {
		LatLng mLatLng = MapUtils.getGeoPointByGps(mLatitude, mLongitude);
		params = "{emp_id:'"+ mLocalUser.getUserid() +"',g_lat:'"+ mLatLng.latitude + "',g_lng:'"+ mLatLng.longitude +"',g_time:'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "'}";
		mProperties.put("parms", params);
		//
		try {
			String str = WebServiceUtils.requestM(mProperties, Constansts.METHOD_OF_ADD, MGPSService.this);
			Map<String,Object> mR = new Str2Json().addResult(str);
			String isSuc = "";
			if(mR != null) {
				isSuc = (String)mR.get("v");
			} 
			if(!"ok".equals(isSuc)) {
				insertMsg(params);
			} 
	} catch (Exception e) {
		insertMsg(params);
	}
	}
	
	
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
	
	public void reGetCurrentUser() {
		mLocalUser =  new DBService(MGPSService.this).getCurrentUser();
		((MyApplication)getApplication()).localUser = mLocalUser;
		((MyApplication)getApplication()).userId = mLocalUser.getUserid();
	}
	
	/**
	 * @param params
	 */
	public void insertMsg(String params) {
		_LocalMsg msg = new _LocalMsg();
		msg.setUsrid(mLocalUser.getUserid());
		msg.setParams(params);
		new DBService(MGPSService.this).inserLocs(msg);//TODO need?
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		this.stopSelf();
	}

}
