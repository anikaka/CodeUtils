package com.TY.bhgis.Geometry;

public abstract interface IPolygon
{
  public abstract void setExteriorRing(ILinearRing paramILinearRing);

  public abstract ILinearRing getExteriorRing();

  public abstract int getNumInteriorRing();

  public abstract void setInteriorRing(int paramInt, ILinearRing paramILinearRing);

  public abstract ILinearRing getInteriorRing(int paramInt);
}

