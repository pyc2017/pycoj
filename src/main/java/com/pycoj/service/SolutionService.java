package com.pycoj.service;

import com.pycoj.dao.QuestionDao;
import com.pycoj.util.MyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Heyman on 2017/5/17.
 */
@Service("sService")
public class SolutionService {
    @Autowired private QuestionDao questionDao;
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

    /**
     * 运行程序
     * @param id 题目id
     * @param fileName 程序名字
     */
    public void runSolution(int id,String fileName) {

    }
}
