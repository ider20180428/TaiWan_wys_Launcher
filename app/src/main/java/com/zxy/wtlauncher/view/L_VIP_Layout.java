package com.zxy.wtlauncher.view;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import com.zxy.wtlauncher.R;

/**
 * @author jachary.zhao on 2018/3/29.
 * @email zhaoyufei1223@gmail.com
 */
public class L_VIP_Layout extends BaseItemLayout implements IVIewLayout,
        View.OnFocusChangeListener,View.OnClickListener{


    private Context mContext;


    public L_VIP_Layout(Context context) {
        super(context);
        this.mContext=context;
        setGravity(1);
        addView(LayoutInflater.from(this.mContext).inflate(R.layout.z_vip_layout,
                null));
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onFocusChange(View view, boolean b) {

    }

    @Override
    public void initView() {

    }

    @Override
    public void destory() {

    }
}
