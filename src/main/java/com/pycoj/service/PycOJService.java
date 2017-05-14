package com.pycoj.service;

import com.pycoj.entity.Question;
import com.pycoj.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created by Heyman on 2017/5/1.
 */
public interface PycOJService {
    public boolean checkUsernameExist(String username);
    public boolean register(User user);
    public boolean login(User user);
    public List<Question> showQuestions(int page);
    public void updateQuestionState();
    public Question showQuestion(int id);
    public boolean newQuestion(Question question, HttpServletRequest request) throws IOException;
    public String saveSolution(int id,String code,String suffix,int userID);
    public void runSolution(int id);
}
