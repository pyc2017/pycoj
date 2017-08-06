package com.pycoj.dao;

import com.pycoj.entity.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 潘毅烦 on 2017/7/26.
 */
@Repository
public interface CommentDao {
    public boolean save(@Param("comment")Comment comment);
    public List<Comment> selectCommentsByQuestionId(@Param("id")int id);
}
