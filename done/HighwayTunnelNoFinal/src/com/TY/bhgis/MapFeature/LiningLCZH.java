package com.TY.bhgis.MapFeature;

import com.TY.bhgis.Geometry.IGeometry;


public class LiningLCZH {
	private IGeometry geometry;
	private String lczh;//zk1+000


	public LiningLCZH(String lczh, IGeometry geometry) {
		this.lczh = lczh;
		this.geometry = geometry;
	}

	public IGeometry getGeometry() {
		return geometry;
	}

	public String getLczh() {
		return lczh;
	}

}
