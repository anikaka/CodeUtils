package com.tongyan.zhengzhou.common.widgets.calendar;



import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.common.utils.CommonUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
/**
 * 
 * Created by Eclipse3.6.2
 * @ClassName: CalendarHeader
 * @Author wanghb
 * @Date 2012-10-26 下午02:41:27 
 * @Desc: 
 */
public class CalendarHeader {
	
	//private static final String TAG = "CalendarHeader";
	private Context context;
	private Rect headerRect;
	private static final String[] week = new String[]{"日","一","二","三","四","五","六"};
	private float fontSize;
	
	public Rect getHeaderRect() {
		return headerRect;
	}

	public void setHeaderRect(Rect headerRect) {
		this.headerRect = headerRect;
	}

	public CalendarHeader(Context context)
	{
		this.context = context;
	}
	public CalendarHeader(Context context,float fontSize)
	{
		this.context = context;
		this.fontSize = fontSize;
	}
	
	public void drawWeekHeader(Canvas canvas,Rect canlendarRect,float fontSize) 
	{
		int cellWidth = canlendarRect.width()/7;
		Paint paint = new Paint();
		//这里可以实现移动 ，以后需要提取
		//Bitmap bit = BitmapFactory.decodeResource(context.getResources(), R.drawable.p05_calendar_title_bg);
		//Bitmap bitmap = Bitmap.createBitmap(bit, 0, 0, context.getResources().getDisplayMetrics().widthPixels, ScreenUtil.dp2px(context, 20));
		//canvas.drawBitmap(bitmap, 0, 0, paint);
		int start = canlendarRect.left;
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(fontSize-2);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		
		for(int i=0;i<7;i++)
		{
			paint.setColor(Color.BLACK);
			if(week[i].equals("六") || week[i].equals("日"))
			{
				paint.setColor(context.getResources().getColor(R.color.red));
			}
			canvas.drawText(week[i], start+cellWidth/2, CommonUtils.dp2px(context, 20), paint);
			start+=cellWidth;
		}
	}
	public Rect computeDayCellRect()
	{
		//return new Rect(headerRect.left,headerRect.top+16,headerRect.right,headerRect.bottom);
		//Log.i(TAG, "left:" + headerRect.left + "top:" + headerRect.top + "rigth:" + headerRect.right + "bottom:" + headerRect.bottom);
		return new Rect(headerRect.left,headerRect.top+12,headerRect.right,headerRect.bottom);
	}

	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}

	public float getFontSize() {
		return fontSize;
	}
}
