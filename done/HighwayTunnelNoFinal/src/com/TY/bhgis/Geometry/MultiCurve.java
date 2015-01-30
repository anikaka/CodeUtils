package com.TY.bhgis.Geometry;

public abstract class MultiCurve extends GeometryCollection
  implements IMultiCurve
{
  public MultiCurve(int paramInt, IEnvelope paramIEnvelope)
  {
    super(paramInt, paramIEnvelope);
  }

  public MultiCurve(ICurve[] paramArrayOfICurve, IEnvelope paramIEnvelope)
  {
    super((IGeometry[])paramArrayOfICurve, paramIEnvelope);
  }

  public boolean isClosed()
  {
    int i = this.geometries.length;
    for (int j = 0; j < i; j++)
      if (!((ICurve)this.geometries[j]).isClosed())
        return false;
    return true;
  }

  public float getLength()
  {
    int i = this.geometries.length;
    float f = 0.0F;
    for (int j = 0; j < i; j++)
      f += ((ICurve)this.geometries[j]).getLength();
    return f;
  }
}
