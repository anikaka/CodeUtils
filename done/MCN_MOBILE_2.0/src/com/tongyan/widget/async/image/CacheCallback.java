package com.tongyan.widget.async.image;

import android.graphics.Bitmap;

/**
 * 缓存接口,当载入图片时调用 {@link #readCache(String)} 读取缓存,如果读到缓存则直接返回,否则使用
 * {@link ImageLoader} 异步载入数据.如果 {@link ImageRequest#reload(boolean)}
 * 参数为true则不读缓存.通过 {@link #writeCache(String, Bitmap)} 写入缓存.
 */
public interface CacheCallback {

	/**
	 * 读取缓存, 一般在调用此方法的线程执行, 即主线程
	 */
	Bitmap readCache(String key);

	/**
	 * 写缓存, 一般异步执行.
	 */
	boolean writeCache(String key, Bitmap bitmap);
}