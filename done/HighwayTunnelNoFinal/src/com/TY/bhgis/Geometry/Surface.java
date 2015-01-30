package com.TY.bhgis.Geometry;

public abstract class Surface extends Geometry
  implements ISurface
{
  public int getDimension()
  {
    return 2;
  }

  public IPoint getCentroid()
  {
    IEnvelope localIEnvelope;
    if ((localIEnvelope = getEnvelope()) != null)
      return new Point((localIEnvelope.getXMin() + localIEnvelope.getXMax()) / 2.0F, localIEnvelope.getYMin() + localIEnvelope.getYMax() / 2.0F);
    return null;
  }
}
