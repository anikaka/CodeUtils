package com.TY.bhgis.Display;



import com.TY.bhgis.Display.LineSymbol.DashStyle;
import com.TY.bhgis.Geometry.IEnvelope;
import com.TY.bhgis.Geometry.IGeometry;
import com.TY.bhgis.Geometry.IPoint;
import com.TY.bhgis.Geometry.LinearRing;
import com.TY.bhgis.Geometry.Point;
import com.TY.bhgis.Geometry.IPolygon;
import com.TY.bhgis.Util.SymbolHelp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Bitmap.Config;


public class LineFillSymbol extends FillSymbol implements ILineFillSymbol {

	private int offset;
	private int angle;
	private ILineSymbol inlineSymbol;


	private Bitmap BitmapLegend;

	public LineFillSymbol() {
		this.offset = 10;
		this.angle = 45;
		this.inlineSymbol = new SimpleLineSymbol();
		((ISimpleLineSymbol)this.inlineSymbol).setStyle(DashStyle.Solid);
		super.setAlpha(0);

	}

	@Override
	public int getOffset() {
		// TODO Auto-generated method stub
		return this.offset;
	}

	@Override
	public void setOffset(int offset) {
		// TODO Auto-generated method stub
		this.offset = offset;
	}

	@Override
	public int getAngle() {
		// TODO Auto-generated method stub
		return this.angle;
	}

	@Override
	public void setAngle(int angel) {
		// TODO Auto-generated method stub
		this.angle = angel;
	}

	@Override
	public ILineSymbol getInline() {
		// TODO Auto-generated method stub
		return this.inlineSymbol;
	}

	@Override
	public void setInline(ILineSymbol lineSymbol) {
		// TODO Auto-generated method stub
		this.inlineSymbol = lineSymbol;
	}

	@Override
	public DashStyle getStyle() {
		// TODO Auto-generated method stub
		return ((ISimpleLineSymbol)this.inlineSymbol).getStyle();
	}

	@Override
	public void setStyle(DashStyle dStyle) {
		// TODO Auto-generated method stub
		((ISimpleLineSymbol)this.inlineSymbol).setStyle(dStyle);
	}

