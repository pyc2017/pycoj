package com.pycoj.service;

import com.pycoj.dao.QuestionDao;
import com.pycoj.entity.Question;
import com.pycoj.entity.QuestionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Heyman on 2017/5/17.
 */
@Service("qService")
public class QuestionService {
    @Autowired private QuestionDao questionDao;
    @Autowired private QuestionState state;
    /**
     * 选择某一页的问题
     * @param page
     * @return
     */
    public List<Question> showQuestions(int page) {
        return questionDao.selectQuestions((page-1)*20);
    }

    /**
     * 更新问题数量
     */
    public void updateQuestionState() {
        synchronized (state) {
            state.setAmount(questionDao.selectAmountOfQuestions());
        }
    }

    public Question showQuestion(int id) {
        return questionDao.selectQuestionByID(id);
    }


    private static String inAndOut="F:\\ojquestion\\";
    /**
     * 新建问题✔
     * @param question
     * @return
     */
    public boolean newQuestion(Question question, HttpServletRequest request) throws IOException {
        /*****将\r\n换成<p></p>***/
        String temp;
        temp=question.getDescription().replaceAll("\r\n","</p><p>");
        question.setDescription(temp);
        temp=question.getHint().replaceAll("\r\n","</p><p>");
        question.setHint(temp);
        temp=question.getInput().replaceAll("\r\n","</p><p>");
        question.setInput(temp);
        temp=question.getOutput().replaceAll("\r\n","</p><p>");
        question.setOutput(temp);
        questionDao.save(question);
        /*更新状态，需要获得state的锁*/
        synchronized (state){
            int amount=state.getAmount();
            state.setAmount(amount+1);
        }
        /*****************保存上传的输入输出*****************/
        CommonsMultipartResolver resolver=new CommonsMultipartResolver(request.getSession().getServletContext());
        if (resolver.isMultipart(request)){
            //将HttpServletRequest转换成MultipartHttpServletRequest，应该在Controller里面转换并得到文件，将文件传到service处理，迟点改!!!!
            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
            Iterator<String> iter=multiRequest.getFileNames();
            while (iter.hasNext()){
                //根据文件名获取文件信息
                MultipartFile file=multiRequest.getFile(iter.next());
                if (file!=null){
                    File dir=new File(inAndOut+question.getId());
                    if (!dir.exists()){
                        dir.mkdir();
                    }
                    File newFile=new File(dir,"input");
                    if (!newFile.exists()){
                        newFile.createNewFile();
                    }else{
                        newFile=new File(dir,"output");
                        newFile.createNewFile();
                    }
                    file.transferTo(newFile);
                }
            }
        }
        return true;
    }
}
