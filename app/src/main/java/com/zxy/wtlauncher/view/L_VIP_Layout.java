package com.zxy.wtlauncher.view;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zxy.wtlauncher.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author jachary.zhao on 2018/3/29.
 * @email zhaoyufei1223@gmail.com
 */
public class L_VIP_Layout extends BaseItemLayout implements IVIewLayout,
        View.OnFocusChangeListener,View.OnClickListener{


    private Context mContext;
    private String sn;//機器序列號,通過ro.boot.serialno獲取
    private Context context;
    private ImageView erweiCodeImage;//生成的序列號二維碼
    private TextView snTextView;
    private Method systemProperties_get = null;
    private View view;
    private String fileString;
    private File file;



    public L_VIP_Layout(Context context) {
        super(context);
        this.mContext=context;
        setGravity(1);
        view=LayoutInflater.from(this.mContext).inflate(R.layout.z_vip_layout,null);
        addView(view);


    }

    BroadcastReceiver qrCodeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

//            if (intent.getAction().equals("com.ider.qrcode")){
//                fileString = intent.getStringExtra("qrcode");
//                Toast.makeText(context,fileString,Toast.LENGTH_LONG).show();
//                file = new File(fileString);
//                Log.d("zhaoyf","onReceicer : filePath = "+fileString);
//                if (file.exists()){
////                    Glide.with(mContext).load(file).into(erweiCodeImage);
//                }
//            }
        }
    };

    private void registReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.ider.qqrcode");
        mContext.registerReceiver(qrCodeReceiver,intentFilter);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onFocusChange(View view, boolean b) {

    }

    @Override
    public void initView() {
        registReceiver();
        erweiCodeImage = (ImageView)view.findViewById(R.id.vip_code_iv);//二維碼
        snTextView = (TextView)view.findViewById(R.id.vip_device_sn_text); //顯示機器序列號
//        final String filePath = getFileRoot(getContext()) + File.separator
//                + "qr_" + System.currentTimeMillis() + ".jpg";
        sn = getAndroidOsSystemProperties("ro.boot.serialno");
        if (sn.equals("")){
            sn = getAndroidOsSystemProperties("ro.serialno");
        }
        if (!sn.equals("")){
            Log.i("FragmentTwoDimen", sn);
            snTextView.setText("機碼序號："+sn);
        }

        final String info = sn+" \n"+getEthMac();
        file = new File("/sdcard/Android/data/com.explorer.ider.bootguide/files/qr_code.jpg");
        if (file.exists()){
            Glide.with(mContext).load(file).into(erweiCodeImage);
        }else {
            Log.d("zhaoyf","file.exists() = false");
        }


    }


    private String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }

        return context.getFilesDir().getAbsolutePath();
    }

    private String getAndroidOsSystemProperties(String key) {
        String ret;
        try {
            systemProperties_get = Class.forName("android.os.SystemProperties").getMethod("get", String.class);
            if ((ret = (String) systemProperties_get.invoke(null, key)) != null)
                return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return "";
    }

    public String getEthMac() {
        File file = new File("/sys/class/net/eth0/address");
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String mac = br.readLine();
            if (!mac.equals(""))
                Log.i("FragmentTwoDimen", mac);
            return mac;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    @Override
    public void destory() {

        mContext.unregisterReceiver(qrCodeReceiver);
    }
}
