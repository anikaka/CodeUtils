package com.TY.bhgis.Geometry;

public final class MultiLineString extends MultiCurve
  implements IMultiLineString
{
  public MultiLineString(int paramInt, IEnvelope paramIEnvelope)
  {
    super(paramInt, paramIEnvelope);
  }

  public MultiLineString(ILineString[] paramArrayOfILineString, IEnvelope paramIEnvelope)
  {
    super((ICurve[])paramArrayOfILineString, paramIEnvelope);
  }

  public final byte getGeometryType()
  {
    return 12;
  }

  public final String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("(");
    int i = this.geometries.length;
    for (int j = 0; j < i; j++)
    {
      ILineString localILineString = (ILineString)this.geometries[j];
      localStringBuffer.append(localILineString.toString());
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
    StringBuffer localStringBuffer = new StringBuffer("MultiLineString (");
    int i = this.geometries.length;
    for (int j = 0; j < i; j++)
    {
      ILineString localILineString = (ILineString)this.geometries[j];
      localStringBuffer.append(localILineString.toString());
      localStringBuffer.append(",");
    }
    localStringBuffer.deleteCharAt(localStringBuffer.length() - 1);
    localStringBuffer.append(")");
    return localStringBuffer.toString();
  }
}

