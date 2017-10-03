package com.pycoj.controller;

import com.pycoj.service.CoderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by Heyman on 2017/4/30.
 */
@Controller
public class IndexController {
    @Autowired @Qualifier("coderService") private CoderService service;

    @RequestMapping(value = "/index/",method = RequestMethod.GET)
    public String index(){
        return "index";
    }

    //测试阶段使用，发布应该删除
    @RequestMapping(value="/register2/",method=RequestMethod.GET)
    public String register(){
        return "register2";
    }

    @RequestMapping(value = "/logout/",method = RequestMethod.GET)
    public String logout(HttpSession session){
        session.removeAttribute("coder");
        session.removeAttribute("currentMatch");
        return "redirect:/index/";
    }

    @RequestMapping(value = "/match",method = RequestMethod.GET)
    public String match(HttpSession session){
        byte[] bytes= (byte[]) session.getAttribute("currentMatch");
        if (bytes==null||bytes.length==0) {
            return "match_entry";
        }else{
            Integer id=Integer.valueOf(new String(bytes));
            return "redirect:/match/index?match="+id;
        }
    }
}