package com.example.administrator.myapplication;

/**
 * Created by Administrator on 2017/3/22.
 */

public class ParkLog {
    Long parkTime;
    Integer longTime;
    Integer moneyCast;
    int parkCar;
    String cost;
    String type;

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCost() {

        return cost;
    }

    public String getType() {
        return type;
    }

    public void setParkTime(Long parkTime) {
        this.parkTime = parkTime;
    }

    public void setLongTime(Integer longTime) {
        this.longTime = longTime;
    }

    public void setMoneyCast(Integer moneyCast) {
        this.moneyCast = moneyCast;
    }

    public void setParkCar(int parkCar) {
        this.parkCar = parkCar;
    }

    public Long getParkTime() {
        return parkTime;
    }

    public Integer getLongTime() {
        return longTime;
    }

    public Integer getMoneyCast() {
        return moneyCast;
    }

    public int getParkCar() {
        return parkCar;
    }

    public ParkLog() {
    }

    public ParkLog(Long parkTime, Integer longTime, Integer moneyCast, int parkCar) {
        this.parkTime = parkTime;
        this.longTime = longTime;
        this.moneyCast = moneyCast;
        this.parkCar = parkCar;
    }
}
