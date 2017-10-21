package com.example.administrator.myapplication;

/**
 * Created by Administrator on 2017/3/5.
 */

public class CarMessage {
    //    驾照分
    String cardscore;
    //    车型
    String carType;
    //     车牌号
    String carNum;

    Double carMoney;

    String carState;
    String carName;
    String carPhone;

    public CarMessage() {

    }

    public CarMessage(String cardscore, String carType, String carNum, Double carMoney, String carState, String carName, String carPhone) {
        this.cardscore = cardscore;
        this.carType = carType;
        this.carNum = carNum;
        this.carMoney = carMoney;
        this.carState = carState;
        this.carName = carName;
        this.carPhone = carPhone;
    }

    public void setCardscore(String cardscore) {
        this.cardscore = cardscore;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public void setCarMoney(Double carMoney) {
        this.carMoney = carMoney;
    }

    public void setCarState(String carState) {
        this.carState = carState;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public void setCarPhone(String carPhone) {
        this.carPhone = carPhone;
    }

    public String getCardscore() {
        return cardscore;
    }

    public String getCarType() {
        return carType;
    }

    public String getCarNum() {
        return carNum;
    }

    public Double getCarMoney() {
        return carMoney;
    }

    public String getCarState() {
        return carState;
    }

    public String getCarName() {
        return carName;
    }

    public String getCarPhone() {
        return carPhone;
    }
}
