package com.pycoj.entity;

/**
 * Created by Heyman on 2017/6/12.
 * 完成情况
 */
public class Submit {
    private int id;
    private int tCost;//完成时间
    private int mCost;//消耗内存
    private int userId;//此次提交的用户的id
    private int questionId;//对应题目ID
    private State state;
    private String dir;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int gettCost() {
        return tCost;
    }

    public void settCost(int tCost) {
        this.tCost = tCost;
    }

    public int getmCost() {
        return mCost;
    }

    public void setmCost(int mCost) {
        this.mCost = mCost;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
