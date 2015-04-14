package com.tongyan.zhengzhou.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.view.View;

/**
 * 
 * Created by Eclipse3.6.2
 * 
 * @ClassName: ImageUtil
 * @Author wanghb
 * @Date 2012-7-11 am 10:04:11
 * @Desc: 图片工具类
 */
public class ImageUtil {

	public static Bitmap ResizeBitmap(Bitmap bitmap, int newWidth) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float temp = ((float) height) / ((float) width);
		int newHeight = (int) ((newWidth) * temp);
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = 0.42f;
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);
		// matrix.postRotate(45);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,matrix, true);
		return resizedBitmap;
	} 
/*	public static Bitmap ResizeBitmap(Bitmap bitmap, int newWidth) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float temp = ((float) height) / ((float) width);
		int newHeight = (int) ((newWidth) * temp);
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);
		// matrix.postRotate(45);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,matrix, true);
		return resizedBitmap;
	} 
*/
	/**
	 * @param url
	 * @return
	 */
	public Bitmap returnBitMapByUrl(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Log.v(tag, bitmap.toString());
		return bitmap;
	}
	/**
	* 获取视频的缩略图
	* 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
	* 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
	* @param videoPath 视频的路径
	* @param width 指定输出视频缩略图的宽度
	* @param height 指定输出视频缩略图的高度度
	* @param kind 参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
	*            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
	* @return 指定大小的视频缩略图
	*/
	public Bitmap getVideoThumbnail(String videoPath, int width, int height,
	int kind) {
	Bitmap bitmap = null;
	// 获取视频的缩略图
	bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
	bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
	ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
	return bitmap;
	}
	/**
	 * 
	 * @param path
	 * @return
	 */
	public Bitmap returnBmpByPath(String path) {
		Bitmap bitmap = null;
		File file = new File(path);
		try {
			if (file.exists()) {
				FileInputStream is = new FileInputStream(file);
				final BitmapFactory.Options options = new BitmapFactory.Options();
				bitmap = BitmapFactory.decodeFile(path, options);// decodeStream(is);
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Log.v(tag, bitmap.toString());
		return bitmap;
	}

	/**
	 * @param path
	 * @param w
	 * @param h
	 */
	public Bitmap getZoomBmp4Path(String path, int w, int h) {
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		if (bitmap != null) {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			Matrix matrix = new Matrix();
			float scaleWidht = ((float) w / width);
			float scaleHeight = ((float) h / height);
			matrix.postScale(scaleWidht, scaleHeight);
			Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
					matrix, true);
			BitmapFactory.Options options = new BitmapFactory.Options();
			return newbmp;
		} else {
			return null;
		}
	}

	public Bitmap getZoomBmpByDecodePath(String path, int w, int h) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		options.inSampleSize = calculateInSampleSize(options, w, h);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

	/**
	 * 计算缩略图压缩的比列，因为每张图片长宽不一样，压缩比列也不一样
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}

	/**
	 * 
	 * @param bm
	 * @return
	 */
	public byte[] Bitmap2Bytes(Bitmap bm) {
		if (bm == null) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}

	public Bitmap getBitmapFromView(View view) {
		view.destroyDrawingCache();
		view.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache(true);
		return bitmap;
	}

	// 将Drawable转化为Bitmap
	public Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
				.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}

	// 获得圆角图片的方法
	public Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	// 获得带阴影的图片方法
	public Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
				width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}

}
