package com.example.administrator.myapplication;

/**
 * Created by Administrator on 2017/3/22.
 */

public class MoneyLog {
    Integer cid;
    Integer money;
    long time;
    int type;
    String mark;

    public MoneyLog() {
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getMark() {
        return mark;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getCid() {
        return cid;
    }

    public Integer getMoney() {
        return money;
    }

    public long getTime() {
        return time;
    }

    public int getType() {
        return type;
    }
}
