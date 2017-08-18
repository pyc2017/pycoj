package com.pycoj.service;

import com.pycoj.dao.MatchDao;
import com.pycoj.dao.MatchSubmitDao;
import com.pycoj.entity.Match;
import com.pycoj.entity.RankObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 潘毅烦 on 2017/8/6.
 */
@Service
public class MatchService {
    @Autowired private MatchDao matchDao;
    @Autowired private MatchSubmitDao matchSubmitDao;
    public boolean saveMatch(Match match){
        return matchDao.save(match);
    }

    /**
     * 根据用户提供的比赛id以及比赛密码进入比赛
     * @param id
     * @param password
     * @return
     */
    public Match getIn(int id,String password){
        return matchDao.selectMatchByIdAndPassword(id,password);
    }

    public boolean checkCreator(int id,int coderId){
        Match match=matchDao.selectMatchById(id);
        if (match==null){
            return false;
        }else if (match.getCreator()!=coderId){
            return false;
        }
        return true;
    }

    public List<RankObject> getRankResult(int matchId){
        return matchSubmitDao.selectMatchSubmitsByMatchId(matchId);
    }
}
