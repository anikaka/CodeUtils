package com.TY.bhgis.Carto;

import com.TY.bhgis.Database.IBHClass;
import com.TY.bhgis.Geometry.IEnvelope;
import com.TY.bhgis.MapLayer.IMapLayer;




public abstract interface IMap {
	public IMapLayer getMapLayer();
	
	public void setMapLayer(IMapLayer mapLayer);

	public IEnvelope load( float RingWidth, float RingLength,
			int RingCount);

	public IEnvelope getFullExtent();

	public IBHClass getBHClass(int type);

	public IBHClass[] getBHClasses();

	public void setBhClasses(IBHClass[] bhcClasses);

	public void clearBHClasses();
}