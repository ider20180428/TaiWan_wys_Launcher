package com.zxy.wtlauncher.util;

import android.content.Context;
import android.graphics.Bitmap;

import java.lang.ref.SoftReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class TvUtil {
	private final static String TAG = "TvUtil";

	public final static int SCREEN_1280 = 1280, SCREEN_1920 = 1920,
			SCREEN_2560 = 2560, SCREEN_3840 = 3840;

	public static int startId = 1000001;
	public static int freeId = 1000001;

	public static int buildId() {
		freeId++;
		return freeId;
	}

	private Map<String, SoftReference<Bitmap>> imageCache;
	private String cachedDir;

	public TvUtil(Context context, Map<String, SoftReference<Bitmap>> imageCache) {
		this.imageCache = imageCache;
		this.cachedDir = "";
	}




	public static String md5(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();

		} catch (NoSuchAlgorithmException e) {
			return "";
		}
	}
}
