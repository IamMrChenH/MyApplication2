package com.example.administrator.myapplication;

/**
 * Created by Administrator on 2017/3/10.
 */

public class Violation {
    int id;
    String carId;
    String vTime;
    String vMsg;
    String score;
    String money;
    String flag;
    String moneyFlag;
    String overTime;
    String vXY;
    String vID;
    String vOffice;
    String photo;

    public Violation() {
    }

    public void setMoneyFlag(String moneyFlag) {
        this.moneyFlag = moneyFlag;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public String getMoneyFlag() {
        return moneyFlag;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public void setvTime(String vTime) {
        this.vTime = vTime;
    }

    public void setvMsg(String vMsg) {
        this.vMsg = vMsg;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    public void setvXY(String vXY) {
        this.vXY = vXY;
    }

    public void setvID(String vID) {
        this.vID = vID;
    }

    public void setvOffice(String vOffice) {
        this.vOffice = vOffice;
    }

    public int getId() {
        return id;
    }

    public String getCarId() {
        return carId;
    }

    public String getvTime() {
        return vTime;
    }

    public String getvMsg() {
        return vMsg;
    }

    public String getScore() {
        return score;
    }

    public String getMoney() {
        return money;
    }

    public String getFlag() {
        return flag;
    }

    public String getOverTime() {
        return overTime;
    }

    public String getvXY() {
        return vXY;
    }

    public String getvID() {
        return vID;
    }

    public String getvOffice() {
        return vOffice;
    }

}
