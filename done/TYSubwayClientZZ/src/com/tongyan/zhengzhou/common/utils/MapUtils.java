package com.tongyan.zhengzhou.common.utils;

import java.util.List;

import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;

public class MapUtils {
	
	public static OverlayOptions drawLine(int color,List<LatLng> points){
		OverlayOptions ooPolyline = null;
		try {
			ooPolyline = new PolylineOptions().width(5).color(color).points(points);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ooPolyline;
	}
	
	public static OverlayOptions drawLine(int lineColor,int fillColor,List<LatLng> points){
		OverlayOptions ooPolygon = null;
		try {
			ooPolygon = new PolygonOptions().points(points)
			.stroke(new Stroke(5, lineColor)).fillColor(fillColor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ooPolygon;
	}
	
}
