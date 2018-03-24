package com.zxy.wtlauncher.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @author jachary.zhao on 2018/3/23.
 * @email zhaoyufei1223@gmail.com
 */
public class Util {

    private static Toast toast;

    public static void showToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    public static void clearToast(){
        toast=null;
    }



}
