package com.TY.bhgis.Display;



import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.TY.bhgis.Display.MarkSymbol.MarkStyle;
import com.TY.bhgis.Geometry.IEnvelope;
import com.TY.bhgis.Geometry.IGeometry;
import com.TY.bhgis.Geometry.IPoint;
import com.TY.bhgis.Geometry.IPolygon;
import com.TY.bhgis.Geometry.LinearRing;
import com.TY.bhgis.Geometry.Point;
import com.TY.bhgis.Util.SymbolHelp;


public class MarkFillSymbol extends FillSymbol implements IMarkFillSymbol {

	private int space;
	private IMarkSymbol markSymbol;

	private Bitmap BitmapLegend;

	public MarkFillSymbol() {
		this.space = 20;

		this.markSymbol = new SimpleMarkSymbol();
		((ISimpleMarkSymbol) this.markSymbol)
				.setStyle(MarkStyle.HollowTriangle);
		this.markSymbol.setSize(3);
		super.setAlpha(0);

	}

	@Override
	public void setSpace(int space) {
		// TODO Auto-generated method stub
		this.space = space;
	}

	@Override
	public int getSpace() {
		// TODO Auto-generated method stub
		return this.space;
	}

	@Override
	public MarkStyle getStyle() {
		// TODO Auto-generated method stub
		return ((ISimpleMarkSymbol) this.markSymbol).getStyle();
	}

	@Override
	public void setStyle(MarkStyle mStyle) {
		// TODO Auto-generated method stub
		((ISimpleMarkSymbol) this.markSymbol).setStyle(mStyle);
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
			// �����
			if (super.getOutline() != null) {
				((ISymbol) super.getOutline()).draw(canvas,
						displayTransformation,
						(IGeometry) ((IPolygon) geometry).getExteriorRing());
			}
			// ���ڲ�����
			if (this.markSymbol != null) {

				// �õ�geometry����С���������������㣬��������㣬ת��Ϊ��Ļ�����
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

				// ����������εĴ�С����һ��λͼ
				Bitmap bitmap2 = Bitmap
						.createBitmap(
								(int) Math.abs(pointMax1.getX()
										- pointMin1.getX()) + 1,
								(int) Math.abs(pointMax1.getY()
										- pointMin1.getY()) + 1,
								Bitmap.Config.ARGB_8888);
				Canvas c = new Canvas(bitmap2);
				// ����geometry��ͼ����״��λͼ�ϲü���һ����ʾ����
				SymbolHelp.ClipBitmap((IGeometry) geometry, c,
						displayTransformation, pointMin1.getX(),
						pointMin1.getY());

				// ����ʱ��̫���ĳɵ���
				if (BitmapLegend == null) {
					BitmapLegend = SymbolHelp.GetCreateRepeater(
							canvas.getWidth(),
							canvas.getHeight(),
							getSeedBitmap(this.markSymbol, this.space));
				}
				// ��ͼ��λͼ�����ü��õ�λͼ��
				c.drawBitmap(BitmapLegend, 0, 0, null);
				// ���ü��õ�λͼ����map��ָ����λ��
				canvas.drawBitmap(bitmap2, pointMin1.getX(), pointMin1.getY(),
						null);
			}
		}
	}


	private Bitmap getSeedBitmap(IMarkSymbol markSymbol, int space) {

		return ((SimpleMarkSymbol) markSymbol).getBitmap(space);
	}
}
