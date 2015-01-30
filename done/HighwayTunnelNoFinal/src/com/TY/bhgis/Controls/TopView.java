package com.TY.bhgis.Controls;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class TopView extends View {
	private TopControl topControl;

	protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3,
			int paramInt4) {
		if (this.topControl == null)
			this.topControl = new TopControl(this, paramInt1, paramInt2);
		super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
	}

	public TopControl getTopControl() {
		return this.topControl;
	}	
	
	protected void onDraw(Canvas paramCanvas) {
		if (topControl != null)
			topControl.paint(paramCanvas);
		super.onDraw(paramCanvas);
	}

	public TopView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		setFocusableInTouchMode(true);
		requestFocusFromTouch();

	}

}
