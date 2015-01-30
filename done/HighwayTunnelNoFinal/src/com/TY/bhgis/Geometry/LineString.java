package com.TY.bhgis.Geometry;

public class LineString extends Curve implements ILineString {
	protected IPoint[] pts;
	private IEnvelope a;

	public LineString(int paramInt, IEnvelope paramIEnvelope) {
		this.pts = new IPoint[paramInt];
		this.a = paramIEnvelope;
	}

	public LineString(IPoint[] paramArrayOfIPoint, int paramInt,
			IEnvelope paramIEnvelope) {
		this.pts = paramArrayOfIPoint;
		this.a = paramIEnvelope;
	}

	public LineString(IPoint paramIPoint1, IPoint paramIPoint2,
			IEnvelope paramIEnvelope) {
		this.pts = new IPoint[2];
		this.pts[0] = paramIPoint1;
		this.pts[1] = paramIPoint2;
		this.a = paramIEnvelope;
	}

	public LineString(IPoint[] paramArrayOfIPoint) {
		this.pts = paramArrayOfIPoint;
	}

	public LineString(Line[] paramArrayOfILines) {
		Line[] lines= paramArrayOfILines;
		this.pts=new Point[lines.length+1];
		pts[0]=lines[0].getStartPoint();
		for (int i = 0; i < lines.length; i++) {
			pts[i+1]=lines[i].getEndPoint();
		}
	}

	public final ILinearRing toLinearRing() {
		if (isRing())
			return new LinearRing(this.pts, this.pts.length);
		return null;
	}

	public final String toString() {
		if (isEmpty())
			return null;
		StringBuffer localStringBuffer;
		(localStringBuffer = new StringBuffer()).append("(");
		int i = this.pts.length;
		for (int j = 0; j < i; j++)
			localStringBuffer.append(this.pts[j].getX()).append(" ").append(this.pts[j].getY()).append(",");
		localStringBuffer.deleteCharAt(localStringBuffer.length() - 1);
		localStringBuffer.append(")");
		return localStringBuffer.toString();
	}

	public byte[] asBinary() {
		return null;
	}

	public String asText() {
		if (isEmpty())
			return null;
		StringBuffer localStringBuffer;
		(localStringBuffer = new StringBuffer()).append("LineString (");
		int i = this.pts.length;
		for (int j = 0; j < i; j++)
			localStringBuffer.append(this.pts[j].getX()).append(" ")
					.append(this.pts[j].getY()).append(",");
		localStringBuffer.deleteCharAt(localStringBuffer.length() - 1);
		localStringBuffer.append(")");
		return localStringBuffer.toString();
	}

	public IGeometry buffer(float paramFloat) {
		return null;
	}

	public Object Clone() {
		LineString localLineString = new LineString(this.pts, this.pts.length,
				getEnvelope());
		int i = getNumPoints();
		for (int j = 0; j < i; j++) {
			IPoint localIPoint = getPoint(j);
			localLineString.setPoint(j, new Point(localIPoint.getX(),
					localIPoint.getY()));
		}
		return localLineString;
	}

	public boolean contains(IGeometry paramIGeometry) {
		return false;
	}

	public IGeometry convexHull() {
		return null;
	}

	public boolean crosses(IGeometry paramIGeometry) {
		return false;
	}

	public IGeometry difference(IGeometry paramIGeometry) {
		return null;
	}

	public boolean disjoint(IGeometry paramIGeometry) {
		return false;
	}

	public float distance(IGeometry paramIGeometry) {
		return 0.0F;
	}

	public boolean equals(IGeometry paramIGeometry) {
		return false;
	}

	public IGeometry intersection(IGeometry paramIGeometry) {
		return null;
	}

	public boolean intersects(IGeometry paramIGeometry) {
		return false;
	}

	public boolean isSimple() {
		return false;
	}

	public boolean overlaps(IGeometry paramIGeometry) {
		return false;
	}

	public boolean relate(IGeometry paramIGeometry, String paramString) {
		return false;
	}

	public IGeometry symDifference(IGeometry paramIGeometry) {
		return null;
	}

	public boolean touches(IGeometry paramIGeometry) {
		switch (paramIGeometry.getGeometryType()) {
		case 1:
			return Arithmetic.GetNearest((IPoint) paramIGeometry, this) < 4.0F;
		}
		return false;
	}

	public IGeometry union(IGeometry paramIGeometry) {
		return null;
	}

	public boolean within(IGeometry paramIGeometry) {
		return false;
	}

	public IPoint getEndPoint() {
		if (this.pts.length > 0)
			return this.pts[(this.pts.length - 1)];
		return null;
	}

	public IEnvelope getEnvelope() {
		return this.a;
	}

	public void recalcEnvelope() {
		Envelope localEnvelope;
		(localEnvelope = new Envelope()).defineFromPoints(this.pts.length,
				this.pts);
		if (this.a != null)
			this.a = null;
		this.a = localEnvelope;
	}

	public byte getGeometryType() {
		return 3;
	}

	public float getLength() {
		return 0.0F;
	}

	public IPoint getStartPoint() {
		if (this.pts.length > 0)
			return this.pts[0];
		return null;
	}

	public int getNumPoints() {
		return this.pts.length;
	}

	public IPoint getPoint(int paramInt) {
		return this.pts[paramInt];
	}

	public void setPoint(int paramInt, IPoint paramIPoint) {
		if (this.pts[paramInt] != null)
			this.pts[paramInt] = null;
		this.pts[paramInt] = paramIPoint;
	}

	public void setPoints(IPoint[] paramArrayOfIPoint) {
		this.pts = paramArrayOfIPoint;
	}
}
