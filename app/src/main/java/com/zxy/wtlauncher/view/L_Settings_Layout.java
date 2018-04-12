package com.zxy.wtlauncher.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.zxy.wtlauncher.R;
import com.zxy.wtlauncher.applist.AppsActivity;
import com.zxy.wtlauncher.widget.ReflectItemView;

/**
 * @author jachary.zhao on 2018/3/29.
 * @email zhaoyufei1223@gmail.com
 */
public class L_Settings_Layout extends BaseItemLayout implements IVIewLayout,
        View.OnFocusChangeListener,View.OnClickListener{


    private Context mContext;
    private int[] ref_ids={R.id.tv_0,R.id.tv_1,R.id.tv_2,R.id.tv_3,R.id.tv_4,R.id.tv_5,R.id.tv_6};
    private ReflectItemView[]reflectItemViews=new ReflectItemView[7];
    private ImageView imageView0,imageView1;
    private TextView textView0,textView1;

    public L_Settings_Layout(Context context) {
        super(context);
        this.mContext=context;
        setGravity(1);
        addView(LayoutInflater.from(this.mContext).inflate(R.layout.z_settings_layout,
                null));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_0:
                launchApp("com.android.tv.settings");
                break;
            case R.id.tv_1:
                startActivity(AppsActivity.class);

                break;
            case R.id.tv_2:

                break;
            case R.id.tv_3:

                break;
            case R.id.tv_4:

                break;
            case R.id.tv_5:

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
            if (i>1){
                reflectItemViews[i].setVisibility(View.INVISIBLE);
            }
        }
        imageView0=(ImageView)findViewById(R.id.tv_iv0);
        imageView1=(ImageView)findViewById(R.id.tv_iv1);
        textView0=(TextView)findViewById(R.id.tv_tv0);
        textView1=(TextView)findViewById(R.id.tv_tv1);
        imageView0.setImageResource(R.drawable.advance_set);
        imageView1.setImageResource(R.drawable.all_apps);
        textView0.setText("系統設定");
        textView1.setText("程式列表");
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
