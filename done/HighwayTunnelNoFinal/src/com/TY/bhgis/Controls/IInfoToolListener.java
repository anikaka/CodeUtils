package com.TY.bhgis.Controls;

import java.util.Vector;

import com.TY.bhgis.Database.IBH;



public interface IInfoToolListener {
	public void notify(MapControl paramMapControl, Vector<IBH> bhs);
}
