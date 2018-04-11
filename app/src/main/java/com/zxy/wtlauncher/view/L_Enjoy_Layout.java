package com.zxy.wtlauncher.view;


import android.content.ComponentName;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxy.wtlauncher.Application;
import com.zxy.wtlauncher.R;
import com.zxy.wtlauncher.util.Util;
import com.zxy.wtlauncher.widget.ReflectItemView;

/**
 * @author jachary.zhao on 2018/3/29.
 * @email zhaoyufei1223@gmail.com
 */
public class L_Enjoy_Layout extends BaseItemLayout implements IVIewLayout,
        View.OnFocusChangeListener,View.OnClickListener{


    private Context mContext;
    private int[] ref_ids={R.id.tv_0,R.id.tv_1,R.id.tv_2,R.id.tv_3,R.id.tv_4,R.id.tv_5,R.id.tv_6};
    private ReflectItemView[]reflectItemViews=new ReflectItemView[7];
    private ImageView[]iconImageViews=new ImageView[7];
    private int[]imageViewIds={R.id.tv_iv0,R.id.tv_iv1,R.id.tv_iv2,R.id.tv_iv3,R.id.tv_iv4,R.id.tv_iv5,R.id.tv_iv6};
    private int[]appNameTextViewIds={R.id.tv_tv0,R.id.tv_tv1,R.id.tv_tv2,R.id.tv_tv3,R.id.tv_tv4,R.id.tv_tv5,R.id.tv_tv6};
    private TextView[]appNameTextViews=new TextView[7];


    private String pkgTags[]={"30","31","32","33","34","35","36"};
    private boolean[]shortCutsStatus={false,false,false,false,false,false,false};
    private String[]shortCutsTag={"300","301","302","303","304","305","306"};
    private ComponentName[]componentNames=new ComponentName[7];


    public L_Enjoy_Layout(Context context) {
        super(context);
        this.mContext=context;
        setGravity(1);
        addView(LayoutInflater.from(this.mContext).inflate(R.layout.z_enjoy_layout,
                null));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_0:

                break;
            case R.id.tv_1:

                break;
            case R.id.tv_2:

                break;
            case R.id.tv_3:

                break;
            case R.id.tv_4:

                break;
            case R.id.tv_5:

                break;
            case R.id.tv_6:

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
            iconImageViews[i]=(ImageView)findViewById(imageViewIds[i]);
            appNameTextViews[i]=(TextView)findViewById(appNameTextViewIds[i]);

            String pkgName= pf.getString(pkgTags[i],null);
            if (null!=pkgName){
                Application app=Application.doApplication(pkgName,mContext);
                if (null!=app){
                    iconImageViews[i].setImageDrawable(app.getIcon());
                    appNameTextViews[i].setText(app.getLabel());
                    shortCutsStatus[i]=true;
                    pf.putBoolean(shortCutsTag[i],true);
                    String className=app.getClassName();
                    if (null!=className){
                        componentNames[i]=new ComponentName(pkgName,app.getClassName());
                    }

                }else {
                    iconImageViews[i].setImageResource(R.drawable.add_apps);
                    appNameTextViews[i].setText("添加");
                }
            }else {
                iconImageViews[i].setImageResource(R.drawable.add_apps);
                appNameTextViews[i].setText("添加");
            }

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
