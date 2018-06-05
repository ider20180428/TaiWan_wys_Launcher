package com.zxy.wtlauncher;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.zxy.wtlauncher.util.LogUtils;
import com.zxy.wtlauncher.util.NetUtil;
import com.zxy.wtlauncher.widget.MainLayout;
import com.zxy.wtlauncher.widget.MainUpView;
import com.zxy.wtlauncher.widget.OpenEffectBridge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author jachary.zhao on 2018/4/11.
 * @email zhaoyufei1223@gmail.com
 */
public class IBizaActivity extends BaseActivity implements View.OnClickListener{

    private View mOldFocus;
    private MainUpView mainUpView1;
    private MainLayout main_lay;
    private OpenEffectBridge mOpenEffectBridge;
    private ImageView buBingImageView;
    private ImageView qiBingImageView;
    private ImageView net_icon,tf_icon,usb_icon,stateBluetooth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_ibiza_activity_layout);
        initView();
    }

    private void initView() {
        main_lay = (MainLayout)findViewById(R.id.main_lay);
        mainUpView1 = (MainUpView)findViewById(R.id.mainUpView1);
        mOpenEffectBridge = (OpenEffectBridge) mainUpView1.getEffectBridge();
        mainUpView1.setUpRectResource(R.drawable.test_rectangle); // 设置移动边框的图片
//        mainUpView1.setDrawUpRectPadding(new Rect(25, 18, 25, 18)); // 边框图片设置间距.
        mainUpView1.setShadowResource(R.drawable.item_shadow); // 设置移动边框的阴影
        mOpenEffectBridge.setTranDurAnimTime(200); // 动画时间
        main_lay.getViewTreeObserver().addOnGlobalFocusChangeListener(
                new ViewTreeObserver.OnGlobalFocusChangeListener() {
                    @Override
                    public void onGlobalFocusChanged(final View oldFocus,
                                                     final View newFocus) {
                        newFocus.bringToFront(); // 防止放大的view被压在下面.
                        float scale = 1.1f;// 缩放比例
                        mainUpView1.setFocusView(newFocus, mOldFocus, scale);
                        mOldFocus = newFocus; // 4.3以下需要自己保存.
                    }
                });

        buBingImageView = (ImageView)findViewById(R.id.bu_bing_iv);
        qiBingImageView = (ImageView)findViewById(R.id.qi_bing_iv);
        buBingImageView.setOnClickListener(this);
        qiBingImageView.setOnClickListener(this);

        net_icon = (ImageView) findViewById(R.id.net_icon);
        tf_icon = (ImageView) findViewById(R.id.tf_icon);
        usb_icon = (ImageView) findViewById(R.id.usb_icon);
        stateBluetooth=(ImageView)findViewById(R.id.ble_icon);
        checkExternalStorage();
        registeservers();
    }


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

    BroadcastReceiver netReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (NetUtil.CONNECTIVITY_CHANGE.equals(action)) {
                updateNetState(getApplicationContext());
            }
        }
    };

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
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(btReceiver, filter);
    }

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


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.bu_bing_iv://步兵 2
                //com.ibizatv.ch2/.activity.SplashActivity
                lunchApp("com.ibizatv.ch2","com.ibizatv.ch2.activity.SplashActivity");

                break;
            case R.id.qi_bing_iv://骑兵 1

                lunchApp("com.yolib.ibiza","com.yolib.ibiza.activity.SplashActivity");

                //com.yolib.ibiza  /.activity.SplashActivity

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            unregisterReceiver(netReceiver);
            unregisterReceiver(mediaReciever);
            unregisterReceiver(btReceiver);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
