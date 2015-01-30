package com.TY.bhgis.Display;

import com.TY.bhgis.Geometry.IEnvelope;
import com.TY.bhgis.Geometry.IPoint;



public final class DisplayTransformation implements IDisplayTransformation {
	private IEnvelope a;
	private IEnvelope b;
	private IEnvelope c;

	private float f;

	public DisplayTransformation(IEnvelope paramIEnvelope1,
			IEnvelope paramIEnvelope2, IEnvelope paramIEnvelope3) {
		this.a = paramIEnvelope1;
		this.b = paramIEnvelope2;
		this.c = paramIEnvelope3;
		this.f = (paramIEnvelope2.getWidth() / paramIEnvelope1.getWidth());

	}
	
//paramBoolean为true则是将屏幕坐标转换成真实坐标
	public final float TransformMeasures(float paramFloat, boolean paramBoolean) {
		if (paramBoolean)
			return paramFloat * this.f;
		return paramFloat / this.f;
	}

	public final void TransformRect(IEnvelope paramIEnvelope1,
			IEnvelope paramIEnvelope2, boolean paramBoolean) {
		paramIEnvelope2.setXMin((paramIEnvelope1.getXMin() - this.b.getXMin())
				/ this.f);
		paramIEnvelope2.setXMax((paramIEnvelope1.getXMax() - this.b.getXMin())
				/ this.f);
		paramIEnvelope2.setYMin((this.b.getYMax() - paramIEnvelope1.getYMax())
				/ this.f);
		paramIEnvelope2.setYMax((this.b.getYMax() - paramIEnvelope1.getYMin())
				/ this.f);
	}

	public final void fromMapPoint(IPoint paramIPoint1, IPoint paramIPoint2) {
		paramIPoint2.setX((paramIPoint1.getX() - this.b.getXMin()) / this.f);
		paramIPoint2.setY((this.b.getYMax() - paramIPoint1.getY()) / this.f);
	}

	public final IEnvelope getBounds() {
		return this.c;
	}

	public final IEnvelope getDeviceFrame() {
		return this.a;
	}

	public final IEnvelope getVisibleBounds() {

		return this.b;
	}

	public final void setBounds(IEnvelope paramIEnvelope) {
		this.c = paramIEnvelope;
	}

	public final void setDeviceFrame(IEnvelope paramIEnvelope) {
		this.a = paramIEnvelope;
	}

	public final void setVisibleBounds(IEnvelope paramIEnvelope) {
		this.b=paramIEnvelope;
		this.f = (this.b.getWidth() / this.a.getWidth());
	}

	public final void toMapPoint(IPoint paramIPoint1, IPoint paramIPoint2) {
		paramIPoint2.setX((this.a.getXMin() + paramIPoint1.getX()) * this.f
				+ this.b.getXMin());
		paramIPoint2.setY((this.a.getYMax() - paramIPoint1.getY()) * this.f
				+ this.b.getYMin());

	}

	public final void toMapPoint(int paramInt1, int paramInt2,
			IPoint paramIPoint) {
		paramIPoint.setX((this.a.getXMin() + paramInt1) * this.f
				+ this.b.getXMin());
		paramIPoint.setY((this.a.getYMax() - paramInt2) * this.f
				+ this.b.getYMin());
	}

	public final float getZoom() {
		return this.f;
	}

	public final void setZoom(float paramFloat) {
		paramFloat = paramFloat / this.f - 1.0F;
		this.b.expand(paramFloat, paramFloat, false);
	}
}
