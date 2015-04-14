package com.tongyan.zhengzhou.common.widgets.calendar;


import java.util.Map;

import com.tongyan.zhengzhou.act.R;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.Paint.Align;

/**
 * 
 * Created by Eclipse3.6.2
 * @ClassName: DayCell
 * @Author wanghb
 * @Date 2012-10-26 pm 02:28:37 
 * @Desc: TODO
 */
public class DayCell {
	//private static final String TAG = "DayCell";
	private Rect rect;
	private String text;
	private boolean selected;
	private int fontColor = Color.BLACK;
	
	private Map<String, Boolean> agendaMap;
	
	public int getFontColor() {
		return fontColor;
	}

	public void setFontColor(int fontColor) {
		this.fontColor = fontColor;
	}

	private int year;
	private int month;
	private int day;
	private boolean today;
	
	private float fontSize;
	
	public boolean isClicked(int mouseX,int mouseY)
	{
		if(rect != null && rect.contains(mouseX, mouseY))
		{
			return true;
		}
		return false;
	}
	
	public void drawDay(Canvas canvas,Context context,float fontSize)
	{
		Paint paint = new Paint();
		/*paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));  
		canvas.drawPaint(paint);  
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC)); */
		//选中状态，增加背景色
		if(selected && !today)
		{
			//a9c3e8
			Shader bgShader = new LinearGradient(rect.left, rect.top, rect.right, rect.bottom, Color.parseColor("#a9c3e8"), Color.parseColor("#a9c3e8"), Shader.TileMode.MIRROR);
			paint.setShader(bgShader);
			canvas.drawRoundRect(new RectF(rect), 6, 6, paint);
		}
		//是今天，增加背景标识
		/*if(today)
		{
			Shader bgShader = new LinearGradient(rect.left, rect.top, rect.right, rect.bottom, context.getResources().getColor(R.color.blue), Color.parseColor("#ffcea8"), Shader.TileMode.MIRROR);
			paint.setShader(bgShader);
			canvas.drawRoundRect(new RectF(rect), 6, 6, paint);
		}*/
		paint.setShader(null);
		paint.setColor(fontColor);
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(fontSize);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		//paint.setStyle(Style.)
		//rect的开始＋宽度/2，这样得到rect的X中点坐标
		//计算字体高度，计算出居中的定位
		//FontMetrics fm = paint.getFontMetrics();
		//int fontHeight = (int) Math.ceil(fm.descent - fm.top)-6;
		if(text != null)
		{
			if(agendaMap != null && agendaMap.size() > 0) {
				/*Paint pt = new Paint();
				pt.setStyle(Paint.Style.FILL_AND_STROKE);
				pt.setColor(Color.YELLOW);
				Path path = new Path();
				path.moveTo(rect.right - rect.width() / 3, rect.top);
				path.lineTo(rect.right, rect.top);
				path.lineTo(rect.right, rect.top + rect.width() / 3);
				path.lineTo(rect.right - rect.width() / 3, rect.top);
				path.close();
				canvas.drawPath(path, pt);*/
				Shader bgShader = new LinearGradient(rect.left, rect.top, rect.right, rect.bottom, context.getResources().getColor(R.color.blue), Color.parseColor("#ffcea8"), Shader.TileMode.MIRROR);
				paint.setShader(bgShader);
				canvas.drawRoundRect(new RectF(rect), 6, 6, paint);
			} 
			paint.setShader(null);
			canvas.drawText(text, rect.left+rect.width()/2, rect.top + rect.height() * 3 /5, paint);
		}
	}
	
	
	public void clearState()
	{
		setText(null);
		setSelected(false);
		setDay(0);
		setMonth(0);
		setYear(0);
		setToday(false);
	}

	public Rect getRect() {
		return rect;
	}

	public void setRect(Rect rect) {
		this.rect = rect;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public boolean isToday() {
		return today;
	}

	public void setToday(boolean today) {
		this.today = today;
	}

	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}

	public float getFontSize() {
		return fontSize;
	}

	public void setAgendaList(Map<String, Boolean> agendaMap) {
		this.agendaMap = agendaMap;
	}

	public Map<String, Boolean> getAgendaList() {
		return agendaMap;
	}
}
