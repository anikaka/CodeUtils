package com.TY.bhgis.Display;

import com.TY.bhgis.Display.LineSymbol.DashStyle;



public interface ILineFillSymbol {
	public DashStyle getStyle();

	public void setStyle(LineSymbol.DashStyle dStyle);

	public int getOffset();

	public void setOffset(int offset);

	public int getAngle();

	public void setAngle(int angel);

	public ILineSymbol getInline();

	public void setInline(ILineSymbol lineSymbol);
}
