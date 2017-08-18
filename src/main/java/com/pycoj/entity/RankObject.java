package com.pycoj.entity;

import java.sql.Timestamp;

/**
 * 作为查询某一个比赛的时候的实体
 */
public class RankObject {
    private String username;//coder nickname
    private int questionId;
    private short ac;
    private Timestamp submitTime;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public short getAc() {
        return ac;
    }

    public void setAc(short ac) {
        this.ac = ac;
    }

    public Timestamp getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Timestamp submitTime) {
        this.submitTime = submitTime;
    }
}
