package com.TY.bhgis.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public final class Image
{
  private int a;
  private int b;
  private Bitmap c;

  public final void clear()
  {
    if (this.c != null)
      this.c.recycle();
    this.c = null;
  }

  public Image(Bitmap paramBitmap)
  {
    this.c = paramBitmap;
    this.a = paramBitmap.getWidth();
    this.b = paramBitmap.getHeight();
  }

  public Image(byte[] paramArrayOfByte)
  {
    this.c = BitmapFactory.decodeByteArray(paramArrayOfByte, 0, paramArrayOfByte.length);
    this.a = this.c.getWidth();
    this.b = this.c.getHeight();
  }

  public Image(String paramString)
  {
    this.c = BitmapFactory.decodeFile(paramString);
    this.a = this.c.getWidth();
    this.b = this.c.getHeight();
  }

  public Image(String paramString, int paramInt)
  {
    this.c = BitmapFactory.decodeFile(paramString);
    this.a = this.c.getWidth();
    this.b = this.c.getHeight();
    if ((paramString.endsWith(".bmp")) || (paramString.endsWith(".jpg")) || (paramString.endsWith(".gif")))
    {
     int[] paramArrayOfInt = new int[this.a * this.b];
      getRGB(paramArrayOfInt, 0, this.a, 0, 0, this.a, this.b);
      for (int i = 0; i < paramArrayOfInt.length; i++)
      {
        if (paramArrayOfInt[i] != paramInt)
          continue;
        paramArrayOfInt[i] = 16777215;
      }
      this.c = null;
      this.c = Bitmap.createBitmap(this.a, this.b, Bitmap.Config.ARGB_8888);
      this.c.setPixels(paramArrayOfInt, 0, this.a, 0, 0, this.a, this.b);
    }
  }

  public Image()
  {
  }

  public final boolean createWithAlpha(String paramString)
  {
    this.c = BitmapFactory.decodeFile(paramString);
    if (this.c == null)
      return false;
    this.a = this.c.getWidth();
    this.b = this.c.getHeight();
    return true;
  }

  public final boolean parse(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return parseWithAlpha(paramArrayOfByte, paramInt1, paramInt2);
  }

  public final boolean parseWithAlpha(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.c = BitmapFactory.decodeByteArray(paramArrayOfByte, paramInt1, paramInt2);
    if (this.c == null)
      return false;
    this.a = this.c.getWidth();
    this.b = this.c.getHeight();
    return true;
  }

  public final int getWidth()
  {
    return this.a;
  }

  public final int getHeight()
  {
    return this.b;
  }

  public final void getRGB(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    this.c.getPixels(paramArrayOfInt, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
  }


  public final void draw(Canvas paramCanvas, int paramInt1, int paramInt2, Paint paramPaint)
  {
    paramCanvas.drawBitmap(this.c, paramInt1, paramInt2, paramPaint);
  }
}
