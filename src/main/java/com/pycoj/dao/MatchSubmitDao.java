package com.pycoj.dao;

import com.pycoj.entity.MatchSubmit;
import com.pycoj.entity.RankObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchSubmitDao {
    public boolean save(@Param("matchSubmit")MatchSubmit matchSubmit);

    public List<RankObject> selectMatchSubmitsByMatchId(@Param("matchId")int matchId);

    /**
     * 保存list，用于redis转移数据到mysql
     * @param list
     * @return
     */
    public boolean saveList(@Param("list")List<MatchSubmit> list);
}
