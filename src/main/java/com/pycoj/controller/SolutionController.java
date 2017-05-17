package com.pycoj.controller;

import com.pycoj.entity.User;
import com.pycoj.service.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Heyman on 2017/5/17.
 */
@Controller
public class SolutionController {
    @Autowired @Qualifier("sService") SolutionService service;

    private static final String[] suffix={".c",".java"};
    @RequestMapping(value="/submit/{id}/",method = RequestMethod.POST)
    public String userSolveQuestion(@RequestParam("code")String code,
                                    @RequestParam("lang")int lang,
                                    @PathVariable int id,
                                    HttpServletRequest request){
        HttpSession session=request.getSession();
        User user=(User)session.getAttribute("user");
        /*存储文件*/
        String fileName=service.saveSolution(id,code,suffix[lang],user.getId());
        /*运行文件*/
        service.runSolution(id,fileName);
        return "redirect:/question_detail/"+id;
    }
}
