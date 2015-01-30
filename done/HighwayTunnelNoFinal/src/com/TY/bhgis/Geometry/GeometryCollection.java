package com.TY.bhgis.Geometry;

public class GeometryCollection extends Geometry
  implements IGeometryCollection
{
  protected IGeometry[] geometries;
  protected IEnvelope envelope;

  public GeometryCollection(IGeometry[] paramArrayOfIGeometry, IEnvelope paramIEnvelope)
  {
    this.geometries = paramArrayOfIGeometry;
    this.envelope = paramIEnvelope;
  }

  public GeometryCollection(int paramInt, IEnvelope paramIEnvelope)
  {
    this.geometries = new IGeometry[paramInt];
    this.envelope = paramIEnvelope;
  }

  public IGeometry getGeometry(int paramInt)
  {
    return this.geometries[paramInt];
  }

  public int getNumGeometries()
  {
    return this.geometries.length;
  }

  public void setGeometry(int paramInt, IGeometry paramIGeometry)
  {
    if (this.geometries[paramInt] != null)
      this.geometries[paramInt] = null;
    this.geometries[paramInt] = paramIGeometry;
  }

  public byte[] asBinary()
  {
    return null;
  }

  public String asText()
  {
    return null;
  }

  public boolean isEmpty()
  {
    return this.geometries.length == 0;
  }

  public boolean isSimple()
  {
    int i = this.geometries.length;
    for (int j = 0; j < i; j++)
      if (!this.geometries[j].isSimple())
        return false;
    return true;
  }

  public int getDimension()
  {
    int i = 0;
    int j = this.geometries.length;
    for (int k = 0; k < j; k++)
      i = this.geometries[k].getDimension() > i ? this.geometries[k].getDimension() : i;
    return i;
  }

  public IEnvelope getEnvelope()
  {
    return this.envelope;
  }

  public void recalcEnvelope()
  {
    if ((this.geometries != null) && (this.geometries.length > 0))
    {
      int i = this.geometries.length;
      IEnvelope localIEnvelope = this.geometries[0].getEnvelope();
      for (int j = 1; j < i; j++)
        localIEnvelope.union(this.geometries[j].getEnvelope());
      if (this.envelope != null)
        this.envelope = null;
      this.envelope = localIEnvelope;
    }
    this.envelope = null;
  }

  public byte getGeometryType()
  {
    return 13;
  }

  public Object Clone()
  {
    int i = this.geometries.length;
    GeometryCollection localGeometryCollection = new GeometryCollection(i, getEnvelope());
    for (int j = 0; j < i; j++)
      localGeometryCollection.setGeometry(j, (IGeometry)((IClone)getGeometry(j)).Clone());
    return localGeometryCollection;
  }

  public boolean contains(IGeometry paramIGeometry)
  {
    return false;
  }

  public boolean crosses(IGeometry paramIGeometry)
  {
    return false;
  }

  public boolean disjoint(IGeometry paramIGeometry)
  {
    return false;
  }

  public boolean equals(IGeometry paramIGeometry)
  {
    return false;
  }

  public boolean intersects(IGeometry paramIGeometry)
  {
    return false;
  }

  public boolean overlaps(IGeometry paramIGeometry)
  {
    return false;
  }

  public boolean relate(IGeometry paramIGeometry, String paramString)
  {
    return false;
  }

  public boolean touches(IGeometry paramIGeometry)
  {
    return false;
  }

  public boolean within(IGeometry paramIGeometry)
  {
    return false;
  }

  public IGeometry buffer(float paramFloat)
  {
    return null;
  }

  public IGeometry convexHull()
  {
    return null;
  }

  public IGeometry difference(IGeometry paramIGeometry)
  {
    return null;
  }

  public float distance(IGeometry paramIGeometry)
  {
    return 0.0F;
  }

  public IGeometry intersection(IGeometry paramIGeometry)
  {
    return null;
  }

  public IGeometry symDifference(IGeometry paramIGeometry)
  {
    return null;
  }

  public IGeometry union(IGeometry paramIGeometry)
  {
    return null;
  }
}
