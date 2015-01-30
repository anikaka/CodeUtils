package com.TY.bhgis.Display;

import android.graphics.Canvas;
import android.graphics.Paint;

public interface IDisplay {
	public Canvas getCanvas();

	public Paint getPaint();

	public IDisplayTransformation getDisplayTransformation();

	public void setDisplayTransformation(
			IDisplayTransformation paramIDisplayTransformation);
}
