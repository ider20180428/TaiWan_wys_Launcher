package com.zxy.wtlauncher;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import com.zxy.wtlauncher.widget.MainLayout;
import com.zxy.wtlauncher.widget.MainUpView;
import com.zxy.wtlauncher.widget.OpenEffectBridge;

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
}
