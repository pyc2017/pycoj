package com.pycoj.concurrency;

import com.google.gson.Gson;
import com.pycoj.dao.MatchSubmitDao;
import com.pycoj.entity.Match;
import com.pycoj.entity.MatchSubmit;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 检测到比赛结束后，将数据从redis转移到MySQL
 */
public class DataTransfer implements Runnable{
    private static Logger log=Logger.getLogger(DataTransfer.class);
    private static long HOUR=3600000;
    private static Gson gson=new Gson();
    private Set<Match> set;
    private MatchSubmitDao dao;
    private StringRedisTemplate template;
    public DataTransfer(Set set, MatchSubmitDao dao, StringRedisTemplate template){
        this.set=set;
        this.dao=dao;
        this.template=template;
    }

    @Override
    public void run() {
        while (true){
            /**
             * set中没有对象，表示当前没有比赛进行，不需要轮询set中对象
             */
            if (set.size()==0){
                synchronized (set){
                    if (set.size()==0) {//双重验证，避免notify先于wait执行，即刚验证完第一个size==0的情况下，另一个线程被CPU调用，然后已经调用了notify，比wait更早
                        try {
                            log.info("waiting for notify");
                            set.wait();
                            log.info("someone notify or awake");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }else{
                /**
                 * 进行轮询
                 */
                long now=System.currentTimeMillis();
                long min=Long.MAX_VALUE;//保存最早一个要结束的比赛的时间
                synchronized (set) {
                    Iterator<Match> iterator = set.iterator();
                    while (iterator.hasNext()) {
                        Match match = iterator.next();
                        long endTime = match.getEndTime().getTime();
                        if (endTime <= now) {
                            //删除当前结点
                            iterator.remove();
                            //从redis中将数据转移到mysql
                            log.info("data transferring...");
                            template.execute(new RedisCallback<Boolean>() {
                                public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                                    RedisSerializer<String> serializer = template.getStringSerializer();
                                    byte[] key = serializer.serialize(String.valueOf(match.getId()));
                                    int index = 0;
                                    while (true) {
                                        List<byte[]> list = redisConnection.lRange(key, index, index + 99);
                                        List<MatchSubmit> msList = new ArrayList<>(list.size());
                                        for (byte[] bytes : list) {
                                            String objString = serializer.deserialize(bytes);
                                            MatchSubmit ms = gson.fromJson(objString, MatchSubmit.class);
                                            if (ms.getAc()==0) continue;//ac的数据已经在数据库中，不需要再次添加
                                            msList.add(ms);
                                        }
                                        dao.saveList(msList);
                                        if (list.size() < 100) break;
                                        else index += 100;
                                    }
                                    //删除redis中全部元素
                                    redisConnection.lTrim(key, Long.MAX_VALUE - 1, Long.MAX_VALUE);
                                    return true;
                                }
                            });
                        }
                        if (endTime < min) {
                            min = endTime;
                        }
                    }
                }
                //睡眠到结束的时间，然后转移
                if (min>now) {
                    try {
                        //最多挂起1小时
                        log.info("start sleeping");
                        Thread.sleep(min - now < HOUR ? min - now : HOUR);
                        log.info("awake...");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
