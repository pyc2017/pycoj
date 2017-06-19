package com.pycoj.controller;

import com.pycoj.entity.Coder;
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

/**
 * Created by Heyman on 2017/5/17.
 */
@Controller
public class SolutionController {
    @Autowired @Qualifier("sService") SolutionService service;
    private static final Program[] program={new CProgram(),new JavaProgram()};

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
    @ResponseBody public int  userSolveQuestion(@RequestParam("code")String code,
                                   @RequestParam("lang")int lang,
                                   @PathVariable int id,
                                   HttpSession session) throws Exception {
        Coder coder=(Coder) session.getAttribute("coder");
        if (coder==null){//需要重新返回
            return -1;
        }
        /*存储文件*/
        String fileName=service.saveSolution(id,code,program[lang]);
        /*运行文件*/
        service.runSolution(id,fileName,program[lang],coder.getId());
        return coder.getId();
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
}