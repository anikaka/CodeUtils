package com.TY.bhgis.Geometry;

public abstract interface ISpatialRelation
{
  public abstract boolean equals(IGeometry paramIGeometry);

  public abstract boolean intersects(IGeometry paramIGeometry);

  public abstract boolean contains(IGeometry paramIGeometry);

  public abstract boolean disjoint(IGeometry paramIGeometry);

  public abstract boolean touches(IGeometry paramIGeometry);

  public abstract boolean crosses(IGeometry paramIGeometry);

  public abstract boolean within(IGeometry paramIGeometry);

  public abstract boolean overlaps(IGeometry paramIGeometry);

  public abstract boolean relate(IGeometry paramIGeometry, String paramString);
}
