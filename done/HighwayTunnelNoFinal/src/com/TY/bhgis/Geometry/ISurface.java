package com.TY.bhgis.Geometry;

public abstract interface ISurface
{
  public abstract float getArea();

  public abstract IPoint getCentroid();

  public abstract IPoint getPointOnSurface();
}
