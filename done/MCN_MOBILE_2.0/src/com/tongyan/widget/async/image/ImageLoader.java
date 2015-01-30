
package com.tongyan.widget.async.image;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * 异步加载图片，可通过{@link ImageLoaderCallback}.获得图片加载的状态。
 * <p>
 * <em><strong>Note: </strong>一般情况下不用直接使用此类而是使用{@link ImageRequest}.</em>
 * </p>
 */
public class ImageLoader {

    private static final String LOG_TAG = ImageLoader.class.getSimpleName();

    private static ImageLoader mImageLoader;

    private CacheCallback mCache;

    public static interface ImageLoaderCallback {

        void onImageLoadingStarted(ImageLoader loader);

        void onImageLoadingEnded(ImageLoader loader, Bitmap bitmap);

        void onImageLoadingFailed(ImageLoader loader, Throwable exception);
    }

    private static final int ON_START = 0x100;

    private static final int ON_FAIL = 0x101;

    private static final int ON_END = 0x102;

    private static ExecutorService sExecutor;

    private static BitmapFactory.Options sDefaultOptions;

    private static AssetManager sAssetManager;

    private ImageLoader(Context context) {
        if (sExecutor == null) {
            sExecutor = Executors.newFixedThreadPool(3);
            // sExecutor = Executors.newSingleThreadExecutor();
        }
        if (sDefaultOptions == null) {
            sDefaultOptions = new BitmapFactory.Options();
            sDefaultOptions.inDither = true;
            sDefaultOptions.inScaled = true;
            sDefaultOptions.inDensity = DisplayMetrics.DENSITY_MEDIUM;
            sDefaultOptions.inTargetDensity = context.getResources().getDisplayMetrics().densityDpi;
        }
        sAssetManager = context.getAssets();
    }

    public static ImageLoader getInstance() {
        return mImageLoader;
    }

    public static synchronized ImageLoader getInstance(Context context) {
        if (mImageLoader == null)
            mImageLoader = new ImageLoader(context);
        return mImageLoader;
    }

    public Future<?> loadImage(String url, ImageLoaderCallback callback) {
        return loadImage(url, callback, null, null, null, null);
    }

    public Future<?> loadImage(String url, ImageLoaderCallback callback, LoadMethod loadMethod) {
        return loadImage(url, callback, loadMethod, null, null, null);
    }

    public Future<?> loadImage(String url, ImageLoaderCallback callback, LoadMethod loadMethod,
            ImageProcessor bitmapProcessor) {
        return loadImage(url, callback, loadMethod, bitmapProcessor, null, null);
    }

    public Future<?> loadImage(String url, ImageLoaderCallback callback, LoadMethod loadMethod,
            ImageProcessor bitmapProcessor, BitmapFactory.Options options) {
        return loadImage(url, callback, loadMethod, bitmapProcessor, options, null);
    }

    public Future<?> loadImage(String url, ImageLoaderCallback callback, LoadMethod loadMethod,
            ImageProcessor bitmapProcessor, BitmapFactory.Options options, String cacheKey) {
        return sExecutor.submit(new ImageFetcher(url, cacheKey, callback, loadMethod, bitmapProcessor, options));
    }

    private class ImageFetcher implements Runnable {

        private String mUrl;

        private String mCacheKey;

        private ImageHandler mHandler;

        private LoadMethod mLoadMethod;

        private ImageProcessor mBitmapProcessor;

        private BitmapFactory.Options mOptions;

        public ImageFetcher(String url, String cacheKey, ImageLoaderCallback callback,
                LoadMethod loadMethod, ImageProcessor bitmapProcessor, BitmapFactory.Options options) {
            mUrl = url;
            mCacheKey = cacheKey;
            mHandler = new ImageHandler(url, callback);
            mLoadMethod = loadMethod;
            mBitmapProcessor = bitmapProcessor;
            mOptions = options;
        }

