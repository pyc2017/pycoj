package com.pycoj;

import com.pycoj.config.RootConfig;
import com.pycoj.entity.QuestionState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Heyman on 2017/5/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
public class EntityTest {
    @Autowired
    private QuestionState state;

    @Test
    public void questionStateTest(){
        System.out.println(state.getAmount());
    }
}
