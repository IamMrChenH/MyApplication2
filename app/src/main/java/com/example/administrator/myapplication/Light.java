package com.example.administrator.myapplication;

import android.graphics.Paint;

/**
 * Created by Administrator on 2017/3/7.
 */

public class Light {
    int redTime;
    int greedTime;
    int nowTime;
    Paint light;

    public void setRedTime(int redTime) {
        this.redTime = redTime;
    }

    public void setGreedTime(int greedTime) {
        this.greedTime = greedTime;
    }

    public void setNowTime(int nowTime) {
        this.nowTime = nowTime;
    }

    public void setLight(Paint light) {
        this.light = light;
    }

    public int getRedTime() {
        return redTime;
    }

    public int getGreedTime() {
        return greedTime;
    }

    public int getNowTime() {
        return nowTime;
    }

    public Paint getLight() {
        return light;
    }

    public Light(int redTime, int greedTime, int nowTime) {
        this.redTime = redTime;
        this.greedTime = greedTime;
        this.nowTime = nowTime;
        this.light=new Paint();
    }
}
