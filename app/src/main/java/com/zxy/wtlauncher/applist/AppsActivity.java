package com.zxy.wtlauncher.applist;

import com.zxy.wtlauncher.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class AppsActivity extends Activity implements AppLicationViewDao.updateView {
    private static final long DEFAULT_TRAN_DUR_ANIM =300 ;
    private static final String TAG ="MainActivity" ;
    GridView appGrid;
    ProgressBar progress,waitProgress;
    TextView appCount,appSize;
    AppLicationViewDao.MyPresenter myPresenter;
    View lastSelected;
    FlyImageView fly;
    boolean isLongClick = false;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);
        myPresenter = new MyPresenterImpl(this,handler);
        registerReceivers();
        initView();
        setListener();
    }
    int currentSelectedLine = 0 ;
    private void setListener() {
        appGrid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemSelected: "+position);
                if(view==null){
                   return;
                }
                if(lastSelected!=null){
                    lastSelected.animate().scaleX(1f).scaleY(1f).setDuration(DEFAULT_TRAN_DUR_ANIM).start();
                }else{
                    appGrid.getChildAt(0).animate().scaleX(1f).scaleY(1f).setDuration(DEFAULT_TRAN_DUR_ANIM).start();
                }
                int[] locations = new int[2];
                view.getLocationOnScreen(locations);

                if (locations[1] <= dip2px(getApplicationContext(),258)+56) {
                    currentSelectedLine = 0;
                } else {
                    currentSelectedLine = 1;
                }
                view.animate().scaleX(1.11f).scaleY(1.11f).setDuration(DEFAULT_TRAN_DUR_ANIM).start();
                Log.i(TAG, "onItemSelected: height ="+view.getHeight()+"width ="+view.getWidth());
                fly.flyTo(fly.getWidth(),fly.getHeight(),getChildLocation(position,currentSelectedLine)[0],getChildLocation(position,currentSelectedLine)[1]);
                lastSelected = view;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        appGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myPresenter.OnApplicationClick(position);
            }
        });
        appGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                isLongClick=true;
                myPresenter.OnApplicationLongClick(position,view);
                return true;
            }
        });
    }

    public int[] getChildLocation(int position,int currentSelectedLine) {
        int x,y ;

        if (currentSelectedLine == 0) {
            y = 56;
            x = dip2px(getApplicationContext(),197*(position%6));
        } else {
            y =56+dip2px(getApplicationContext(),258)+15;
            x = dip2px(getApplicationContext(),197*(position%6));
        }
        return new int[]{x, y};
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public int getDimen(int id) {
        return getResources().getDimensionPixelSize(id);
    }
    private void initView() {
        progress = (ProgressBar) findViewById(R.id.size_pro);
        appGrid = (GridView) findViewById(R.id.recyclerView);
        appCount = (TextView) findViewById(R.id.title_apps);
        appSize = (TextView) findViewById(R.id.totalsize);
        fly = (FlyImageView) findViewById(R.id.mainUpView1);
        waitProgress = (ProgressBar) findViewById(R.id.progress);
        waitProgress.setVisibility(View.GONE);
        myPresenter.getApps();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                appGrid.getChildAt(0).animate().scaleX(1.11f).scaleY(1.11f).setDuration(300).start();
            }
        },300);

    }

    private void registerReceivers() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        filter.addDataScheme("package");
        registerReceiver(packageReceiver, filter);
    }

    @Override
    public void showAppList(BaseAdapter adapter) {
        appGrid.setAdapter(adapter);
    }

    @Override 
    public void showProgressbarandAppcount(int[] progressAndcount,int SDAvailableSize) {
    	appSize.setText("可用空间"+SDAvailableSize+"MB");
        progress.setProgress(progressAndcount[1]);
        appCount.setText("应用程序（"+progressAndcount[0]+"）");
    }

    @Override
    public void showProgressbar() {
        waitProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        waitProgress.setVisibility(View.GONE);
    }


    BroadcastReceiver packageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(Intent.ACTION_PACKAGE_REMOVED)){
                myPresenter.updateAfterUninstall();

            }else if(action.equals(Intent.ACTION_PACKAGE_ADDED)){
                String data = intent.getDataString();
                String packgename = data.substring(data.indexOf(":") + 1,
                        data.length());
                myPresenter.updateAfterInstall(packgename);
            }
        }
    };

    @Override
    protected void onDestroy() {
        try{
            unregisterReceiver(packageReceiver);
        }catch (Exception e){

        }
        super.onDestroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(isLongClick){
                myPresenter.hidenLongClick();
                isLongClick=false;
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
