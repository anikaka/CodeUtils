package com.TY.bhgis.Display;

import android.graphics.Canvas;
import android.graphics.Paint;

public final class Display
  implements IDisplay
{
  private IDisplayTransformation b;
  private Canvas c;
  private Paint d;

  public final Canvas getCanvas()
  {
    return this.c;
  }

  public final Paint getPaint()
  {
    return this.d;
  }

  public Display(Canvas paramCanvas, IDisplayTransformation paramIDisplayTransformation)
  {
    this.c = paramCanvas;
    this.b = paramIDisplayTransformation;
    this.d = new Paint(0);
    this.d.setStrokeWidth(1.0F);
    this.d.setStyle(Paint.Style.FILL);
  }
  public final IDisplayTransformation getDisplayTransformation()
  {
    return this.b;
  }

  public final void setDisplayTransformation(IDisplayTransformation paramIDisplayTransformation)
  {
    this.b = paramIDisplayTransformation;
  }
}