	@Override
	public void draw(Canvas canvas,
			IDisplayTransformation displayTransformation, IGeometry geometry) {
		if (geometry.getGeometryType() == 5) {
			LinearRing localLinearRing = (LinearRing) ((IPolygon) geometry)
					.getExteriorRing();
			if (localLinearRing.getNumPoints() <= 0)
				return;

			Path path = new Path();
			Point point = new Point();
			displayTransformation.fromMapPoint(localLinearRing.getPoint(0),
					point);
			path.moveTo(point.getX(), point.getY());
			for (int k = 1; k < localLinearRing.getNumPoints(); k++) {
				displayTransformation.fromMapPoint(localLinearRing.getPoint(k),
						point);
				path.lineTo(point.getX(), point.getY());
			}
			Paint paint = new Paint();
			paint.setColor(super.getColor());
			paint.setAlpha(super.getAlpha());
			paint.setStyle(Paint.Style.FILL);
			paint.setAntiAlias(true);
			canvas.drawPath(path, paint);

			// 画外框
			if (super.getOutline() != null) {
				((ISymbol) super.getOutline()).draw(canvas,
						displayTransformation,
						(IGeometry) ((IPolygon) geometry).getExteriorRing());
			}
			// 画内部填充线
			if (this.inlineSymbol != null) {

				// 得到geometry的最小外包矩形左上坐标点，右下坐标点，转换为屏幕坐标点
				geometry.recalcEnvelope();
				IEnvelope envelope = geometry.getEnvelope();
				IPoint pointMin = new Point(envelope.getXMin(),
						envelope.getYMax());
				IPoint pointMax = new Point(envelope.getXMax(),
						envelope.getYMin());
				IPoint pointMin1 = new Point();
				displayTransformation.fromMapPoint(pointMin, pointMin1);
				IPoint pointMax1 = new Point();
				displayTransformation.fromMapPoint(pointMax, pointMax1);

				// 按照外包矩形的大小创建一个位图
				Bitmap bitmap2 = Bitmap
						.createBitmap(
								(int) Math.abs(pointMax1.getX()
										- pointMin1.getX()) + 1,
								(int) Math.abs(pointMax1.getY()
										- pointMin1.getY()) + 1,
								Bitmap.Config.ARGB_8888);
				Canvas c = new Canvas(bitmap2);
				// 按照geometry的图形形状在位图上裁剪出一个显示区域
				SymbolHelp.ClipBitmap((IGeometry) geometry, c, displayTransformation,
						pointMin1.getX(), pointMin1.getY());

				// 生成时间太慢改成单例
				if (BitmapLegend == null) {
					BitmapLegend = SymbolHelp.GetCreateRepeater(
							canvas.getWidth(),
							canvas.getHeight(),
							getSeedBitmap(this.inlineSymbol, this.offset,
									this.angle));
				}
				// 将图例位图画到裁剪好的位图上
				c.drawBitmap(BitmapLegend, 0, 0, null);
				//将裁剪好的位图画到map上指定的位置
				canvas.drawBitmap(bitmap2, pointMin1.getX(), pointMin1.getY(),
						null);
			}

		}
	}
	private  Bitmap getSeedBitmap(ILineSymbol lineSymbol, int offset,
			int mAngle) {
		int angle = mAngle;
		// 角度在0到90度之间为true
		boolean flag = true;
		if (mAngle > 180) {
			int i = 1;
			while (angle < 180) {
				angle = mAngle - i * 180;
				i++;
			}
		}
		if (mAngle < 0)
			angle = 45;
		if (mAngle < 180 && mAngle > 90) {
			angle = Math.abs(mAngle - 180);
			flag = false;
		}
		Bitmap bitmap = null;
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(lineSymbol.getWidth());
		paint.setColor(lineSymbol.getColor());

		if (mAngle == 0 || mAngle == 90) {
			int zwidth = (int) 3 * offset;
			int zheight = (int) 3 * offset;
			bitmap = Bitmap.createBitmap(zwidth, zheight,
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);

			if (((ISimpleLineSymbol)lineSymbol).getStyle() == DashStyle.Dot) {
				PathEffect mEffects = new DashPathEffect(new float[] {
						(float) zwidth / 4, (float) zwidth / 4 }, 1);
				paint.setPathEffect(mEffects);
			}
			if (mAngle == 0) {
				canvas.drawLine(0f, offset*0.75f, 3 * offset, offset*0.75f, paint);
				canvas.drawLine(0f,  offset*2.25f, 3 * offset, offset*2.25f, paint);
			}
			if (mAngle == 90) {
				canvas.drawLine(offset, 0, offset, 3 * offset, paint);
				canvas.drawLine(2 * offset, 0, 2 * offset, 3 * offset, paint);
			}

		} else {

			double sin = Math.sin(angle * Math.PI / 180);
			double cos = Math.cos(angle * Math.PI / 180);
			// 种子位图的宽高
			double zweight = 2 * (offset / sin);
			double zheight = 2 * (offset / cos);

			bitmap = Bitmap.createBitmap((int) zweight, (int) zheight,
					Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);

			if (((ISimpleLineSymbol)lineSymbol).getStyle()  == DashStyle.Dot) {
				PathEffect mEffects = new DashPathEffect(new float[] {
						(float) Math.sqrt((zweight / 2) * (zweight / 2)
								+ (zheight / 2) * (zheight / 2)) / 4,
						(float) Math.sqrt((zweight / 2) * (zweight / 2)
								+ (zheight / 2) * (zheight / 2)) / 4 }, 1);
				paint.setPathEffect(mEffects);
			}
			if (flag) {
				canvas.drawLine(0f, (float) zheight / 2, (float) zweight / 2,
						0f, paint);
				canvas.drawLine(0f, (float) zheight, (float) zweight, 0f, paint);
				canvas.drawLine((float) zweight / 2, (float) zheight,
						(float) zweight, (float) zheight / 2, paint);
			} else {
				canvas.drawLine(0f, (float) zheight / 2, (float) zweight / 2,
						(float) zheight, paint);
				canvas.drawLine(0f, 0f, (float) zweight, (float) zheight, paint);
				canvas.drawLine((float) zweight / 2, 0f, (float) zweight,
						(float) zheight / 2, paint);
			}
		}
		return bitmap;
	}
	
}
