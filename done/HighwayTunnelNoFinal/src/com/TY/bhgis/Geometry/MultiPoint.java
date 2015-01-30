package com.TY.bhgis.Geometry;

public final class MultiPoint extends GeometryCollection
  implements IMultiPoint
{
  public MultiPoint(int paramInt, IEnvelope paramIEnvelope)
  {
    super(paramInt, paramIEnvelope);
  }

  public MultiPoint(IPoint[] paramArrayOfIPoint, IEnvelope paramIEnvelope)
  {
    super((IGeometry[])paramArrayOfIPoint, paramIEnvelope);
  }

  public final void recalcEnvelope()
  {
    Envelope localEnvelope;
    (localEnvelope = new Envelope()).defineFromPoints(this.geometries.length, (IPoint[])this.geometries);
    if (this.envelope != null)
      this.envelope = null;
    this.envelope = localEnvelope;
  }

  public final String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("(");
    int i = this.geometries.length;
    for (int j = 0; j < i; j++)
    {
      IPoint localIPoint = (IPoint)this.geometries[j];
      localStringBuffer.append(localIPoint.getX());
      localStringBuffer.append(" ");
      localStringBuffer.append(localIPoint.getY());
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
    StringBuffer localStringBuffer = new StringBuffer("MultiPoint (");
    int i = this.geometries.length;
    for (int j = 0; j < i; j++)
    {
      IPoint localIPoint = (IPoint)this.geometries[j];
      localStringBuffer.append(localIPoint.getX());
      localStringBuffer.append(" ");
      localStringBuffer.append(localIPoint.getY());
      localStringBuffer.append(",");
    }
    localStringBuffer.deleteCharAt(localStringBuffer.length() - 1);
    localStringBuffer.append(")");
    return localStringBuffer.toString();
  }

  public final byte getGeometryType()
  {
    return 8;
  }

  public final boolean isSimple()
  {
    return true;
  }

  public final int getDimension()
  {
    return 0;
  }
}
