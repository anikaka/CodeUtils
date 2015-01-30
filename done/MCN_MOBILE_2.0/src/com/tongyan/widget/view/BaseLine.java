package com.tongyan.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
/**
 * @ClassName BaseLine 
 * @author wanghb
 * @date 2013-7-16 pm 03:15:17
 * @desc TODO
 */
public class BaseLine extends View {

	private Context context;
	private Paint paint;
	
	public BaseLine(Context context) {
		super(context);
		this.context = context;
	}
	
	public BaseLine(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint = new Paint();
		paint.setColor(Color.GRAY);
		int n = context.getWallpaperDesiredMinimumHeight();
		canvas.drawLine(0, 0, n, 1, paint);//n what
	}
	
}
