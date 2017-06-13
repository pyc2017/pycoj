package com.pycoj.service;

import com.pycoj.dao.QuestionDao;
import com.pycoj.dao.SubmitDao;
import com.pycoj.entity.State;
import com.pycoj.entity.Submit;
import com.pycoj.service.abstracts.Program;
import com.pycoj.util.MyUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Heyman on 2017/5/17.
 */
@Service("sService")
public class SolutionService {
    private static final Logger log=Logger.getLogger(SolutionService.class);
    private static final AtomicInteger compilationTaskCount=new AtomicInteger(0);
    private static final AtomicInteger solutionTaskCount=new AtomicInteger(0);

    /**
     * 上传文件路径
     */
    @Autowired @Qualifier("filePrefix") private String filePrefix;
    @Autowired @Qualifier("inAndOut") private String questionDir;
    @Autowired private SubmitDao submitDao;

    /**
     * 用户上传代码
     * @param id 解决的题目id
     * @param code 代码
     * @param programType 文件类型
     * @return 返回存储该程序的文件夹名字
     */
    public String saveSolution(int id, String code, Program programType) {
        try {
            String randomString=MyUtil.getRandomString();
            //filePrefix / id / random string
            File file=new File(filePrefix+id+"/"+randomString);
            if (!file.exists()){
                file.mkdir();//创建问题文件夹，用来存储各个用户提交的对应问题的结局方案
            }
            File file2=new File(file, programType.getFileName());
            if (!file2.exists()) {
                file2.createNewFile();//新建这个文件
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
            return randomString;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e2){
            return null;
        }
    }

    /**
     * 运行程序，并且将信息保存到数据库中
     * @param id 题目id
     * @param dirName 路径名字
     * @param program 程序类型
     */
    public Submit runSolution(int id, String dirName, Program program) throws Exception {
        while (compilationTaskCount.get()>100){
            log.info("当前正在编译的程序的数量为："+compilationTaskCount.get());
        }
        compilationTaskCount.getAndIncrement();
        State compileResult=program.compile(filePrefix+id+"/"+dirName);
        compilationTaskCount.getAndDecrement();
        //设置完成信息
        Submit submitInfo=new Submit();
        submitInfo.setDir(dirName);
        submitInfo.setQuestionId(id);
        if (compileResult.getState()==0) {
            while (solutionTaskCount.get()>100){
                log.info("当前正在运行的程序的数量为:"+solutionTaskCount.get());
            }
            solutionTaskCount.getAndIncrement();
            submitInfo.setState(program.run(filePrefix+id+"/"+dirName,questionDir,id));
            solutionTaskCount.getAndDecrement();
        }else{
            submitInfo.setState(compileResult);
        }
        submitDao.save(submitInfo);
        return submitInfo;
    }
}