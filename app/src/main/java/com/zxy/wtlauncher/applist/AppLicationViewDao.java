package com.zxy.wtlauncher.applist;

import android.view.View;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/11/24.
 */

public class AppLicationViewDao {

     public  interface updateView{
          void showAppList(BaseAdapter adapter);
          void showProgressbarandAppcount(int[] progressAndcount,int SDAvailableSize);
          void showProgressbar();
          void hideProgressbar();
     }

    public static interface MyPresenter {
         void getApps();
         int[] getProgressAndAppcount();
         void OnApplicationLongClick(int position,View view);
         void OnApplicationClick(int position);
         void hidenLongClick();
         void updateAfterUninstall();
         void updateAfterInstall(String packageName);

    }
}
