package com.TY.bhgis.MapFeature;

import com.TY.bhgis.Geometry.IGeometry;



public class LiningBZ {
	private IGeometry geometry;
	private String content;
	private String lczh;

	public LiningBZ(String lczh, IGeometry geometry, String content) {
		this.lczh = lczh;
		this.geometry = geometry;
		this.content = content;

	}

	public IGeometry getGeometry() {
		return geometry;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLCZH() {
		return lczh;
	}
	
}
