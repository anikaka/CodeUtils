package com.TY.bhgis.Display;



import com.TY.bhgis.Display.MarkSymbol.TextMarkStyle;
import com.TY.bhgis.Geometry.IEnvelope;
import com.TY.bhgis.Geometry.IGeometry;
import com.TY.bhgis.Geometry.IPoint;
import com.TY.bhgis.Geometry.IPolygon;
import com.TY.bhgis.Geometry.LinearRing;
import com.TY.bhgis.Geometry.Point;

import android.graphics.Canvas;

import android.graphics.Paint;
import android.graphics.Path;



public class FillTextSymbol extends FillSymbol implements IFillTextSymbol {

	private ITextMarkSymbol textMarkSymbol;

	public FillTextSymbol() {
		this.textMarkSymbol = new TextMarkSymbol();
		this.textMarkSymbol.setText("");
		this.textMarkSymbol.setTextMarkStyle(TextMarkStyle.qita);

		super.setAlpha(0);
	}

	@Override
	public void draw(Canvas canvas,
			IDisplayTransformation displayTransformation, IGeometry geometry) {
		if (geometry.getGeometryType() == 5) {
			LinearRing localLinearRing = (LinearRing) ((IPolygon) geometry).getExteriorRing();
			if (localLinearRing.getNumPoints() <= 0)
				return;
			Path path = new Path();
			Point point = new Point();
			displayTransformation.fromMapPoint(localLinearRing.getPoint(0),point);
			path.moveTo(point.getX(), point.getY());
			for (int k = 1; k < localLinearRing.getNumPoints(); k++) {
				displayTransformation.fromMapPoint(localLinearRing.getPoint(k),point);
				path.lineTo(point.getX(), point.getY());
			}
			Paint paint = new Paint();
			paint.setColor(super.getColor());
			paint.setAlpha(super.getAlpha());
			paint.setStyle(Paint.Style.FILL);
			paint.setAntiAlias(true);
			canvas.drawPath(path, paint);
			// 画外框
			if (super.getOutline() != null) {
				((ISymbol) super.getOutline()).draw(canvas,displayTransformation,(IGeometry) ((IPolygon) geometry).getExteriorRing());
			}
			// 画内部字符
			if (this.getTextMarkSymbol().getText() != "") {
				// 得到geometry的最小外包矩形左上坐标点，右下坐标点，转换为屏幕坐标点
				geometry.recalcEnvelope();
				IEnvelope envelope = geometry.getEnvelope();
				float x = envelope.getXMax() - envelope.getWidth()/2;
				float y = envelope.getYMax() - envelope.getHeight()/2;
//				if (this.getTextMarkSymbol().getText().length()>1) {
//					x=envelope.getXMin() + envelope.getWidth()/(this.getTextMarkSymbol().getText().length()*2);
//					//x=envelope.getCenterPoint().getX() + envelope.getWidth()/(this.getTextMarkSymbol().getText().getBytes().length/2-1);
//				}
				IPoint pointCenter = new Point(x, y);
				((ISymbol) this.textMarkSymbol).draw(canvas,displayTransformation, (IGeometry) pointCenter);
			}
		}
	}

	@Override
	public ITextMarkSymbol getTextMarkSymbol() {

		return this.textMarkSymbol;
	}

	@Override
	public void setTextMarkSymbol(ITextMarkSymbol textMarkSymbol) {

		this.textMarkSymbol = textMarkSymbol;
	}
}