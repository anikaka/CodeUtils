package com.TY.bhgis.Geometry;

public abstract interface IGeometry
{
  public abstract int getDimension();

  public abstract byte getGeometryType();

  public abstract IEnvelope getEnvelope();

  public abstract void recalcEnvelope();

  public abstract String asText();

  public abstract byte[] asBinary();

  public abstract boolean isEmpty();

  public abstract boolean isSimple();
}
