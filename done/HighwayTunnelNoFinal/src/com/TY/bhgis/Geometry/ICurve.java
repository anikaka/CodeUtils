package com.TY.bhgis.Geometry;

public abstract interface ICurve
{
  public abstract float getLength();

  public abstract IPoint getStartPoint();

  public abstract IPoint getEndPoint();

  public abstract boolean isClosed();

  public abstract boolean isRing();
}
