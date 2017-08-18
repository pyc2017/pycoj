package com.pycoj.service;

import com.pycoj.dao.MatchDao;
import com.pycoj.dao.QuestionDao;
import com.pycoj.entity.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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
    @Autowired private MatchDao matchDao;
    /**
     * 选择某一页的问题
     * @param page
     * @return
     */
    public List<Question> showQuestions(int page) {
        return questionDao.selectQuestions((page-1)*20);
    }

    /**
     * 查询比赛对应的问题，先查询比赛的时间
     * @param matchId
     * @return
     */
    public Dto<List<MatchQuestion>> showMatchQuestions(int matchId){
        Match match=matchDao.selectMatchById(matchId);
        long now=System.currentTimeMillis();
        if (now>match.getStartTime().getTime()&&now<match.getEndTime().getTime()) {
            return new Dto<>(questionDao.selectMatchQuestions(matchId),true,"");
        }else{
            return new Dto<>(null,false,String.valueOf(match.getStartTime().getTime())+","+String.valueOf(match.getEndTime().getTime()));
        }
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


    @Autowired @Qualifier("questionDir") private File questionDirection;
    @Autowired @Qualifier("program") private File programDir;
    /**
     * 新建问题
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
        File questionDir=new File(questionDirection, String.valueOf(question.getId()));
        if (!questionDir.exists()){//新建目录
            questionDir.mkdir();
        }
        File solutionDir=new File(programDir, String.valueOf(question.getId()));
        if (!solutionDir.exists()){
            solutionDir.mkdir();
        }
        File targetZip=new File(questionDir,"example.zip");//zip解压后的实际位置
        targetZip.createNewFile();
        zip.transferTo(targetZip);
        unzip(targetZip,questionDir);
        return true;
    }

    @Autowired @Qualifier("matchProgramDir") private File matchProgramDir;
    @Autowired @Qualifier("matchQuestionDir") private File matchQuestionDir;

    public boolean newMatchQuestion(MatchQuestion question, MultipartFile zip) throws IOException {
        questionDao.save2(question);
        File questionDir=new File(matchQuestionDir, String.valueOf(question.getId()));
        if (!questionDir.exists()){//新建目录
            questionDir.mkdir();
        }
        File solutionDir=new File(matchProgramDir, String.valueOf(question.getId()));
        if (!solutionDir.exists()){
            solutionDir.mkdir();
        }
        File targetZip=new File(questionDir,"example.zip");
        targetZip.createNewFile();
        zip.transferTo(targetZip);
        unzip(targetZip,questionDir);
        return true;
    }

    /**
     * 对zip进行解压
     * @param targetZip zip文件
     * @param questionDir 保存题目输入输出用例的文件夹
     * @throws IOException
     */
    private void unzip(File targetZip,File questionDir) throws IOException {
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
    }

    /**
     * 用户获取在比赛中已经解决的问题
     * @param matchId
     * @param coderId
     * @return
     */
    public Integer[] getSolvedQuestionsInMatch(int matchId, int coderId){
        return questionDao.selectMatchQuestionWhichAcIsZeroWithMatchIdAndCoderId(matchId,coderId);
    }
}
