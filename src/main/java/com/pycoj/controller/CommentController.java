package com.pycoj.controller;

import com.pycoj.entity.Coder;
import com.pycoj.entity.Comment;
import com.pycoj.entity.Dto;
import com.pycoj.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by 潘毅烦 on 2017/7/26.
 */
@Controller
public class CommentController {
    @Autowired private CommentService cService;

    @RequestMapping(value = "/comment/{questionId}",method = RequestMethod.POST)
    @ResponseBody
    public Dto<Comment> comment(@PathVariable int questionId,
                                @RequestParam("content")String content,
                       HttpSession session){
        Coder coder=(Coder)session.getAttribute("coder");
        if (coder==null){
            return new Dto<>(null,false,"access denied");
        }
        Comment comment=new Comment();
        comment.setContent(content);
        comment.setQuestionId(questionId);
        comment.setCoderId(coder.getId());
        if (cService.saveComment(comment)==true) {
            return new Dto<>(comment, true, "");
        }else{
            return new Dto<>(comment,false,"unknowed error");
        }
    }

    @RequestMapping(value = "/comment/get/{id}",method = RequestMethod.GET)
    @ResponseBody
    public Dto<List<Comment>> getComment(@PathVariable int id){
        return new Dto<>(cService.getComments(id),true,"");
    }
}
