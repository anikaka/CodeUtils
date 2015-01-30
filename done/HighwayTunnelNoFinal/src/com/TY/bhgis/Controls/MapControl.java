package com.TY.bhgis.Controls;
import com.TY.bhgis.Carto.IMap;
import com.TY.bhgis.Carto.Map;
import com.TY.bhgis.Carto.SelectDraw;
import com.TY.bhgis.Database.IBH;
import com.TY.bhgis.Display.Display;
import com.TY.bhgis.Display.DisplayTransformation;
import com.TY.bhgis.Display.IDisplay;
import com.TY.bhgis.Geometry.Envelope;
import com.TY.bhgis.Geometry.IClone;
import com.TY.bhgis.Geometry.IEnvelope;
import com.TY.bhgis.MapFeature.HighWayLining;
import com.TY.bhgis.MapFeature.LiningPosition;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.view.View;

public class MapControl {
	
	private IMapTool mDrawTool;
	private ICustomDraw customDraw;
	public boolean noCustomDraw = false;
	private SelectDraw selectDraw;
	public boolean noSelectDraw = false;
	public boolean bhBitmapDraw = false;
	private int color = Color.WHITE;
	// 当前的extent
	protected IEnvelope extent;
	// 刚加载的extent
	protected IEnvelope initExtent;
	protected IDisplay display;
	protected Canvas g;
	protected Paint paint;
	protected Paint clearPaint;
	protected Bitmap image;
	protected Canvas bhCanvas;
	protected Bitmap bhBitmap;
	protected int width;
	protected int height;
	protected View view;
	protected IMap map;
	
	private IBH[] selectionBHs;

	public MapControl(View mView, int mWidth, int mHeight) {
		this.width = mWidth;
		this.height = mHeight;
		this.view = mView;
		this.image = Bitmap.createBitmap(getWidth(), getHeight(),Bitmap.Config.ARGB_8888);
		this.g = new Canvas();
		this.g.setBitmap(this.image);
		this.bhBitmap = Bitmap.createBitmap(getWidth(), getHeight(),Bitmap.Config.ARGB_8888);
		this.bhCanvas = new Canvas();
		this.bhCanvas.setBitmap(this.bhBitmap);
		this.paint = new Paint();
		this.clearPaint=new Paint();
		this.clearPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		this.display = new Display(this.g, null);
	}
	
	
	public void paint(Canvas canvas) {
		this.paint.setColor(this.color);
		canvas.drawRect(0.0F, 0.0F, getWidth(), getHeight(), this.paint);
		canvas.drawBitmap(this.image, 0.0F, 0.0F, this.paint);
		if (this.mDrawTool != null)
			this.mDrawTool.draw(canvas);
		if ((this.customDraw != null) && (!this.noCustomDraw))
		{	
			bhCanvas.drawRect(0.0F, 0.0F, getWidth(), getHeight(), clearPaint);			
			this.customDraw.draw(bhCanvas);
			canvas.drawBitmap(this.bhBitmap, 0.0F, 0.0F, null);
		}
		if (this.noCustomDraw && this.bhBitmapDraw) {
			canvas.drawBitmap(this.bhBitmap, 0.0F, 0.0F,null);
		}
		if (selectDraw != null && !this.noSelectDraw)
			this.selectDraw.draw(canvas);
	}

	public void pointerPressed(int x, int y) {
		if (this.mDrawTool != null)
			this.mDrawTool.pointerPressed(x, y);
	}

	public void pointerDragged(int x, int y) {
		if (this.mDrawTool != null)
			this.mDrawTool.pointerDragged(x, y);
	}

	public void pointerReleased(int x, int y) {
		if (this.mDrawTool != null)
			this.mDrawTool.pointerReleased(x, y);
	}

	public void setPanTool() {
		this.mDrawTool = new PanTool(this);
	}
	
	/**
	 * view整体刷新
	 */
	public void repaint() {
		this.view.postInvalidate();
	}

