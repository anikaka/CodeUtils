package com.TY.bhgis.MapLayer;

import com.TY.bhgis.Display.IDisplay;
import com.TY.bhgis.Geometry.IEnvelope;
import com.TY.bhgis.MapFeature.HighWayLining;



public interface IMapLayer {

	public HighWayLining[] getLinings();

	public float getLiningWidth();

	public float getLiningHeigth();

	public void setLiningWidth(float width);

	public void setLiningLength(float length);

	public int getVisibleCount();

	public int getVisibleStartLczh();
	
	public  HighWayLining getHighWayLining(int lczh);

	public void draw(IDisplay display, IEnvelope envelope);

}
