package com.pycoj.concurrency;

import com.pycoj.dao.CoderDao;
import com.pycoj.dao.QuestionDao;
import com.pycoj.dao.SubmitDao;
import com.pycoj.entity.State;
import com.pycoj.entity.Submit;
import com.pycoj.service.abstracts.Program;
import com.pycoj.websocket.handler.SolutionHandler;
import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Heyman on 2017/6/14.
 */
public class ProgramExecution implements Runnable {
    private static final Logger log=Logger.getLogger(ProgramExecution.class);
    private File codeDirPrefix;
    private String codeDir;
    private File questionDir;
    private int id;//question id
    private Program program;
    private SubmitDao submitDao;
    private CoderDao coderDao;
    private Submit submitInfo;
    private QuestionDao questionDao;

    /**
     *
     * @param codeDirPrefix 代码路径前缀，譬如F:\ojprogram\
     * @param codeDir 代码生成的随机字符串
     * @param questionDir 题目的路径，F:\ojquestion\
     * @param id 题目id
     * @param program 题目接口
     * @param submitDao 持久层接口
     * @param submitInfo 提交用户id
     */
    public ProgramExecution(File codeDirPrefix, String codeDir, File questionDir, int id, Program program, SubmitDao submitDao, CoderDao coderDao, QuestionDao questionDao, Submit submitInfo) {
        this.codeDirPrefix = codeDirPrefix;
        this.codeDir=codeDir;
        this.questionDir = questionDir;
        this.id = id;
        this.program = program;
        this.submitDao=submitDao;
        this.coderDao=coderDao;
        this.questionDao=questionDao;
        this.submitInfo=submitInfo;
    }

    public void run() {
        try {
            /**
             * websocket session对象，发送内容：
             * state.toString() 发送state的内容，前端根据state的状态码判断状态
             */
            WebSocketSession session= (WebSocketSession) SolutionHandler.getMap().get(submitInfo.getCoderId());//获取对应session
            State compileResult=program.compile(new File(new File(codeDirPrefix, String.valueOf(id)),codeDir));//prefix / id / dir
            if (compileResult.getState()==0) {//编译成功
                submitInfo.setStates(program.run(codeDirPrefix.getAbsolutePath()+"/"+id+"/"+codeDir,questionDir.getAbsolutePath(),id,session,true));
            }else{//编译失败
                if (session.isOpen())
                    session.sendMessage(new TextMessage(compileResult.toString()));
                submitInfo.setStates(new State[]{compileResult});
            }
            //查看是否全AC
            State[] stateArray=submitInfo.getStates();
            boolean success=true;
            for (State s:stateArray){
                if (s.getState()!=0){
                    success=false;
                    break;
                }
            }
            submitInfo.setAc(success);
            submitDao.saveSubmit(submitInfo);
            if (success){//全部用例都AC了
                int submitCount=submitDao.selectCountSubmitByCoderIdAndQuestionId(submitInfo.getCoderId(),id);
                if (submitCount==0){
                    //第一次ac，添加ac次数，避免用户在同一问题上多次提交正确代码，恶意提高自身的ac数
                    coderDao.updateACCount(submitInfo.getCoderId());
                    //同时，添加该题目的完成数量
                    questionDao.updateAddSubmit(id);
                }
            }
            submitDao.saveState(submitInfo);
            //服务器端主动关闭此次连接
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
