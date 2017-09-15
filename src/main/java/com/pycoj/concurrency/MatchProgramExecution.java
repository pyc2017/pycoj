package com.pycoj.concurrency;

import com.pycoj.dao.MatchDao;
import com.pycoj.dao.MatchSubmitDao;
import com.pycoj.entity.Match;
import com.pycoj.entity.MatchSubmit;
import com.pycoj.entity.State;
import com.pycoj.entity.program.Program;
import com.pycoj.websocket.handler.MatchHandler;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class MatchProgramExecution implements Runnable {
    private static Logger log=Logger.getLogger(MatchProgramExecution.class);
    private File codeDirPrefix;
    private String codeDir;
    private File questionDir;
    private Program program;
    private MatchSubmit submitInfo;
    private StringRedisTemplate template;
    private MatchSubmitDao dao;

    public MatchProgramExecution(File codeDirPrefix, String codeDir, File questionDir,
                                 Program program, MatchSubmit info, StringRedisTemplate template,
                                 MatchSubmitDao dao) {
        this.codeDirPrefix = codeDirPrefix;
        this.codeDir = codeDir;
        this.questionDir = questionDir;
        this.program = program;
        this.submitInfo = info;
        this.template = template;
        this.dao=dao;
    }

    @Override
    public void run() {
        try{
            /**
             * websocket session对象，发送内容：
             * state.toString() 发送state的内容，前端根据state的状态码判断状态
             */
            WebSocketSession session= MatchHandler.getMap().get(submitInfo.getMatchId()).get(submitInfo.getCoderId());//获取对应session
            int questionId=submitInfo.getQuestionId();
            State compileResult=program.compile(new File(new File(codeDirPrefix, String.valueOf(questionId)),codeDir));//prefix / id / dir
            if (compileResult.getState()==0) {//编译成功
                //执行程序
                State[] states=program.run(codeDirPrefix.getAbsolutePath()+"/"+questionId+"/"+codeDir,questionDir.getAbsolutePath(),-questionId,session,false);
                //遍历返回的结果集，设置submitInfo相关信息
                for (State s:states){
                    if (s.getState()!=0){
                        submitInfo.setAc((short) s.getState());
                        break;
                    }
                }
            }else{//编译失败
                submitInfo.setAc((short) 1);
            }
            session.sendMessage(new TextMessage(submitInfo.toString()));
            //持久化本次解决方案的运行信息
            //dao.save(submitInfo);
            //比赛期间，修改为存储在redis中
            if (submitInfo.getAc()==0){
                //先将ac的信息保存到数据库中，方便查询此题目是否已经ac
                dao.save(submitInfo);
            }
            /**
             * 存储到redis中
             */
            template.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    log.info("saving in redis");
                    RedisSerializer<String> serializer=template.getStringSerializer();
                    byte[] key=serializer.serialize(/*"match"+*/String.valueOf(submitInfo.getMatchId()));
                    byte[] value=serializer.serialize(submitInfo.toRedisString());
                    redisConnection.lPush(key,value);
                    return true;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
