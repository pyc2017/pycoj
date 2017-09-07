package com.pycoj.controller;

import com.pycoj.entity.Coder;
import com.pycoj.entity.Dto;
import com.pycoj.entity.State;
import com.pycoj.entity.Submit;
import com.pycoj.service.SolutionService;
import com.pycoj.service.abstracts.CProgram;
import com.pycoj.service.abstracts.JavaProgram;
import com.pycoj.service.abstracts.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.File;

/**
 * Created by Heyman on 2017/5/17.
 */
@Controller
public class SolutionController {
    @Autowired @Qualifier("sService") SolutionService service;
    @Autowired Program[] programs;
    /**
     * 上传文件路径
     */
    @Autowired @Qualifier("program") private File filePrefix;
    @Autowired @Qualifier("questionDir") private File questionDir;
    @Autowired @Qualifier("matchProgramDir") private File matchProgramDir;
    @Autowired @Qualifier("matchQuestionDir") private File matchQuestionDir;

    /**
     *
     * @param code 提交代码
     * @param lang 语言类型
     * @param id 题目id
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/submit/{id}",method = RequestMethod.POST)
    @ResponseBody public boolean userSolveQuestion(@RequestParam("code")String code,
                                   @RequestParam("lang")int lang,
                                   @PathVariable int id,
                                   HttpSession session) throws Exception {
        Coder coder=(Coder) session.getAttribute("coder");
        if (coder==null){//需要重新返回
            return false;
        }
        Long lastUploadTime= (Long) session.getAttribute("lastUpload");
        if (lastUploadTime==null||System.currentTimeMillis()-lastUploadTime>15000) {
            /*保存这次上传的时间*/
            session.setAttribute("lastUpload",System.currentTimeMillis());
            /*存储文件*/
            String fileName = service.saveSolution(filePrefix, id, code, programs[lang]);
            /*运行文件*/
            service.runSolution(filePrefix, questionDir, id, fileName, programs[lang], coder.getId());
            return true;
        }else{
            /*短时间内不能多次上传解决方案*/
            return false;
        }
    }

    /**
     * coder查询最近一次提交的结果
     * @param id
     * @param session
     * @return
     */
    @RequestMapping(value = "/submit/ask/{id}",method = RequestMethod.GET)
    @ResponseBody public State[] coderQueryForTheLastSubmit(@PathVariable int id,
                                                            HttpSession session){
        Coder coder= (Coder) session.getAttribute("coder");
        return service.getStates(id,coder.getId());
    }

    /**
     * 用户在比赛阶段提交解决方案
     * @param code
     * @param lang
     * @param matchId
     * @param questionId
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/match/submit/{matchId}/{questionId}",method = RequestMethod.POST)
    @ResponseBody
    public Dto<Object> userSolveQuestion(@RequestParam("code")String code,
                                 @RequestParam("lang")int lang,
                                 @PathVariable int matchId,
                                 @PathVariable int questionId,
                                 HttpSession session) throws Exception {
        Coder coder=(Coder) session.getAttribute("coder");
        if (coder==null){//需要重新返回
            return new Dto<>(null,false,"access denied");
        }
        Integer currentMatchId= (Integer) session.getAttribute("currentMatch");
        /**
         * 对比赛、题目进行验证，防止没有比赛权限也可以提交代码
         */
        Dto validateResult=service.validateMatchInformation(currentMatchId,matchId,questionId);
        if (validateResult.isSuccess()==false){
            return validateResult;
        }
        Long lastUploadTime= (Long) session.getAttribute("lastUpload");
        if (lastUploadTime==null||System.currentTimeMillis()-lastUploadTime>15000) {
            /*保存这次上传的时间*/
            session.setAttribute("lastUpload", System.currentTimeMillis());
        /*存储文件*/
            String fileName = service.saveSolution(matchProgramDir, questionId, code, programs[lang]);
        /*运行文件*/
            service.runMatchSolution(matchProgramDir, matchQuestionDir, questionId, fileName, programs[lang], coder.getId(), matchId);
            return validateResult;
        }else {
            /*不允许短时间内大量上传解决方案*/
            validateResult.setSuccess(false);
            validateResult.setInfo("reject");
            return validateResult;
        }
    }
}