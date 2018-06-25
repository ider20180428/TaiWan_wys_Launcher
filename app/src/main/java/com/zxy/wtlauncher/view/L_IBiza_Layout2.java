package com.zxy.wtlauncher.view;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.zxy.wtlauncher.Application;
import com.zxy.wtlauncher.R;
import com.zxy.wtlauncher.widget.ReflectItemView;

/**
 * @author jachary.zhao on 2018/3/29.
 * @email zhaoyufei1223@gmail.com
 */
public class L_IBiza_Layout2 extends BaseItemLayout implements IVIewLayout,
        View.OnFocusChangeListener,View.OnClickListener{


    private Context mContext;
    private ReflectItemView ibiza1_Ref,ibiza2_Ref;
    private ImageView ibiza1_imageview,ibiza2_imageview;
    private TextView ibiza1_Textview,ibiza2_Textview;


    public L_IBiza_Layout2(Context context) {
        super(context);
        this.mContext=context;
        setGravity(1);
        addView(LayoutInflater.from(this.mContext).inflate(R.layout.z_ibiza_layout2,
                null));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.ibiza1_ref:
                launchApp("com.yolib.ibiza","com.yolib.ibiza.activity.SplashActivity");
                break;
            case R.id.ibiza2_ref:
                launchApp("com.ibizatv.ch2","com.ibizatv.ch2.activity.SplashActivity");
                break;
        }

    }

    @Override
    public void onFocusChange(View view, boolean b) {

    }

    @Override
    public void initView() {
        Application app1=Application.doApplication("com.yolib.ibiza",mContext);
        Application app2=Application.doApplication("com.ibizatv.ch2",mContext);
        ibiza1_Ref = (ReflectItemView)findViewById(R.id.ibiza1_ref);
        ibiza2_Ref = (ReflectItemView)findViewById(R.id.ibiza2_ref);
        ibiza1_Ref.setOnClickListener(this);
        ibiza2_Ref.setOnClickListener(this);

        ibiza1_imageview=(ImageView)findViewById(R.id.ibiza1_iv);
        ibiza2_imageview=(ImageView)findViewById(R.id.ibiza2_iv);
        ibiza1_Textview=(TextView)findViewById(R.id.ibiza1_tv);
        ibiza2_Textview=(TextView)findViewById(R.id.ibiza2_tv);



        if(null!=app1){
            ibiza1_imageview.setImageDrawable(app1.getIcon());
            ibiza1_Textview.setText(app1.getLabel());
        }
        if (null!=app2){
            ibiza2_imageview.setImageDrawable(app2.getIcon());
            ibiza2_Textview.setText(app2.getLabel());
        }





    }

    @Override
    public void destory() {

    }
}
