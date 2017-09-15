package com.pycoj.service;

import com.pycoj.concurrency.DataTransfer;
import com.pycoj.concurrency.MatchProgramExecution;
import com.pycoj.concurrency.ProgramExecution;
import com.pycoj.dao.*;
import com.pycoj.entity.*;
import com.pycoj.entity.program.Program;
import com.pycoj.util.MyUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * Created by Heyman on 2017/5/17.
 */
@Service("sService")
public class SolutionService implements DisposableBean{
    private static final Logger log=Logger.getLogger(SolutionService.class);

    @Autowired private SubmitDao submitDao;
    @Autowired private MatchSubmitDao matchSubmitDao;
    @Autowired private MatchDao matchDao;
    @Autowired private CoderDao coderDao;
    @Autowired private QuestionDao questionDao;
    @Autowired private ExecutorService fixedPool;
    @Autowired private StringRedisTemplate template;
    /**
     * 懒加载
     * 用于保存正在redis中存储代码结果的比赛对象
     */
    private Set<Match> set;

    /**
     * 用户上传代码
     * @param id 解决的题目id
     * @param code 代码
     * @param programType 文件类型
     * @return 返回存储该程序的文件夹名字
     */
    public String saveSolution(File filePrefix,int id, String code, Program programType) {
        try {

            String randomString=MyUtil.getRandomString();
            //filePrefix / id / random string
            File file=new File(filePrefix,id+"/"+randomString);
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
    public Submit runSolution(File filePrefix,File questionDir,int id, String dirName, Program program,int coderId) throws Exception {
        //设置完成信息
        Submit submitInfo=new Submit();
        submitInfo.setDir(dirName);
        submitInfo.setQuestionId(id);
        //设置用户信息
        submitInfo.setCoderId(coderId);
        //使用线程池管理，减少线程的创建、销毁的开销
        fixedPool.execute(
                new ProgramExecution(filePrefix,dirName,questionDir,id,program,submitDao,coderDao,questionDao,submitInfo)
        );
        return submitInfo;
    }

    public void runMatchSolution(File filePrefix,File questionDir,int id,String dirName,Program program,Coder coder,int matchId){
        if (set==null){
            synchronized (this){
                if (set==null) {
                    set = new HashSet<>();
                    //运行监测比赛结果的线程，该线程用于将redis数据转移到mysql
                    new Thread(new DataTransfer(set,matchSubmitDao,template)).start();
                }
            }
        }
        if (!set.contains(new Match(matchId))){
            synchronized (set){
                log.info("new match start,id:"+matchId);
                Match newMatch=matchDao.selectMatchById(matchId);
                set.add(newMatch);
                //唤醒轮询的线程
                set.notify();
            }
        }
        MatchSubmit submitInfo=new MatchSubmit();
        submitInfo.setQuestionId(id);
        submitInfo.setCoderId(coder.getId());
        submitInfo.setUsername(coder.getUsername());
        submitInfo.setMatchId(matchId);
        //设置提交时间
        submitInfo.setSubmitTime(new Timestamp(System.currentTimeMillis()));
        fixedPool.execute(
                new MatchProgramExecution(filePrefix,dirName,questionDir,program,submitInfo,template,matchSubmitDao)
        );
    }

    public State[] getStates(int questionId,int coderId){
        return submitDao.selectStatesByCoderIdAndQuestionId(coderId,questionId);
    }

    /**
     * 验证比赛以及题目的正确性，以及验证提交时间是否合法
     * @param currentMatchId
     * @param postMatchId
     * @param questionId
     * @return
     */
    public Dto<Object> validateMatchInformation(Integer currentMatchId, int postMatchId, int questionId,int coderId){
        if (currentMatchId==null||currentMatchId!=postMatchId||questionDao.checkQuestionAndMatch(postMatchId,questionId,coderId)==0){
            /**
             * 非法访问，因为当前比赛id与提交id不符合，或者该题目不属于该比赛
             */
            return new Dto<>(null,false,"illegal");
        }
        /**
         * 开始验证比赛时间
         */
        Match match=matchDao.selectMatchById(postMatchId);
        long now=System.currentTimeMillis();
        if (match.getStartTime().getTime()>now){
            return new Dto<>(null,false,"It's not time yet.");
        }else if (match.getEndTime().getTime()<now){
            return new Dto<>(null,false,"Time-out.");
        }else{
            return new Dto<>(null,true,"");
        }
    }

    /**
     * 验证一般题目是否已经ac
     * @param questionId
     * @param coderId
     * @return 若已经ac，则返回false
     */
    public boolean validateNormalInformation(int coderId,int questionId){
        return submitDao.selectCountSubmitByCoderIdAndQuestionId(coderId,questionId)==0;
    }

    @Override
    public void destroy() throws Exception {
        log.info("SolutionService FixedThreadPool shutdown...");
        fixedPool.shutdownNow();
    }
}