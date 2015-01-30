package com.tongyan.widget.async.image;

import android.graphics.Bitmap;

/**
 * 自定义图片加载方法
 */
public interface LoadMethod {
	Bitmap load(String path);
}