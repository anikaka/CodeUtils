package com.TY.bhgis.Controls;

import java.util.Vector;

import com.TY.bhgis.Carto.IMap;
import com.TY.bhgis.Carto.SelectDraw;
import com.TY.bhgis.Database.BH;
import com.TY.bhgis.Database.IBH;
import com.TY.bhgis.Database.IBHClass;
import com.TY.bhgis.Display.IDisplayTransformation;
import com.TY.bhgis.Geometry.Envelope;
import com.TY.bhgis.Geometry.IEnvelope;
import com.TY.bhgis.Geometry.IGeometry;
import com.TY.bhgis.Geometry.IPoint;
import com.TY.bhgis.Geometry.Point;
import com.TY.bhgis.Util.mapUtil;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;



public class SelectBHTool implements IMapTool {
	private MapControl mapControl;

	private IInfoToolListener f;

	private IPoint[] points;

	public SelectBHTool(MapControl paramMapControl,
			IInfoToolListener paramIInfoToolListener) {
		this.mapControl = paramMapControl;
		this.f = paramIInfoToolListener;
		points = new IPoint[2];
	}

	public void draw(Canvas canvas) {
		if (points[0] != null && points[1] != null) {
			IDisplayTransformation displayTransformation = mapControl
					.getDisplay().getDisplayTransformation();

			IPoint point = points[0];
			IPoint pt = new Point();
			displayTransformation.fromMapPoint(point, pt);

			IPoint point2 = points[1];
			IPoint pt2 = new Point();
			displayTransformation.fromMapPoint(point2, pt2);
			Path path = new Path();
			path.moveTo(pt.getX(), pt.getY());
			Paint paint = new Paint();
			paint.setColor(Color.GREEN);
			paint.setStyle(Style.STROKE);

			path.lineTo(pt.getX(), pt2.getY());
			path.lineTo(pt2.getX(), pt2.getY());
			path.lineTo(pt2.getX(), pt.getY());
			path.lineTo(pt.getX(), pt.getY());
			canvas.drawPath(path, paint);

		}
	}

	public void pointerDragged(int x, int y) {
		IPoint point = new Point();
		mapControl.getDisplay().getDisplayTransformation()
				.toMapPoint(x, y, point);
		points[1] = point;
		this.mapControl.repaint();
	}

	public void pointerPressed(int x, int y) {
		IPoint point = new Point();
		mapControl.getDisplay().getDisplayTransformation()
				.toMapPoint(x, y, point);
		points[0] = point;
		this.mapControl.noCustomDraw = true;
		this.mapControl.bhBitmapDraw = true;
	}

	public void pointerReleased(int x, int y) {

		IPoint point = new Point();
		mapControl.getDisplay().getDisplayTransformation()
				.toMapPoint(x, y, point);
		points[1] = point;
		IEnvelope envelope = null;
		float XMin, XMax, YMin, YMax;
		if (points[0].getX() < points[1].getX()) {
			XMin = points[0].getX();
			XMax = points[1].getX();
		} else {
			XMax = points[0].getX();
			XMin = points[1].getX();
		}
		if (points[0].getY() < points[1].getY()) {
			YMin = points[0].getY();
			YMax = points[1].getY();
		} else {
			YMax = points[0].getY();
			YMin = points[1].getY();
		}

		envelope = new Envelope(XMin, XMax, YMin, YMax, (byte) 0);

		Vector<IBH> bhsSelect = new Vector<IBH>();

		IMap map = this.mapControl.getMap();

		for (int i = 0; i < map.getBHClasses().length; i++) {
			{
				IBHClass bhClass = map.getBHClasses()[i];
				Vector<IBH> bhs = bhClass.getBHs();
				for (int j = 0; j < bhs.size(); j++) {
					IBH bh = bhs.get(j);
					if (bh.getShape() != null && bh.getVisible()) {
						if (mapUtil.isContain(envelope, bh.getShape())) {
							bhsSelect.add(bh);
						}
					}
				}
			}
		}

		points[0] = null;
		points[1] = null;

		IBH[] selectBHs = new BH[bhsSelect.size()];

		IGeometry[] geometries = new IGeometry[bhsSelect.size()];
		for (int i = 0; i < bhsSelect.size(); i++) {
			IBH bh = bhsSelect.get(i);
			selectBHs[i] = bh;
			geometries[i] = bh.getShape();
		}
		mapControl.setSelectionBHs(selectBHs);
		SelectDraw selectDraw = new SelectDraw((IGeometry[]) geometries,
				mapControl.getDisplay().getDisplayTransformation());
		mapControl.setSelectDraw(selectDraw);
		this.f.notify(this.mapControl, bhsSelect);

		this.mapControl.repaint();
		this.mapControl.noCustomDraw = false;
		this.mapControl.bhBitmapDraw = false;
	
	}
}
