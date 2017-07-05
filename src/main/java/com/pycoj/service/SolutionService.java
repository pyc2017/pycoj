package com.pycoj.service;

import com.pycoj.concurrency.ProgramExecution;
import com.pycoj.dao.QuestionDao;
import com.pycoj.dao.SubmitDao;
import com.pycoj.entity.State;
import com.pycoj.entity.Submit;
import com.pycoj.service.abstracts.Program;
import com.pycoj.util.MyUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Heyman on 2017/5/17.
 */
@Service("sService")
public class SolutionService implements DisposableBean{
    private static final Logger log=Logger.getLogger(SolutionService.class);

    /**
     * 上传文件路径
     */
    @Autowired @Qualifier("filePrefix") private String filePrefix;
    @Autowired @Qualifier("inAndOut") private String questionDir;
    @Autowired private SubmitDao submitDao;
    @Autowired private ExecutorService fixedPool;

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
     * 在子线程中新建子进程运行程序
     * @param id 题目id
     * @param dirName 局部路径名字
     * @param program 程序类型
     */
    public void runSolution(int id, String dirName, Program program,int coderId) throws Exception {
        //使用线程池管理，减少线程的创建、销毁的开销
        fixedPool.execute(
                new ProgramExecution(filePrefix,dirName,questionDir,id,program,submitDao,coderId)
        );
        //删除上次的完成信息
        submitDao.deleteSubmitAndStateByCoderIdAndQuestionId(coderId,id);
    }

    public State[] getStates(int questionId,int coderId){
        return submitDao.selectStatesByCoderIdAndQuestionId(coderId,questionId);
    }

    @Override
    public void destroy() throws Exception {
        log.info("SolutionService FixedThreadPool shutdown...");
        fixedPool.shutdownNow();
    }
}