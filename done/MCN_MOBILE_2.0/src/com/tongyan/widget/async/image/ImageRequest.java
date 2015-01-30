/*
 * Copyright (C) 2010 Cyril Mottier (http://www.cyrilmottier.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tongyan.widget.async.image;

import java.util.concurrent.Future;

import com.tongyan.widget.async.image.ImageLoader.ImageLoaderCallback;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * An {@link ImageRequest} may be used to request an image from the network. The
 * process of requesting for an image is done in three steps:
 * <ul>
 * <li>Instantiate a new {@link ImageRequest}</li>
 * <li>Call {@link #load(Context)}/{@link #load(Context, LoadMethod)} to start
 * loading the image</li>
 * <li>Listen to loading state changes using a {@link ImageRequestCallback}</li>
 * </ul>
 * 
 * @author Cyril Mottier
 */
public class ImageRequest {

    public static interface ImageRequestCallback {

        /**
         * Callback to be invoked when the request processing started.
         * 
         * @param request The ImageRequest that started
         */
        void onImageRequestStarted(ImageRequest request);

        /**
         * Callback to be invoked when the request processing failed.
         * 
         * @param request ImageRequest that failed
         * @param throwable The Throwable that occurs
         */
        void onImageRequestFailed(ImageRequest request, Throwable throwable);

        /**
         * Callback to be invoked when the request processing ended.
         * 
         * @param request ImageRequest that ended
         * @param image The resulting Bitmap
         */
        void onImageRequestEnded(ImageRequest request, Bitmap image);

        /**
         * Callback to be invoked when the request processing has been
         * cancelled.
         * 
         * @param request ImageRequest that has been cancelled
         */
        void onImageRequestCancelled(ImageRequest request);
    }

    private static ImageLoader sImageLoader;

    private Future<?> mFuture;

    private String mUrl;

    private String mCacheKey;

    private ImageRequestCallback mCallback;

    private ImageProcessor mBitmapProcessor;

    private BitmapFactory.Options mOptions;

    public ImageRequest(String url, ImageRequestCallback callback) {
        this(url, callback, null, null,null);
    }

    public ImageRequest(String url, ImageRequestCallback callback, ImageProcessor bitmapProcessor) {
        this(url, callback, bitmapProcessor, null, null);
    }

    public ImageRequest(String url, ImageRequestCallback callback, ImageProcessor bitmapProcessor,
            BitmapFactory.Options options) {
        this(url, callback, bitmapProcessor, options, null);
    }

    public ImageRequest(String url, ImageRequestCallback callback, ImageProcessor bitmapProcessor,
            BitmapFactory.Options options, String cacheKey) {
        mUrl = url;
        mCacheKey = cacheKey;
        mCallback = callback;
        mBitmapProcessor = bitmapProcessor;
        mOptions = options;
    }

    public void setImageRequestCallback(ImageRequestCallback callback) {
        mCallback = callback;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getCacheKey() {
        return mCacheKey;
    }

    /**
     * load(context, null), 见{@link #load(Context, LoadMethod)}
     * 
     * @param context
     */
    public void load(Context context) {
        load(context, null);
    }

    /**
     * 加载图片
     * 
     * @param context
     * @param loadMethod 自定义的图片加载方法,如果为null则用默认的加载方法
     */
    public void load(Context context, LoadMethod loadMethod) {
        if (mFuture == null) {
            if (sImageLoader == null)
                sImageLoader = ImageLoader.getInstance(context);
            mFuture = sImageLoader.loadImage(mUrl, new InnerCallback(), loadMethod, mBitmapProcessor, mOptions, mCacheKey);
        }
    }

    public void cancel() {
        if (!isCancelled()) {
            // Here we do not want to force the task to be interrupted. Indeed,
            // it may be useful to keep the result in a cache for a further use
            mFuture.cancel(false);
            if (mCallback != null) {
                mCallback.onImageRequestCancelled(this);
            }
        }
    }

    public final boolean isCancelled() {
        return mFuture.isCancelled();
    }

    private class InnerCallback implements ImageLoaderCallback {

        public void onImageLoadingStarted(ImageLoader loader) {
            if (mCallback != null) {
                mCallback.onImageRequestStarted(ImageRequest.this);
            }
        }

        public void onImageLoadingEnded(ImageLoader loader, Bitmap bitmap) {
            if (mCallback != null && !isCancelled()) {
                mCallback.onImageRequestEnded(ImageRequest.this, bitmap);
            }
            mFuture = null;
        }

        public void onImageLoadingFailed(ImageLoader loader, Throwable exception) {
            if (mCallback != null && !isCancelled()) {
                mCallback.onImageRequestFailed(ImageRequest.this, exception);
            }
            mFuture = null;
        }
    }

}
