package com.pycoj.controller;

import com.pycoj.entity.Question;
import com.pycoj.entity.QuestionState;
import com.pycoj.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created by Heyman on 2017/5/17.
 */
@Controller
public class QuestionController {
    @Autowired private QuestionState state;
    @Autowired private Question question;
    @Autowired @Qualifier("qService") private QuestionService service;

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
        return "detail";
    }

    /**
     * 进入新建题目的页面
     * @return
     */
    @RequestMapping(value="/new/",method = RequestMethod.GET)
    public String newQuestion(){
        return "newquestion";
    }

    /**
     * 执行新建题目
     * @param title
     * @param description
     * @param input
     * @param output
     * @param hint
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/new/",method = RequestMethod.POST)
    public String newQuestion(@RequestParam("title")String title,
                              @RequestParam("description")String description,
                              @RequestParam("input")String input,
                              @RequestParam("output")String output,
                              @RequestParam("hint")String hint,
                              HttpServletRequest request) throws IOException {
        question.setDescription(description);
        question.setTitle(title);
        question.setHint(hint);
        question.setInput(input);
        question.setOutput(output);
        service.newQuestion(question,request);
        return "index";
    }
}
