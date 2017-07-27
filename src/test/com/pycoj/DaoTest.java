package com.pycoj;

import com.pycoj.config.RootConfig;
import com.pycoj.dao.QuestionDao;
import com.pycoj.dao.CoderDao;
import com.pycoj.dao.SubmitDao;
import com.pycoj.entity.Question;
import com.pycoj.entity.Coder;
import com.pycoj.entity.State;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Properties;

/**
 * Created by Heyman on 2017/5/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager",defaultRollback = true)
@Transactional
@ContextConfiguration(classes = {com.pycoj.config.PersistanceConfig.class, RootConfig.class})
public class DaoTest {
    @Autowired
    private CoderDao coderDao;
    @Autowired
    private QuestionDao questionDao;
    @Autowired private SubmitDao submitDao;

    @Test
    public void checkUsernameExistTest(){
        System.out.println(coderDao.selectUsernameByUsername("hehehehahdhasf"));
    }

    @Test
    public void saveUserTest(){
        Coder coder =new Coder();
        coder.setUsername("pyc_657996510");
        coder.setPassword("chicago01");
        coder.setEmail("losangels03@163.com");
        System.out.println(coderDao.save(coder));
    }

    @Test
    public void saveQuestionTest(){
        Question q=new Question();
        q.setDescription("最短路");
        q.setInput("");
        q.setOutput("");
        q.setHint("");
        q.setTag("");
        q.setTitle("eleven");
        questionDao.save(q);
        System.out.println(q.getId());
    }

    @Test
    public void selectQuestionTest(){
        Question q=questionDao.selectQuestionByID(57);
    }

    @Test
    public void getLatestStatesTest(){
        State[] states=submitDao.selectStatesByCoderIdAndQuestionId(1,1);
        Assert.notNull(states,"states is null");
        System.out.println(states.length);
    }
}
