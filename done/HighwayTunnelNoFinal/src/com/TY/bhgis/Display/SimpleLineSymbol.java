package com.TY.bhgis.Display;


import com.TY.bhgis.Geometry.IGeometry;
import com.TY.bhgis.Geometry.ILineString;
import com.TY.bhgis.Geometry.IPoint;
import com.TY.bhgis.Geometry.Point;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;

public class SimpleLineSymbol extends LineSymbol implements ISimpleLineSymbol {

	private int color;
	private float width;
	private DashStyle dashStyle;

	public SimpleLineSymbol() {
		this.color=Color.BLACK;
		this.width=1f;
		this.dashStyle = DashStyle.Solid;
	}


	@Override
	public int getColor() {
		
		return this.color;
	}

	@Override
	public void setColor(int paramInt) {
		
		this.color = paramInt;
	}

	@Override
	public void setColor(int paramInt1, int paramInt2, int paramInt3) {
		
		this.color = Color.rgb(paramInt1, paramInt2, paramInt3);
	}

	@Override
	public float getWidth() {
		
		return this.width;
	}

	@Override
	public void setWidth(float paramFloat) {
		
		this.width = paramFloat;

	}


	@Override
	public DashStyle getStyle() {
		
		return this.dashStyle;
	}

	@Override
	public void setStyle(DashStyle dStyle) {
		
		this.dashStyle = dStyle;
	}

	@Override
	public void draw(Canvas canvas,IDisplayTransformation displayTransformation, IGeometry geometry) {
		if (geometry.getGeometryType() == 3 || geometry.getGeometryType() == 7) {
			ILineString lineString = (ILineString) geometry;
			IPoint point = lineString.getPoint(0);
			Point point2 = new Point();
			displayTransformation.fromMapPoint(point, point2);
			Path path = new Path();
//			float startX = point2.getX();
//			float startY = point2.getY();
			path.moveTo(point2.getX(), point2.getY());
			for (int i = 1; i < lineString.getNumPoints(); i++) {
				point = lineString.getPoint(i);
				IPoint pt1 = new Point();
				displayTransformation.fromMapPoint(point, pt1);
//				  float dx = Math.abs(pt1.getX() - startX);
//		          float dy = Math.abs( pt1.getY() - startY);
//				if (dx >= 4 || dy >= 4) {
//					path.quadTo(startX, startY, (startX+pt1.getX())/2, (startX+pt1.getY())/2);
//					  startX = pt1.getX();
//		              startY = pt1.getY();
//				}
				 path.lineTo(pt1.getX(), pt1.getY());
			}
			Paint paint = new Paint();
			if (dashStyle == DashStyle.Dot) {	
				PathEffect mEffects = new DashPathEffect(new float[] {(float) 5, (float) 5 }, 1);
				paint.setPathEffect(mEffects);
			}
			paint.setAntiAlias(true);
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(this.color);
			paint.setStrokeWidth(this.width);
			canvas.drawPath(path, paint);
		}
	}
	
}
