package com.tongyan.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class MDialog extends Dialog {

	public MDialog(Context context, int style) {
		super(context, style);
	}

	/**
	 * 创建自定义dialog
	 * 
	 * @param layout
	 *            布局ID
	 * @param dialogWidthPercent
	 *            占父屏宽%
	 * @param dialogHeightPercent
	 *            占父屏高%
	 * @param m
	 */
	public void createDialog(int layout, double dialogWidthPercent,
			double dialogHeightPercent, WindowManager m) {
		setContentView(layout);
		/** 将对话框的大小按屏幕大小的百分比设置 */
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		LayoutParams params = this.getWindow().getAttributes();
		params.height = (int) (d.getHeight() * dialogHeightPercent); // 高度设置为屏幕的0.6
		params.width = (int) (d.getWidth() * dialogWidthPercent); // 宽度设置为屏幕的0.95
		this.onWindowAttributesChanged(params);
//		setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
		this.show();
	}
	
}
