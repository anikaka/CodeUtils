package com.TY.bhgis.Geometry;

public abstract interface IMultiSurface
{
  public abstract float getArea();

  public abstract IPoint getCentroid();

  public abstract IPoint getPointOnSurface();
}

