package com.TY.bhgis.Geometry;

public final class MultiPolygon extends MultiSurface
  implements IMultiPolygon
{
  public MultiPolygon(int paramInt, IEnvelope paramIEnvelope)
  {
    super(paramInt, paramIEnvelope);
  }

  public MultiPolygon(IPolygon[] paramArrayOfIPolygon, IEnvelope paramIEnvelope)
  {
    super((ISurface[])paramArrayOfIPolygon, paramIEnvelope);
  }

  public final IPoint getPointOnSurface()
  {
    return null;
  }

  public final byte getGeometryType()
  {
    return 10;
  }

  public final String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("(");
    int i = this.geometries.length;
    for (int j = 0; j < i; j++)
    {
      IPolygon localIPolygon = (IPolygon)this.geometries[j];
      localStringBuffer.append(localIPolygon.toString());
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
    StringBuffer localStringBuffer = new StringBuffer("MultiPolygon (");
    int i = this.geometries.length;
    for (int j = 0; j < i; j++)
    {
      IPolygon localIPolygon = (IPolygon)this.geometries[j];
      localStringBuffer.append(localIPolygon.toString());
      localStringBuffer.append(",");
    }
    localStringBuffer.deleteCharAt(localStringBuffer.length() - 1);
    localStringBuffer.append(")");
    return localStringBuffer.toString();
  }
}
