package com.pycoj.service;

/**
 * Created by Heyman on 2017/5/1.
 */
public interface EmailService {
    public boolean send(String to);
    public boolean checkToken(String email,String token);
}
