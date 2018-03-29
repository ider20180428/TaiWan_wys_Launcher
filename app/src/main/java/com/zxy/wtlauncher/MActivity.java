package com.zxy.wtlauncher;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import com.zxy.wtlauncher.view.L_Enjoy_Layout;
import com.zxy.wtlauncher.view.L_IBiza_Layout;
import com.zxy.wtlauncher.view.L_Settings_Layout;
import com.zxy.wtlauncher.view.L_TV_Layout;
import com.zxy.wtlauncher.view.L_Tools_Layout;
import com.zxy.wtlauncher.view.L_VIP_Layout;
import com.zxy.wtlauncher.widget.MainUpView;
import com.zxy.wtlauncher.widget.OpenEffectBridge;
import com.zxy.wtlauncher.widget.ReflectItemView;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jachary.zhao on 2018/3/29.
 * @email zhaoyufei1223@gmail.com
 */
public class MActivity extends BaseActivity implements View.OnFocusChangeListener{
    private List<View> viewList;
    private L_VIP_Layout l_vip_layout;
    private L_Enjoy_Layout l_enjoy_layout;
    private L_IBiza_Layout l_iBiza_layout;
    private L_Settings_Layout l_settings_layout;
    private L_TV_Layout l_tv_layout;
    private L_Tools_Layout l_tools_layout;
    private ViewPager viewpager;
    private OpenEffectBridge mSavebridge;
    private View mOldFocus;
    private LinearLayout[]linearLayouts=new LinearLayout[6];
    private int[]refIds={R.id.bottom_lin0,R.id.bottom_lin1,R.id.bottom_lin2,R.id.bottom_lin3,R.id.bottom_lin4,
            R.id.bottom_lin5};
    private MyPagerAdapter myPagerAdapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_main_layout);
        mContext=this;
        viewList = new ArrayList<View>();
        viewpager = (ViewPager) findViewById(R.id.main_layout_viewpager);
        myPagerAdapter = new MyPagerAdapter();
        for (int i=0;i<linearLayouts.length;i++){
            linearLayouts[i]=(LinearLayout)findViewById(refIds[i]);
            linearLayouts[i].setOnFocusChangeListener(this);
        }
        initView();
    }

    private void initView(){
        l_enjoy_layout = new L_Enjoy_Layout(this);
        l_iBiza_layout = new L_IBiza_Layout(this);
        l_settings_layout = new L_Settings_Layout(this);
        l_tv_layout = new L_TV_Layout(this);
        l_vip_layout = new L_VIP_Layout(this);
        l_tools_layout = new L_Tools_Layout(this);
        l_enjoy_layout.initView();
        l_iBiza_layout.initView();
        l_settings_layout.initView();
        l_tv_layout.initView();
        l_vip_layout.initView();
        l_tools_layout.initView();
        viewList.add(l_settings_layout);
        viewList.add(l_tools_layout);
        viewList.add(l_tv_layout);
        viewList.add(l_enjoy_layout);
        viewList.add(l_vip_layout);
        viewList.add(l_iBiza_layout);
        viewpager.setAdapter(myPagerAdapter);
        linearLayouts[2].requestFocus();
        for (View view : viewList) {
            MainUpView mainUpView = (MainUpView) view
                    .findViewById(R.id.mainUpView1);
//            mainUpView.setUpRectResource(R.drawable.test_rectangle); //
//            mainUpView.setShadowResource(R.drawable.item_shadow); //
            OpenEffectBridge bridget = (OpenEffectBridge) mainUpView
                    .getEffectBridge();
            bridget.setTranDurAnimTime(250);
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
                            float scale = 1.08f;

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
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }


    @Override
    public void onFocusChange(View view, boolean b) {

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

}
