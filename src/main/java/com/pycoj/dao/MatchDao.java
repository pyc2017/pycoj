package com.pycoj.dao;

import com.pycoj.entity.Match;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by 潘毅烦 on 2017/8/6.
 */
@Repository
public interface MatchDao {
    public boolean save(@Param("match")Match match);
    public Match selectMatchByIdAndPassword(@Param("id")int id,@Param("password")String password);
    public Match selectMatchById(@Param("id")int id);
}
