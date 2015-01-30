package com.TY.bhgis.Display;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.TY.bhgis.Geometry.IGeometry;
import com.TY.bhgis.Geometry.IPoint;
import com.TY.bhgis.Geometry.Point;
import com.TY.bhgis.Util.Image;

public class PictureMarkerSymbol extends MarkSymbol implements
		IPictureMarkerSymbol {
	private Image image;

	public PictureMarkerSymbol(Image paramImage) {

		this.image = paramImage;
	}

	public void setPicture(Image paramImage) {
		this.image = paramImage;
	}

	public Image getPicture() {
		return this.image;
	}

	@Override
	public int getColor() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setColor(int paramInt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setColor(int paramInt1, int paramInt2, int paramInt3) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setSize(int paramFloat) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Canvas canvas,
			IDisplayTransformation displayTransformation, IGeometry geometry) {
		if (geometry.getGeometryType() != 1)
			return;
		Paint paint = new Paint();
		IPoint point = new Point();
		displayTransformation.fromMapPoint((IPoint) geometry, point);
		this.getPicture()
				.draw(canvas,
						(int) (point).getX() - this.getPicture().getWidth() / 2,
						(int) (point).getY() - this.getPicture().getHeight()
								/ 2, paint);
	}

}
