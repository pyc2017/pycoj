package com.pycoj.entity;

import java.sql.Timestamp;

/**
 * Created by 潘毅烦 on 2017/8/12.
 */
public class MatchSubmit {
    private int id;
    private int coderId;
    private int questionId;
    private short ac;//0成功，1编译不成功，2超时，3超内存，4输出不正确
    private Timestamp submitTime;
    private int matchId;

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
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
