package com.pycoj.controller;

import com.pycoj.entity.Coder;
import com.pycoj.entity.Comment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by 潘毅烦 on 2017/7/26.
 */
@Controller
public class CommentController {
    @RequestMapping(value = "/comment/{questionId}/",method = RequestMethod.POST)
    @ResponseBody
    public boolean comment(@PathVariable int questionId,
                           @RequestParam("ojContent")String content,
                           HttpSession session){
        Coder coder= (Coder) session.getAttribute("coder");
        if (coder==null){//未登录
            return false;
        }
        Comment comment=new Comment();
        comment.setCoderId(coder.getId());
        comment.setContent(content);
        comment.setQuestionId(questionId);
        return true;
    }
}
