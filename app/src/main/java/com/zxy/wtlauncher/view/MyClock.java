package com.zxy.wtlauncher.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zxy.wtlauncher.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author jachary.zhao on 2018/4/4.
 * @email zhaoyufei1223@gmail.com
 */
public class MyClock extends LinearLayout {

    static SimpleDateFormat sdf_time = new SimpleDateFormat("HH:mm");
    static SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd");
    static Calendar cal = Calendar.getInstance();
    private TextView textViewTime, textViewDate, textViewWeek;

    private String[] weekDays=new String[7];
    private int[]weekStringId={R.string.sunday,R.string.monday,R.string.tuesday,R.string.wednesday,
    R.string.thursday,R.string.friday,R.string.saturday};

    public MyClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 使用layoutinflater把布局加载到本ViewGroup
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.clock_layout, this);

        for (int i=0;i<weekDays.length;i++){
            weekDays[i]=context.getResources().getString(weekStringId[i]);
        }
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewWeek = (TextView) findViewById(R.id.textViewWeek);

        startThread();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    public static String getCurrentTime(Date date) {

        sdf_time.format(date);
        return sdf_time.format(date);
    }

    public static String getCurrentDate(Date date) {

        sdf_date.format(date);
        return sdf_date.format(date);
    }

    public String getCurrentWeekDay(Date dt) {
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }

    private void startThread() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    handler.sendEmptyMessage(12);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }
        }).start();
    }

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 12) {
                Date date = new Date();
                textViewTime.setText(getCurrentTime(date));
                textViewDate.setText(getCurrentDate(date));
                textViewWeek.setText(getCurrentWeekDay(date));
            }

        }
    };



}
