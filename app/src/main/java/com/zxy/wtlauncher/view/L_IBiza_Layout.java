package com.zxy.wtlauncher.view;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.zxy.wtlauncher.IBizaActivity;
import com.zxy.wtlauncher.R;
import com.zxy.wtlauncher.util.Util;
import com.zxy.wtlauncher.widget.ReflectItemView;

/**
 * @author jachary.zhao on 2018/3/29.
 * @email zhaoyufei1223@gmail.com
 */
public class L_IBiza_Layout extends BaseItemLayout implements IVIewLayout,
        View.OnFocusChangeListener,View.OnClickListener{


    private Context mContext;
    private Button button;


    public L_IBiza_Layout(Context context) {
        super(context);
        this.mContext=context;
        setGravity(1);
        addView(LayoutInflater.from(this.mContext).inflate(R.layout.z_ibiza_layout,
                null));
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.ibiza_btn:

                startActivity(IBizaActivity.class);
                break;
        }



    }

    @Override
    public void onFocusChange(View view, boolean b) {

    }

    @Override
    public void initView() {
        button = (Button)findViewById(R.id.ibiza_btn);
        button.setOnClickListener(this);

    }

    @Override
    public void destory() {

    }
}
