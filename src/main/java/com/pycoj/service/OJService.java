package com.pycoj.service;

import com.pycoj.dao.QuestionDao;
import com.pycoj.dao.UserDao;
import com.pycoj.entity.Question;
import com.pycoj.entity.QuestionState;
import com.pycoj.entity.User;
import com.pycoj.util.MyUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Heyman on 2017/5/1.
 */
@Service("serviceImpl")
public class OJService {
    private static final Logger log=Logger.getLogger(OJService.class);
    @Autowired
    private UserDao userDao;
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    @Qualifier("tokenMap")
    private Map map;
    @Autowired
    private QuestionState state;

    /**
     * 注册的时候检查用户名是否重复，没有重复则返回真
     * @param username
     * @return
     */
    public boolean checkUsernameExist(String username) {
        if (userDao.selectUsernameByUsername(username)>0){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 注册业务
     * @param user
     * @return
     */
    public boolean register(User user) {
        try {
            if (userDao.selectUsernameByUsername(user.getUsername()) > 0) {
                //再次检测
                return false;
            } else {
                userDao.save(user);
                map.remove(user.getEmail());//移除token
                return true;
            }
        }catch (Exception e){
            return false;
        }
    }

    public boolean login(User user) {
        User result;
        if ((result=userDao.selectUserByUsernameAndPassword(user))!=null){
            user.setEmail(result.getEmail());
            user.setId(result.getId());
            return true;
        }else {
            return false;
        }
    }

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
        state.setAmount(questionDao.selectAmountOfQuestions());
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

    /**
     * 上传文件路径
     */
    private static String filePrefix="F:\\ojprogram\\";
    /**
     * 用户上传代码
     * @param id 解决的题目id
     * @param code 代码
     * @param suffix 文件后缀
     * @param userID 提交的用户的id
     * @return
     */
    public String saveSolution(int id, String code, String suffix, int userID) {
        try {
            File file=new File(filePrefix+id);
            if (!file.exists()){
                file.mkdir();//创建问题文件夹，用来存储各个用户提交的对应问题的结局方案
            }
            File file2=new File(file, MyUtil.getRandomString()+suffix);
            while (file2.exists()) {
                file2=new File(file,MyUtil.getRandomString()+suffix);
            }
            if (!file2.exists()) {
                file.createNewFile();//新建这个文件
            }
            FileOutputStream os=new FileOutputStream(file2);
            FileChannel channel=os.getChannel();
            ByteBuffer buffer=ByteBuffer.allocate(1+code.length());

            /*向文件中添加内容*/
            buffer.clear();
            buffer.put(code.getBytes());
            buffer.flip();
            while (buffer.hasRemaining()){
                channel.write(buffer);
            }
            channel.close();
            os.close();

            /*向数据库中存储完成情况*/
            questionDao.saveSolution(userID,id,file2.getName());
            return file2.getName();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e2){
            return null;
        }
    }

    public void runSolution(int id) {

    }
}
