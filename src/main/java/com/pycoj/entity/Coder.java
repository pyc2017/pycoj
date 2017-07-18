package com.pycoj.entity;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by Heyman on 2017/4/30.
 */
@Component
public class Coder {
    private int id;
    private String password;
    private String email;
    private String username;
    private String headImage;
    private String nickname;
    private int acAmount;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAcAmount() {
        return acAmount;
    }

    public void setAcAmount(int acAmount) {
        this.acAmount = acAmount;
    }

    @Override
    public String toString(){
        return "{\"nickname\":\""+nickname+
                "\",\"headImage\":\""+headImage+
                "\",\"id\":\""+id+
                "\",\"ac\":\""+acAmount+
                "\"}";
    }
}