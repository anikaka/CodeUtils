package com.TY.bhgis.Display;


import com.TY.bhgis.Geometry.IGeometry;
import com.TY.bhgis.Geometry.IPoint;
import com.TY.bhgis.Geometry.Point;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class TextMarkSymbol extends MarkSymbol implements ITextMarkSymbol {

	private String content;
	private boolean horizontal = true;
	private boolean first = true;

	private int textsize;
	private int color;
	private TextMarkStyle textMarkStyle;

	private boolean left = true;// 用于滴漏和道床脱开

	public TextMarkSymbol() {
		this.textsize = 20;
		this.color = Color.BLACK;
		this.textMarkStyle = TextMarkStyle.DiLou;
		this.content = "221";
	}

	@Override
	public String getText() {
		
		return this.content;
	}

	@Override
	public void setText(String content) {
		
		this.content = content;
	}

	@Override
	public int getTextSize() {
		
		return this.textsize;
	}

	@Override
	public void setTextSize(int textSize) {
		
		this.textsize = textSize;
	}

	@Override
	public TextMarkStyle getTextMarkStyle() {
		
		return this.textMarkStyle;
	}

	@Override
	public void setTextMarkStyle(TextMarkStyle textMarkStyle) {
		
		this.textMarkStyle = textMarkStyle;
	}

	@Override
	public int getColor() {
		
		return this.color;
	}

	@Override
	public void setColor(int paramInt) {
		
		this.color = paramInt;
	}

	@Override
	public void setColor(int paramInt1, int paramInt2, int paramInt3) {
		
		this.color = Color.rgb(paramInt1, paramInt2, paramInt3);
	}

	@Override
	public int getSize() {
		
		return 0;
	}

	@Override
	public void setSize(int paramFloat) {
		

	}

	public boolean isHorizontal() {
		return horizontal;
	}

	public void setHorizontal(boolean horizontal) {
		this.horizontal = horizontal;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	@Override
	public void draw(Canvas canvas,
			IDisplayTransformation displayTransformation, IGeometry geometry) {
		
		if (geometry instanceof IPoint) {
			IPoint point = (IPoint) geometry;
			IPoint point2 = new Point();
			displayTransformation.fromMapPoint(point, point2);
			Paint paint = new Paint();
			paint.setColor(this.color);
			paint.setStrokeWidth(1);
			paint.setTextSize(this.textsize);
			float x = point2.getX();
			float y = point2.getY();
			if (this.textMarkStyle == TextMarkStyle.DiLou) {
				String value = this.content;
				if (Float.valueOf(this.content) < 1) {
					value = "<1";

				} else if (Float.valueOf(this.content) > 60) {
					value = "∞";

				}
				if (!left) {

					x = x + 2 * this.textsize;
					RectF rect = new RectF(x - this.textsize, y - this.textsize
							* 2 / 3, x
							+ (this.textsize * value.length() * 2 / 3), y
							+ this.textsize * 2 / 3);
					if (value.length() == 1) {
						canvas.drawText(value,
								x - this.textsize * value.length() / 2, y
										+ this.textsize / 4, paint);
					} else {
						canvas.drawText(value,
								x - this.textsize * value.length() / 4, y
										+ this.textsize / 4, paint);
					}
					paint.setAntiAlias(true);
					paint.setStyle(Paint.Style.STROKE);
					canvas.drawOval(rect, paint);
					canvas.drawLine(x - this.textsize, y,
							x - 2 * this.textsize, y, paint);
				} else {

					x = x - 2 * this.textsize;
					RectF rect = new RectF(x
							- (this.textsize * value.length() * 2 / 3), y
							- this.textsize * 2 / 3, x + this.textsize, y
							+ this.textsize * 2 / 3);
					if (value.length() == 1) {
						canvas.drawText(value,
								x - this.textsize * value.length() / 6, y
										+ this.textsize / 4, paint);
					} else {
						canvas.drawText(value,
								x - this.textsize * value.length() / 3, y
										+ this.textsize / 4, paint);
					}
					paint.setAntiAlias(true);
					paint.setStyle(Paint.Style.STROKE);
					canvas.drawOval(rect, paint);
					canvas.drawLine(x + this.textsize, y,
							x + 2 * this.textsize, y, paint);
				}

			} else if (this.textMarkStyle == TextMarkStyle.GuanPianCuoTai) {

				if (horizontal && first) {
					canvas.drawText(this.content, x - 3 * this.textsize / 2, y
							- this.textsize / 2, paint);
					paint.setStyle(Paint.Style.STROKE);
					paint.setAntiAlias(true);
					canvas.drawLine(x - this.textsize, y, x + this.textsize, y,
							paint);
				} else if (horizontal && !first) {
					canvas.drawText(this.content, x + this.textsize, y
							- this.textsize / 2, paint);
					paint.setStyle(Paint.Style.STROKE);
					paint.setAntiAlias(true);
					canvas.drawLine(x - this.textsize, y, x + this.textsize, y,
							paint);

				} else if (!horizontal && first) {
					canvas.drawText(
							this.content,
							x
									- (this.textsize * content.length() / 2 + this.textsize / 4),
							y - this.textsize, paint);
					paint.setStyle(Paint.Style.STROKE);
					paint.setAntiAlias(true);
					canvas.drawLine(x, y - this.textsize, x, y + this.textsize,
							paint);
				} else {
					canvas.drawText(
							this.content,
							x
									- (this.textsize * content.length() / 2 + this.textsize / 4),
							y + 3 * this.textsize / 2, paint);
					paint.setStyle(Paint.Style.STROKE);
					paint.setAntiAlias(true);
					canvas.drawLine(x, y - this.textsize, x, y + this.textsize,
							paint);
				}

			} else if (this.textMarkStyle == textMarkStyle.GuanPianJieFeng) {
				paint.setStyle(Paint.Style.STROKE);
				paint.setAntiAlias(true);

				canvas.drawLine(x - this.textsize / 2, y + this.textsize/2, x, y
						- this.textsize/2, paint);
				canvas.drawLine(x, y - this.textsize/2, x + this.textsize / 2, y
						+ this.textsize/2, paint);
			} else if (this.textMarkStyle == textMarkStyle.DaoChuangTuoKai) {
				paint.setStyle(Paint.Style.STROKE);
				paint.setAntiAlias(true);
				if (!left) {
					// x做个偏移，使其显示的图例与点接近
					x = x - this.textsize / 2;
					RectF rect = new RectF(x - this.textsize,
							y - this.textsize, x + this.textsize, y
									+ this.textsize);
					canvas.drawArc(rect, -70, 140, false, paint);
					double x1 = x + this.textsize / 2;
					double y1 = y + this.textsize
							* Math.sin(60 * Math.PI / 180);

					canvas.drawLine((float) x1, (float) y1, (float) x1,
							(float) (2 * y - y1), paint);
				} else {
					// x做个偏移，使其显示的图例与点接近
					x = x + this.textsize/2;
					RectF rect = new RectF(x - this.textsize,
							y - this.textsize, x + this.textsize, y
									+ this.textsize);
					canvas.drawArc(rect, 110, 140, false, paint);
					double x1 = x - this.textsize / 2;
					double y1 = y + this.textsize
							* Math.sin(60 * Math.PI / 180);

					canvas.drawLine((float) x1, (float) y1, (float) x1,
							(float) (2 * y - y1), paint);
				}

			} else {
				
				Rect rectstr = new Rect();
				//返回包围整个字符串的最小的一个Rect区域
				paint.getTextBounds(this.content, 0, this.content.length(), rectstr); 
				int strwid = rectstr.width();
				int strhei = rectstr.height();

//				g.drawText(content, rect.centerX()
//						- strwid/2,
//						rect.centerY()+strhei/2, paint);
				//这里的x和y要是文字的中心点
				canvas.drawText(this.content, x- strwid/2, y+strhei/2, paint);
			}

		}
	}

}
