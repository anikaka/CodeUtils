package com.TY.bhgis.Controls;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

public class kuang {
	public RectF rect;
	public String content;
	public int color;
	private int size;
	private Paint paint;
	public boolean isDrawRect = true;// �Ƿ񻰱߿�



	public kuang(RectF rect, String content) {
		this.rect = rect;
		this.content = content;
		this.color = Color.BLACK;
		this.size = 16;
		paint = new Paint();
	}

	public void draw(Canvas g) {
		paint.setColor(color);
		paint.setStyle(Paint.Style.STROKE);
		paint.setTextSize(size);
		String familyName = "����";
		Typeface font = Typeface.create(familyName,Typeface.BOLD);
		paint.setTypeface(font);
		if (isDrawRect) {
			g.drawRect(rect, paint);
		}
				
		if (content.length() > 0) {
			Rect rectstr = new Rect();
			//���ذ�Χ�����ַ�������С��һ��Rect����
			paint.getTextBounds(this.content, 0, this.content.length(), rectstr); 
			int strwid = rectstr.width();
			int strhei = rectstr.height();
			g.drawText(content, rect.centerX()- strwid/2,rect.centerY()+strhei/2, paint);
		}

	}
}
