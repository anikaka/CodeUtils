package com.TY.bhgis.Controls;

import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.TY.bhgis.Util.Type;

public class TopControl {

	private int color = Color.WHITE;
	protected Canvas g;
	protected Paint paint;
	protected Bitmap image;
	protected int width;
	protected int height;
	protected View view;

	public TopControl(View mView, int mWidth, int mHeight) {
		this.width = mWidth;
		this.height = mHeight;
		this.view = mView;
		this.image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		this.g = new Canvas();
		this.g.setBitmap(this.image);
		this.paint = new Paint();
	}

	public void paint(Canvas canvas) {
		this.paint.setColor(this.color);
		canvas.drawRect(0.0F, 0.0F, width, height, this.paint);
		canvas.drawBitmap(this.image, 0.0F, 0.0F, this.paint);
	}

	/**
	 * view整体刷新
	 */
	public void repaint() {
		this.view.postInvalidate();
	}

	public void loadTop(String holeType) {
		init(getTopRects(holeType),getGridline());
	}

	public void closeTop() {
		System.gc();
		repaint();
	}

	private void init(Vector<kuang> kuangs, Vector<gridline> gridlines) {
		this.paint.setColor(this.color);
		this.g.drawRect(0, 0, this.width, this.height, paint);
		draw(kuangs,gridlines);
		this.repaint();
	}

	private void draw(Vector<kuang> kuangs,Vector<gridline> gridlines) {
		for (int i = 0; i < kuangs.size(); i++) {
			kuangs.get(i).draw(this.g);
		}
		for (int i = 0; i < gridlines.size(); i++) {
			gridlines.get(i).draw(this.g);
		}
	}
	
	// 头部区域的绘制类集合
	private Vector<kuang> getTopRects(String holeType) {
		Vector<kuang> kuangs = new Vector<kuang>();

		// 绘制头部
		float topHeight = this.height;// 头部区域的高
		float topWidth = this.width;
		float startX = 0;
		float endX = 0;
		float gridWith = (float) (topWidth / 19);// 一个格网宽度
	

		// 里程桩号栏宽度
		float lczhWidth = (float) (topWidth *2/ 19);
		endX = lczhWidth;
		RectF lczhrect = new RectF(startX, 0, endX, topHeight);
		kuangs.add(new kuang(lczhrect, "里程桩号"));

		// 衬砌标题栏
		float titleWidth = (float)(topWidth *16/ 19);
		startX += lczhWidth;
		endX += titleWidth;
		RectF titlerect = new RectF(startX, 0, endX, topHeight);
		kuangs.add(new kuang(titlerect, ""));

		String left_qjx = "左墙角线";
		String right_qjx = "右墙角线";
		if (holeType.equals(Type.outhole)) {  // 假如是从出口进的那么左墙角线和右墙角线正好相反
			left_qjx = "右墙角线";
			right_qjx = "左墙角线";
		}
		
		float qjxWidth =  topWidth/19 + topWidth / 38;// 墙角线标题栏宽度
		float qgxWidth = (topWidth*5 / 19);// 起拱线标题栏宽度
		float gdWidth = (topWidth*3 / 19);// 拱顶标题栏宽度

		endX = startX + qjxWidth;
		RectF qjxrect1 = new RectF(startX, 0, endX, topHeight);
		kuang qjxkuang1 = new kuang(qjxrect1, left_qjx);
		qjxkuang1.isDrawRect = false;
		kuangs.add(qjxkuang1);

		startX = endX;
		endX += qgxWidth;
		RectF qgxrect1 = new RectF(startX, 0, endX, topHeight);
		kuang qgxkuang1 = new kuang(qgxrect1, "起拱线");
		qgxkuang1.isDrawRect = false;
		kuangs.add(qgxkuang1);

		startX = endX;
		endX += gdWidth;
		RectF gdrect = new RectF(startX, 0, endX, topHeight);
		kuang gdkuang = new kuang(gdrect, "拱顶");
		gdkuang.isDrawRect = false;
		kuangs.add(gdkuang);

		startX = endX;
		endX += qgxWidth;
		RectF qgxrect2 = new RectF(startX, 0, endX, topHeight);
		kuang qgxkuang2 = new kuang(qgxrect2, "起拱线");
		qgxkuang2.isDrawRect = false;
		kuangs.add(qgxkuang2);

		startX = endX;
		endX += qjxWidth;
		RectF qjxrect2 = new RectF(startX, 0, endX, topHeight);
		kuang qjxkuang2 = new kuang(qjxrect2, right_qjx);
		qjxkuang2.isDrawRect = false;
		kuangs.add(qjxkuang2);

		// 备注栏
		RectF brect = new RectF( topWidth*18/19,0, topWidth, topHeight);
		kuangs.add(new kuang(brect, "备注"));

		return kuangs;
	}
	
	private Vector<gridline> getGridline() {
		Vector<gridline> gridlines = new Vector<gridline>();
		float gridWidth = (float) (this.width / 19);// 一个格网宽度
		float gridHeight = (float) this.height; 
		//int dotlineHeight = (int) (this.height * 12 / 15);// 虚线的长度
		/*************
		 * 粗线
		 * ***********/
		float startX = (float) (this.width *2/ 19);
		float startY = 0;
		float endX = startX;
		float endY =gridHeight;
		gridlines.add(new gridline(startX, startY, endX, endY));
		startX = (float) (this.width*18 / 19);
		startY = 0;
		endX = startX;
		endY =gridHeight;
		gridlines.add(new gridline(startX, startY, endX, endY));
		return gridlines;
	}
	
	

}
