package com.zxy.wtlauncher.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxy.wtlauncher.Application;
import com.zxy.wtlauncher.R;
import com.zxy.wtlauncher.adapter.TvGridAdapter;
import com.zxy.wtlauncher.util.PreferenceManager;
import com.zxy.wtlauncher.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jachary.zhao on 2018/3/24.
 * @email zhaoyufei1223@gmail.com
 */
public class BaseItemLayout extends RelativeLayout{

    public PreferenceManager pf;
    public Context mContext;

    public TextView[]appNameTextViews=new TextView[7];
    public ImageView[]iconImageViews=new ImageView[7];
    public ComponentName[]componentNames=new ComponentName[7];
    public boolean[]shortCutsStatus={false,false,false,false,false,false,false};

    public String[]itemTags={"10","11","12","13","14","16","20","21","22","23","24","25","26",
            "30","31","32","33","34","35","36"};

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
//            Util.showToast(mContext, mContext.getResources().getString(R.string.app_not_found));
            launchApp(componentName.getPackageName());
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

    protected void showAddDialog(final int i, final String shortCutsTag, final String pkgTag, List<String> getlocalApps, Context context) {
        final List<Application> apps = Application.loadAllApplication(context, getlocalApps);
        final MyDialog dialog = new MyDialog(context, R.layout.add_dialogt,
                R.style.MyDialog);
        View view = dialog.getWindow().getDecorView();
        final TvMarqueeText mt = (TvMarqueeText) view.findViewById(R.id.mt_title);
        mt.startMarquee();
        TvHorizontalGridView tgv_list = (TvHorizontalGridView) view.findViewById(R.id.tgv_list);
        tgv_list.setAdapter(new TvGridAdapter(context, apps));
        dialog.show();
        tgv_list.setOnItemClickListener(new TvHorizontalGridView.OnItemClickListener() {

            @Override
            public void onItemClick(View item, int position) {
                Application app = apps.get(position);
                pf.putString(pkgTag, app.getPackageName());
                showAppShortCut(i,pkgTag,shortCutsTag);
                mt.stopMarquee();
                dialog.dismiss();
                dialog.cancel();
            }
        });

    }


    public List<String> getlocalApps(){
        List<String> localapps = new ArrayList<String>();
        for (int i = 0; i < itemTags.length; i++) {
            String packString = pf.getPackage(itemTags[i]);
            if(packString!=null){
                localapps.add(packString);
            }
        }
        return localapps;
    }

    public void showAppShortCut(int i,String pkgTag,String shortCutsTag){
        String pkgName= pf.getString(pkgTag,null);
        if (null!=pkgName){
            Application app=Application.doApplication(pkgName,mContext);
            if (null!=app){
                iconImageViews[i].setImageDrawable(app.getIcon());
                appNameTextViews[i].setText(app.getLabel());
                shortCutsStatus[i]=true;
                pf.putBoolean(shortCutsTag,true);

                String className=app.getClassName();
                if (null!=className){
                    componentNames[i]=new ComponentName(pkgName,className);
                }else {
                    componentNames[i]=new ComponentName(pkgName,"");
                }
            }
        }
    }

    public void showAdd(int i,String pkgTags){
        shortCutsStatus[i]=false;
        iconImageViews[i].setImageResource(R.drawable.add_apps);
        appNameTextViews[i].setText("添加");
        pf.delete(pkgTags);
    }




}
