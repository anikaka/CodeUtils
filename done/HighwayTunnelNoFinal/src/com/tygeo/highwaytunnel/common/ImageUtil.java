package com.tygeo.highwaytunnel.common;

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
/**
 * 
 * Created by Eclipse3.6.2
 * @ClassName: ImageUtil
 * @Author wanghb
 * @Date 2012-7-11 am 10:04:11 
 * @Desc: 鍥剧墖宸ュ叿绫� */
public class ImageUtil {   
      
	/**
	 * 
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
		//Log.v(tag, bitmap.toString());
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
			if(file.exists()) {
				FileInputStream is = new FileInputStream(file);
				bitmap = BitmapFactory.decodeStream(is);
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Log.v(tag, bitmap.toString());
	
		return bitmap;
		
	} 
	public Bitmap getZoomBmpByDecodePath(String path, int w, int h) {
		final  BitmapFactory.Options options = new BitmapFactory.Options();  
		options.inJustDecodeBounds = true;  
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, w, h);  
        options.inJustDecodeBounds = false;  
		return  BitmapFactory.decodeFile(path, options);
	}
	/**
	 * 计算缩略图压缩的比列，因为每张图片长宽不一样，压缩比列也不一样
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
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}
	/**
	 * 
	 * @param b
	 * @return
	 */
	public Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}  

	
    //鏀惧ぇ缂╁皬鍥剧墖   
    public Bitmap zoomBitmap(Bitmap bitmap,int w,int h){   
        int width = bitmap.getWidth();   
        int height = bitmap.getHeight();   
        Matrix matrix = new Matrix();   
        float scaleWidht = ((float)w / width);   
        float scaleHeight = ((float)h / height);   
        matrix.postScale(scaleWidht, scaleHeight);   
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);   
        return newbmp;   
    }   
    
    //灏咲rawable杞寲涓築itmap   
     public Bitmap drawableToBitmap(Drawable drawable){   
            int width = drawable.getIntrinsicWidth();   
            int height = drawable.getIntrinsicHeight();   
            Bitmap bitmap = Bitmap.createBitmap(width, height,   
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888   
                            : Bitmap.Config.RGB_565);   
            Canvas canvas = new Canvas(bitmap);   
            drawable.setBounds(0,0,width,height);   
            drawable.draw(canvas);   
            return bitmap;   
        }   
        
     //鑾峰緱鍦嗚鍥剧墖鐨勬柟娉�
    public Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx){   
           
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap   
                .getHeight(), Config.ARGB_8888);   
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
    //鑾峰緱甯﹂槾褰辩殑鍥剧墖鏂规硶   
    public Bitmap createReflectionImageWithOrigin(Bitmap bitmap){   
        final int reflectionGap = 4;   
        int width = bitmap.getWidth();   
        int height = bitmap.getHeight();   
           
        Matrix matrix = new Matrix();   
        matrix.preScale(1, -1);   
           
        Bitmap reflectionImage = Bitmap.createBitmap(bitmap,    
                0, height/2, width, height/2, matrix, false);   
           
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height/2), Config.ARGB_8888);   
           
        Canvas canvas = new Canvas(bitmapWithReflection);   
        canvas.drawBitmap(bitmap, 0, 0, null);   
        Paint deafalutPaint = new Paint();   
        canvas.drawRect(0, height,width,height + reflectionGap,   
                deafalutPaint);   
           
        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);   
           
        Paint paint = new Paint();   
        LinearGradient shader = new LinearGradient(0,   
                bitmap.getHeight(), 0, bitmapWithReflection.getHeight()   
                + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);   
        paint.setShader(shader);   
        // Set the Transfer mode to be porter duff and destination in   
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));   
        // Draw a rectangle using the paint with our linear gradient   
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()   
                + reflectionGap, paint);   
    
        return bitmapWithReflection;   
    }   
       
}  
