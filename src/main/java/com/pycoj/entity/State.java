package com.pycoj.entity;

/**
 * Created by Heyman on 2017/6/12.
 * 运行情况
 */
public class State {
    private int id;
    private int submitId;
    private int timeCost;
    private int memoryCost;
    private int state;//0成功，1编译不成功，2超时，3超内存，4输出不正确
    private String info;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubmitId() {
        return submitId;
    }

    public void setSubmitId(int submitId) {
        this.submitId = submitId;
    }

    public int getTimeCost() {
        return timeCost;
    }

    public void setTimeCost(int timeCost) {
        this.timeCost = timeCost;
    }

    public int getMemoryCost() {
        return memoryCost;
    }

    public void setMemoryCost(int memoryCost) {
        this.memoryCost = memoryCost;
    }

    public State(int submitId, int timeCost, int memoryCost, int state, String info) {
        this.submitId = submitId;
        this.timeCost = timeCost;
        this.memoryCost = memoryCost;
        this.state = state;
        this.info = info;
    }
}
