package com.example.administrator.myapplication;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/2/26.
 */

public class Car {
    //小车名称
    int name;

    public int getName() {
        return name;
    }

    //横纵坐标
    int x;
    int y;
    //目标坐标
    int targerX;
    int targerY;
    //小车当前方向
    int round;
    //小车图片
    Bitmap image;
    //小车路线
    int luxian;
    //是否停车 停车时间 停车位置
    boolean isPark;
    long parkTime;
    int parkAdd;
    //小车速度
    int speed;
    //是否运作
    boolean isRun;


    //停车场信息
    long beginPartTime;
    String partType;

    public void setPartType(String partType) {
        this.partType = partType;
    }

    public String getPartType() {
        return partType;
    }

    public long getBeginPartTime() {
        return beginPartTime;
    }

    public void setBeginPartTime(long beginPartTime) {
        this.beginPartTime = beginPartTime;
    }

    public void setLuxian(int luxian) {
        this.luxian = luxian;
    }

    public boolean isPark() {
        return isPark;
    }

    public void setPark(boolean park) {
        isPark = park;
    }

    public long getParkTime() {
        return parkTime;
    }

    public void setParkTime(long parkTime) {
        this.parkTime = parkTime;
    }

    public Car(int x, int y, Bitmap image, int luxian, int name) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.targerX = x;
        this.targerY = y;
        this.speed = 10;
        this.image = image;
        this.luxian = luxian;
        this.isPark = false;
        this.parkTime = 0;
        this.parkAdd = 0;
        this.isRun = true;
        this.round = 270;
    }

    public void setParkAdd(int parkAdd) {
        this.parkAdd = parkAdd;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public double getPointLimit(Car car) {

        return Math.sqrt(Math.pow(x - car.x, 2) + Math.pow(y - car.y, 2));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getTargerX() {
        return targerX;
    }

    public int getTargerY() {
        return targerY;
    }

    public int getRound() {
        return round;
    }

    public Bitmap getImage() {
        return image;
    }

    public int getLuxian() {
        return luxian;
    }

    public int getParkAdd() {
        return parkAdd;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isRun() {
        return isRun;
    }

    @Override
    public String toString() {
        return "Car{" +
                "x=" + x +
                ", y=" + y +
                ", targerX=" + targerX +
                ", targerY=" + targerY +
                ", round=" + round +
                ", image=" + image +
                ", luxian=" + luxian +
                ", isPark=" + isPark +
                ", parkTime=" + parkTime +
                ", parkAdd=" + parkAdd +
                ", speed=" + speed +
                ", isRun=" + isRun +
                '}';
    }
}
