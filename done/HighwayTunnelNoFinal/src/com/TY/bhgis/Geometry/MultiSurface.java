package com.TY.bhgis.Geometry;

public abstract class MultiSurface extends GeometryCollection
  implements IMultiSurface
{
  public MultiSurface(int paramInt, IEnvelope paramIEnvelope)
  {
    super(paramInt, paramIEnvelope);
  }

  public MultiSurface(ISurface[] paramArrayOfISurface, IEnvelope paramIEnvelope)
  {
    super((IGeometry[])paramArrayOfISurface, paramIEnvelope);
  }

  public float getArea()
  {
    int i = this.geometries.length;
    float f = 0.0F;
    for (int j = 0; j < i; j++)
      f += ((ISurface)this.geometries[j]).getArea();
    return f;
  }

  public IPoint getCentroid()
  {
    IEnvelope localIEnvelope;
    if ((localIEnvelope = getEnvelope()) != null)
      return new Point((localIEnvelope.getXMin() + localIEnvelope.getXMax()) / 2.0F, localIEnvelope.getYMin() + localIEnvelope.getYMax() / 2.0F);
    return null;
  }

  public int getDimension()
  {
    return 2;
  }
}
