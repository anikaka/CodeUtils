package com.TY.bhgis.Display;


import com.TY.bhgis.Geometry.IGeometry;

import android.graphics.Canvas;


public interface ISymbol {
 public void draw(Canvas canvas,IDisplayTransformation displayTransformation,IGeometry geometry);
}
