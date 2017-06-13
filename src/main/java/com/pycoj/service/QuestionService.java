package com.pycoj.service;

import com.pycoj.dao.QuestionDao;
import com.pycoj.entity.Question;
import com.pycoj.entity.QuestionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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


    @Autowired @Qualifier("inAndOut") private String inAndOut;
    @Autowired @Qualifier("filePrefix") private String filePrefix;
    /**
     * 新建问题✔
     * @param question
     * @return
     */
    public boolean newQuestion(Question question, MultipartFile input,MultipartFile output) throws IOException {
        questionDao.save(question);
        /*更新状态，需要获得state的锁*/
        synchronized (state){
            int amount=state.getAmount();
            state.setAmount(amount+1);
        }
        /*****************保存上传的输入输出用例*****************/
        File realDocument=new File(inAndOut+question.getId());
        if (!realDocument.exists()){//新建目录
            realDocument.mkdir();
        }
        File realInputFile=new File(inAndOut+question.getId()+"\\input.txt");
        File realOutputFile=new File(inAndOut+question.getId()+"\\output.txt");
        File solutionDir=new File(filePrefix+question.getId());
        if (!realInputFile.exists()){//新建文件
            realInputFile.createNewFile();
        }
        if (!realOutputFile.exists()){//新建文件
            realOutputFile.createNewFile();
        }
        if (!solutionDir.exists()){
            solutionDir.mkdir();
        }
        input.transferTo(realInputFile);
        output.transferTo(realOutputFile);
        return true;
    }
}
