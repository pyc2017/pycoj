package com.pycoj.dao;

import com.pycoj.entity.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by 潘毅烦 on 2017/7/26.
 */
@Repository
public interface CommentDao {
    public boolean save(@Param("comment")Comment comment);
}
