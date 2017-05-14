package com.pycoj.controller;

import com.pycoj.entity.Question;
import com.pycoj.entity.QuestionState;
import com.pycoj.entity.User;
import com.pycoj.service.EmailService;
import com.pycoj.service.PycOJService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by Heyman on 2017/4/30.
 */
@Controller
public class IndexController {
    private static Logger logger=Logger.getLogger(IndexController.class);
    @Autowired
    @Qualifier("serviceImpl")
    private PycOJService service;
    @Autowired
    @Qualifier("emailService")
    private EmailService eService;
    @Autowired
    private User user;
    @Autowired
    private QuestionState state;
    @Autowired
    private Question question;

    @RequestMapping(value = "/index/",method = RequestMethod.GET)
    public String index(){
        logger.info("test logger.info()");
        return "index";
    }

    //测试阶段使用，发布应该删除
    @RequestMapping(value="/register2/",method=RequestMethod.GET)
    public String register(){
        return "register2";
    }

    /**
     * 注册第一步，先输入邮箱，验证邮箱再进行下一步
     * @return
     */
    @RequestMapping(value="/register_email/",method = RequestMethod.GET)
    public String register1(){
        return "register1";
    }

    @RequestMapping(value="/register/send/",method = RequestMethod.POST)
    @ResponseBody
    public void sendRegisterToken(@RequestParam("email")String email){
        //发送token地址到注册邮箱中
        eService.send(email);
    }

    /**
     * 注册第二步，从邮箱进入网站，输入用户名、密码
     * @return
     */
    @RequestMapping(value="/register/",method = RequestMethod.GET)
    public String register2(@RequestParam("email") String email,
                             @RequestParam("token") String token){
        //验证token
        if (eService.checkToken(email,token)) {
            return "register2";
        }else{
            //token过期或不存在，需要重新注册
            return "register1";
        }
    }

    /**
     * 检测用户名是否重复
     * @param username
     * @param response
     */
    @RequestMapping(value="/register/checku/",method = RequestMethod.POST)
    @ResponseBody
    public void checkUsername(@RequestParam("oj_username")String username,
                              HttpServletResponse response){
        PrintWriter out=null;
        response.setContentType("application/text;charset=UTF-8");
        try {
            out=response.getWriter();
            if (service.checkUsernameExist(username)){
                out.write("true");
            }else{
                out.write("false");
            }
        } catch (IOException e) {
        }finally {
            out.close();
        }
    }

    @RequestMapping(value="/register/",method=RequestMethod.POST)
    @ResponseBody
    public String doRegister(@RequestParam("oj_username")String username,
                             @RequestParam("oj_password")String password,
                             @RequestParam("oj_email")String email,
                             HttpServletRequest request){
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        if (service.register(user)){
            //成功并设置session
            HttpSession session=request.getSession();
            session.setAttribute("user",user);
            return "success";
        }else{
            return "fail";
        }
    }

    /**
     * 进入登录页面
     * @return
     */
    @RequestMapping(value="/login/",method = RequestMethod.GET)
    public String login(){
        return "login";
    }

    /**
     * 实现登录
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value="/login/",method = RequestMethod.POST)
    @ResponseBody
    public String doLogin(@RequestParam("oj_username")String username,
                           @RequestParam("oj_password")String password,
                           HttpServletRequest request){
        HttpSession session=request.getSession();
        user.setUsername(username);
        user.setPassword(password);
        if (service.login(user)){
            session.setAttribute("user",user);
            return "success";
        }else{
            return "fail";
        }
    }

    /**
     * 根据页码返回对应的题目
     * @param page
     * @return
     */
    @RequestMapping(value="/search/",method = RequestMethod.GET)
    @ResponseBody
    public List<Question> showQuestions(@RequestParam("page")int page){
        return service.showQuestions(page);
    }

    /**
     * 获取问题数量，页码问题在前端使用jquery解决
     * @return
     */
    @RequestMapping(value="/getpageamount/",method = RequestMethod.GET)
    @ResponseBody
    public int getPageAmount(){
        if (state.getAmount()==-1){
            service.updateQuestionState();
        }
        return state.getAmount();
    }

    @RequestMapping(value="/question_detail/{id}",method = RequestMethod.GET)
    public String getQuestionDetail(@PathVariable int id,
                                    Model model){
        model.addAttribute("question",service.showQuestion(id));
        return "detail";
    }

    @RequestMapping(value="/new/",method = RequestMethod.GET)
    public String newQuestion(){
        return "newquestion";
    }

    @RequestMapping(value="/new/",method = RequestMethod.POST)
    public String newQuestion(@RequestParam("title")String title,
                               @RequestParam("description")String description,
                               @RequestParam("input")String input,
                               @RequestParam("output")String output,
                               @RequestParam("hint")String hint,
                               HttpServletRequest request) throws IOException {
        question.setDescription(description);
        question.setTitle(title);
        question.setHint(hint);
        question.setInput(input);
        question.setOutput(output);
        service.newQuestion(question,request);
        return "index";
    }

    private static final String[] suffix={".c",".java"};
    @RequestMapping(value="/submit/{id}/",method = RequestMethod.POST)
    public String userSolveQuestion(@RequestParam("code")String code,
                                     @RequestParam("lang")int lang,
                                     @PathVariable int id,
                                     HttpServletRequest request){
        HttpSession session=request.getSession();
        user=(User)session.getAttribute("user");
        /*存储文件*/
        String fileName=service.saveSolution(id,code,suffix[lang],user.getId());
        /*运行文件*/
        return "redirect:/question_detail/"+id;
    }
}