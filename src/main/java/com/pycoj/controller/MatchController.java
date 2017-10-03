package com.pycoj.controller;

import com.pycoj.entity.*;
import com.pycoj.service.MatchService;
import com.pycoj.util.MyUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by 潘毅烦 on 2017/8/6.
 */
@Controller
public class MatchController {
    private static final Logger log=Logger.getLogger(MatchController.class);
    @Autowired private MatchService mService;

    /**
     * 根据比赛id以及密码进入某一个比赛中，需要向cookie中添加该比赛的允许证明
     * @param id
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "/match",method = RequestMethod.POST)
    @ResponseBody
    public Dto<Match> match(@RequestParam("matchId")int id,
                            @RequestParam("matchPassword")String password,
                            HttpSession session){
        Coder coder= MyUtil.getCurrentCoder(session);
        if (coder==null){
            return new Dto<>(null,false,"access denied");
        }
        Match match;
        if ((match=mService.getIn(id,password))==null){
            return new Dto<>(null,false,"denied");
        }else{
            match.setPassword(null);
            session.setAttribute("currentMatch",id);
            return new Dto<>(match,true,"");
        }
    }

    /**
     * 新建一个比赛
     * @param password
     * @param startTime
     * @param endTime
     * @param name
     * @param session
     * @return
     */
    @RequestMapping(value = "/match/new",method = RequestMethod.POST)
    @ResponseBody
    public Dto<Match> newMatch(@RequestParam("matchPassword")String password,
                               @RequestParam("startTime")long startTime,
                               @RequestParam("endTime")long endTime,
                               @RequestParam("matchName")String name,
                               HttpSession session){
        Coder coder= MyUtil.getCurrentCoder(session);
        if (coder==null){
            return new Dto<>(null,false,"access denied");
        }
        Match match=new Match();
        match.setCreator(coder.getId());
        match.setEndTime(new Timestamp(endTime));
        match.setStartTime(new Timestamp(startTime));
        match.setPassword(password);
        match.setName(name);
        mService.saveMatch(match);
        return new Dto<>(match,true,"");
    }

    /**
     * 进入到比赛的页面中去
     * @param id 比赛id
     * @param session
     * @return
     */
    @RequestMapping(value = "/match/index",method = RequestMethod.GET)
    public String match(@RequestParam("match") int id,
                        HttpSession session){
        byte[] bytes= (byte[]) session.getAttribute("currentMatch");
        if (bytes==null||bytes.length==0){
            return "match_entry";
        }
        Integer current=Integer.valueOf(new String(bytes));
        if (current==null||current!=id){
            return "match_entry";
        }
        return "match";
    }

    /**
     * 判断是否该用户是否为当前比赛的创建者
     * @param session
     * @param id match id
     * @return
     */
    @RequestMapping(value="/match/checkCreator",method = RequestMethod.GET)
    @ResponseBody
    public boolean checkCreator(HttpSession session,
                                @RequestParam("id")int id){
        Coder coder= MyUtil.getCurrentCoder(session);
        if (coder==null){
            return false;
        }
        return mService.checkCreator(id,coder.getId());
    }

    @RequestMapping(value = "/match/exit",method = RequestMethod.GET)
    public String exitMatch(HttpSession session){
        session.removeAttribute("currentMatch");
        return "redirect:/index/";
    }

    /**
     * 获取比赛的排名
     * @param matchId
     * @return
     */
    @RequestMapping(value = "/match/rank",method = RequestMethod.GET)
    @ResponseBody
    public Dto<List<RankObject>> rank(@RequestParam("mid")int matchId){
        return new Dto<>(mService.getRankResult(matchId),true,"");
    }
}
