package com.zxy.wtlauncher.util;



import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceManager {
	Context context;
	private static SharedPreferences preferences;
	private static PreferenceManager manager;
	Editor editor;
	
	public static PreferenceManager getInstance(Context context) {
		if(manager == null) {
			manager = new PreferenceManager(context);
		}
		return manager;
	}

	private PreferenceManager(Context context) {
		this.context = context;
		preferences = context.getSharedPreferences("fla", Context.MODE_PRIVATE);
		editor = preferences.edit();
	}


	public  void putString(String tag, String pkgName) {
		if (preferences.getString(tag, null) != null) {
			editor.remove(tag);
			editor.commit();
		}
		editor.putString(tag, pkgName);
		editor.commit();
	}

	public void delete(String tag) {
		editor.remove(tag);
		editor.commit();
	}
	
	public synchronized String getPackage(String tag) {

		String  packageName = preferences.getString(tag, null);
		
		return packageName;
	}

	
	public void putBoolean(String key, boolean b) {
		editor.putBoolean(key, b);
		editor.commit();
	}
	
	public boolean getBoolean(String key) {
		return preferences.getBoolean(key, false);
	}

	public String getString(String key,String def) {
		return preferences.getString(key, def);
	}
	
}
