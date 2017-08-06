package com.pycoj.entity;

import java.io.Serializable;

/**
 * Created by 潘毅烦 on 2017/8/2.
 */
public class Dto<T> implements Serializable{
    private T data;
    private boolean success;
    private String info;
    public Dto(T data,boolean success,String info){
        this.data=data;
        this.success=success;
        this.info=info;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
