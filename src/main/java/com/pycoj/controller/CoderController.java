package com.pycoj.controller;

import com.pycoj.entity.Coder;
import com.pycoj.service.EmailService;
import com.pycoj.service.CoderService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Heyman on 2017/5/16.
 */
@Controller
public class CoderController {
    private Logger log=Logger.getLogger(CoderController.class);
    @Autowired @Qualifier("emailService") private EmailService eService;
    @Autowired @Qualifier("coderService") private CoderService service;

    /**
     * 注册第一步，先输入邮箱，验证邮箱再进行下一步
     * @return
     */
    @RequestMapping(value="/register_email/",method = RequestMethod.GET)
    public String register1(){
        return "register1";
    }

    @RequestMapping(value="/register/send/",method = RequestMethod.POST)
    @ResponseBody
    public void sendRegisterToken(@RequestParam("email")String email){
        //发送token地址到注册邮箱中
        eService.send(email);
    }

    /**
     * 注册第二步，从邮箱进入网站，输入用户名、密码
     * @return
     */
    @RequestMapping(value="/register/",method = RequestMethod.GET)
    public String register2(@RequestParam("email") String email,
                            @RequestParam("token") String token){
        //验证token
        if (eService.checkToken(email,token)) {
            return "register2";
        }else{
            //token过期或不存在，需要重新注册
            return "register1";
        }
    }

    /**
     * 检测用户名是否重复
     * @param username
     * @param response
     */
    @RequestMapping(value="/register/checku/",method = RequestMethod.POST)
    @ResponseBody
    public void checkUsername(@RequestParam("oj_username")String username,
                              HttpServletResponse response){
        PrintWriter out=null;
        response.setContentType("application/text;charset=UTF-8");
        try {
            out=response.getWriter();
            if (service.checkUsernameExist(username)){
                out.write("true");
            }else{
                out.write("false");
            }
        } catch (IOException e) {
        }finally {
            out.close();
        }
    }

    @RequestMapping(value="/register/",method=RequestMethod.POST)
    @ResponseBody
    public String doRegister(
            Coder coder,
            HttpServletRequest request){
        if (service.register(coder)){
            //成功并设置session
            HttpSession session=request.getSession();
            session.setAttribute("coder", coder);
            return "success";
        }else{
            return "fail";
        }
    }

    /**
     * 进入登录页面
     * @return
     */
    @RequestMapping(value="/login/",method = RequestMethod.GET)
    public String login(){
        return "login";
    }

    /**
     * 实现登录
     * @return
     */
    @RequestMapping(value="/login/",method = RequestMethod.POST)
    @ResponseBody
    public String doLogin(
            Coder coder,
            HttpServletRequest request){
        HttpSession session=request.getSession();
        if (service.login(coder)){
            session.setAttribute("coder", coder);
            return "success";
        }else{
            return "fail";
        }
    }
}
