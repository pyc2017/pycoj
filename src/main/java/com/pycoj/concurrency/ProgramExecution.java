package com.pycoj.concurrency;

import com.pycoj.dao.CoderDao;
import com.pycoj.dao.QuestionDao;
import com.pycoj.dao.SubmitDao;
import com.pycoj.entity.State;
import com.pycoj.entity.Submit;
import com.pycoj.service.abstracts.Program;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Heyman on 2017/6/14.
 */
public class ProgramExecution implements Runnable {
    private String codeDirPrefix;
    private String codeDir;
    private String questionDir;
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
    public ProgramExecution(String codeDirPrefix, String codeDir, String questionDir, int id, Program program, SubmitDao submitDao, CoderDao coderDao, QuestionDao questionDao, Submit submitInfo) {
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
            State compileResult=program.compile(new File(codeDirPrefix+id+"/"+codeDir));//prefix / id / dir
            submitDao.saveSubmit(submitInfo);
            if (compileResult.getState()==0) {//编译成功
                submitInfo.setStates(program.run(codeDirPrefix+id+"/"+codeDir,questionDir,id));
            }else{//编译失败
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
        } catch (Exception e) {
        }
    }
}
