package com.TY.bhgis.Display;



import com.TY.bhgis.Display.MarkSymbol.MarkStyle;
import com.TY.bhgis.Geometry.IGeometry;
import com.TY.bhgis.Geometry.ILineString;
import com.TY.bhgis.Geometry.IPoint;
import com.TY.bhgis.Geometry.Point;
import com.TY.bhgis.Util.MathUtil;



import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;


public class MultiLineSymbol extends LineSymbol implements IMultiLineSymbol {

	private int color;
	private float width;
	private IMarkSymbol markSymbol;
	private int interval;

	public MultiLineSymbol() {
		this.color = Color.RED;
		this.width = 1f;
		markSymbol = new SimpleMarkSymbol();
		markSymbol.setSize(5);
		((ISimpleMarkSymbol) this.markSymbol).setStyle(MarkStyle.Cross);
		this.interval = 20;
	}

	@Override
	public int getColor() {
		// TODO Auto-generated method stub
		return this.color;
	}

	@Override
	public void setColor(int paramInt) {
		// TODO Auto-generated method stub
		this.color = paramInt;
	}

	@Override
	public void setColor(int paramInt1, int paramInt2, int paramInt3) {
		// TODO Auto-generated method stub
		this.color = Color.rgb(paramInt1, paramInt2, paramInt3);
	}

	@Override
	public float getWidth() {
		// TODO Auto-generated method stub
		return this.width;
	}

	@Override
	public void setWidth(float paramFloat) {
		// TODO Auto-generated method stub
		this.width = paramFloat;
	}

	@Override
	public void draw(Canvas canvas,
			IDisplayTransformation displayTransformation, IGeometry geometry) {
		if (geometry.getGeometryType() == 4) {

			ILineString lineString = (ILineString) geometry;
			IPoint point = lineString.getPoint(0);
			Point pt0 = new Point();
			displayTransformation.fromMapPoint(point, pt0);
			Path path = new Path();
			path.moveTo(pt0.getX(), pt0.getY());

			point = lineString.getPoint(1);

			IPoint pt1 = new Point();
			displayTransformation.fromMapPoint(point, pt1);

			path.lineTo(pt1.getX(), pt1.getY());

			Paint paint = new Paint();

			paint.setAntiAlias(true);

			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(this.color);
			paint.setStrokeWidth(this.width);
			canvas.drawPath(path, paint);

			// 画点样式部分
			IPoint[] points = getPoints(pt0, pt1, this.interval);
			Bitmap bitmap = ((SimpleMarkSymbol) markSymbol)
					.getBitmap(markSymbol.getSize() * 2);
			for (int i = 0; i < points.length; i++) {
				float x = points[i].getX() - markSymbol.getSize();
				float y = points[i].getY() - markSymbol.getSize();
				canvas.drawBitmap(bitmap, x, y, null);
			}
		}
	}

	@Override
	public void setMarkSymbol(IMarkSymbol markSymbol) {
		// TODO Auto-generated method stub
		this.markSymbol = markSymbol;
	}

	@Override
	public IMarkSymbol getMarkSymbol() {
		// TODO Auto-generated method stub
		return this.markSymbol;
	}

	@Override
	public void setInterval(int interval) {
		// TODO Auto-generated method stub
		this.interval = interval;
	}

	@Override
	public int getInterval() {
		// TODO Auto-generated method stub
		return this.interval;
	}

	//根据间隔距计算出直线上的间隔点
	private IPoint[] getPoints(IPoint stratPt, IPoint endPt, int interval) {

		double distance = MathUtil.getDistance(stratPt.getX(), stratPt.getY(),
				endPt.getX(), endPt.getY());
		
		Fangcheng fangcheng = new Fangcheng(stratPt.getX(), stratPt.getY(),
				endPt.getX(), endPt.getY());

		int count = (int) (distance - 1) / interval;
		IPoint[] points = new IPoint[count];
		float dy = (endPt.getY() - stratPt.getY()) / count;
		if (Math.abs(dy)<0.1) {
			float dx = (endPt.getX() - stratPt.getX()) / count;
			for (int i = 1; i <= count; i++) {
				float y = endPt.getY();
				float x = stratPt.getX()-dx/2 + dx * i;
				IPoint pt = new Point(x, y);
				points[i - 1] = pt;
			}
		}
		else {
			for (int i = 1; i <= count; i++) {
				float y = stratPt.getY()-dy/2 + dy * i;
				float x = fangcheng.getX(y);
				IPoint pt = new Point(x, y);
				points[i - 1] = pt;
			}
		}
		

		return points;

	}

	// 直线方程
	class Fangcheng {
		private float k;
		private float m;

		private boolean flag = true;
		private float x;

		public Fangcheng(float x1, float y1, float x2, float y2) {
			if (Math.abs(x2 - x1) > 0.1) {
				this.k = (y2 - y1) / (x2 - x1);
				this.m = y1 - this.k * x1;
				flag = false;
			} else {
				x = x1;
				flag = true;
			}

		}

		// 通过y得到x
		public float getX(float y) {
			if (flag) {
				return x;
			} else {
				return (y - this.m) / this.k;
			}

		}


	}
}
