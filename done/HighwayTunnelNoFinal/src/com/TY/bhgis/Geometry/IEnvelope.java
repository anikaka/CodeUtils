package com.TY.bhgis.Geometry;

public abstract interface IEnvelope
{
  public abstract void centerAt(IPoint paramIPoint);

  public abstract void defineFromPoints(int paramInt, IPoint[] paramArrayOfIPoint);

  public abstract void expand(float paramFloat1, float paramFloat2, boolean paramBoolean);

  public abstract float getHeight();

  public abstract void setHeight(float paramFloat);

  public abstract IPoint getLowerLeft();

  public abstract void setLowerLeft(IPoint paramIPoint);

  public abstract IPoint getLowerRight();

  public abstract void setLowerRight(IPoint paramIPoint);

  public abstract void offset(float paramFloat1, float paramFloat2);

  public abstract void putCoords(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);

  public abstract void putCoords(IEnvelope paramIEnvelope);

  public abstract void queryCoords(Float paramFloat1, Float paramFloat2, Float paramFloat3, Float paramFloat4);

  public abstract void union(IEnvelope paramIEnvelope);

  public abstract void setUpperLeft(IPoint paramIPoint);

  public abstract IPoint getUpperLeft();

  public abstract void setUpperRight(IPoint paramIPoint);

  public abstract IPoint getUpperRight();

  public abstract void setWidth(float paramFloat);

  public abstract float getWidth();

  public abstract void setXMax(float paramFloat);

  public abstract float getXMax();

  public abstract void setXMin(float paramFloat);

  public abstract float getXMin();

  public abstract void setYMax(float paramFloat);

  public abstract float getYMax();

  public abstract void setYMin(float paramFloat);

  public abstract float getYMin();
  public abstract IPoint getCenterPoint();
}
