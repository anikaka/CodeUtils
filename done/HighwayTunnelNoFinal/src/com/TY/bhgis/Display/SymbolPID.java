package com.TY.bhgis.Display;


import com.TY.bhgis.Geometry.Envelope;
import com.TY.bhgis.Geometry.IEnvelope;
import com.TY.bhgis.Geometry.IGeometry;
import com.TY.bhgis.Geometry.IPoint;
import com.TY.bhgis.Geometry.Point;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class SymbolPID {
	private int color;
	private int alpha;
	private int textsize;
	private String pid;

	public SymbolPID(String pid) {
		this.color = Color.BLACK;
		this.alpha = 255;
		this.textsize = 15;
		this.pid = pid;
	}

	public void draw(Canvas canvas,IDisplayTransformation displayTransformation, IGeometry geometry) {

		Paint paint = new Paint();
		paint.setColor(this.color);
		paint.setStrokeWidth(6);
		paint.setTextSize(this.textsize);
		paint.setStyle(Paint.Style.FILL);
		
		
		geometry.recalcEnvelope();
		IEnvelope envelope = null;
		if (geometry.getGeometryType() == 1) {
			IPoint point = ((IPoint) geometry);
			envelope = new Envelope(point.getX() - 0.2f, point.getX() + 0.2f,
					point.getY() - 0.2f, point.getY() + 0.2f, (byte) 0);

		}
		else {
			envelope = geometry.getEnvelope();

		}
		float x = envelope.getXMax();
		float y = envelope.getYMax();
		Point point = new Point();
		displayTransformation.fromMapPoint(new Point(x, y), point);
		canvas.drawText(this.pid, point.getX(), point.getY(), paint);

	}
}
