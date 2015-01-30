package com.TY.bhgis.Controls;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MapView extends View {
	//private FrameControl frameControl;
	private MapControl mapControl;
	
	@Override
	protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3,
			int paramInt4) {
//		if (this.frameControl == null)
//			this.frameControl = new FrameControl(this, paramInt1, paramInt2);
		if (this.mapControl==null) {
			this.mapControl=new MapControl(this, paramInt1, paramInt2);
		}
		super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
	}
	
	@Override
	protected void onDraw(Canvas paramCanvas) {
//		if (frameControl != null)
//			frameControl.paint(paramCanvas);
		if (mapControl!=null) {
			mapControl.paint(paramCanvas);
		}
		super.onDraw(paramCanvas);
	}

	
	public MapView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		setFocusableInTouchMode(true);
		requestFocusFromTouch();
	}
	
//	public FrameControl getFrameControl() {
//		return this.frameControl;
//	}
	public MapControl getMapControl() {
		return this.mapControl;
	}

	@Override
	public boolean onTouchEvent(MotionEvent paramMotionEvent) {
		paramMotionEvent.getX();
		paramMotionEvent.getY();
		int i;
		int j;
		if ((i = paramMotionEvent.getPointerCount()) == 1) {
			i = (int) paramMotionEvent.getX(0);
			j = (int) paramMotionEvent.getY(0);
		} else if (paramMotionEvent.getPointerId(0) == 0) {
			i = (int) paramMotionEvent.getX(0);
			j = (int) paramMotionEvent.getY(0);
		} else {
			i = (int) paramMotionEvent.getX(1);
			j = (int) paramMotionEvent.getY(1);
		}
		switch (paramMotionEvent.getAction()) {
		case 0:
			this.mapControl.pointerPressed(i, j);
			return true;
		case 1:
			this.mapControl.pointerReleased(i, j);
			break;
		case 2:
			this.mapControl.pointerDragged(i, j);
		}
		return super.onTouchEvent(paramMotionEvent);
	}
	
}
