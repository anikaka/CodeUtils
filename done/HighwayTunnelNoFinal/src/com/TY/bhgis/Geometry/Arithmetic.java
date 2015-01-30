package com.TY.bhgis.Geometry;

public final class Arithmetic {
	public static boolean EntireContains(IPolygon paramIPolygon,
			IPoint paramIPoint) {
		LinearRing linearRing = (LinearRing) paramIPolygon.getExteriorRing();
		int i = 0;
		Point localPoint = new Point(paramIPoint.getX(), 0.0F);
		int j = linearRing.getNumPoints() - 1;
		for (int k = 0; k < j; k++) {
			if (!Intersect(linearRing.getPoint(k), linearRing.getPoint(k + 1),
					paramIPoint, localPoint))
				continue;
			i++;
		}
		if (Intersect(linearRing.getPoint(j), linearRing.getPoint(0),
				paramIPoint, localPoint))
			i++;
		return i % 2 == 1;
	}

	public static final float getDistance(float paramFloat1, float paramFloat2,
			float paramFloat3, float paramFloat4) {
		double d1 = paramFloat2 / 180.0F * 3.141592653589793D;
		double d2 = paramFloat1 / 180.0F * 3.141592653589793D;
		double d3 = paramFloat4 / 180.0F * 3.141592653589793D;
		double d4 = paramFloat3 / 180.0F * 3.141592653589793D;
		double d5;
		return (float) (d5 = Math.acos(d5 = Math.sin(d1) * Math.sin(d3)
				+ Math.cos(d1) * Math.cos(d3) * Math.cos(d4 - d2)) * 6370693.4856530577D);
	}

	public static final float Distance(IPoint paramIPoint1, IPoint paramIPoint2) {
		return (float) Math.sqrt((paramIPoint1.getX() - paramIPoint2.getX())
				* (paramIPoint1.getX() - paramIPoint2.getX())
				+ (paramIPoint1.getY() - paramIPoint2.getY())
				* (paramIPoint1.getY() - paramIPoint2.getY()));
	}

	public static final float Distance(IPoint paramIPoint, float paramFloat1,
			float paramFloat2) {
		return (float) Math.sqrt((paramIPoint.getX() - paramFloat1)
				* (paramIPoint.getX() - paramFloat1)
				+ (paramIPoint.getY() - paramFloat2)
				* (paramIPoint.getY() - paramFloat2));
	}

	public static final float Distance(float paramFloat1, float paramFloat2,
			float paramFloat3, float paramFloat4) {
		return (float) Math.sqrt((paramFloat1 - paramFloat3)
				* (paramFloat1 - paramFloat3) + (paramFloat2 - paramFloat4)
				* (paramFloat2 - paramFloat4));
	}

	public static final float GetNearest(IPoint paramIPoint,
			ILineString paramILineString) {

		return 0.0f;
	}

	public static IPoint GetFoot(IPoint paramIPoint1, IPoint paramIPoint2,
			IPoint paramIPoint3) {
		return null;
	}

	public static boolean EntireContains(IPoint[] paramArrayOfIPoint,
			IPoint paramIPoint) {
		int i = 0;
		float f = paramIPoint.getX();
		paramIPoint = new Point(f, paramIPoint.getY());
		Point localPoint = new Point(f, 0.0F);
		for (int j = 0; j < paramArrayOfIPoint.length - 1; j++) {
			if ((f == paramArrayOfIPoint[j].getX())
					|| (f == paramArrayOfIPoint[(j + 1)].getX())) {
				f = (float) (f + 1.E-005D);
				paramIPoint.setX(f);
				localPoint.setX(f);
			}
			if (!Intersect(paramArrayOfIPoint[j], paramArrayOfIPoint[(j + 1)],
					paramIPoint, localPoint))
				continue;
			i++;
		}
		if (Intersect(paramArrayOfIPoint[(paramArrayOfIPoint.length - 1)],
				paramArrayOfIPoint[0], paramIPoint, localPoint))
			i++;
		return i % 2 == 1;
	}

	public static final boolean Intersect(IPoint paramIPoint1,
			IPoint paramIPoint2, IPoint paramIPoint3, IPoint paramIPoint4) {
		tempP locala1 = GetTempP(paramIPoint1, paramIPoint2);
		tempP locala2 = GetTempP(paramIPoint1, paramIPoint3);
		tempP locala3 = GetTempP(paramIPoint1, paramIPoint4);
		double d1 = locala1.a * locala2.b - locala1.b * locala2.a;
		double d2 = locala1.a * locala3.b - locala1.b * locala3.a;
		locala1 = GetTempP(paramIPoint3, paramIPoint4);
		locala2 = GetTempP(paramIPoint3, paramIPoint1);
		locala3 = GetTempP(paramIPoint3, paramIPoint2);
		double d3 = locala1.a * locala2.b - locala1.b * locala2.a;
		double d4 = locala1.a * locala3.b - locala1.b * locala3.a;
		return (d1 * d2 <= 0.0D) && (d3 * d4 <= 0.0D);
	}

	private static final tempP GetTempP(IPoint paramIPoint1, IPoint paramIPoint2) {
		float f1 = paramIPoint2.getX() - paramIPoint1.getX();
		float f2 = paramIPoint2.getY() - paramIPoint1.getY();
		return new tempP(f1, f2);
	}

	public static final boolean EntireContains(IPoint[] paramArrayOfIPoint1,
			IPoint[] paramArrayOfIPoint2) {
		for (int i = 0; i < paramArrayOfIPoint1.length; i++)
			if (!EntireContains(paramArrayOfIPoint2, paramArrayOfIPoint1[i]))
				return false;
		return true;
	}


}	
final class tempP {
	float a;
	float b;

	public tempP(float paramFloat1, float paramFloat2) {
		this.a = paramFloat1;
		this.b = paramFloat2;
	}
}
