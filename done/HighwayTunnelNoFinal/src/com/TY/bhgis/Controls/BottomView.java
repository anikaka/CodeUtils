package com.TY.bhgis.Controls;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class BottomView extends View {

	private BottomControl bottomControl;
	protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3,int paramInt4) {
		if (this.bottomControl == null)
			this.bottomControl = new BottomControl(this, paramInt1, paramInt2);
		super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
	}

	public BottomControl getBottomControl() {
		return this.bottomControl;
	}

	protected void onDraw(Canvas paramCanvas) {
		if (bottomControl != null)
			bottomControl.paint(paramCanvas);
		super.onDraw(paramCanvas);
	}

	public BottomView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		setFocusableInTouchMode(true);
		requestFocusFromTouch();
	}

}
