package com.pycoj;

import com.pycoj.dao.QuestionDao;
import com.pycoj.dao.UserDao;
import com.pycoj.entity.Question;
import com.pycoj.entity.User;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Properties;

/**
 * Created by Heyman on 2017/5/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager",defaultRollback = true)
@Transactional
@ContextConfiguration(classes = {com.pycoj.config.PersistanceConfig.class})
public class DaoTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    @Qualifier("jdbcProperties")
    private Properties p;

    @Test
    public void checkUsernameExistTest(){
        System.out.println(userDao.selectUsernameByUsername("hehehehahdhasf"));
    }

    @Test
    public void saveUserTest(){
        User user=new User();
        user.setUsername("pyc_657996510");
        user.setPassword("chicago01");
        user.setEmail("losangels03@163.com");
        System.out.println(userDao.save(user));
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
    public void readFileTest(){
        System.out.println(p.getProperty("username"));
    }
}
