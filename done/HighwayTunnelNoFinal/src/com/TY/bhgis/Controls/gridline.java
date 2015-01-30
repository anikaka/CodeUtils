package com.TY.bhgis.Controls;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;

public class gridline {
	public float startX;
	public float startY;
	public float endX;
	public float endY;
	public int color;
	private int width;
	private boolean isdot=false;
	

	private Paint paint;

	public gridline(float startX, float startY, float endX, float endY) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.color = Color.BLACK;
		this.width = 2;
		paint = new Paint();
	}

	public int getLineWidth() {
		return width;
	}

	public void setLineWidth(int width) {
		this.width = width;
	}
	public boolean isIsdot() {
		return isdot;
	}

	public void setIsdot(boolean isdot) {
		this.isdot = isdot;
	}
	public void draw(Canvas g) {
		paint.setColor(color);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(this.width);
		if (isdot) {
			PathEffect mEffects = new DashPathEffect(new float[] {
					(float) 5, (float) 5 }, 1);
			paint.setPathEffect(mEffects);
		}
		g.drawLine(startX, startY, endX, endY, paint);
	}
}
