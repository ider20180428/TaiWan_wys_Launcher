package com.zxy.wtlauncher.view;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.zxy.wtlauncher.R;
import com.zxy.wtlauncher.util.Util;
import com.zxy.wtlauncher.widget.ReflectItemView;

/**
 * @author jachary.zhao on 2018/3/29.
 * @email zhaoyufei1223@gmail.com
 */
public class L_Settings_Layout extends BaseItemLayout implements IVIewLayout,
        View.OnFocusChangeListener,View.OnClickListener{


    private Context mContext;
    private int[] ref_ids={R.id.tv_0,R.id.tv_1,R.id.tv_2,R.id.tv_3,R.id.tv_4,R.id.tv_5};
    private ReflectItemView[]reflectItemViews=new ReflectItemView[6];

    public L_Settings_Layout(Context context) {
        super(context);
        this.mContext=context;
        setGravity(1);
        addView(LayoutInflater.from(this.mContext).inflate(R.layout.z_tv_layout,
                null));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_0:
                Util.showToast(mContext,"1111");
                break;
            case R.id.tv_1:
                Util.showToast(mContext,"222");
                break;
            case R.id.tv_2:
                Util.showToast(mContext,"3333");
                break;
            case R.id.tv_3:
                Util.showToast(mContext,"4444");
                break;
            case R.id.tv_4:
                Util.showToast(mContext,"5555");
                break;
            case R.id.tv_5:
                Util.showToast(mContext,"6666");
                break;

        }

    }

    @Override
    public void onFocusChange(View view, boolean b) {

    }

    @Override
    public void initView() {
        for (int i=0;i<ref_ids.length;i++){
            reflectItemViews[i]=(ReflectItemView) findViewById(ref_ids[i]);
            reflectItemViews[i].setOnClickListener(this);
        }
    }

    @Override
    public void destory() {
        if (this.reflectItemViews!=null){
            for (ReflectItemView reflectItemView:reflectItemViews){
                reflectItemView=null;
            }
        }
        this.ref_ids=null;
        this.reflectItemViews=null;

    }
}
