package com.pycoj.service;

import com.pycoj.dao.CommentDao;
import com.pycoj.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by 潘毅烦 on 2017/7/26.
 */
@Service
public class CommentService {
    @Autowired private CommentDao commentDao;
    public boolean saveComment(Comment comment){
        if (commentDao.save(comment)){
            comment.setCreateTime(new Timestamp(System.currentTimeMillis()));
            return true;
        }else{
            return false;
        }
    }

    public List<Comment> getComments(int id){
        return commentDao.selectCommentsByQuestionId(id);
    }
}
