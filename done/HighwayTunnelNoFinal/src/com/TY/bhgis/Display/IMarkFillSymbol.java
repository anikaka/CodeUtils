package com.TY.bhgis.Display;

import com.TY.bhgis.Display.MarkSymbol.MarkStyle;




public interface IMarkFillSymbol {
	public void setMarkSymbol(IMarkSymbol markSymbol);

	public IMarkSymbol getMarkSymbol();

	public void setSpace(int space);

	public int getSpace();

	public MarkStyle getStyle();

	public void setStyle(MarkSymbol.MarkStyle mStyle);
}
