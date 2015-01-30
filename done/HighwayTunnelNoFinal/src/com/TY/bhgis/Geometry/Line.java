package com.TY.bhgis.Geometry;

public final class Line extends LineString
  implements ILine
{
  public Line()
  {
    super(2, null);
  }

  public Line(IPoint paramIPoint1, IPoint paramIPoint2, IEnvelope paramIEnvelope)
  {
    super(paramIPoint1, paramIPoint2, paramIEnvelope);
  }

  public final byte getGeometryType()
  {
    return 4;
  }

  public final boolean IsSimple()
  {
    return true;
  }

  public final boolean IsClosed()
  {
    return false;
  }

  public final boolean IsRing()
  {
    return false;
  }

  public final Object clone()
  {
    Line localLine = new Line();
    IPoint localIPoint = getStartPoint();
    localLine.setPoint(0, new Point(localIPoint.getX(), localIPoint.getY()));
    localIPoint = getEndPoint();
    localLine.setPoint(1, new Point(localIPoint.getX(), localIPoint.getY()));
    return localLine;
  }
}
