package com.TY.bhgis.Display;

import com.TY.bhgis.Display.LineSymbol.DashStyle;


public interface ISimpleLineSymbol {
	public DashStyle getStyle();

	public void setStyle(LineSymbol.DashStyle dStyle);
	
}
