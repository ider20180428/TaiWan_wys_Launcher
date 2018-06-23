package com.zxy.wtlauncher.applist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.zxy.wtlauncher.Application;
import com.zxy.wtlauncher.R;
import com.zxy.wtlauncher.StartActivity;
import com.zxy.wtlauncher.util.Constant;

@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class ApplicationUtil {




	public static List<Application> loadAllApplication(Context context) {
		PackageManager pm = context.getPackageManager();
		List<Application> apps = new ArrayList<Application>();
		Intent main = new Intent(Intent.ACTION_MAIN, null);
		main.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> resolves = pm.queryIntentActivities(main, 0);
		// 锟斤拷锟斤拷锟狡斤拷锟斤拷锟斤拷锟斤拷
		Collections.sort(resolves, new ResolveInfo.DisplayNameComparator(pm));
		for (int i = 0; i < resolves.size(); i++) {
			Application app;
			ResolveInfo info = resolves.get(i);
			String pkgName = info.activityInfo.applicationInfo.packageName;
				app = doApplication(context, pkgName);
				apps.add(app);

			
		}
		return apps;
	}

	public static Application doCleanApplication(Context context) {
		Application app = new Application();
		app.setLabel(context.getString(R.string.clean_activity));
		app.setIcon(context.getResources().getDrawable(R.drawable.onkeyclean));
		app.setPackageName(Constant.TOOL_CLEAN_MASTER);
		return app;
	}



	public static void startApp(Context context, Application app) {
		PackageManager pm = context.getPackageManager();
		String pkgName = app.getPackageName();
		Intent intent = pm.getLaunchIntentForPackage(pkgName);
		if (intent != null) {
			context.startActivity(intent);
		}else {
			if (pkgName.equals(Constant.TOOL_CLEAN_MASTER)){
				context.startActivity(new Intent(context, StartActivity.class));
			}
		}
	}


	
	/**
	 * 锟斤拷取一锟斤拷apk锟侥硷拷锟侥帮拷锟斤拷
	 * @param context
	 * @param apkFile
	 * @return
	 */
	public static String getApkPackageName(Context context, File apkFile) {
		String packageName = null;
		try {
			PackageInfo info = context.getPackageManager().getPackageArchiveInfo(
					apkFile.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
			packageName = info.packageName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return packageName;
	}

	public static  boolean isSystemApp(Context context,Application app) {
		PackageInfo info;
		try {
			info = context.getPackageManager().getPackageInfo(app.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			return ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
		} catch (NameNotFoundException e) {
//			e.printStackTrace();
		}
		return false;

	}


	/**
	 * 锟斤拷锟捷帮拷锟斤拷锟斤拷锟斤拷一锟斤拷app锟斤拷锟斤拷
	 * @param context
	 * @param pkgName
	 * @return
	 */
	public  static Application doApplication(Context context, String pkgName) {

		if (pkgName != null) {
			PackageManager pm = context.getPackageManager();
			if (pm == null) {
				pm = context.getPackageManager();
			}
			Application app = null;
			ApplicationInfo info;
			try {
				info = pm.getApplicationInfo(pkgName, 0);
				app = new Application(pkgName);
				app.setIntent(pm.getLaunchIntentForPackage(pkgName));
				app.setLabel(info.loadLabel(pm).toString());
				app.setIcon(info.loadIcon(pm));
				String dir = info.publicSourceDir;
				Double size = Double.valueOf((int) new File(dir).length());
				app.setSize( (double)Math.round(size/1024/1024*100)/100);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			return app;
		}
		return  null;
	}

	
	public static void installApkFile(Context context, File apkFile) {
		// 锟斤拷锟斤拷锟斤拷锟斤拷锟芥几锟斤拷锟斤拷锟�  
        Intent intent = new Intent(Intent.ACTION_VIEW);  
        intent.setDataAndType(Uri.fromFile(apkFile),  
                "application/vnd.android.package-archive");  
        context.startActivity(intent); 
	}

	public static void uninstallApp(Context context,Application app) {
		Uri uri = Uri.parse("package:" + app.getPackageName());
		Intent intent = new Intent(Intent.ACTION_DELETE, uri);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);


	}


}
