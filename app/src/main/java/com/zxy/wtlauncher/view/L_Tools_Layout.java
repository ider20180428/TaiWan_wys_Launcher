package com.zxy.wtlauncher.view;


import android.content.ComponentName;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.zxy.wtlauncher.Application;
import com.zxy.wtlauncher.R;
import com.zxy.wtlauncher.StartActivity;
import com.zxy.wtlauncher.util.Util;
import com.zxy.wtlauncher.widget.ReflectItemView;

/**
 * @author jachary.zhao on 2018/3/29.
 * @email zhaoyufei1223@gmail.com
 */
public class L_Tools_Layout extends BaseItemLayout implements IVIewLayout,
        View.OnFocusChangeListener,View.OnClickListener,View.OnLongClickListener{


    private Context mContext;
    private int[] ref_ids={R.id.tv_0,R.id.tv_1,R.id.tv_2,R.id.tv_3,R.id.tv_4,R.id.tv_5,R.id.tv_6};
    private ReflectItemView[]reflectItemViews=new ReflectItemView[7];

    private int[]imageViewIds={R.id.tv_iv0,R.id.tv_iv1,R.id.tv_iv2,R.id.tv_iv3,R.id.tv_iv4,R.id.tv_iv5,R.id.tv_iv6};
    private int[]appNameTextViewIds={R.id.tv_tv0,R.id.tv_tv1,R.id.tv_tv2,R.id.tv_tv3,R.id.tv_tv4,R.id.tv_tv5,R.id.tv_tv6};


    private String pkgTags[]={"10","11","12","13","14","15","16"};
    private String[]shortCutsTag={"100","101","102","103","104","105","106"};


    public L_Tools_Layout(Context context) {
        super(context);
        this.mContext=context;
        setGravity(1);
        addView(LayoutInflater.from(this.mContext).inflate(R.layout.z_tv_layout_max,
                null));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_0:
                if (shortCutsStatus[0]){
                    launchApp(componentNames[0]);
                }else {
                    showAddDialog(0,shortCutsTag[0],pkgTags[0],getlocalApps(),mContext);
                }
                break;
            case R.id.tv_1:
                if (shortCutsStatus[1]){
                    launchApp(componentNames[1]);
                }else {
                    showAddDialog(1,shortCutsTag[1],pkgTags[1],getlocalApps(),mContext);
                }
                break;
            case R.id.tv_2:
                if (shortCutsStatus[2]){
                    launchApp(componentNames[2]);
                }else {
                    showAddDialog(2,shortCutsTag[2],pkgTags[2],getlocalApps(),mContext);
                }
                break;
            case R.id.tv_3:
                if (shortCutsStatus[3]){
                    launchApp(componentNames[3]);
                }else {
                    showAddDialog(3,shortCutsTag[3],pkgTags[3],getlocalApps(),mContext);
                }
                break;
            case R.id.tv_4:
                if (shortCutsStatus[4]){
                    launchApp(componentNames[4]);
                }else {
                    showAddDialog(4,shortCutsTag[4],pkgTags[4],getlocalApps(),mContext);
                }
                break;
            case R.id.tv_5:
                startActivity(StartActivity.class);//一键清理

                break;
            case R.id.tv_6:
                if (shortCutsStatus[6]){
                    launchApp(componentNames[6]);
                }else {
                    showAddDialog(6,shortCutsTag[6],pkgTags[6],getlocalApps(),mContext);

                }
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
            reflectItemViews[i].setOnLongClickListener(this);
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
                        componentNames[i]=new ComponentName(pkgName,className);
                    }else {
                        componentNames[i]=new ComponentName(pkgName,"");
                    }

                }else {
                    if (pkgName.equals("clean")){
                        iconImageViews[i].setImageResource(R.drawable.onkeyclean);
                        appNameTextViews[i].setText("一鍵清理");
                    }else {
                        iconImageViews[i].setImageResource(R.drawable.add_apps);
                        appNameTextViews[i].setText("添加");
                    }
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

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()){
            case R.id.tv_0:
                showAdd(0,pkgTags[0]);
                break;
            case R.id.tv_1:
                showAdd(1,pkgTags[1]);
                break;
            case R.id.tv_2:
                showAdd(2,pkgTags[2]);
                break;
            case R.id.tv_3:
                showAdd(3,pkgTags[3]);
                break;
            case R.id.tv_4:
                showAdd(4,pkgTags[4]);
                break;
            case R.id.tv_6:
               showAdd(6,pkgTags[6]);
                break;
        }
        return true;
    }
}
