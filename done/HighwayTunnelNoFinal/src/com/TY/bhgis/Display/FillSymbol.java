package com.TY.bhgis.Display;

import com.TY.bhgis.Geometry.IGeometry;
import com.TY.bhgis.Geometry.IPolygon;
import com.TY.bhgis.Geometry.LinearRing;
import com.TY.bhgis.Geometry.Point;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;



public class FillSymbol extends Symbol implements IFillSymbol {

	private int color;
	private int alpha;
	private ILineSymbol mLineSymbol;
 
	public FillSymbol()
	{
		this.color=Color.RED;
		this.alpha=255;
		this.mLineSymbol=new SimpleLineSymbol();
	}
	@Override
	public void draw(Canvas canvas,
			IDisplayTransformation displayTransformation, IGeometry geometry) {
		if (geometry.getGeometryType() == 5) {
			LinearRing localLinearRing = (LinearRing) ((IPolygon) geometry)
					.getExteriorRing();

			if (localLinearRing.getNumPoints() <= 0)
				return;

			Path path = new Path();
			Point point = new Point();
			displayTransformation.fromMapPoint(localLinearRing.getPoint(0),
					point);
			path.moveTo(point.getX(), point.getY());
			for (int k = 1; k < localLinearRing.getNumPoints(); k++) {
				displayTransformation.fromMapPoint(localLinearRing.getPoint(k),point);
				path.lineTo(point.getX(), point.getY());
			}
			Paint paint = new Paint();
			paint.setColor(this.color);
			paint.setAlpha(this.alpha);
			paint.setStyle(Paint.Style.FILL);
			// this.paint.setStrokeWidth(symbol.getLineWidth());
			canvas.drawPath(path, paint);
			if (this.mLineSymbol!=null) {
				((ISymbol)this.mLineSymbol).draw(canvas, displayTransformation, (IGeometry) ((IPolygon) geometry).getExteriorRing());
			}
		}

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
	public ILineSymbol getOutline() {
	
		return this.mLineSymbol;
	}

	@Override
	public void setOutline(ILineSymbol paramILineSymbol) {
	
		this.mLineSymbol = paramILineSymbol;
	}

	@Override
	public void setAlpha(int paramInt) {
	
		this.alpha=paramInt;
	}

	@Override
	public int getAlpha() {
	
		return this.alpha;
	}

}
