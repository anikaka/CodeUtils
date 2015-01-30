package com.TY.bhgis.Display;

import com.TY.bhgis.Geometry.IEnvelope;
import com.TY.bhgis.Geometry.IPoint;



public interface IDisplayTransformation {
	 public abstract void fromMapPoint(IPoint paramIPoint1, IPoint paramIPoint2);

	  public abstract void toMapPoint(IPoint paramIPoint1, IPoint paramIPoint2);

	  public abstract void toMapPoint(int paramInt1, int paramInt2, IPoint paramIPoint);

	  public abstract void TransformRect(IEnvelope paramIEnvelope1, IEnvelope paramIEnvelope2, boolean paramBoolean);

	  public abstract IEnvelope getDeviceFrame();

	  public abstract void setDeviceFrame(IEnvelope paramIEnvelope);

	  public abstract IEnvelope getBounds();

	  public abstract void setBounds(IEnvelope paramIEnvelope);

	  public abstract IEnvelope getVisibleBounds();

	  public abstract void setVisibleBounds(IEnvelope paramIEnvelope);


	  public abstract float TransformMeasures(float paramFloat, boolean paramBoolean);

	  public abstract float getZoom();

	  public abstract void setZoom(float paramFloat);

}
