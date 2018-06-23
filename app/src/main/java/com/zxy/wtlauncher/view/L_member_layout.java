package com.zxy.wtlauncher.view;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zxy.wtlauncher.R;

import org.w3c.dom.Text;

/**
 * @author jachary.zhao on 2018/6/22.
 * @email zhaoyufei1223@gmail.com
 */
public class L_member_layout extends BaseItemLayout {


    public L_member_layout(Context context) {
        super(context);
        this.mContext=context;
        setGravity(1);
        View view= LayoutInflater.from(this.mContext).inflate(R.layout.z_member_layout,null);
        addView(view);

        TextView contactUs = (TextView)findViewById(R.id.contact_us);
        contactUs.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }





}
