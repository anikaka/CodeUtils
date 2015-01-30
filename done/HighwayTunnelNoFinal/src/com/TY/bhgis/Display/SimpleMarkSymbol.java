package com.TY.bhgis.Display;



import com.TY.bhgis.Geometry.IGeometry;
import com.TY.bhgis.Geometry.IPoint;
import com.TY.bhgis.Geometry.Point;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;

public class SimpleMarkSymbol extends MarkSymbol implements IMarkSymbol,
		ISimpleMarkSymbol {

	private int size;
	private int color;
	private MarkStyle markStyle;

	public SimpleMarkSymbol() {
		this.size = 10;
		this.color = Color.BLUE;

		this.markStyle = MarkStyle.SolidCircle;

	}

	@Override
	public MarkStyle getStyle() {
		// TODO Auto-generated method stub
		return this.markStyle;
	}

	@Override
	public void setStyle(MarkStyle mStyle) {
		// TODO Auto-generated method stub
		this.markStyle = mStyle;
	}

	@Override
	public int getColor() {
		// TODO Auto-generated method stub
		return this.color;
	}

	@Override
	public void setColor(int paramInt) {
		// TODO Auto-generated method stub
		this.size = paramInt;
	}

	@Override
	public void setColor(int paramInt1, int paramInt2, int paramInt3) {
		// TODO Auto-generated method stub
		this.color = Color.rgb(paramInt1, paramInt2, paramInt3);
	}

	public void draw(Canvas canvas,
			IDisplayTransformation displayTransformation, IGeometry geometry) {
		if (geometry.getGeometryType() == 1) {
			IPoint point = (IPoint) geometry;
			IPoint point2 = new Point();
			displayTransformation.fromMapPoint(point, point2);

			Bitmap bitmap= getBitmap(this.size*2);
			canvas.drawBitmap(bitmap, point2.getX()-this.size, point2.getY()-this.size, null);

		}

	}

	public Bitmap getBitmap(int space)
	{
		Bitmap bitmap=Bitmap.createBitmap(space, space, Config.ARGB_8888);
		Canvas canvas=new Canvas(bitmap);
		
		
		Paint paint = new Paint();
		paint.setColor(this.color);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(1);
		if (this.markStyle == MarkStyle.SolidCircle
				|| this.markStyle == MarkStyle.HollowCircle) {
			if (this.markStyle == MarkStyle.SolidCircle)
				paint.setStyle(Style.FILL);
			else if (this.markStyle == MarkStyle.HollowCircle)
				paint.setStyle(Style.STROKE);

			canvas.drawCircle(space/2, space/2, this.size,
					paint);
		} else if (this.markStyle == MarkStyle.SolidTriangle
				|| this.markStyle == MarkStyle.HollowTriangle) {
			
			if (this.markStyle == MarkStyle.SolidTriangle)
				paint.setStyle(Style.FILL);
			else if (this.markStyle == MarkStyle.HollowTriangle)
				paint.setStyle(Style.STROKE);

			int dx = (int) (this.size * Math.cos(30 * Math.PI / 180));
			int dy = (int) (this.size * Math.sin(30 * Math.PI / 180));
			Path path = new Path();
			// 从正三角形的左下角开始画
			path.moveTo(space/2 - dx, space/2 + dy);
			// 正上方
			path.lineTo(space/2, space/2 - this.size);
			// 右下角
			path.lineTo(space/2 + dx, space/2 + dy);
			// 左下角
			path.lineTo(space/2 - dx, space/2 + dy);
			canvas.drawPath(path, paint);
		}
		else if(this.markStyle==MarkStyle.Cross){
			int dx = (int) (this.size * Math.cos(45 * Math.PI / 180));
			int dy = (int) (this.size * Math.sin(45 * Math.PI / 180));
			canvas.drawLine(space/2-dx, space/2-dy, space/2+dx,space/2+dy, paint);
			canvas.drawLine(space/2-dx, space/2+dy, space/2+dx, space/2-dy, paint);
		}
		
		return bitmap;
		
	}
	
	@Override
	public void setSize(int paramFloat) {
		// TODO Auto-generated method stub
		this.size = paramFloat;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return this.size;
	}

}
