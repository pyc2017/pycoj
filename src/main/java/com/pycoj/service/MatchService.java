package com.pycoj.service;

import com.google.gson.Gson;
import com.pycoj.dao.MatchDao;
import com.pycoj.dao.MatchSubmitDao;
import com.pycoj.entity.Match;
import com.pycoj.entity.RankObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 潘毅烦 on 2017/8/6.
 */
@Service
public class MatchService {
    @Autowired private MatchDao matchDao;
    @Autowired private MatchSubmitDao matchSubmitDao;
    @Autowired private StringRedisTemplate template;
    @Autowired private Gson gson;
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
        Match match=matchDao.selectMatchById(matchId);
        /*正在比赛中，则查询redis*/
        if (match.getEndTime().getTime()>System.currentTimeMillis()){
            List<byte[]> result=template.execute(new RedisCallback<List<byte[]>>() {
                public List<byte[]> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    RedisSerializer<String> serializer=template.getStringSerializer();
                    byte[] key=serializer.serialize(String.valueOf(matchId));
                    List<byte[]> list=redisConnection.lRange(key,0,Long.MAX_VALUE);
                    return list;
                }
            });
            List<RankObject> list=new ArrayList<>(result.size()+1);
            for (byte[] bytes:result){
                RankObject ro=gson.fromJson(new String(bytes),RankObject.class);
                list.add(ro);
            }
            RankObject matchInfo=new RankObject();
            matchInfo.setSubmitTime(match.getStartTime());
            list.add(matchInfo);
            return list;
        }else{
            return matchSubmitDao.selectMatchSubmitsByMatchId(matchId);
        }
    }
}
