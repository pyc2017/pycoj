package com.pycoj.concurrency;

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
    private int coderId;

    /**
     *
     * @param codeDirPrefix 代码路径前缀，譬如F:\ojprogram\
     * @param codeDir 代码生成的随机字符串
     * @param questionDir 题目的路径，F:\ojquestion\
     * @param id 题目id
     * @param program 题目接口
     * @param submitDao 持久层接口
     * @param coderId 提交用户id
     */
    public ProgramExecution(String codeDirPrefix, String codeDir, String questionDir, int id, Program program, SubmitDao submitDao, int coderId) {
        this.codeDirPrefix = codeDirPrefix;
        this.codeDir=codeDir;
        this.questionDir = questionDir;
        this.id = id;
        this.program = program;
        this.submitDao=submitDao;
        this.coderId=coderId;
    }

    public void run() {
        try {
            State compileResult=program.compile(new File(codeDirPrefix+id+"/"+codeDir));//prefix / id / dir
            //设置完成信息
            Submit submitInfo=new Submit();
            submitInfo.setDir(codeDir);
            submitInfo.setQuestionId(id);
            //设置用户信息
            submitInfo.setCoderId(coderId);
            submitDao.saveSubmit(submitInfo);
            if (compileResult.getState()==0) {//编译成功
                submitInfo.setStates(program.run(codeDirPrefix+id+"/"+codeDir,questionDir,id));
            }else{//编译失败
                submitInfo.setStates(new State[]{compileResult});
            }
            submitDao.saveState(submitInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
