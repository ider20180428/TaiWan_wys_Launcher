package com.zxy.wtlauncher.view;

import android.content.Context;
import android.view.View;

/**
 * @author jachary.zhao on 2018/3/24.
 * @email zhaoyufei1223@gmail.com
 */
public class TV_ItemLayout extends BaseItemLayout implements IVIewLayout,View.OnFocusChangeListener,View.OnClickListener{


    private Context context;

    public TV_ItemLayout(Context context){
        super(context);
        this.context=context;
    }

    @Override
    public void initView() {

    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void onFocusChange(View view, boolean b) {

    }


    @Override
    public void destory() {

    }
}
