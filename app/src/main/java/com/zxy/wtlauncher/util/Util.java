package com.zxy.wtlauncher.util;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

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


    public static void clearToast() {
        toast = null;
    }

    public static int getSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
        }
        return version;
    }

    public static String readFile(String fileName) {
        String b = "";
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(fileName), "utf-8"));
            while ((b = br.readLine()) != null) { // 按行读取
                sb.append(b);
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();

    }

    public static boolean writeSysFile(String c, String v) {

        File file = new File(c);
        if (!file.exists()) {
            return false;
        }

        // write
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(c), 4);
            try {
                out.write(v);
            } finally {
                out.close();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }


}
