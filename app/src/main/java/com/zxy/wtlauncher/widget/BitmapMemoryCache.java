package com.zxy.wtlauncher.widget;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 
* @ClassName: BitmapMemoryCache 
* @author zhaoyufei
* @date 2016-7-1
 */
public class BitmapMemoryCache {

	private static final String TAG = "BitmapMemoryCache";

	private static BitmapMemoryCache sInstance = new BitmapMemoryCache();

	private LruCache<String, Bitmap> mMemoryCache;

	/**
	 */
	public static BitmapMemoryCache getInstance() {
		return BitmapMemoryCache.sInstance;
	}

	private BitmapMemoryCache() {
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		int cacheSize = maxMemory / 8;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getByteCount() / 1024;
			}
		};
	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}

	/**
	 */
	public synchronized void removeImageCache(String key) {
		if (key != null) {
			if (mMemoryCache != null) {
				Bitmap bm = mMemoryCache.remove(key);
				if (bm != null) {
					bm.recycle();
				}
			}
		}
	}

}