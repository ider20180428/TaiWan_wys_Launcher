package com.zxy.wtlauncher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import com.zxy.wtlauncher.applist.ApplicationUtil;
import com.zxy.wtlauncher.util.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Application {
    private String label;
    private Drawable icon;
    private Intent in;
    private String packageName;
    private String className;
    private String type;
    private String tag;
    private boolean isChecked = false;
    private double size;


    public double getSize() {
        return size;
    }


    public void setSize(double size) {
        this.size = size;
    }


    public Application() {
    }


    public String getTag() {
        return tag;
    }


    public void setTag(String tag) {
        this.tag = tag;
    }


    public Application(String packageName) {
        this.packageName = packageName;
        LogUtils.isDebug=true;
    }

    public Application(String label, String packageName) {
        this.label = label;
        this.packageName = packageName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Intent getIntent() {
        return in;
    }

    public void setIntent(Intent in) {
        this.in = in;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static List<Application> loadAllApplication(Context context, List<String> list) {
        if (list.size() == 0) {
            return ApplicationUtil.loadAllApplication(context);
        } else {
            PackageManager pm = context.getPackageManager();
            List<Application> apps = new ArrayList<Application>();
            Intent main = new Intent(Intent.ACTION_MAIN, null);
            main.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> resolves = pm.queryIntentActivities(main, 0);
            Collections.sort(resolves, new ResolveInfo.DisplayNameComparator(pm));
            for (int i = 0; i < resolves.size(); i++) {

                ResolveInfo info = resolves.get(i);
                String pkgName = info.activityInfo.applicationInfo.packageName;
                if (!list.contains(pkgName)) {
                    String label = info.loadLabel(pm).toString().trim();
                    Drawable icon = info.activityInfo.loadIcon(pm);
                    String activityName = info.activityInfo.name;
                    Intent intent = new Intent();
                    intent.setClassName(pkgName, activityName);
                    Application app = new Application();
                    app.setLabel(label);
                    app.setIcon(icon);
                    app.setClassName(activityName);
                    app.setPackageName(pkgName);
                    app.setIntent(intent);
                    app.setChecked(false);
                    apps.add(app);
                    System.out.println(label + "/" + pkgName + "/" + activityName);
                }

            }
            return apps;
        }

    }

    public static Application doAddApplication(Context context) {
        Application app = new Application();
        app.setLabel(context.getString(R.string.add));
        app.setIcon(context.getResources().getDrawable(R.drawable.icon_add));
        app.setPackageName("app.add");
        return app;
    }

    public static Application doApplication(String pkgName, Context context) {
        PackageManager pm = context.getPackageManager();
        Application app = null;
        ApplicationInfo info;
        try {
            info = pm.getApplicationInfo(pkgName, 0);
            app = new Application(pkgName);
            app.setIntent(pm.getLaunchIntentForPackage(pkgName));
            app.setLabel(info.loadLabel(pm).toString());
            app.setIcon(info.loadIcon(pm));
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        return app;
    }

    @Override
    public boolean equals(Object o) {

        return this.getPackageName().equals(((Application) o).getPackageName());
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }


    public static void startApp(Context context, Application app) {
        PackageManager pm = context.getPackageManager();
        String pkgName = app.getPackageName();
        Intent intent = pm.getLaunchIntentForPackage(pkgName);
        if (intent != null) {
            context.startActivity(intent);
        }
    }


    public static void startApp(Context context, String pkgName) {
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(pkgName);
        if (intent != null) {
            context.startActivity(intent);
        }
    }
}
