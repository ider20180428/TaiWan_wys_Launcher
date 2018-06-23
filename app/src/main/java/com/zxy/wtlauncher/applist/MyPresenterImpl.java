package com.zxy.wtlauncher.applist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import com.zxy.wtlauncher.Application;
import com.zxy.wtlauncher.R;
import com.zxy.wtlauncher.util.Constant;

/**
 * Created by Administrator on 2016/11/24.
 */

public class MyPresenterImpl implements AppLicationViewDao.MyPresenter {
    AppLicationViewDao.updateView  activity;
    ApplicationAdapter adapter;
    List<Application> apps;
    Context context;
    Handler mHandler;
    View oldView;
    int position = 0;

    public MyPresenterImpl(AppLicationViewDao.updateView activity,Handler mHandler) {
        this.context = (Context) activity;
        this.activity = activity;
        this.mHandler = mHandler;
    }

    @Override
    public void getApps() {
        activity.showProgressbar();
        new Thread() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        apps = ApplicationUtil.loadAllApplication(context);
                        apps.add(0,ApplicationUtil.doCleanApplication(context));//clean up
                        adapter = new ApplicationAdapter(apps);
                        activity.showAppList(adapter);
                        activity.showProgressbarandAppcount(getProgressAndAppcount(),getSDAvailableSize());
                        activity.hideProgressbar();
                    }
                });
            }
        }.start();


    }

    @Override
    public int[] getProgressAndAppcount() {

        int count = apps.size();
        int progress = (getSDTotalSize()-getSDAvailableSize())*100/getSDTotalSize();
        int[] datas = new int[]{count,progress};
        return datas;
    }

    @Override
    public void OnApplicationLongClick(int position, View view) {
        this.position = position;
        Application app = apps.get(position);
        Button btn = (Button)view.findViewById(R.id.btn_uninstall);
        final  Button btnopen = (Button)view.findViewById(R.id.btn_open);
        btn.setFocusable(true);
        btnopen.setFocusable(true);
        btn.setClickable(true);
        btnopen.setClickable(true);
         if(ApplicationUtil.isSystemApp(context,app)||app.getPackageName().equals(Constant.TOOL_CLEAN_MASTER)){
             btn.setVisibility(View.INVISIBLE);
             btn.setFocusable(false);
             FrameLayout.LayoutParams params =new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,50);
             params.gravity=Gravity.CENTER;
             btnopen.setLayoutParams(params);
             btnopen.setNextFocusDownId(btnopen.getId());
             btnopen.setNextFocusDownId(btnopen.getId());
             btnopen.setNextFocusLeftId(btnopen.getId());
             btnopen.setNextFocusRightId(btnopen.getId());
         }
        btnopen.setOnClickListener(clicker);
        btn.setOnClickListener(clicker);
        view.findViewById(R.id.select_view).animate().alpha(1f).setDuration(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                btnopen.requestFocus();
            }
        });
        oldView = view;
    }

    View.OnClickListener clicker = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.btn_open){
                ApplicationUtil.startApp(context,apps.get(position));
            }else if(v.getId()==R.id.btn_uninstall){
                hidenLongClick();
                ApplicationUtil.uninstallApp(context,apps.get(position));
            }
        }
    };
    @Override
    public void OnApplicationClick(int position) {
        Log.i("presenter", "OnApplicationClick: "+position);
        ApplicationUtil.startApp(context,apps.get(position));
    }

    @Override
    public void hidenLongClick() {
        oldView.findViewById(R.id.select_view).animate().alpha(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Button btn = (Button)oldView.findViewById(R.id.btn_uninstall);
                final  Button btnopen = (Button)oldView.findViewById(R.id.btn_open);
                btn.setFocusable(false);
                btnopen.setFocusable(false);
                btn.setClickable(false);
                btnopen.setClickable(false);
            }
        });
    }

    @Override
    public void updateAfterUninstall() {
        apps.remove(position);
        adapter.notifyDataSetChanged();
        activity.showProgressbarandAppcount(getProgressAndAppcount(),getSDAvailableSize());
    }

    @Override
    public void updateAfterInstall(String packageName) {
         Application app = ApplicationUtil.doApplication(context,packageName);
        if(app!=null){
            apps.add(app);
            adapter.notifyDataSetChanged();
            activity.showProgressbarandAppcount(getProgressAndAppcount(),getSDAvailableSize());
        }
    }

    class ApplicationAdapter extends BaseAdapter {
        List<Application> apps;
        public ApplicationAdapter(List<Application> apps) {
            this.apps = apps;
        }

        @Override
        public int getCount() {
            return apps.size();
        }

        @Override
        public Object getItem(int position) {
            return apps.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GridViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new GridViewHolder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_view, parent, false);
                viewHolder.iv = (ImageView)convertView.findViewById(R.id.app_icon);
                viewHolder.tv = (TextView)convertView.findViewById(R.id.textView);
                viewHolder.head_tv = (TextView)convertView.findViewById(R.id.app_size);
                convertView.setTag(viewHolder);
            }
            else{
                viewHolder = (GridViewHolder)convertView.getTag();
            }
            Application app = apps.get(position);
            viewHolder.iv.setImageDrawable(app.getIcon());
            viewHolder.tv.setText(app.getLabel());

            if (app.getPackageName().equals(Constant.TOOL_CLEAN_MASTER)){
                viewHolder.head_tv.setText("0.08M");
            }else {
                viewHolder.head_tv.setText(app.getSize()+"M");
            }
//            Log.i("TAG", "getView: ----------"+isfirst);
//            if(position==0){
//                convertView.animate().scaleX(1.11f).scaleY(1.11f).setDuration(300).start();
//                isfirst =false;
//            }
            return convertView;
        }
    }
    class GridViewHolder{
        public ImageView iv;
        public TextView tv;
        public TextView head_tv;
    }

    /**
     * 鑾峰緱SD鍗℃�诲ぇ灏�
     *
     * @return
     */
    private int getSDTotalSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return (int)(blockSize*totalBlocks/1048576);
    }
    /**
     * 鑾峰緱sd鍗″墿浣欏閲忥紝鍗冲彲鐢ㄥぇ灏�
     *
     * @return
     */
    public  int getSDAvailableSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return (int)(blockSize*availableBlocks/1048576);
    }
}
