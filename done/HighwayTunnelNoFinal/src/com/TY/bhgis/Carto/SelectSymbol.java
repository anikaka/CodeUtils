package com.TY.bhgis.Carto;

import android.graphics.Color;

public class SelectSymbol {
	private float size=8;//Ô²°ë¾¶
	private float lineWidth=2;//Ïß¿í¶È
	private int color=Color.rgb(0, 255, 255);
	private int Alpha=150;
	public float getSize() {
		return size;
	}
	public void setSize(float size) {
		this.size = size;
	}
	public float getLineWidth() {
		return lineWidth;
	}
	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getAlpha() {
		return Alpha;
	}
	public void setAlpha(int alpha) {
		Alpha = alpha;
	}
	
}
