package com.TY.bhgis.Carto;

import java.util.Iterator;

import com.TY.bhgis.Database.IBHClass;
import com.TY.bhgis.Geometry.Envelope;
import com.TY.bhgis.Geometry.IEnvelope;
import com.TY.bhgis.Geometry.IGeometry;
import com.TY.bhgis.Geometry.ILineString;
import com.TY.bhgis.Geometry.ILinearRing;
import com.TY.bhgis.Geometry.IPoint;
import com.TY.bhgis.Geometry.IPolygon;
import com.TY.bhgis.Geometry.LinearRing;
import com.TY.bhgis.Geometry.Point;
import com.TY.bhgis.Geometry.Polygon;
import com.TY.bhgis.Geometry.IClone;
import com.TY.bhgis.MapFeature.HighWayLining;
import com.TY.bhgis.MapFeature.LiningPosition;
import com.TY.bhgis.MapLayer.IMapLayer;
import com.TY.bhgis.MapLayer.MapLayer;
import com.TY.bhgis.Util.mapUtil;

public final class Map implements IMap {
	
	private IMapLayer mapLayer;
	private String mapname;
	private IBHClass[] bhcClasses;
	private IEnvelope g;
	public static float dxy = 100;
	public static boolean flag = true;// true底图的里程从小到大 从进口开始

	public Map(String paramString, IMapLayer mapLayer, IBHClass[] bhcClasses) {
		this.mapname = paramString;
		this.mapLayer = mapLayer;
		this.bhcClasses = bhcClasses;
	}

	public Map() {
	}

	public final IEnvelope getFullExtent() {
		return (IEnvelope) ((IClone) this.g).Clone();
	}

	public final String getName() {
		return this.mapname;
	}

	public final void setName(String paramString) {
		this.mapname = paramString;
	}

	public final IEnvelope load( float LiningWidth,float LiningLength, int LiningCount) {

		try {
			IEnvelope envelope = new Envelope();
			envelope.setXMin(dxy - LiningWidth*2);
			envelope.setXMax(dxy + LiningLength + LiningWidth);
			envelope.setYMin(dxy);
			envelope.setYMax(dxy + LiningWidth * LiningCount );
			this.g = envelope;
			// // 环类型
			// switch (RingType) {
			// case 0:
			HighWayLining[] highWayLinings = new HighWayLining[LiningCount];
			IGeometry liningGeoSeed = getLiningGeoSeed(LiningWidth, LiningLength);
			LiningPosition[] liningPoistionGeoSeed = getLiningPositionGeoSeed(LiningWidth, LiningLength);
	
//			byte[] in = DataProvider.getImage(RingType);
//			Bitmap mBitmap = BitmapFactory.decodeByteArray(in, 0, in.length);
		
			for (int i = 0; i < LiningCount; i++) {
				LiningPosition[] liningPositions = null;
					liningPositions = new LiningPosition[liningPoistionGeoSeed.length];
					for (int j = 0; j < liningPoistionGeoSeed.length; j++) {
						IGeometry geo = movePolygon((IPolygon) (liningPoistionGeoSeed[j].getGeometry()),LiningWidth * i);
						LiningPosition liningPosition = new LiningPosition(geo,liningPoistionGeoSeed[j].getPosition());
						liningPositions[j] = liningPosition;
					}
				IGeometry geo = movePolygon((IPolygon) liningGeoSeed, LiningWidth* i);
				HighWayLining highWayLining = null;
				if (flag) {
					highWayLining = new HighWayLining(mapUtil.minLczh + i, geo,liningPositions);
				} else {
					highWayLining = new HighWayLining(mapUtil.maxLczh - i, geo,liningPositions);
				}
					//highWayLining.setBitmap(mBitmap);
				highWayLinings[i] = highWayLining;
			}
			this.mapLayer = new MapLayer(highWayLinings, LiningWidth, LiningLength);
			return (IEnvelope) ((IClone) envelope).Clone();
		} catch (Exception localException) {
			System.out.println("Map.load error:" + localException.getMessage());
		}
		return null;
	}

	@Override
	public IMapLayer getMapLayer() {
		return this.mapLayer;
	}

	@Override
	public void setMapLayer(IMapLayer mapLayer) {
		this.mapLayer = mapLayer;
	}

	@Override
	public IBHClass getBHClass(int type) {
		for (int i = 0; i < bhcClasses.length; i++) {
			if (bhcClasses[i].getBHType() == type) {
				return bhcClasses[i];
			}
		}
		return null;
	}

	@Override
	public IBHClass[] getBHClasses() {

		return this.bhcClasses;
	}

	@Override
	public void setBhClasses(IBHClass[] bhcClasses) {

		this.bhcClasses = bhcClasses;
	}

	@Override
	public void clearBHClasses() {

		if (this.bhcClasses == null) {
			return;
		}
		for (int i = 0; i < this.bhcClasses.length; i++) {
			this.bhcClasses[i] = null;
		}
	}

	private IGeometry getLiningGeoSeed(float LiningWidth, float LiningLength) {
		IPoint[] points = new Point[5];
		points[0] = new Point(dxy, dxy);
		points[1] = new Point(dxy + LiningLength, dxy);
		points[2] = new Point(dxy + LiningLength, dxy + LiningWidth);
		points[3] = new Point(dxy, dxy + LiningWidth);
		points[4] = new Point(dxy, dxy);
		ILinearRing linearRing = new LinearRing(points, 0);
		IGeometry geometry = new Polygon(linearRing);
		return geometry;
	}

	private LiningPosition[] getLiningPositionGeoSeed( float LiningWidth,
			float LiningLength) {
		
		java.util.Map<IPoint[], String> parts = mapUtil.getParts();
		LiningPosition[] liningPositions = new LiningPosition[parts.size()];
		// IGeometry[] geometries = new Polygon[parts.size()];
		Iterator it = parts.entrySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
			IPoint[] part = (IPoint[]) entry.getKey();
			IPoint[] points = new Point[part.length];
			for (int j = 0; j < points.length; j++) {

				points[j] = new Point(dxy + LiningLength * part[j].getX(), dxy
						+ LiningWidth * part[j].getY());
			}
			liningPositions[i] = new LiningPosition(new Polygon(new LinearRing(
					points, 0)), Integer.valueOf((String) entry.getValue()));
			i++;
		}
		return liningPositions;
	}

	private IGeometry movePolygon(IPolygon polygon, float width) {
		ILineString lineString = (ILineString) polygon.getExteriorRing();
		IPoint[] points = new Point[lineString.getNumPoints()];
		for (int i = 0; i < lineString.getNumPoints(); i++) {
			IPoint point = lineString.getPoint(i);
			IPoint pt = new Point(point.getX(), point.getY());
			pt.setY(pt.getY() + width);
			points[i] = pt;
		}
		ILinearRing linearRing = new LinearRing(points, 0);
		return (IGeometry) (new Polygon(linearRing));
	}





}
