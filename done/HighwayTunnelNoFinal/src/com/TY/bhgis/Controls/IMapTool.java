package com.TY.bhgis.Controls;

import android.graphics.Canvas;

public interface IMapTool {
	  public abstract void pointerDragged(int x0, int y0);

	  public abstract void pointerPressed(int x0, int y0);

	  public abstract void pointerReleased(int x0, int y0);

	  public abstract void draw(Canvas canvas);
}

