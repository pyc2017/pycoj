package com.pycoj.service;

import com.pycoj.dao.QuestionDao;
import com.pycoj.entity.Question;
import com.pycoj.entity.QuestionState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Heyman on 2017/5/17.
 */
@Service("qService")
public class QuestionService {
    private static Logger log= Logger.getLogger(QuestionService.class);
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


    @Autowired @Qualifier("questionDir") private String questionDirection;
    @Autowired @Qualifier("program") private String programDir;
    /**
     * 新建问题✔
     * @param question
     * @return
     */
    public boolean newQuestion(Question question, MultipartFile zip) throws IOException {
        questionDao.save(question);
        /*更新状态，需要获得state的锁*/
        synchronized (state){
            int amount=state.getAmount();
            state.setAmount(amount+1);
        }
        /*****************保存上传的输入输出用例*****************/
        File questionDir=new File(questionDirection+question.getId());
        if (!questionDir.exists()){//新建目录
            questionDir.mkdir();
        }
        File solutionDir=new File(programDir+question.getId());
        if (!solutionDir.exists()){
            solutionDir.mkdir();
        }
        File targetZip=new File(questionDir,"example.zip");//zip解压后的实际位置
        targetZip.createNewFile();
        zip.transferTo(targetZip);
        ZipInputStream zipInputStream=new ZipInputStream(new FileInputStream(targetZip));
        BufferedInputStream bzin=new BufferedInputStream(zipInputStream);
        //解压
        ZipEntry entry=null;
        while ((entry=zipInputStream.getNextEntry())!=null&&!entry.isDirectory()){
            File unzipFiles=new File(questionDir,entry.getName());
            FileOutputStream out=new FileOutputStream(unzipFiles);//新建目标文件，然后将压缩文件中的内容复制到文件输出流out
            BufferedOutputStream bout=new BufferedOutputStream(out);
            int b;
            while ((b=bzin.read())!=-1){
                bout.write(b);
            }
            bout.close();
            out.close();
        }
        zipInputStream.close();
        bzin.close();
        targetZip.delete();
        return true;
    }
}
