package com.pycoj.concurrency;

import com.pycoj.dao.MatchSubmitDao;
import com.pycoj.entity.MatchSubmit;
import com.pycoj.entity.State;
import com.pycoj.service.abstracts.Program;
import com.pycoj.websocket.handler.MatchHandler;
import com.pycoj.websocket.handler.SolutionHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.File;
import java.io.IOException;

public class MatchProgramExecution implements Runnable {
    private File codeDirPrefix;
    private String codeDir;
    private File questionDir;
    private int id;//question id
    private Program program;
    private MatchSubmit submitInfo;
    private MatchSubmitDao dao;

    public MatchProgramExecution(File codeDirPrefix, String codeDir, File questionDir, int id, Program program, MatchSubmit info, MatchSubmitDao dao) {
        this.codeDirPrefix = codeDirPrefix;
        this.codeDir = codeDir;
        this.questionDir = questionDir;
        this.id = id;
        this.program = program;
        this.submitInfo = info;
        this.dao = dao;
    }

    @Override
    public void run() {
        try{
            /**
             * websocket session对象，发送内容：
             * state.toString() 发送state的内容，前端根据state的状态码判断状态
             */
            WebSocketSession session= MatchHandler.getMap().get(submitInfo.getMatchId()).get(submitInfo.getCoderId());//获取对应session
            State compileResult=program.compile(new File(new File(codeDirPrefix, String.valueOf(id)),codeDir));//prefix / id / dir
            if (compileResult.getState()==0) {//编译成功
                //执行程序
                State[] states=program.run(codeDirPrefix.getAbsolutePath()+"/"+id+"/"+codeDir,questionDir.getAbsolutePath(),id,session,false);
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
            dao.save(submitInfo);
        } catch (IOException e) {
        } catch (Exception e) {
        }
    }
}
