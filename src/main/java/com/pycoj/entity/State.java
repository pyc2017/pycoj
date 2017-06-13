package com.pycoj.entity;

/**
 * Created by Heyman on 2017/6/12.
 * 运行情况
 */
public class State {
    private int state;//0成功，1编译失败，2超时，3超内存，4输出不正确
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

    public State(int state, String info) {
        this.state = state;
        this.info = info;
    }
}
