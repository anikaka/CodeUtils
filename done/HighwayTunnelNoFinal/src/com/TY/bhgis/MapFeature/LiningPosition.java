package com.TY.bhgis.MapFeature;

import com.TY.bhgis.Geometry.IGeometry;



public class LiningPosition {
	private IGeometry geometry;
	private int position;//18,17,16,15,14,13,12,11,21,22,23,24,25,26,27,28 
	public LiningPosition(IGeometry geometry,int position)
	{
		this.geometry=geometry;
		this.position=position;
	}
	public IGeometry getGeometry()
	{
		return this.geometry;
	}
	public int getPosition()
	{
		return this.position;
	}
}
