package com.zxy.wtlauncher;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import com.zxy.wtlauncher.util.Util;

/**
 * @author jachary.zhao on 2018/3/23.
 * @email zhaoyufei1223@gmail.com
 */
public class BaseActivity extends Activity {




    public void toActivity(Class activity){
        startActivity(new Intent(this,activity));
    }


    public void lunchApp(String pkgName,String activity){
        Intent intent = new Intent();
        ComponentName componentName=new ComponentName(pkgName,activity);
        intent.setComponent(componentName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (Exception e) {
            Util.showToast(this, getResources().getString(R.string.app_not_found));
        }
    }


    public void launchApp(String pkgName) {
        try {
            Intent intent = this.getPackageManager().getLaunchIntentForPackage(	pkgName);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            Util.showToast(this, getResources().getString(R.string.app_not_found));
        }
    }
}
