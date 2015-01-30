package com.TY.bhgis.MapLayer;

import com.TY.bhgis.Carto.Map;
import com.TY.bhgis.Display.FillSymbol;
import com.TY.bhgis.Display.FillTextSymbol;
import com.TY.bhgis.Display.IDisplay;
import com.TY.bhgis.Display.IDisplayTransformation;
import com.TY.bhgis.Display.IFillTextSymbol;
import com.TY.bhgis.Display.ILineSymbol;
import com.TY.bhgis.Display.ISymbol;
import com.TY.bhgis.Display.LineSymbol.DashStyle;
import com.TY.bhgis.Display.SimpleLineSymbol;
import com.TY.bhgis.Geometry.IEnvelope;
import com.TY.bhgis.Geometry.LineString;
import com.TY.bhgis.MapFeature.HighWayLining;
import com.TY.bhgis.Util.mapUtil;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;


public class MapLayer implements IMapLayer {

	private HighWayLining[] highWayLinings;
	

	private float liningwidth;
	private float lininglength;
	private int VisibleCount;// 可见环数量
	private int startLczh;// 起始可见环号

	public MapLayer(HighWayLining[] highWayLinings, float liningwidth,
			float lininglength) {
		this.lininglength = lininglength;
		this.liningwidth = liningwidth;
		this.highWayLinings = highWayLinings;
	
	}

	@Override
	public HighWayLining[] getLinings() {
		
		return this.highWayLinings;
	}

	@Override
	public float getLiningWidth() {
		
		return this.liningwidth;
	}

	@Override
	public float getLiningHeigth() {
		
		return this.lininglength;
	}

	@Override
	public void draw(IDisplay display, IEnvelope envelope) {
		ISymbol symbol = new FillSymbol();
		((FillSymbol) symbol).setAlpha(0);
		ILineSymbol lineSymbol = new SimpleLineSymbol();
		lineSymbol.setColor(Color.BLACK);
		lineSymbol.setWidth(0.5f);
		ILineSymbol linedotSymbol = new SimpleLineSymbol();
		((SimpleLineSymbol)linedotSymbol).setStyle(DashStyle.Dot);
		linedotSymbol.setColor(Color.BLACK);
		ILineSymbol lineblodSymbol = new SimpleLineSymbol();
		lineblodSymbol.setColor(Color.BLACK);
		lineblodSymbol.setWidth(2);
		((FillSymbol) symbol).setOutline(lineSymbol);
		ISymbol symbolText = new FillTextSymbol();
		Canvas canvas = display.getCanvas();
		IDisplayTransformation displayTransformation = display.getDisplayTransformation();
		// 显示里程的数量
		int count = (int) Math.ceil(Math.abs(envelope.getHeight())/ this.liningwidth) + 1;
		this.VisibleCount = count;
		int lczh = 0;
		if (Map.flag) {
			lczh = (int) Math.floor(((envelope.getYMin() - Map.dxy) / this.liningwidth))+ mapUtil.minLczh;
			if (lczh < mapUtil.minLczh)
				lczh = mapUtil.minLczh;
		} else {
			lczh = mapUtil.maxLczh- (int) Math.floor(((envelope.getYMin() - Map.dxy) / this.liningwidth));
			if (lczh > mapUtil.maxLczh)
				lczh = mapUtil.maxLczh;
		}
		this.startLczh = lczh;
		for (int i = 0; i < this.highWayLinings.length; i++) {
			HighWayLining highWayLining = this.highWayLinings[i];
			if (lczh == highWayLining.getLczh() && count > 0) {
				symbol.draw(canvas, displayTransformation,highWayLining.getGeometry());
				// 绘制环号区域
				((IFillTextSymbol) symbolText).getTextMarkSymbol().setText(highWayLining.getLiningXH().getLczh());
				symbolText.draw(canvas, displayTransformation, highWayLining.getLiningXH().getGeometry());
				setBZ(highWayLining);
				((IFillTextSymbol) symbolText).getTextMarkSymbol().setText(highWayLining.getLiningBZ().getContent());
				symbolText.draw(canvas, displayTransformation, highWayLining.getLiningBZ().getGeometry());
				// 绘制部位区域
//				for (int j = 0; j < highWayLining.getLiningPositionFeatures().length; j++) {
//					LiningPosition ringPosition = highWayLining
//							.getLiningPositionFeatures()[j];
//					symbol.draw(canvas, display.getDisplayTransformation(),
//							ringPosition.getGeometry());
//
//				}
				//绘制虚线和粗线部分
				for (int j = 0; j < highWayLining.getDotLines().length; j++) {
					LineString line= highWayLining.getDotLines()[j];
					((SimpleLineSymbol)linedotSymbol).setStyle(DashStyle.Dot);
					((SimpleLineSymbol)linedotSymbol).draw(canvas, displayTransformation, line);
				}
				for (int j = 0; j < highWayLining.getBoldLines().length; j++) {
					LineString line= highWayLining.getBoldLines()[j];
					((SimpleLineSymbol)lineblodSymbol).draw(canvas, displayTransformation, line);
				}
				// 绘制底图区域
				((FillSymbol) symbol).draw(canvas, displayTransformation, highWayLining.getGeometry());
				if (Map.flag) {
					lczh++;
				} else {
					lczh--;
				}
				count--;
				highWayLining.setVisible(true);
				continue;
			}
			highWayLining.setVisible(false);
		}	
	}
	
	@Override
	public void setLiningWidth(float width) {
		
		this.liningwidth = width;
	}

	@Override
	public void setLiningLength(float length) {
		
		this.lininglength = length;
	}

	public int getVisibleCount() {
		return VisibleCount;
	}

	public int getVisibleStartLczh() {
		return startLczh;
	}

	

	@Override
	public  HighWayLining getHighWayLining(int lczh){

		
		for (int i = 0; i < highWayLinings.length; i++) {
			HighWayLining highWayLining = highWayLinings[i];
			if (highWayLining.getLczh() == lczh) {
				return highWayLining;
			}
		}
		return null;
	}

	private void drawImage(Canvas canvas, Bitmap image, int height, int length,
			float left, float top) {
		Bitmap bitmap = Bitmap.createScaledBitmap(image, length, height, true);
		canvas.drawBitmap(bitmap, left, top, null);
	}

	// 设置地图显示右边的bz
	private void setBZ(HighWayLining highWayLining) {
		
	}


	
}
