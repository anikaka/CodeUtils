package com.TY.bhgis.Geometry;

public abstract interface IGeometryCollection
{
  public abstract int getNumGeometries();

  public abstract void setGeometry(int paramInt, IGeometry paramIGeometry);

  public abstract IGeometry getGeometry(int paramInt);
}