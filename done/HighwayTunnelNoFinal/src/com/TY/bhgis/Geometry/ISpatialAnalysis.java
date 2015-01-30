package com.TY.bhgis.Geometry;

public abstract interface ISpatialAnalysis
{
  public abstract float distance(IGeometry paramIGeometry);

  public abstract IGeometry buffer(float paramFloat);

  public abstract IGeometry convexHull();

  public abstract IGeometry intersection(IGeometry paramIGeometry);

  public abstract IGeometry union(IGeometry paramIGeometry);

  public abstract IGeometry difference(IGeometry paramIGeometry);

  public abstract IGeometry symDifference(IGeometry paramIGeometry);
}
