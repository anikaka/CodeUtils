package com.TY.bhgis.MapFeature;

import com.TY.bhgis.Geometry.IEnvelope;
import com.TY.bhgis.Geometry.IGeometry;
import com.TY.bhgis.Geometry.ILinearRing;
import com.TY.bhgis.Geometry.IPoint;
import com.TY.bhgis.Geometry.LineString;
import com.TY.bhgis.Geometry.LinearRing;
import com.TY.bhgis.Geometry.Point;
import com.TY.bhgis.Geometry.Polygon;
import com.TY.bhgis.Util.mapUtil;

public class HighWayLining {

	private int lczh;// 里程桩号
	private IGeometry geometry;// 一个环的范围
	private LiningPosition[] liningPositionFeatures;// 显示部位的
	// private Bitmap image;// 环的底图图片
	private LiningBZ liningBZ;// 显示备注信息的
	private LiningLCZH liningLCZH;// 显示里程桩号的
	private LineString[] dotLines;
	private LineString[] boldLines;

	private boolean flag = false;// 如果显示为true

	/**
	 * 高速公路隧道衬砌类，包括里程桩号，环的图形区域，环内的部位，环的里程桩号和备注区域
	 * @param ringid
	 * @param geometry
	 * @param ringPositionFeatures
	 */
	
	public HighWayLining(int lczh, IGeometry geometry,LiningPosition[] liningPositionFeatures) {
		this.lczh = lczh;
		this.geometry = geometry;
		this.liningPositionFeatures = liningPositionFeatures;
		this.geometry.recalcEnvelope();
		IEnvelope envelope = this.geometry.getEnvelope();
		float minx = envelope.getXMin();
		float miny = envelope.getYMin();
		float maxx = envelope.getXMax();
		float maxy = envelope.getYMax();
		float d = envelope.getWidth() / 16;
		IPoint[] points = new Point[5];
		points[0] = new Point(minx, miny);
		points[1] = new Point(minx, maxy);
		points[2] = new Point(minx - 2 * d, maxy);
		points[3] = new Point(minx - 2 * d, miny);
		points[4] = new Point(minx, miny);
		ILinearRing linearRing = new LinearRing(points, 0);
		
		this.liningLCZH = new LiningLCZH(mapUtil.getLczhStr(lczh), new Polygon(linearRing));
		points = new Point[5];
		points[0] = new Point(maxx, miny);
		points[1] = new Point(maxx, maxy);
		points[2] = new Point(maxx + d, maxy);
		points[3] = new Point(maxx + d, miny);
		points[4] = new Point(maxx, miny);
		linearRing = new LinearRing(points, 0);
		this.liningBZ = new LiningBZ(mapUtil.getLczhStr(lczh), new Polygon(linearRing), "");
		this.boldLines = new LineString[3];
		this.dotLines = new LineString[12];
		float tempX = minx;
		LineString lineString = null;
		for (int i = 0; i < this.boldLines.length; i++) {
			points = new Point[2];
			tempX += 4 * d;
			points[0] = new Point(tempX, miny);
			points[1] = new Point(tempX, maxy);
			lineString = new LineString(points);
			this.boldLines[i] = lineString;
		}
		tempX = minx;
		for (int i = 1; i <= this.dotLines.length; i++) {
			points = new Point[2];
			tempX += d;
			points[0] = new Point(tempX, miny);
			points[1] = new Point(tempX, maxy);
			lineString = new LineString(points);
			this.dotLines[i - 1] = lineString;
			if (i % 3 == 0) {
				tempX += d;
			}
		}
	}

	public int getLczh() {
		return this.lczh;
	}

	public IGeometry getGeometry() {
		return this.geometry;
	}

	// public void setBitmap(Bitmap image) {
	// this.image = image;
	// }
	//
	// public Bitmap getBitmap() {
	// return this.image;
	// }

	public void setGeometry(IGeometry geometry) {
		this.geometry = geometry;
	}

	public LiningPosition[] getLiningPositionFeatures() {
		return this.liningPositionFeatures;
	}

	public LiningBZ getLiningBZ() {
		return liningBZ;
	}

	public LiningLCZH getLiningXH() {
		return liningLCZH;
	}

	public boolean isVisible() {
		return flag;
	}

	public void setVisible(boolean flag) {
		this.flag = flag;
	}

	public LineString[] getDotLines() {
		return dotLines;
	}

	public void setDotLines(LineString[] dotLines) {
		this.dotLines = dotLines;
	}

	public LineString[] getBoldLines() {
		return boldLines;
	}

	public void setBoldLines(LineString[] boldLines) {
		this.boldLines = boldLines;
	}

}
