package com.example.administrator.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.administrator.myapplication.LzhTool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mario on 2017/3/30.
 */

public class LinghtsService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        super.onCreate();
        NoAndOffLights();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void NoAndOffLights() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.CHINA);
                        Date date = new Date(System.currentTimeMillis());//获取当前时间
                        String time = String.valueOf(formatter.format(date));
                        Log.e("12", dataOne("18:00"));
                        if (Integer.valueOf(dataOne(time)) >= Integer.valueOf(dataOne("18:00")) || Integer.valueOf(dataOne(time)) <= Integer.valueOf(dataOne("06:00"))) {
                            //晚上
                            LzhTool.colors = "#ffff00";
                        } else if (Integer.valueOf(dataOne(time)) < Integer.valueOf(dataOne("18:00")) && Integer.valueOf(dataOne(time)) > Integer.valueOf(dataOne("06:00"))) {
                            //白天
                            if (false) {
                                LzhTool.colors = "#ffff00";
                            } else {
                                LzhTool.colors = "#00ffff00";
                            }
                        }
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("2333333", e.toString());
                    }
                }
            }
        }).start();
    }

    /**
     * 调此方法输入所要转换的时间输入例如（"2014-06-14-16-09-00"）返回时间戳
     *
     * @param time
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String dataOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("HH:mm", Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 6);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }
}
