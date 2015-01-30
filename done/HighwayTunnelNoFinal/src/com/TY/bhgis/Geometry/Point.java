package com.TY.bhgis.Geometry;

public final class Point extends Geometry implements IPoint {
	private float a;
	private float b;

	public Point(float paramFloat1, float paramFloat2) {
		this.a = paramFloat1;
		this.b = paramFloat2;
	}

	public Point() {
	}

	public final String toString() {
		return "(" + this.a + " " + this.b + ")";
	}

	public final byte[] asBinary() {
		return null;
	}

	public final String asText() {
		return "POINT (" + this.a + " " + this.b + ")";
	}

	public final IGeometry buffer(float paramFloat) {
		return null;
	}

	public final Object Clone() {
		Point localPoint;
		return localPoint = new Point(this.a, this.b);
	}

	public final boolean contains(IGeometry paramIGeometry) {
		return false;
	}

	public final IGeometry convexHull() {
		return null;
	}

	public final boolean crosses(IGeometry paramIGeometry) {
		return false;
	}

	public final IGeometry difference(IGeometry paramIGeometry) {
		return null;
	}

	public final boolean disjoint(IGeometry paramIGeometry) {
		return false;
	}

	public final float distance(IGeometry paramIGeometry) {
		return 0.0F;
	}

	public final boolean equals(IGeometry paramIGeometry) {
		return ((paramIGeometry instanceof IPoint))
				&& (((IPoint) paramIGeometry).getX() == this.a)
				&& (((IPoint) paramIGeometry).getY() == this.b);
	}

	public final IGeometry intersection(IGeometry paramIGeometry) {
		return null;
	}

	public final boolean intersects(IGeometry paramIGeometry) {
		return false;
	}

	public final boolean isEmpty() {
		return false;
	}

	public final boolean isSimple() {
		return true;
	}

	public final boolean overlaps(IGeometry paramIGeometry) {
		return false;
	}

	public final boolean relate(IGeometry paramIGeometry, String paramString) {
		return false;
	}

	public final IGeometry symDifference(IGeometry paramIGeometry) {
		return null;
	}

	public final boolean touches(IGeometry paramIGeometry) {
		switch (paramIGeometry.getGeometryType()) {
		case 1:
			if ((Arithmetic.Distance(this, (IPoint) paramIGeometry)) >= 7.0F)
				break;
			return true;
		}
		return false;
	}

	public final IGeometry union(IGeometry paramIGeometry) {
		return null;
	}

	public final boolean within(IGeometry paramIGeometry) {
		return false;
	}

	public final int getDimension() {
		return 0;
	}

	public final IEnvelope getEnvelope() {
		return null;
	}

	public final void recalcEnvelope() {
	}

	public final byte getGeometryType() {
		return 1;
	}

	public final float getX() {
		return this.a;
	}

	public final float getY() {
		return this.b;
	}

	public final void setX(float paramFloat) {
		this.a = paramFloat;
	}

	public final void setY(float paramFloat) {
		this.b = paramFloat;
	}
}
