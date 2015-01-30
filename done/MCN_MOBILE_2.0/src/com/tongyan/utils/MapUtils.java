package com.tongyan.utils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;


import android.location.Criteria;

/**
 * 
 * @className GPSUtil
 * @author wanghb
 * @date 2014-5-8 PM 04:14:00
 * @Desc TODO
 */
public class MapUtils {
	
	/**
     * 返回查询条件
     * @return
     */
    public static Criteria getCriteria(){
        Criteria criteria=new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细 
        criteria.setAccuracy(Criteria.ACCURACY_FINE);    
        //设置是否要求速度
        criteria.setSpeedRequired(true);
        // 设置是否允许运营商收费  
        criteria.setCostAllowed(true);
        //设置是否需要方位信息
        criteria.setBearingRequired(true);
        //设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求  
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }
	
    /**
	 * Wgs84ToBaidu(GPS坐标转百度坐标)
	 * @param mLat
	 * @param mLon
	 * @return
	 */
    public static LatLng getGeoPointByGps(double mLat, double mLon) {
    	LatLng mGeoPoint = new LatLng(mLat , mLon);
    	LatLng mBDGeoPoint = new CoordinateConverter().from(CoordinateConverter.CoordType.GPS).coord(mGeoPoint).convert();//fromWgs84ToBaidu(mGeoPoint);
		return mBDGeoPoint;
	}
    
    /**
     * 实例化百度坐标的点
     * @param mLat
     * @param mLon
     * @return
     */
    public static LatLng getGeoPointByBaidu(double mLat, double mLon) {
    	return new LatLng(mLat ,  mLon);
    }
	
}
