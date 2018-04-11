package com.zxy.wtlauncher.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RelativeLayout;

import com.zxy.wtlauncher.Application;
import com.zxy.wtlauncher.R;
import com.zxy.wtlauncher.util.PreferenceManager;
import com.zxy.wtlauncher.util.Util;

/**
 * @author jachary.zhao on 2018/3/24.
 * @email zhaoyufei1223@gmail.com
 */
public class BaseItemLayout extends RelativeLayout{

    public PreferenceManager pf;
    public Context mContext;
    public BaseItemLayout(Context context) {
        super(context);
        this.mContext=context;
        pf=PreferenceManager.getInstance(mContext,"zhaoyf");
    }



    public void startActivity(Class className) {
        mContext.startActivity(new Intent(mContext, className));
    }

    public void launchApp(String packageName, String className) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, className));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try {
            mContext.startActivity(intent);
        } catch (Exception e) {
            Util.showToast(mContext,mContext.getResources().getString(R.string.app_not_found));
        }

    }

    public void launchApp(ComponentName componentName) {
        Intent intent = new Intent();
        intent.setComponent(componentName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            mContext.startActivity(intent);
        } catch (Exception e) {
            Util.showToast(mContext, mContext.getResources().getString(R.string.app_not_found));
        }
    }

    public void launchApp(String pkgName) {
        try {
            Intent intent = mContext.getPackageManager()
                    .getLaunchIntentForPackage(pkgName);
            mContext.startActivity(intent);
        } catch (Exception e) {
            Util.showToast(mContext, mContext.getResources().getString(R.string.app_not_found));
        }
    }


}
