package com.TY.bhgis.Geometry;

public final class LinearRing extends LineString implements ILinearRing {
	public LinearRing(IPoint[] paramArrayOfIPoint, int paramInt) {
		super(paramArrayOfIPoint, paramInt, null);
	}

	public LinearRing(int paramInt, IEnvelope paramIEnvelope) {
		super(paramInt, paramIEnvelope);
	}

	public LinearRing(Line[] lines) {
		super(lines);
	}

	public final byte getGeometryType() {
		return 7;
	}

	public final boolean isSimple() {
		return true;
	}

	public final boolean isClosed() {
		return true;
	}

	public final boolean isCCW() {
		return false;
	}

	public final Object clone() {
		LinearRing localLinearRing = new LinearRing(this.pts, this.pts.length);
		int i = getNumPoints();
		for (int j = 0; j < i; j++) {
			IPoint localIPoint = getPoint(j);
			localLinearRing.setPoint(j, new Point(localIPoint.getX(),
					localIPoint.getY()));
		}
		return localLinearRing;
	}
}