        public void run() {

            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

            final Handler h = mHandler;
            Bitmap bitmap = null;
            Throwable throwable = null;

            h.sendMessage(Message.obtain(h, ON_START));

            try {
                if (TextUtils.isEmpty(mUrl)) {
                    throw new Exception("The given URL cannot be null or empty");
                }

                // 如果自定义了加载方法,则用自定义的方法
                if (mLoadMethod != null) {
                    bitmap = mLoadMethod.load(mUrl);
                } else {

                    InputStream inputStream = null;

                    // Asset
                    if (mUrl.startsWith("file:///android_asset/")) {
                        inputStream = sAssetManager.open(mUrl.replaceFirst("file:///android_asset/", ""));
                    }
                    // File
                    else if (mUrl.startsWith("file:///") || mUrl.startsWith("/")) {
                        if (mUrl.startsWith("file:///"))
                            mUrl = mUrl.replaceFirst("file:///", "/");
                        inputStream = new FileInputStream(mUrl);
                    }
                    // NetWork
                    else {
                        // 在用URL类加载图片时，发现有的机型上面通过URL类获得的InputStream解析获得的图片总是null，故使用HttpClient
                        HttpGet httpRequest = new HttpGet(mUrl);
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpParams httpParams = new BasicHttpParams();
                        HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
                        HttpConnectionParams.setSoTimeout(httpParams, 5000);
                        httpRequest.setParams(httpParams);
                        HttpResponse response = (HttpResponse)httpclient.execute(httpRequest);
                        HttpEntity entity = response.getEntity();
                        BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
                        InputStream instream = bufHttpEntity.getContent();
                        BufferedInputStream bi = new BufferedInputStream(instream);
                        inputStream = bi;
                    }

                    // 虽然AsyncImageView中有设置BitmapFactory.Options的方法，但一般情况下都未知图片的大小，也就无法计算相应的inSampleSize，
                    // 也就无法设置相应的BitmapFactory.Options，所以一般情况下还是根据自己的需要自定义LoadMethod为好
                    bitmap = BitmapFactory.decodeStream(inputStream, null, (mOptions == null) ? sDefaultOptions : mOptions);
                    inputStream.close();
                }
                if (mBitmapProcessor != null && bitmap != null) {
                    final Bitmap processedBitmap = mBitmapProcessor.processImage(bitmap);
                    if (processedBitmap != null) {
                        bitmap.recycle();
                        bitmap = processedBitmap;
                    }
                }

            } catch (Exception e) {
                Log.e(LOG_TAG, "Error while fetching image", e);
                throwable = e;
            }

            if (bitmap == null) {
                if (throwable == null) {
                    throwable = new Exception("Skia image decoding failed");
                }
                h.sendMessage(Message.obtain(h, ON_FAIL, throwable));
            } else {
                h.sendMessage(Message.obtain(h, ON_END, bitmap));
                if (mCache != null) {
                    mCache.writeCache(TextUtils.isEmpty(mCacheKey) ? mUrl : mCacheKey, bitmap);
                }
            }
        }
    }

    public CacheCallback getCache() {
        return mCache;
    }

    public void setCache(CacheCallback mCache) {
        this.mCache = mCache;
    }

    private class ImageHandler extends Handler {

        private String mUrl;

        private ImageLoaderCallback mCallback;

        private ImageHandler(String url, ImageLoaderCallback callback) {
            mUrl = url;
            mCallback = callback;
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case ON_START:
                    if (mCallback != null) {
                        mCallback.onImageLoadingStarted(ImageLoader.this);
                    }
                    break;

                case ON_FAIL:
                    if (mCallback != null) {
                        mCallback.onImageLoadingFailed(ImageLoader.this, (Throwable)msg.obj);
                    }
                    break;

                case ON_END:
                    if (mCallback != null) {
                        mCallback.onImageLoadingEnded(ImageLoader.this, (Bitmap)msg.obj);
                    }
                    break;

                default:
                    super.handleMessage(msg);
                    break;
            }
        };
    }

    @Override
    protected void finalize() throws Throwable {
        sExecutor.shutdown();
        super.finalize();
    }
}