	/**
	 * view局部刷新
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public void repaint(int left, int top, int right, int bottom) {
		this.view.postInvalidate(left, top, right, bottom);
	}
	
	
	/**
	 * view延迟刷新
	 * @param deleyMilliseconds1
	 */
	public void repaint(int deleyMilliseconds) {
		this.view.postInvalidateDelayed(deleyMilliseconds);
	}

	public boolean loadMap( float RingWidth, float RingLength,int RingCount) {
		this.map = new Map();
		this.extent = this.map.load( RingWidth, RingLength, RingCount);
		if (this.extent == null)
			return false;
		float f1;
		IEnvelope envelope = new Envelope(0.0F, getWidth(), 0.0F, getHeight(),(byte) 0);
		if ((f1 = envelope.getHeight() / envelope.getWidth()) < this.extent.getHeight() / this.extent.getWidth())
			this.extent.setHeight(this.extent.getWidth() * f1);
		else
		this.extent.setWidth(this.extent.getHeight() / f1);
		this.initExtent = (IEnvelope) ((IClone) this.extent).Clone();
		this.display.setDisplayTransformation(new DisplayTransformation(envelope, this.extent, this.map.getFullExtent()));
		try {
			init(this.extent);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	

	public void closeMap() {
		if (this.map != null) {
			b();
			this.map = null;
			this.extent = null;
			System.gc();
			repaint();
		}
	}

	private void b() {
		HighWayLining[] highWayLinings = this.map.getMapLayer().getLinings();
		for (int i = 0; i < highWayLinings.length; i++) {
			LiningPosition[] liningPositions = highWayLinings[i].getLiningPositionFeatures();
			for (int j = 0; j < liningPositions.length; j++) {
				liningPositions[j] = null;
			}
			highWayLinings[i] = null;
		}
		System.gc();
	}


	private void init(IEnvelope paramIEnvelope) {
		this.extent.putCoords(paramIEnvelope);
		this.display.getDisplayTransformation().setVisibleBounds(paramIEnvelope);
		this.paint.setColor(this.color);
		this.g.drawRect(0, 0, this.width, this.height, paint);
		this.map.getMapLayer().draw(display, paramIEnvelope);
		this.repaint();
	}

	
	public void refresh() {
		init(this.extent);
	}

	public void refresh(IEnvelope envelope) {
		init(envelope);
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public IEnvelope getFullExtent() {
		if (this.map != null)
			return this.map.getFullExtent();
		return null;
	}

	public Bitmap getImageCache() {
		return image;
	}

	public void setCurrentTool(IMapTool mDrawTool) {
		if (this.mDrawTool != null)
			this.mDrawTool = null;
		this.mDrawTool = mDrawTool;
	}

	public Canvas getImageCanvas() {
		return g;
	}

	public int getBackColor() {
		return color;
	}

	public void setBackColor(int color) {
		this.color = color;
	}

	public IEnvelope getExtent() {
		if (this.extent == null)
			return null;
		return this.extent;
	}

	public IEnvelope getInitExtent() {
		return (IEnvelope) ((IClone) this.initExtent).Clone();
	}

	public void setCustomDraw(ICustomDraw paramICustomDraw) {
		if (this.customDraw != null)
			this.customDraw = null;
		this.customDraw = paramICustomDraw;
	}

	public IMapTool getCurrentTool() {
		return this.mDrawTool;
	}

	public ICustomDraw getCustomDraw() {
		return this.customDraw;
	}

	public IDisplay getDisplay() {
		return this.display;
	}

	public IMap getMap() {
		return this.map;
	}

	public void setSelectDraw(SelectDraw paramSelectDraw) {
		if (this.selectDraw != null)
			this.selectDraw = null;
		this.selectDraw = paramSelectDraw;
	}

	public SelectDraw getSelectDraw() {
		return this.selectDraw;
	}
	
	public IBH[] getSelectionBHs()
	{
		return this.selectionBHs;
	}
	public void setSelectionBHs(IBH[] bhs)
	{
		this.selectionBHs=bhs;
	}

}
