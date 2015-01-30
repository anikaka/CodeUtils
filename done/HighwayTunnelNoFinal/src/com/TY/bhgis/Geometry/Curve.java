package com.TY.bhgis.Geometry;

public abstract class Curve extends Geometry
  implements ICurve
{
  public boolean isEmpty()
  {
    return (getStartPoint() == null) || (getEndPoint() == null);
  }

  public int getDimension()
  {
    return 1;
  }

  public boolean isClosed()
  {
    return ((ISpatialRelation)getStartPoint()).equals((IGeometry)getEndPoint());
  }

  public boolean isRing()
  {
    return (isClosed()) && (isSimple());
  }
}
