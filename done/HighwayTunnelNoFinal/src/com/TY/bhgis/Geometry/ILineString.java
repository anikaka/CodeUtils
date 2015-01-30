package com.TY.bhgis.Geometry;

public abstract interface ILineString
{
  public abstract int getNumPoints();

  public abstract void setPoint(int paramInt, IPoint paramIPoint);

  public abstract IPoint getPoint(int paramInt);

  public abstract ILinearRing toLinearRing();

  public abstract void setPoints(IPoint[] paramArrayOfIPoint);
}
