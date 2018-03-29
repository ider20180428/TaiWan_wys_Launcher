package com.zxy.wtlauncher.util;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class NetUtil {
	static boolean DEBUG = true;
	public static String CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
	public static String RSSI_CHANGE = "android.net.wifi.RSSI_CHANGED";
	public static String WIFI_STATE_CHANGE = WifiManager.WIFI_STATE_CHANGED_ACTION;


	public static boolean isWifiConnect(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Service.CONNECTIVITY_SERVICE);
		NetworkInfo wifiInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return wifiInfo.isConnected() && wifiInfo.isAvailable();
	}


	@SuppressLint("InlinedApi")
	public static boolean isEthernetConnect(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Service.CONNECTIVITY_SERVICE);
		NetworkInfo etherInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
		return etherInfo.isConnected() && etherInfo.isAvailable();
	}

	public static boolean isNetworkAvailable(Context context) {
		return isWifiConnect(context) || isEthernetConnect(context);
	}


	public static int wifiLevel(Context context) {
		WifiManager manager = (WifiManager) context
				.getSystemService(Service.WIFI_SERVICE);
		WifiInfo info = manager.getConnectionInfo();
		return Math.abs(info.getRssi());
	}


	public static InputStream getInputStream(String strUrl) {
		InputStream in = null;
		try {
			URL url = new URL(strUrl);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(5000);
			connection.connect();
			in = connection.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return in;
	}

	public static String getStringFromUrl(String strUrl) {
		InputStream in = null;
		String content = null;
		URL url;
		try {
			url = new URL(strUrl);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(5000);
			connection.connect();
			in = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String str = null;
			content = new String();
			while ((str = br.readLine()) != null) {
				content = content + str;
				str = null;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return content;
	}


	public static Bitmap downloadBitmap(String imageUrl) {
		Bitmap bitmap = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		try {
			URL url = new URL(imageUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.setReadTimeout(10 * 1000);
			is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap;
	}

	
}
