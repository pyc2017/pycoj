package com.pycoj.controller;

import com.pycoj.entity.State;
import com.pycoj.entity.Submit;
import com.pycoj.entity.User;
import com.pycoj.service.SolutionService;
import com.pycoj.service.abstracts.CProgram;
import com.pycoj.service.abstracts.JavaProgram;
import com.pycoj.service.abstracts.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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
    @ResponseBody public State userSolveQuestion(@RequestParam("code")String code,
                                   @RequestParam("lang")int lang,
                                   @PathVariable int id,
                                   HttpSession session) throws Exception {
        /*HttpSession session=request.getSession();
        User user=(User)session.getAttribute("user");*/
        /*存储文件*/
        String fileName=service.saveSolution(id,code,program[lang]);
        /*运行文件*/
        Submit s=service.runSolution(id,fileName,program[lang]);
        return s.getState();
    }
}