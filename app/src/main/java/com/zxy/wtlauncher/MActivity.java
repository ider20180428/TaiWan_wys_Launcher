package com.zxy.wtlauncher;

import android.animation.Animator;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.zxy.wtlauncher.adapter.TvGridAdapter;
import com.zxy.wtlauncher.util.Constant;
import com.zxy.wtlauncher.util.LogUtils;
import com.zxy.wtlauncher.util.NetUtil;
import com.zxy.wtlauncher.util.PreferenceManager;
import com.zxy.wtlauncher.util.Util;
import com.zxy.wtlauncher.view.L_Enjoy_Layout;
import com.zxy.wtlauncher.view.L_IBiza_Layout;
import com.zxy.wtlauncher.view.L_IBiza_Layout2;
import com.zxy.wtlauncher.view.L_Settings_Layout;
import com.zxy.wtlauncher.view.L_TV_Layout;
import com.zxy.wtlauncher.view.L_Tools_Layout;
import com.zxy.wtlauncher.view.L_VIP_Layout;
import com.zxy.wtlauncher.view.L_member_layout;
import com.zxy.wtlauncher.view.LauncherItem;
import com.zxy.wtlauncher.view.MyDialog;
import com.zxy.wtlauncher.view.TvHorizontalGridView;
import com.zxy.wtlauncher.view.TvMarqueeText;
import com.zxy.wtlauncher.widget.MainUpView;
import com.zxy.wtlauncher.widget.OpenEffectBridge;
import com.zxy.wtlauncher.widget.ReflectItemView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jachary.zhao on 2018/3/29.
 * @email zhaoyufei1223@gmail.com
 */
