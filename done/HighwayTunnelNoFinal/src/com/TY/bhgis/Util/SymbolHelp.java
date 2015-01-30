package com.TY.bhgis.Util;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Path;

import com.TY.bhgis.Display.IDisplayTransformation;
import com.TY.bhgis.Geometry.IGeometry;
import com.TY.bhgis.Geometry.ILineString;
import com.TY.bhgis.Geometry.IPoint;
import com.TY.bhgis.Geometry.IPolygon;
import com.TY.bhgis.Geometry.Point;


public class SymbolHelp {
	// 获得种子位图
	

	// 获得填充后的位图
	public static Bitmap GetCreateRepeater(int width, int height, Bitmap src) {
		int countWidth = (width + src.getWidth() - 1) / src.getWidth();
		int countHeight = (height + src.getHeight() - 1) / src.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		for (int i = 0; i < countHeight; i++) {
			for (int idx = 0; idx < countWidth; ++idx) {
				canvas.drawBitmap(src, idx * src.getWidth(),
						i * src.getHeight(), null);
			}
		}

		return bitmap;
	}

	public static void ClipBitmap(IGeometry geometry, Canvas canvas,
			IDisplayTransformation displayTransformation, float dx, float dy) {
		Path path = new Path();

		ILineString linearRing = (ILineString) ((IPolygon) geometry)
				.getExteriorRing();
		IPoint point = new Point();
		displayTransformation.fromMapPoint(linearRing.getPoint(0), point);
		point = MovePoint(point, dx, dy);
		path.moveTo(point.getX(), point.getY());
		for (int i = 1; i < linearRing.getNumPoints(); i++) {
			displayTransformation.fromMapPoint(linearRing.getPoint(i), point);
			point = MovePoint(point, dx, dy);
			path.lineTo(point.getX(), point.getY());
		}
		canvas.clipPath(path);
	}

	private static IPoint MovePoint(IPoint point, float x, float y) {
		IPoint pt = new Point(point.getX() - x, point.getY() - y);
		return pt;
	}


}
