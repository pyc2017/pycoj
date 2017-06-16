package com.pycoj.entity;

/**
 * Created by Heyman on 2017/6/12.
 * 完成情况
 */
public class Submit {
    private int id;
    private int coderId;//此次提交的用户的id
    private int questionId;//对应题目ID
    private State[] states;
    private String dir;

    public State[] getStates() {
        return states;
    }

    public void setStates(State[] states) {
        this.states = states;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCoderId() {
        return coderId;
    }

    public void setCoderId(int coderId) {
        this.coderId = coderId;
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
