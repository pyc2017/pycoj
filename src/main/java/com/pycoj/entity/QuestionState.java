package com.pycoj.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 题目的情况，减少对数据库的IO
 * Created by Heyman on 2017/5/5.
 */
@Component
public class QuestionState {
    @Value("-1")
    private int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
