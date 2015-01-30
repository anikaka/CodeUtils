package com.TY.bhgis.Controls;

import com.TY.bhgis.Geometry.*;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class PanTool implements IMapTool {

	private IPoint point;
	private MapControl mapControl;
	private Bitmap image;
	private Paint paint;
	private int e;
	private int f;
	private int g = -1;
	private int h = -1;
	private Bitmap oimage;
	private Canvas ocanvas;
	private boolean k = false;

	public PanTool(MapControl paramMapControl) {
		this.mapControl = paramMapControl;
		this.paint = new Paint();
		this.paint.setColor(-1);
		this.oimage = Bitmap.createBitmap(paramMapControl.getWidth(),paramMapControl.getHeight(), paramMapControl.getImageCache().getConfig());
		this.ocanvas = new Canvas();
		this.ocanvas.setBitmap(this.oimage);
		this.paint.setColor(paramMapControl.getBackColor());
		this.ocanvas.drawRect(0.0F, 0.0F, paramMapControl.getWidth(),paramMapControl.getHeight(), this.paint);
	}

	@Override
	public void draw(Canvas arg0) {
		
	}


	@Override
	public void pointerDragged(int arg0, int arg1) {
		
		if ((this.k) && (this.g != arg0) && (this.h != arg1)) {
			this.g = arg0;
			this.h = arg1;
			ocanvas = this.mapControl.getImageCanvas();
			ocanvas.drawRect(0.0F, 0.0F, this.image.getWidth(),this.image.getHeight(), this.paint);
			ocanvas.drawBitmap(this.image, 0, arg1 - this.f,this.paint);
			this.mapControl.repaint();
		}
	}

	@Override
	public void pointerPressed(int arg0, int arg1) {
		this.point = new Point();
		this.mapControl.getDisplay().getDisplayTransformation().toMapPoint(arg0, arg1, this.point);
		this.e = arg0;
		this.f = arg1;
		this.k = true;
		this.image = Bitmap.createBitmap(this.mapControl.getImageCache());
		this.mapControl.noCustomDraw = true;
		this.mapControl.noSelectDraw = true;
	}

	@Override
	public void pointerReleased(int arg0, int arg1) {

		if (this.point == null)
			return;
		IPoint mPoint = new Point();
		this.mapControl.getDisplay().getDisplayTransformation().toMapPoint(arg0, arg1, mPoint);
		IEnvelope envelope = this.mapControl.getExtent();
		IEnvelope fulleEnvelope = this.mapControl.getFullExtent();
		// DX如果大于0则envelope右移
		float DX = this.point.getX() - mPoint.getX();
		// DY如果大于0则envelope上移
		float DY = this.point.getY() - mPoint.getY();
		float DXMin = fulleEnvelope.getXMin() - envelope.getXMin();
		float DXMax = fulleEnvelope.getXMax() - envelope.getXMax();
		float DYMin = fulleEnvelope.getYMin() - envelope.getYMin();
		float DYMax = fulleEnvelope.getYMax() - envelope.getYMax();
		float dx, dy;
		if (DX > 0 && Math.abs(DX) > Math.abs(DXMax)) {
			dx = DXMax;
		} else if (DX < 0 && Math.abs(DX) > Math.abs(DXMin)) {
			dx = DXMin;
		} else {
			dx = DX;
		}
		if (DY > 0 && Math.abs(DY) > Math.abs(DYMax) || DY > 0
				&& fulleEnvelope.getYMax() < envelope.getYMax()) {
			dy = DYMax;
		} else if (DY < 0 && Math.abs(DY) > Math.abs(DYMin) || DY < 0
				&& fulleEnvelope.getYMin() > envelope.getYMin()) {
			dy = DYMin;
		} else {
			dy = DY;
		}

		envelope.offset(0, dy);

		this.mapControl.noCustomDraw = false;
		this.mapControl.noSelectDraw = false;
		this.mapControl.refresh(envelope);
		this.point = null;
		this.k = false;
		if (this.image != null)
			this.image.recycle();
		this.image = null;
	}

}