public class MActivity extends BaseActivity implements View.OnFocusChangeListener,View.OnClickListener{
    private List<View> viewList;
    private L_member_layout l_member_layout;
    private L_Enjoy_Layout l_enjoy_layout;
    private L_IBiza_Layout2 l_iBiza_layout2;
    private L_Settings_Layout l_settings_layout;
    private L_TV_Layout l_tv_layout;
    private L_Tools_Layout l_tools_layout;
    private ViewPager viewpager;
    private OpenEffectBridge mSavebridge;
    private View mOldFocus;
    private LinearLayout[]linearLayouts=new LinearLayout[6];
    private int[]refIds={R.id.bottom_lin0,R.id.bottom_lin1,R.id.bottom_lin2,R.id.bottom_lin3,
            R.id.bottom_lin4,R.id.bottom_lin5};
    private MyPagerAdapter myPagerAdapter;
    private Context mContext;
    private int lastBottomFocus;
    private PreferenceManager pf;
    private LauncherItem currentItem =null;
    private ImageView net_icon,tf_icon,usb_icon,stateBluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_main_layout);
        mContext=this;
        pf = PreferenceManager.getInstance(this,"zhaoyf");

        Log.i("zxy", "isinstall ="+pf.getBoolean("isInstall"));
        if(!pf.getBoolean("isInstall")){
            toActivity(InstallActivity.class);
        }
        net_icon = (ImageView) findViewById(R.id.net_icon);
        tf_icon = (ImageView) findViewById(R.id.tf_icon);
        usb_icon = (ImageView) findViewById(R.id.usb_icon);
        stateBluetooth=(ImageView)findViewById(R.id.ble_icon);
        checkExternalStorage();
        registeservers();
        if (null==savedInstanceState) {
            Intent intent = new Intent("com.ider.launchermovie.MY_BROADCAST");
            sendBroadcast(intent);
        }

        viewList = new ArrayList<View>();
        viewpager = (ViewPager) findViewById(R.id.main_layout_viewpager);
        myPagerAdapter = new MyPagerAdapter();

        for (int i=0;i<linearLayouts.length;i++){
            linearLayouts[i]=(LinearLayout)findViewById(refIds[i]);
            linearLayouts[i].setOnFocusChangeListener(this);
        }
        initView();
    }



    private void addAppPkgName(){

//        pf.putString("10","com.clickdigital.tvserver");//遙控器
//        pf.putString("11", "com.droidlogic.appinstall");//應用安裝
//        pf.putString("12", "com.hpplay.happyplay.aw");//樂播投屏
//        pf.putString("13", "com.shafa.market");//沙發管家
//        pf.putString("14", "com.droidlogic.miracast");//MIRACAST
//        pf.putString("15", "clean");//一鍵清理
//
//        pf.putString("20","com.js.litv.home");//LiTV
//        pf.putString("21", "com.qianxun.tvbox");//千寻
//        pf.putString("22", "org.amotv.videolive");//AMOTV
//        pf.putString("23", "com.qiyi.tv.tw");//爱奇艺
//        pf.putString("24", "com.moretv.android");//云视听
//        pf.putString("25", "com.l2tv.ltv");//com.l2tv.ltv
//
//        pf.putString("30", "com.google.android.youtube");//YouTuBe
//        pf.putString("31", "com.igs.mjstar31");//明星3缺1
//        pf.putString("32", "com.godgame.mj.android");//麻将神来也16张麻将
//        pf.putString("33", "com.tencent.karaoketv");//全民K歌
//        pf.putString("34", "tv.twitch.android.app");//Twitch
        pf.putString("10",Constant.APP_INSTALL);
        pf.putString("20",Constant.APP_NETFILX);
        pf.putString("30",Constant.APP_YOUTUBE);



        /**
         com.clickdigital.tvserver   遥控器
         com.droidlogic.appinstall   安装
         com.hpplay.happyplay.aw    乐播投屏
         com.shafa.market           沙发管家
         com.droidlogic.miracast
         com.tencent.karaoketv      全民K歌
         com.google.android.youtube
         */


    }





    private void initView(){

        l_settings_layout = new L_Settings_Layout(this);
        l_tools_layout = new L_Tools_Layout(this);
        l_tv_layout = new L_TV_Layout(this);
        l_enjoy_layout = new L_Enjoy_Layout(this);
        l_member_layout = new L_member_layout(this);
        l_iBiza_layout2 = new L_IBiza_Layout2(this);

        l_enjoy_layout.initView();
        l_iBiza_layout2.initView();
        l_settings_layout.initView();
        l_tv_layout.initView();
        l_tools_layout.initView();

        viewList.add(l_settings_layout);
        viewList.add(l_tools_layout);
        viewList.add(l_tv_layout);
        viewList.add(l_enjoy_layout);
        viewList.add(l_member_layout);
        viewList.add(l_iBiza_layout2);

        viewpager.setAdapter(myPagerAdapter);
        linearLayouts[2].requestFocus();
        setBottomItemLayoutBg(2);
        for (View view : viewList) {
            MainUpView mainUpView = (MainUpView)view.findViewById(R.id.mainUpView1);

//            if (view==l_iBiza_layout){
//                mainUpView.setUpRectResource(R.drawable.ibiza_btn_retangles); //
//            }else {
//                mainUpView.setUpRectResource(R.drawable.ibiza_btn_retangle); //
//            }
//            mainUpView.setUpRectResource(R.drawable.ibiza_btn_retangle);
            mainUpView.setUpRectResource(R.drawable.test_rectangle); // 设置移动边框的图片
//        mainUpView1.setDrawUpRectPadding(new Rect(25, 18, 25, 18)); // 边框图片设置间距.
//            mainUpView.setShadowResource(R.drawable.item_shadow); // 设置移动边框的阴影

            OpenEffectBridge bridget = (OpenEffectBridge) mainUpView
                    .getEffectBridge();
            bridget.setTranDurAnimTime(200);
        }
        viewpager.getViewTreeObserver().addOnGlobalFocusChangeListener(
                new ViewTreeObserver.OnGlobalFocusChangeListener() {
                    @Override
                    public void onGlobalFocusChanged(View oldFocus,
                                                     View newFocus) {
                        int position = viewpager.getCurrentItem();
                        final MainUpView mainUpView = (MainUpView) viewList
                                .get(position).findViewById(R.id.mainUpView1);
                        final OpenEffectBridge bridge = (OpenEffectBridge) mainUpView
                                .getEffectBridge();
                        if (!(newFocus instanceof ReflectItemView)) {
                            mainUpView.setUnFocusView(mOldFocus);
                            bridge.setVisibleWidget(true);
                            mSavebridge = null;
                        } else {
                            newFocus.bringToFront();
                            mSavebridge = bridge;
                            bridge.setOnAnimatorListener(new OpenEffectBridge.NewAnimatorListener() {
                                @Override
                                public void onAnimationStart(
                                        OpenEffectBridge bridge, View view,
                                        Animator animation) {
                                }

                                @Override
                                public void onAnimationEnd(
                                        OpenEffectBridge bridge1, View view,
                                        Animator animation) {
                                    if (mSavebridge == bridge1)
                                        bridge.setVisibleWidget(false);
                                }
                            });
                            float scale = 1.1f;

                            mainUpView.setFocusView(newFocus, mOldFocus, scale);
                        }
                        mOldFocus = newFocus;
                    }
                });


        viewpager.setOffscreenPageLimit(6);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position > 0) {
                    MainUpView mainUpView = (MainUpView) viewList.get(
                            position - 1).findViewById(R.id.mainUpView1);
                    OpenEffectBridge bridge = (OpenEffectBridge) mainUpView
                            .getEffectBridge();
                    bridge.setVisibleWidget(true);
                }
                if (position < (viewpager.getChildCount() - 1)) {
                    MainUpView mainUpView = (MainUpView) viewList.get(
                            position + 1).findViewById(R.id.mainUpView1);
                    OpenEffectBridge bridge = (OpenEffectBridge) mainUpView
                            .getEffectBridge();
                    bridge.setVisibleWidget(true);
                }
                setBottomItemLayoutBg(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

    }

    private void setBottomItemLayoutBg(int position) {
        if (position != lastBottomFocus){
//            if (position==5){
//                ibizaLayout.setSelected(true);
//                linearLayouts[lastBottomFocus].setSelected(false);
//            }else {
//                linearLayouts[position].setSelected(true);
//                linearLayouts[lastBottomFocus].setSelected(false);
//                if (lastBottomFocus==5){
//                    ibizaLayout.setSelected(false);
//                }else {
//                    linearLayouts[lastBottomFocus].setSelected(false);
//                }
//            }
            linearLayouts[position].setSelected(true);
            linearLayouts[lastBottomFocus].setSelected(false);
            lastBottomFocus = position;
        }
    }


    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus){
            switch (view.getId()){
                case R.id.bottom_lin0:
                    viewpager.setCurrentItem(0);
                    break;
                case R.id.bottom_lin1:
                    viewpager.setCurrentItem(1);
                    break;
                case R.id.bottom_lin2:
                    viewpager.setCurrentItem(2);
                    break;
                case R.id.bottom_lin3:
                    viewpager.setCurrentItem(3);
                    break;
                case R.id.bottom_lin4:
                    viewpager.setCurrentItem(4);
                    break;
                case R.id.bottom_lin5:
                    viewpager.setCurrentItem(5);
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {


    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return viewList.size();
        }


        @Override
        public int getItemPosition(Object object) {
            View view = (View) object;
            int currentPage = viewpager.getCurrentItem();
            if (currentPage == (Integer) view.getTag()) {
                return POSITION_UNCHANGED;
            } else {
                return POSITION_NONE;
            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            View view = viewList.get(position);
            view.setTag(position);
            return view;
        }
    }


    private void registeservers() {
        IntentFilter filter;

        filter = new IntentFilter();
        filter.addAction(NetUtil.CONNECTIVITY_CHANGE);
        filter.addAction(NetUtil.RSSI_CHANGE);
        filter.addAction(NetUtil.WIFI_STATE_CHANGE);
        registerReceiver(netReceiver, filter);

        filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addDataScheme("file");
        registerReceiver(mediaReciever, filter);

        filter = new IntentFilter();
        filter.addAction("ider.install.over");
        registerReceiver(installReceiver, filter);

        filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(btReceiver, filter);
    }


    BroadcastReceiver installReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            addAppPkgName();
            l_enjoy_layout.initView();
            l_tv_layout.initView();
            l_tools_layout.initView();

        };
    };
    BroadcastReceiver netReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (NetUtil.CONNECTIVITY_CHANGE.equals(action)) {
                updateNetState(getApplicationContext());
            }
        }
    };

    private void  updateNetState(Context context){
        if (NetUtil.isNetworkAvailable(context)) {
            if(NetUtil.isEthernetConnect(context)) {
                net_icon.setImageResource(R.drawable.eth_icon);
            } else {
                net_icon.setImageResource(R.drawable.icon_net);
            }
        } else {
            net_icon.setImageResource(R.drawable.icon_neterror);
        }
    }

    BroadcastReceiver mediaReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
                Log.i("tag", "4444");
                checkExternalStorage();
            }
            if(Intent.ACTION_MEDIA_UNMOUNTED.equals(action)){
                checkExternalStorage();
            }
        }
    };

    private void checkExternalStorage() {
        String usb1_path = getExternalPaths()[0];
        String usb2_path = getExternalPaths()[1];
        String tf_path = getExternalPaths()[2];
        usb_icon.setImageResource(R.drawable.icon_usb_false);
        tf_icon.setImageResource(R.drawable.icon_sdcard_false);
        if (usb1_path != null) {
            File usb1 = new File(usb1_path);
            if (usb1.exists() && usb1.getTotalSpace() > 0) {
                usb_icon.setImageResource(R.drawable.icon_usb);
            }
        }

        if (usb2_path != null) {
            File usb2 = new File(usb2_path);
            if (usb2.exists() && usb2.getTotalSpace() > 0) {
                usb_icon.setImageResource(R.drawable.icon_usb);
            }
        }

        if (tf_path != null) {
            File tf = new File(tf_path);
            if (tf.exists() && tf.getTotalSpace() > 0) {
                tf_icon.setImageResource(R.drawable.icon_sdcard);
            }
        }

    }

    private String[] getExternalPaths() {
        Log.i("tag", "SDK version = " + Build.VERSION.SDK_INT+getCpuName());
        if (Build.VERSION.SDK_INT < 23) {
            String[] path = new String[3];

            if (Build.DEVICE.contains("rk")) {
                path[0] = "/mnt/usb_storage/USB_DISK0";
                path[1] = "/mnt/usb_storage/USB_DISK1";
                path[2] = null;
            } else if (getCpuName().contains("Amlogic")) {
                String usbP = "/storage/external_storage";
                Log.i("tag", "111");
                File file = new File(usbP);
                if(file!=null&&file.listFiles().length>0){
                    switch (file.listFiles().length){
                        case 1:
                            if(!file.listFiles()[0].getName().contains("sdcard")){
                                path[0] = usbP + "/" +file.listFiles()[0].getName();

                            }else{
                                path[0] = null;
                            }
                            path[1] = null;
                            break;
                        case 2:
                            if(!file.listFiles()[0].getName().contains("sdcard")) {
                                path[0] = usbP + "/" + file.listFiles()[0].getName();
                                if(!file.listFiles()[1].getName().contains("sdcard")){
                                    path[1] = usbP + "/" +file.listFiles()[1].getName();
                                }else{
                                    path[1] = null;
                                }
                            }else {
                                path[0] = usbP + "/" + file.listFiles()[1].getName();
                                path[1] = null;
                            }


                            break;
                        case 3:
                            path[0] = usbP + "/" +file.listFiles()[0].getName();
                            path[1] = usbP + "/" +file.listFiles()[1].getName();
                            break;
                    }

                }
                path[2] = "/storage/external_storage/sdcard1";
            } else if(getCpuName().equals("sun8i")) {
                path[0] = "/mnt/usbhost/Storage01";
                path[1] = "/mnt/usbhost/Storage02";
                path[2] = null;
            }
            return path;
        }else{
            return getNUSBPath();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private String[] getNUSBPath() {
        String[] paths = new String[3];
        StorageManager storageManager = (StorageManager) getSystemService(Service.STORAGE_SERVICE);
        try {
            Class SM = Class.forName("android.os.storage.StorageManager");
            Class VI = Class.forName("android.os.storage.VolumeInfo");
            Class DI = Class.forName("android.os.storage.DiskInfo");
            Method getVolumes = SM.getDeclaredMethod("getVolumes");
            Method getPath = VI.getDeclaredMethod("getPath");
            Method getDiskInfo = VI.getDeclaredMethod("getDisk");
            Method isMountedReadable = VI.getDeclaredMethod("isMountedReadable");
            Method getType = VI.getDeclaredMethod("getType");

            Method isUsb = DI.getDeclaredMethod("isUsb");
            Method isSd = DI.getDeclaredMethod("isSd");
            List volumeInfos = (List) getVolumes.invoke(storageManager);
            for(int i = 0; i < volumeInfos.size(); i++) {

                Object volume = volumeInfos.get(i);
                if(volume != null &&isMountedReadable.invoke(volume).toString().equals("true")&&(Integer) getType.invoke(volume) == 0) {
                    File path = (File) getPath.invoke(volumeInfos.get(i));
                    Log.i("tag", "path = " + path.getAbsolutePath());
                    Object diskInfo = getDiskInfo.invoke(volumeInfos.get(i));
                    String isUSB = isUsb.invoke(diskInfo).toString();
                    String isSD = isSd.invoke(diskInfo).toString();
                    if(isUSB.equals("true")) {
                        if(paths[0] == null) {
                            paths[0] = path.getAbsolutePath();
                        } else {
                            paths[1] = path.getAbsolutePath();
                        }
                    }
                    if(isSD.equals("true")) {
                        paths[2] = path.getAbsolutePath();
                    }
                }

            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return paths;
    }

    public String getCpuName() {
        File file = new File("/proc/cpuinfo");
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Hardware")) {
                    String cpu = line.split(":")[1];
                    return cpu.trim();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "unknown";
    }

    protected void showAddDialog(List<String> getlocalApps,Context context) {
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
                pf.putString(currentItem.getTag().toString(), app.getPackageName());
                currentItem.showApplication(app);
                mt.stopMarquee();
                dialog.dismiss();
                dialog.cancel();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    BroadcastReceiver btReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtils.d(" intent.getAction() = "+ intent.getAction());
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                setBtState(blueState);
            }
        }
    };

    private void setBtState(int state) {
        switch (state) {
            case BluetoothAdapter.STATE_ON:
                LogUtils.d(" BluetoothAdapter.STATE_ON ");
                stateBluetooth.setImageResource(R.drawable.ble_on);
                break;
            case BluetoothAdapter.STATE_OFF:
                LogUtils.d(" BluetoothAdapter.STATE_OFF ");
                stateBluetooth.setImageResource(R.drawable.ble_off);
                break;
            case BluetoothAdapter.STATE_CONNECTED:
                LogUtils.d(" BluetoothAdapter.STATE_CONNECTED ");
                stateBluetooth.setImageResource(R.drawable.ble_on);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        l_enjoy_layout.destory();
        l_iBiza_layout2.destory();
        l_settings_layout.destory();
        l_tv_layout.destory();
        l_tools_layout.destory();
        try{
            unregisterReceiver(netReceiver);
            unregisterReceiver(mediaReciever);
            unregisterReceiver(installReceiver);
            unregisterReceiver(btReceiver);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
