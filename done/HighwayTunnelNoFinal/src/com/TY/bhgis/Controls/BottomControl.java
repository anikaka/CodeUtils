package com.TY.bhgis.Controls;

import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;


import com.TY.bhgis.Util.Type;

public class BottomControl {

	private int color = Color.WHITE;

	protected Canvas g;
	protected Paint paint;
	protected Bitmap image;
	protected int width;
	protected int height;
	protected View view;

	public BottomControl(View mView, int mWidth, int mHeight) {
		this.width = mWidth;
		this.height = mHeight;
		this.view = mView;
		this.image = Bitmap
				.createBitmap(width, height, Bitmap.Config.ARGB_8888);
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

	public void loadBottom(String holeType) {
		init(getBottomRects(holeType),getGridline());
	}

	public void closeBottom() {

		System.gc();
		repaint();
	}

	private void init(Vector<kuang> kuangs,Vector<gridline> gridlines) {
		this.paint.setColor(this.color);
		this.g.drawRect(0, 0, this.width, this.height, paint);
		draw(kuangs,gridlines);
		this.repaint();
	}

	private void draw(Vector<kuang> kuangs, Vector<gridline> gridlines) {
		for (int i = 0; i < kuangs.size(); i++) {
			kuangs.get(i).draw(this.g);
		}
		for (int i = 0; i < gridlines.size(); i++) {
			gridlines.get(i).draw(this.g);
		}
	}
	
	// 底部区域的绘制类集合
	private Vector<kuang> getBottomRects(String holeType) {
		Vector<kuang> kuangs = new Vector<kuang>();

		// 绘制底部
		//int bottomHeight = (int) (this.height * 2 / 15);// 底部区域的高
		float bottomHeight = this.height;
		float bottomWidth = this.width;
		float startX = 0;
		float endX = 0;
		float startY = 0;
		float endY = startY;

		// 里程桩号栏宽度
		float lczhWidth = (float) (bottomWidth * 2 / 19);
		endX = lczhWidth;
		RectF lczhrect = new RectF(startX, startY, endX, bottomHeight);
		kuangs.add(new kuang(lczhrect, ""));

		// 衬砌底部标题栏
		startX += lczhWidth;

		int gridWidth = (int) (bottomWidth / 19);// 一个格网宽度
		
		int bottomgridHeight = (int) (bottomHeight / 3);

		kuangs.addAll(getButtomNum(startX, endX, startY, endY,bottomWidth,bottomHeight));

		startY = startY + bottomgridHeight;
		endY = startY + bottomgridHeight;

		startX = (float) (bottomWidth * 2 / 19);
		endX = (float) (bottomWidth * 6 / 19);
		RectF left_bqrect = new RectF(startX, startY, endX, endY);
		//kuang left_bqkuang = new kuang(left_bqrect, "边 墙");
		kuang left_bqkuang = new kuang(left_bqrect, "");
		kuangs.add(left_bqkuang);

		startX = endX;
		endX = (float) (bottomWidth * 14 / 19);
		RectF left_gbrect = new RectF(startX, startY, endX, endY);
	//	kuang left_gbkuang = new kuang(left_gbrect, "拱 部");
		kuang gbkuang = new kuang(left_gbrect, "");
		kuangs.add(gbkuang);

		startX = endX;
		endX = (float) (bottomWidth * 18 / 19);
		RectF right_bqrect = new RectF(startX, startY, endX, endY);
		//kuang right_bqkuang = new kuang(right_bqrect, "边 墙");
		kuang right_bqkuang = new kuang(right_bqrect, "");
		kuangs.add(right_bqkuang);

		String holename = "进                          口";
		if (holeType.equals(Type.outhole)) {// 假如是从出口进的那么左墙角线和右墙角线正好相反
			holename = "出                          口";
		}
		startY = startY + bottomgridHeight;
		endY = startY + bottomgridHeight;
		startX = (float) (bottomWidth * 2 / 19);
		endX = (float) (bottomWidth * 18 / 19);
		RectF holerect = new RectF(startX, startY, endX, endY);
		kuang holekuang = new kuang(holerect, holename);
		kuangs.add(holekuang);

		// 备注栏
		
		RectF brect = new RectF(bottomWidth*18/19, 0, bottomWidth,
				this.height);
		kuangs.add(new kuang(brect, ""));

		return kuangs;
	}

	private Vector<gridline> getGridline() {
		Vector<gridline> gridlines = new Vector<gridline>();

		float gridWidth = (float) (this.width / 19);// 一个格网宽度
		float gridHeight=(float) (this.height / 3);
		float boldWidth = gridWidth * 4;// 粗线之间的间距
		//int dotlineHeight = (int) (this.height * 12 / 15);// 虚线的长度
		
		float boldHeight1 = (float) ( 2 * gridHeight );// 起拱线的长度
		float boldHeight2 = (float) (  gridHeight );// 拱顶线的长度
		

		
		/*************
		 * 粗线
		 * ***********/
		float startX =(float) (this.width *2/ 19);
		float startY =0;
		float endX = startX;
		float endY = this.height;
		gridlines.add(new gridline(startX, startY, endX, endY));

		startX = (float) (this.width *6/ 19);
		startY = 0;
		endX = startX;
		endY = startY+boldHeight1;
		gridlines.add(new gridline(startX, startY, endX, endY));

		startX = (float) (this.width *10/ 19);
		startY = 0;
		endX = startX;
		endY = startY+boldHeight2;
		gridlines.add(new gridline(startX, startY, endX, endY));

		startX = (float) (this.width *14/ 19);
		startY = 0;
		endX = startX;
		endY = startY+boldHeight1;
		gridlines.add(new gridline(startX, startY, endX, endY));
		
		
		
		startX = (float) (this.width *18/ 19);
		startY = 0;
		endX = startX;
		endY = this.height;
		gridlines.add(new gridline(startX, startY, endX, endY));
	
		return gridlines;

		
	}

	// 底部区域的数字绘制类集合
	private Vector<kuang> getButtomNum(float startX, float endX, float startY,
			float endY,float bottomWidth,float bottomHeight ) {
		Vector<kuang> kuangs = new Vector<kuang>();
		int bottomgridHeight = (int) (bottomHeight / 3);
		endX = (int) (bottomWidth*3 / 19);
		endY += bottomgridHeight;
		RectF left_rect8 = new RectF(startX, startY, endX, endY);
		kuang left_kuang8 = new kuang(left_rect8, "8");
		kuangs.add(left_kuang8);

		startX = endX;
		endX = (int) (bottomWidth*4 / 19);
		RectF left_rect7 = new RectF(startX, startY, endX, endY);
		kuang left_kuang7 = new kuang(left_rect7, "7");
		kuangs.add(left_kuang7);

		startX = endX;
		endX =(int) (bottomWidth*5 / 19);
		RectF left_rect6 = new RectF(startX, startY, endX, endY);
		kuang left_kuang6 = new kuang(left_rect6, "6");
		kuangs.add(left_kuang6);

		startX = endX;
		endX =(int) (bottomWidth*6 / 19);
		RectF left_rect5 = new RectF(startX, startY, endX, endY);
		kuang left_kuang5 = new kuang(left_rect5, "5");
		kuangs.add(left_kuang5);

		startX = endX;
		endX = (int) (bottomWidth*7 / 19);
		RectF left_rect4 = new RectF(startX, startY, endX, endY);
		kuang left_kuang4 = new kuang(left_rect4, "4");
		kuangs.add(left_kuang4);

		startX = endX;
		endX = (int) (bottomWidth*8 / 19);
		RectF left_rect3 = new RectF(startX, startY, endX, endY);
		kuang left_kuang3 = new kuang(left_rect3, "3");
		kuangs.add(left_kuang3);

		startX = endX;
		endX =(int) (bottomWidth*9 / 19);
		RectF left_rect2 = new RectF(startX, startY, endX, endY);
		kuang left_kuang2 = new kuang(left_rect2, "2");
		kuangs.add(left_kuang2);

		startX = endX;
		endX =(int) (bottomWidth*10/ 19);
		RectF left_rect1 = new RectF(startX, startY, endX, endY);
		kuang left_kuang1 = new kuang(left_rect1, "1");
		kuangs.add(left_kuang1);

		startX = endX;
		endX = (int) (bottomWidth*11 / 19);
		RectF right_rect1 = new RectF(startX, startY, endX, endY);
		kuang right_kuang1 = new kuang(right_rect1, "1");
		kuangs.add(right_kuang1);

		startX = endX;
		endX = (int) (bottomWidth*12 / 19);
		RectF right_rect2 = new RectF(startX, startY, endX, endY);
		kuang right_kuang2 = new kuang(right_rect2, "2");
		kuangs.add(right_kuang2);

		startX = endX;
		endX = (int) (bottomWidth*13 / 19);
		RectF right_rect3 = new RectF(startX, startY, endX, endY);
		kuang right_kuang3 = new kuang(right_rect3, "3");
		kuangs.add(right_kuang3);

		startX = endX;
		endX = (int) (bottomWidth*14 / 19);
		RectF right_rect4 = new RectF(startX, startY, endX, endY);
		kuang right_kuang4 = new kuang(right_rect4, "4");
		kuangs.add(right_kuang4);

		startX = endX;
		endX = (int) (bottomWidth*15 / 19);
		RectF right_rect5 = new RectF(startX, startY, endX, endY);
		kuang right_kuang5 = new kuang(right_rect5, "5");
		kuangs.add(right_kuang5);

		startX = endX;
		endX = (int) (bottomWidth*16 / 19);
		RectF right_rect6 = new RectF(startX, startY, endX, endY);
		kuang right_kuang6 = new kuang(right_rect6, "6");
		kuangs.add(right_kuang6);

		startX = endX;
		endX = (int) (bottomWidth*17 / 19);
		RectF right_rect7 = new RectF(startX, startY, endX, endY);
		kuang right_kuang7 = new kuang(right_rect7, "7");
		kuangs.add(right_kuang7);

		startX = endX;
		endX = (int) (bottomWidth*18 / 19);
		RectF right_rect8 = new RectF(startX, startY, endX, endY);
		kuang right_kuang8 = new kuang(right_rect8, "8");
		kuangs.add(right_kuang8);
		return kuangs;
	}

	
	
	


}
