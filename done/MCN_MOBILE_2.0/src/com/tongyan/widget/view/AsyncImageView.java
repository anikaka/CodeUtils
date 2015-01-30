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

package com.tongyan.widget.view;

import com.tongyan.widget.async.image.CacheCallback;
import com.tongyan.widget.async.image.ImageLoader;
import com.tongyan.widget.async.image.ImageProcessor;
import com.tongyan.widget.async.image.ImageRequest;
import com.tongyan.widget.async.image.ImageRequest.ImageRequestCallback;
import com.tongyan.widget.async.image.LoadMethod;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * <p>
 * A {@link AsyncImageView} is a network-aware ImageView. It may display images
 * from the web according to a URL. {@link AsyncImageView} takes care of loading
 * asynchronously images on the Internet. It also caches images in an
 * application-wide cache to prevent loading images several times.
 * </p>
 * <p>
 * Clients may listen the {@link OnImageViewLoadListener} to be notified of the
 * current image loading state.
 * </p>
 * <p>
 * {@link AsyncImageView} may be extremely useful in ListView's row. To prevent
 * your {@link AsyncImageView} from downloading while scrolling or flinging it
 * is a good idea to pause it using {@link #setPaused(boolean)} method. Once the
 * scrolling/flinging is over, <em>un-pause</em> your {@link AsyncImageView}s
 * using <code>setPaused(false)</code>
 * </p>
 * 
 * @author Cyril Mottier
 */
public class AsyncImageView extends ImageView implements ImageRequestCallback {

    private static final String LOG_TAG = AsyncImageView.class.getSimpleName();

    /**
     * Clients may listen to {@link AsyncImageView} changes using a
     * {@link OnImageViewLoadListener}.
     * 
     * @author Cyril Mottier
     */
    public static interface OnImageViewLoadListener {

        /**
         * Called when the image started to load
         * 
         * @param imageView The AsyncImageView that started loading
         */
        void onLoadingStarted(AsyncImageView imageView);

        /**
         * Called when the image ended to load that is when the image has been
         * downloaded and is ready to be displayed on screen
         * 
         * @param imageView The AsyncImageView that ended loading
         */
        void onLoadingEnded(AsyncImageView imageView, Bitmap image);

        /**
         * Called when the image loading failed
         * 
         * @param imageView The AsyncImageView that failed to load
         */
        void onLoadingFailed(AsyncImageView imageView, Throwable throwable);
    }

    private static final int IMAGE_SOURCE_UNKNOWN = -1;

    private static final int IMAGE_SOURCE_RESOURCE = 0;

    private static final int IMAGE_SOURCE_DRAWABLE = 1;

    private static final int IMAGE_SOURCE_BITMAP = 2;

    private int mImageSource;

    private Bitmap mDefaultBitmap;

    private Drawable mDefaultDrawable;

    private int mDefaultResId;

    private String mUrl;

    private String mCacheKey;

    private LoadMethod mLoadMethod;

    private ImageRequest mRequest;

    private CacheCallback mCache;

    private boolean mPaused;

    private Bitmap mBitmap;

    private OnImageViewLoadListener mOnImageViewLoadListener;

    private ImageProcessor mImageProcessor;

    private BitmapFactory.Options mOptions;

    public AsyncImageView(Context context) {
        this(context, null);
    }

    public AsyncImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AsyncImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initializeDefaultValues();
    }

    private void initializeDefaultValues() {
        mImageSource = IMAGE_SOURCE_UNKNOWN;
        mPaused = false;
    }

    /**
     * Return true if this AsyncImageView is currently loading an image.
     * 
     * @return true if this AsyncImageView is currently loading an image.
     *         Otherwise it returns false.
     */
    public boolean isLoading() {
        return mRequest != null;
    }

    /**
     * Return true if the displayed image has been correctly loaded.
     * 
     * @return true if this AsyncImageView succeed to load the image at the
     *         given url.
     */
    public boolean isLoaded() {
        return mRequest == null && mBitmap != null;
    }

    /**
     * Pause this AsyncImageView preventing it from downloading the image. The
     * download process will start back once setPaused(false) is called.
     * 
     * @param paused
     */
    public void setPaused(boolean paused) {
        if (mPaused != paused) {
            mPaused = paused;
            if (!paused) {
                reload();
            }
        }
    }

    /**
     * Helper to {@link #setOptions(Options)} that simply sets the inDensity for
     * loaded image.
     * 
     * @param inDensity
     * @see AsyncImageView#setOptions(Options)
     */
    public void setInDensity(int inDensity) {
        if (mOptions == null) {
            mOptions = new BitmapFactory.Options();
            mOptions.inDither = true;
            mOptions.inScaled = true;
            mOptions.inTargetDensity = getContext().getResources().getDisplayMetrics().densityDpi;
        }

        mOptions.inDensity = inDensity;
    }

    /**
     * Assign an Options object to this {@link AsyncImageView}. Those options
     * are used internally by the {@link AsyncImageView} when decoding the
     * image. This may be used to prevent the default behavior that loads all
     * images as mdpi density.
     * 
     * @param options
     */
    public void setOptions(BitmapFactory.Options options) {
        mOptions = options;
    }

    /**
     * Reload the image pointed by the given URL
     */
    public void reload() {
        reload(false);
    }
    
    
    public void clear() {
    	if(mBitmap != null) {
    		mBitmap.recycle();
    		mBitmap = null;
    	}
    }
    
    
    /**
     * Reload the image pointed by the given URL. You may want to force
     * reloading by setting the force parameter to true.
     * 
     * @param force if true the AsyncImageView won't look into the
     *            application-wide cache.
     */
    public void reload(boolean force) {
        if (mRequest == null && mUrl != null) {

            // Prior downloading the image ... let's look in a cache !
            mBitmap = null;
            if (!force) {
                // This may take a long time.
                mBitmap = readCache();
            }

            if (mBitmap != null) {
                setImageBitmap(mBitmap);
                return;
            }

            setDefaultImage();
            mRequest = new ImageRequest(mUrl, this, mImageProcessor, mOptions, mCacheKey);
            mRequest.load(getContext(), mLoadMethod);
            if (ImageLoader.getInstance() != null && ImageLoader.getInstance().getCache() == null) {
                ImageLoader.getInstance().setCache(mCache);
            }
        }
    }

    /**
     * Force the loading to be stopped.
     */
    public void stopLoading() {
        if (mRequest != null) {
            mRequest.cancel();
            mRequest = null;
        }
    }

    /**
     * Register a callback to be invoked when an event occured for this
     * AsyncImageView.
     * 
     * @param listener The listener that will be notified
     */
    public void setOnImageViewLoadListener(OnImageViewLoadListener listener) {
        mOnImageViewLoadListener = listener;
    }

    public void setCacheCallback(CacheCallback callback) {
        mCache = callback;
    }

    /**
     * See {@link #setPath(String, LoadMethod, String)}
     */
    public void setPath(String path) {
        setPath(path, null, null);
    }
    
    /**
     * See {@link #setPath(String, LoadMethod, String)}
     */
    public void setPath(String path, LoadMethod loadMethod) {
        setPath(path, loadMethod, null);
    }

    /**
     * 设置要加载的图片的路径, 可为网络路径, Asset文件路径(file:///android_asset), 本地图片路径(file:///或/)
     * 
     * @param path 要加载的图片的路径, 若为null则加载默认图片
     * @param loadMethod 自定义的图片加载的方法, 可以null, 使用默认的加载方法
     * @param cacheKey 缓存key
     */
    public void setPath(String path, LoadMethod loadMethod, String cacheKey) {

        // Check the url has changed
        if (mBitmap != null && path != null && path.equals(mUrl)) { // TODO mBitmap != null necessary?
            return;
        }

        stopLoading();
        mUrl = path;
        mCacheKey = cacheKey;
        mLoadMethod = loadMethod;

        // Setting the url to an empty string force the displayed image to the
        // default image
        if (TextUtils.isEmpty(mUrl)) {
            mBitmap = null;
            setDefaultImage();
        } else {
            if (!mPaused) {
                reload();
            } else {
                // We're paused: let's look in a synchronous and efficient cache
                // prior using the default image.
                mBitmap = readCache(); // TODO 可能会耗时间
                if (mBitmap != null) {
                    setImageBitmap(mBitmap);
                } else {
                    setDefaultImage();
                }
            }
        }
    }

    /**
     * Set the default bitmap as the content of this AsyncImageView
     * 
     * @param bitmap The bitmap to set
     */
    public void setDefaultImageBitmap(Bitmap bitmap) {
        mImageSource = IMAGE_SOURCE_BITMAP;
        mDefaultBitmap = bitmap;
        setDefaultImage();
    }

    /**
     * Set the default drawable as the content of this AsyncImageView
     * 
     * @param drawable The drawable to set
     */
    public void setDefaultImageDrawable(Drawable drawable) {
        mImageSource = IMAGE_SOURCE_DRAWABLE;
        mDefaultDrawable = drawable;
        setDefaultImage();
    }

    /**
     * Set the default resource as the content of this AsyncImageView
     * 
     * @param resId The resource identifier to set
     */
    public void setDefaultImageResource(int resId) {
        mImageSource = IMAGE_SOURCE_RESOURCE;
        mDefaultResId = resId;
        setDefaultImage();
    }

    /**
     * Set an image processor to this AsyncImageView. An ImageProcessor may be
     * used in order to work on the retrieved Bitmap prior displaying it on
     * screen.
     * 
     * @param imageProcessor The {@link ImageProcessor} to set
     * @see ImageProcessor
     */
    public void setImageProcessor(ImageProcessor imageProcessor) {
        mImageProcessor = imageProcessor;
    }

    private void setDefaultImage() {
        if (mBitmap == null) {
            switch (mImageSource) {
                case IMAGE_SOURCE_BITMAP:
                    setImageBitmap(mDefaultBitmap);
                    break;
                case IMAGE_SOURCE_DRAWABLE:
                    setImageDrawable(mDefaultDrawable);
                    break;
                case IMAGE_SOURCE_RESOURCE:
                    setImageResource(mDefaultResId);
                    break;
                default:
                    setImageDrawable(null);
                    break;
            }
        }
    }

    private Bitmap readCache() {
        if (mCache != null)
            return mCache.readCache(TextUtils.isEmpty(mCacheKey) ? mUrl : mCacheKey);
        return null;
    }

    static class SavedState extends BaseSavedState {
        String url;

        String cacheKey;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            url = in.readString();
            cacheKey = in.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(url);
            out.writeString(cacheKey);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.url = mUrl;
        ss.cacheKey = mCacheKey;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState)state;
        super.onRestoreInstanceState(ss.getSuperState());

        setPath(ss.url, mLoadMethod, ss.cacheKey);

    }

    public void onImageRequestStarted(ImageRequest request) {
        if (mOnImageViewLoadListener != null) {
            mOnImageViewLoadListener.onLoadingStarted(this);
        }
    }

    public void onImageRequestFailed(ImageRequest request, Throwable throwable) {
        mRequest = null;
        if (mOnImageViewLoadListener != null) {
            mOnImageViewLoadListener.onLoadingFailed(this, throwable);
        }
    }

    public void onImageRequestEnded(ImageRequest request, Bitmap image) {
        mBitmap = image;
        setImageBitmap(image);
        if (mOnImageViewLoadListener != null) {
            mOnImageViewLoadListener.onLoadingEnded(this, image);
        }
        mRequest = null;
    }

    public void onImageRequestCancelled(ImageRequest request) {
        mRequest = null;
        if (mOnImageViewLoadListener != null) {
            mOnImageViewLoadListener.onLoadingFailed(this, null);
        }
    }
}
