package com.TY.bhgis.Geometry;

public final class Polygon extends Surface
  implements IPolygon
{
  private ILinearRing a;
  private ILinearRing[] b;

  public Polygon(ILinearRing paramILinearRing, ILinearRing[] paramArrayOfILinearRing)
  {
    this.a = paramILinearRing;
    this.b = paramArrayOfILinearRing;
  }

  public Polygon(ILinearRing paramILinearRing, int paramInt)
  {
    this.a = paramILinearRing;
    if (paramInt > 0)
      this.b = new ILinearRing[paramInt];
  }

  public Polygon(ILinearRing paramILinearRing)
  {
    this.a = paramILinearRing;
  }

  public final String toString()
  {
    if (isEmpty())
      return null;
    StringBuffer localStringBuffer;
    (localStringBuffer = new StringBuffer()).append("(");
    localStringBuffer.append((LineString)this.a);
    localStringBuffer.append(",");
    int i = getNumInteriorRing();
    for (int j = 0; j < i; j++)
    {
      localStringBuffer.append((LineString)this.b[j]);
      localStringBuffer.append(",");
    }
    localStringBuffer.deleteCharAt(localStringBuffer.length() - 1);
    localStringBuffer.append(")");
    return localStringBuffer.toString();
  }

  public final byte[] asBinary()
  {
    return null;
  }

  public final String asText()
  {
    if (isEmpty())
      return null;
    StringBuffer localStringBuffer;
    (localStringBuffer = new StringBuffer()).append("POLYGON (");
    localStringBuffer.append((LineString)this.a);
    localStringBuffer.append(",");
    int i = getNumInteriorRing();
    for (int j = 0; j < i; j++)
    {
      localStringBuffer.append((LineString)this.b[j]);
      localStringBuffer.append(",");
    }
    localStringBuffer.deleteCharAt(localStringBuffer.length() - 1);
    localStringBuffer.append(")");
    return localStringBuffer.toString();
  }

  public final IGeometry buffer(float paramFloat)
  {
    return null;
  }

  public final Object Clone()
  {
    return null;
  }

  public final boolean contains(IGeometry paramIGeometry)
  {
    return false;
  }

  public final IGeometry convexHull()
  {
    return null;
  }

  public final boolean crosses(IGeometry paramIGeometry)
  {
    return false;
  }

  public final IGeometry difference(IGeometry paramIGeometry)
  {
    return null;
  }

  public final boolean disjoint(IGeometry paramIGeometry)
  {
    return false;
  }

  public final float distance(IGeometry paramIGeometry)
  {
    return 0.0F;
  }

  public final boolean equals(IGeometry paramIGeometry)
  {
    return false;
  }

  public final IGeometry intersection(IGeometry paramIGeometry)
  {
    return null;
  }

  public final boolean intersects(IGeometry paramIGeometry)
  {
    return false;
  }

  public final boolean isEmpty()
  {
    return (this.a == null) && (this.b == null);
  }

  public final boolean isSimple()
  {
    return false;
  }

  public final boolean overlaps(IGeometry paramIGeometry)
  {
    return false;
  }

  public final boolean relate(IGeometry paramIGeometry, String paramString)
  {
    return false;
  }

  public final IGeometry symDifference(IGeometry paramIGeometry)
  {
    return null;
  }

  public final boolean touches(IGeometry paramIGeometry)
  {
    return false;
  }

  public final IGeometry union(IGeometry paramIGeometry)
  {
    return null;
  }

  public final boolean within(IGeometry paramIGeometry)
  {
    switch (paramIGeometry.getGeometryType())
    {
    case 1:
      return Arithmetic.EntireContains(this, (IPoint)paramIGeometry);
    }
    return false;
  }

  public final IEnvelope getEnvelope()
  {
    if (this.a != null)
      return ((IGeometry)this.a).getEnvelope();
    return null;
  }

  public final void recalcEnvelope()
  {
    if (this.a != null)
      ((IGeometry)this.a).recalcEnvelope();
  }

  public final byte getGeometryType()
  {
    return 5;
  }

  public final ILinearRing getExteriorRing()
  {
    return this.a;
  }

  public final ILinearRing getInteriorRing(int paramInt)
  {
    return this.b[paramInt];
  }

  public final int getNumInteriorRing()
  {
    if (this.b == null)
      return 0;
    return this.b.length;
  }

  public final void setExteriorRing(ILinearRing paramILinearRing)
  {
    if (this.a != null)
      this.a = null;
    this.a = paramILinearRing;
  }

  public final void setInteriorRing(int paramInt, ILinearRing paramILinearRing)
  {
    if (this.b[paramInt] != null)
      this.b[paramInt] = null;
    this.b[paramInt] = paramILinearRing;
  }

  public final float getArea()
  {
    if (isEmpty())
      return 0.0F;
    ILineString localILineString;
    int i = (localILineString = (ILineString)this.a).getNumPoints();
    float f1 = 0.0F;
    IPoint localIPoint1;
    IPoint localIPoint2;
    for (int j = 0; j < i - 1; j++)
    {
      localIPoint1 = localILineString.getPoint(j);
      localIPoint2 = localILineString.getPoint(j + 1);
      f1 += (localIPoint2.getX() + localIPoint1.getX()) * (localIPoint2.getY() - localIPoint1.getY()) / 2.0F;
    }
    float f2 = Math.abs(f1);
    int k = getNumInteriorRing();
    for (int m = 0; m < k; m++)
    {
      if ((localILineString = (ILineString)this.b[m]) == null)
        break;
      i = localILineString.getNumPoints();
      f1 = 0.0F;
      for (int n = 0; n < i - 1; n++)
      {
        localIPoint1 = localILineString.getPoint(n);
        localIPoint2 = localILineString.getPoint(n + 1);
        f1 += (localIPoint2.getX() + localIPoint1.getX()) * (localIPoint2.getY() - localIPoint1.getY()) / 2.0F;
      }
      f2 -= Math.abs(f1);
    }
    return f2;
  }

  public final IPoint getPointOnSurface()
  {
    return null;
  }
}
