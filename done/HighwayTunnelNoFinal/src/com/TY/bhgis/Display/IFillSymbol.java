package com.TY.bhgis.Display;

public interface IFillSymbol {
	public void setAlpha(int paramInt);

	public int getAlpha();

	public int getColor();

	public void setColor(int paramInt);

	public void setColor(int paramInt1, int paramInt2, int paramInt3);

	public ILineSymbol getOutline();

	public void setOutline(ILineSymbol paramILineSymbol);
}