package com.TY.bhgis.Carto;

import java.util.ArrayList;

import com.TY.bhgis.Display.IDisplayTransformation;
import com.TY.bhgis.Geometry.IGeometry;
import com.TY.bhgis.Geometry.ILineString;
import com.TY.bhgis.Geometry.IPoint;
import com.TY.bhgis.Geometry.IPolygon;
import com.TY.bhgis.Geometry.LinearRing;
import com.TY.bhgis.Geometry.MultiLineString;
import com.TY.bhgis.Geometry.MultiPoint;
import com.TY.bhgis.Geometry.MultiPolygon;
import com.TY.bhgis.Geometry.Point;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;


public class SelectDraw {

	private ArrayList<IGeometry> geometries;
	private IDisplayTransformation displayTransformation;
	private SelectSymbol symbol;
	private Paint paint;

	public SelectDraw(IGeometry geometry,
			IDisplayTransformation displayTransformation) {
		geometries = new ArrayList<IGeometry>();
		if (geometry != null) {
			geometries.add(geometry);
		}
		init(displayTransformation);
	}

	public SelectDraw(IGeometry[] geometrys,IDisplayTransformation displayTransformation) {
		geometries = new ArrayList<IGeometry>();
		if (geometrys != null) {
			for (int i = 0; i < geometrys.length; i++) {
				IGeometry geometry = geometrys[i];
				geometries.add(geometry);
			}

		}
		init(displayTransformation);
	}

	private void init(IDisplayTransformation displayTransformation) {
		this.displayTransformation = displayTransformation;
		symbol = new SelectSymbol();
		this.paint = new Paint();
		this.paint.setAntiAlias(true);
		this.paint.setColor(symbol.getColor());
		this.paint.setAlpha(symbol.getAlpha());
		
	}

	public SelectSymbol getSymbol() {
		return symbol;
	}

	public void setSymbol(SelectSymbol symbol) {
		this.symbol = symbol;
	}

	public void draw(Canvas paramCanvas) {
		for (int i = 0; i < geometries.size(); i++) {
			IGeometry geometry=geometries.get(i);
			if(geometry.getGeometryType()==1||geometry.getGeometryType()==8)
			{
				drawPoint(geometry, paramCanvas);
			}
			if (geometry.getGeometryType()==3||geometry.getGeometryType()==4||geometry.getGeometryType()==7||geometry.getGeometryType()==12) {
				drawLineString(geometry, paramCanvas);
			}
			if (geometry.getGeometryType()==5||geometry.getGeometryType()==10) {
				drawPolygon(geometry, paramCanvas);
			}
		}
	}

	private void drawPoint(IGeometry geometry, Canvas c) {
		if (geometry.getGeometryType() == 8) {
			MultiPoint multiPoint = (MultiPoint) geometry;
			for (int i = 0; i < multiPoint.getNumGeometries(); i++) {
				drawPoint(multiPoint.getGeometry(i), c);
			}
			return;

		}
		if (geometry.getGeometryType() != 1)
			return;
		Point point = new Point();
		this.displayTransformation.fromMapPoint((IPoint) geometry, point);
		paint.setColor(symbol.getColor());
		paint.setStyle(Style.FILL);
		c.drawCircle(point.getX(), point.getY(), symbol.getSize(), paint);

	}

	private void drawLineString(IGeometry geometry, Canvas c) {

		if (geometry.getGeometryType() == 12) {
			MultiLineString multiLineString = (MultiLineString) geometry;
			for (int i = 0; i < multiLineString.getNumGeometries(); i++)
				drawLineString(multiLineString.getGeometry(i), c);
			return;
		}
		if (geometry == null)
			return;
		if ((geometry.getGeometryType() != 3)
				&& (geometry.getGeometryType() != 4)
				&& (geometry.getGeometryType() != 7))
			return;
		if ((((ILineString) geometry).getNumPoints() - 1) <= 0)
			return;
		this.paint.setColor(symbol.getColor());
		Point point = new Point();
		this.displayTransformation.fromMapPoint(
				((ILineString) geometry).getPoint(0), point);
		this.paint.setFlags(1);// Ïû³ý¾â³Ý
		this.paint.setStrokeWidth(symbol.getLineWidth());
		for (int i = 1; i < ((ILineString) geometry).getNumPoints(); i++) {
			Point point2 = new Point();
			this.displayTransformation.fromMapPoint(
					((ILineString) geometry).getPoint(i), point2);
			c.drawLine((int) point.getX(), (int) point.getY(),
					(int) point2.getX(), (int) point2.getY(), this.paint);
			point.setX(point2.getX());
			point.setY(point2.getY());
		}
		this.paint.setFlags(0);
		this.paint.setStrokeWidth(1.0F);
	}

	private void drawPolygon(IGeometry geometry, Canvas c) {

		if (geometry.getGeometryType() == 10) {
			MultiPolygon multiPolygon = (MultiPolygon) geometry;
			for (int i = 0; i < multiPolygon.getNumGeometries(); i++)
				drawPolygon(multiPolygon.getGeometry(i), c);
			return;
		}
		if (geometry.getGeometryType() != 5)
			return;
		LinearRing localLinearRing = (LinearRing) ((IPolygon) geometry)
				.getExteriorRing();

		if (localLinearRing.getNumPoints() <= 0)
			return;

		
		Path path = new Path();
		Point point = new Point();
		this.displayTransformation.fromMapPoint(localLinearRing.getPoint(0),
				point);
		path.moveTo(point.getX(), point.getY());
		for (int k = 1; k < localLinearRing.getNumPoints(); k++) {
			this.displayTransformation.fromMapPoint(
					localLinearRing.getPoint(k), point);
			path.lineTo(point.getX(), point.getY());
		}
	
		this.paint.setStyle(Paint.Style.FILL);
		//this.paint.setStrokeWidth(symbol.getLineWidth());
		c.drawPath(path, this.paint);
		
	}

}
