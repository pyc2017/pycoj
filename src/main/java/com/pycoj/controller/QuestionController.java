package com.pycoj.controller;

import com.pycoj.entity.*;
import com.pycoj.service.MatchService;
import com.pycoj.service.QuestionService;
import com.pycoj.util.MyUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Created by Heyman on 2017/5/17.
 */
@Controller
public class QuestionController {
    private static Logger log=Logger.getLogger(QuestionController.class);

    @Autowired private QuestionState state;
    @Autowired @Qualifier("qService") private QuestionService service;
    @Autowired private MatchService mService;

    /**
     * 根据页码返回对应的题目
     * @param page
     * @return
     */
    @RequestMapping(value="/search/",method = RequestMethod.GET)
    @ResponseBody
    public List<Question> showQuestions(@RequestParam("page")int page){
        return service.showQuestions(page);
    }

    /**
     * 获取问题数量，页码问题在前端使用jquery解决
     * @return
     */
    @RequestMapping(value="/getpageamount/",method = RequestMethod.GET)
    @ResponseBody
    public int getPageAmount(){
        if (state.getAmount()==-1){
            service.updateQuestionState();
        }
        return state.getAmount();
    }

    /**
     * 进入题目
     * @param id 题目id
     * @param model
     * @return
     */
    @RequestMapping(value="/question_detail/{id}",method = RequestMethod.GET)
    public String getQuestionDetail(@PathVariable int id,
                                    Model model){
        model.addAttribute("question",service.showQuestion(id));
        return "question/detail";
    }

    /**
     * 进入新建题目的页面
     * @return
     */
    @RequestMapping(value="/new/",method = RequestMethod.GET)
    public String newQuestion(){
        return "question/new";
    }

    /**
     * 执行新建题目
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/new/",method = RequestMethod.POST)
    public String newQuestion(Question _q,
                              @RequestPart("zip") MultipartFile zip) throws IOException {
        log.info("{ Admin start creating question:\ntitile:"+_q.getTitle()+" description:"+_q.getDescription()+"\ninput file:"+zip.getOriginalFilename()+"}");
        service.newQuestion(_q,zip);
        return "index";
    }

    /**
     * 向比赛中新建题目
     * @param _q
     * @param zip
     * @param session
     * @return
     */
    @RequestMapping(value = "/newMatchQuestion",method = RequestMethod.POST)
    public String newMatchQuestion(MatchQuestion _q,
                                   @RequestParam("zip")MultipartFile zip,
                                   HttpSession session) throws IOException {
        Coder coder= MyUtil.getCurrentCoder(session);
        if (coder==null || !mService.checkCreator(_q.getMatchId(),coder.getId())){
            return "redirect:/login/";
        }
        service.newMatchQuestion(_q,zip);
        return "redirect:/match/index?match="+_q.getMatchId();
    }

    @RequestMapping(value = "/match/question",method = RequestMethod.GET)
    @ResponseBody
    public Dto<List<MatchQuestion>> getMatchQuestions(@RequestParam("m")int id,
                                                      HttpSession session){
        byte[] bytes= (byte[]) session.getAttribute("currentMatch");
        if (bytes==null||bytes.length==0) return new Dto(null,false,"access denied");
        Integer current=Integer.valueOf(new String(bytes));
        if (current==null||current!=id){
            return new Dto(null,false,"access denied");
        }else{
            return service.showMatchQuestions(id);
        }
    }

    /**
     * 用户查询自己在本次比赛中已完成的题目
     * @param matchId
     * @param session
     * @return
     */
    @RequestMapping(value = "/match/solved",method = RequestMethod.GET)
    @ResponseBody
    public Dto<Integer[]> coderGetSolvedQuestionList(@RequestParam("mid")int matchId,
                                                HttpSession session){
        Coder coder= (Coder) session.getAttribute("coder");
        if (coder==null){
            return new Dto<>(null,false,"access denied");
        }
        return new Dto(service.getSolvedQuestionsInMatch(matchId,coder.getId()),true,"");
    }
}