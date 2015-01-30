package com.TY.bhgis.Geometry;

public class Envelope implements IClone, IEnvelope {
	private float a;
	private float b;// XMax
	private float c;
	private float d;// YMax

	public Envelope(float paramFloat1, float paramFloat2, float paramFloat3,float paramFloat4, byte paramByte) {
		if (paramFloat1 < paramFloat2) {
			this.a = paramFloat1;
			this.b = paramFloat2;
		} else {
			this.a = paramFloat2;
			this.b = paramFloat1;
		}
		if (paramFloat3 < paramFloat4) {
			this.c = paramFloat3;
			this.d = paramFloat4;
			return;
		}
		this.c = paramFloat4;
		this.d = paramFloat3;
	}

	public Envelope() {
		this.b = 3.4028235E+38F;
		this.d = 3.4028235E+38F;
	}

	private Envelope(float paramFloat1, float paramFloat2, float paramFloat3,
			float paramFloat4) {
		this.a = paramFloat1;
		this.b = paramFloat2;
		this.c = paramFloat3;
		this.d = paramFloat4;
	}

	public static IEnvelope createEnvelope(float paramFloat1,
			float paramFloat2, float paramFloat3, float paramFloat4) {
		return new Envelope(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
	}

	public final String toString() {
		StringBuffer localStringBuffer;
		(localStringBuffer = new StringBuffer()).append("xmin=");
		localStringBuffer.append(this.a);
		localStringBuffer.append(" xmax=");
		localStringBuffer.append(this.b);
		localStringBuffer.append(" ymin=");
		localStringBuffer.append(this.c);
		localStringBuffer.append(" ymax=");
		localStringBuffer.append(this.d);
		return localStringBuffer.toString();
	}

	public void centerAt(IPoint paramIPoint) {
		if (paramIPoint != null) {
			float f1 = paramIPoint.getX() - (this.b + this.a) / 2.0F;
			float f2 = paramIPoint.getY() - (this.d + this.c) / 2.0F;
			this.a += f1;
			this.b += f1;
			this.c += f2;
			this.d += f2;
		}
	}

	public void defineFromPoints(int paramInt, IPoint[] paramArrayOfIPoint) {
		if ((paramArrayOfIPoint != null) && (paramArrayOfIPoint.length > 0)
				&& (paramInt > 0) && (paramArrayOfIPoint.length >= paramInt)) {
			float f1 = 3.4028235E+38F;
			float f2 = 1.4E-45F;
			float f3 = 3.4028235E+38F;
			float f4 = 1.4E-45F;
			for (int i = 0; i < paramInt; i++) {
				if (paramArrayOfIPoint[i] == null)
					continue;
				float f5 = paramArrayOfIPoint[i].getX();
				float f6 = paramArrayOfIPoint[i].getY();
				if (f5 < f1)
					f1 = f5;
				if (f5 > f2)
					f2 = f5;
				if (f6 < f3)
					f3 = f6;
				if (f6 <= f4)
					continue;
				f4 = f6;
			}
			this.a = f1;
			this.b = f2;
			this.c = f3;
			this.d = f4;
		}
	}

	public void expand(float paramFloat1, float paramFloat2,
			boolean paramBoolean) {
		if ((paramFloat1 == 0.0F) && (paramFloat2 == 0.0F))
			return;
		if (!paramBoolean) {
			paramFloat1 = getWidth() * paramFloat1 / 2.0F;
			paramFloat2 = getHeight() * paramFloat2 / 2.0F;
		}
		this.a -= paramFloat1;
		this.c -= paramFloat2;
		this.b += paramFloat1;
		this.d += paramFloat2;
	}

	public void offset(float paramFloat1, float paramFloat2) {
		if ((paramFloat1 == 0.0F) && (paramFloat2 == 0.0F))
			return;
		this.a += paramFloat1;
		this.b += paramFloat1;
		this.c += paramFloat2;
		this.d += paramFloat2;
	}

	public void putCoords(float paramFloat1, float paramFloat2,
			float paramFloat3, float paramFloat4) {
		if (paramFloat1 < paramFloat3) {
			this.a = paramFloat1;
			this.b = paramFloat3;
		} else {
			this.a = paramFloat3;
			this.b = paramFloat1;
		}
		if (paramFloat2 < paramFloat4) {
			this.c = paramFloat2;
			this.d = paramFloat4;
			return;
		}
		this.c = paramFloat4;
		this.d = paramFloat2;
	}

	public void putCoords(IEnvelope paramIEnvelope) {
		this.a = paramIEnvelope.getXMin();
		this.c = paramIEnvelope.getYMin();
		this.b = paramIEnvelope.getXMax();
		this.d = paramIEnvelope.getYMax();
	}

	public void queryCoords(Float paramFloat1, Float paramFloat2,
			Float paramFloat3, Float paramFloat4) {
	}

	public void union(IEnvelope paramIEnvelope) {
		if (paramIEnvelope.getXMin() < this.a)
			this.a = paramIEnvelope.getXMin();
		if (paramIEnvelope.getXMax() > this.b)
			this.b = paramIEnvelope.getXMax();
		if (paramIEnvelope.getYMin() < this.c)
			this.c = paramIEnvelope.getYMin();
		if (paramIEnvelope.getYMax() > this.d)
			this.d = paramIEnvelope.getYMax();
	}

	public float getHeight() {
		return this.d - this.c;
	}

	public IPoint getLowerLeft() {
		return new Point(this.a, this.c);
	}

	public IPoint getLowerRight() {
		return new Point(this.b, this.c);
	}

	public IPoint getUpperLeft() {
		return new Point(this.a, this.d);
	}

	public IPoint getUpperRight() {
		return new Point(this.b, this.d);
	}

	public float getWidth() {
		return this.b - this.a;
	}

	public float getXMax() {
		return this.b;
	}

	public float getXMin() {
		return this.a;
	}

	public float getYMax() {
		return this.d;
	}

	public float getYMin() {
		return this.c;
	}

	public void setHeight(float paramFloat) {
		if (paramFloat > 0.0F)
			this.d = (this.c + paramFloat);
	}

	public void setLowerLeft(IPoint paramIPoint) {
		if ((paramIPoint.getX() < this.b) && (paramIPoint.getY() < this.d)) {
			this.a = paramIPoint.getX();
			this.c = paramIPoint.getY();
		}
	}

	public void setLowerRight(IPoint paramIPoint) {
		if ((paramIPoint.getX() > this.a) && (paramIPoint.getY() < this.d)) {
			this.b = paramIPoint.getX();
			this.c = paramIPoint.getY();
		}
	}

	public void setUpperLeft(IPoint paramIPoint) {
		if ((paramIPoint.getX() < this.b) && (paramIPoint.getY() > this.c)) {
			this.a = paramIPoint.getX();
			this.d = paramIPoint.getY();
		}
	}

	public void setUpperRight(IPoint paramIPoint) {
		if ((paramIPoint.getX() > this.a) && (paramIPoint.getY() > this.c)) {
			this.b = paramIPoint.getX();
			this.d = paramIPoint.getY();
		}
	}

	public void setWidth(float paramFloat) {
		if (paramFloat > 0.0F)
			this.b = (this.a + paramFloat);
	}

	public void setXMax(float paramFloat) {
		if (paramFloat >= this.a)
			this.b = paramFloat;
	}

	public void setXMin(float paramFloat) {
		if (paramFloat <= this.b)
			this.a = paramFloat;
	}

	public void setYMax(float paramFloat) {
		if (paramFloat >= this.c)
			this.d = paramFloat;
	}

	public void setYMin(float paramFloat) {
		if (paramFloat <= this.d)
			this.c = paramFloat;
	}

	public Object Clone() {
		return new Envelope(this.a, this.b, this.c, this.d, (byte) 0);
	}

	@Override
	public IPoint getCenterPoint() {

		return new Point((this.b - this.a) / 2 + this.a, (this.d - this.c) / 2
				+ this.c);
	}
}
