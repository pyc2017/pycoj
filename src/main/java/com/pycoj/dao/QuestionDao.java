package com.pycoj.dao;

import com.pycoj.entity.Question;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Heyman on 2017/5/4.
 */
@Repository
public interface QuestionDao {
    /**
     * 从start开始查询20条题目
     * @param start 开始题目的下标
     * @return
     */
    public List<Question> selectQuestions(@Param("start")int start);

    /**
     * 添加题目
     * @param question
     * @return
     */
    public boolean save(@Param("question")Question question);

    /**
     * 获取题目的总数
     * @return
     */
    public int selectAmountOfQuestions();

    public Question selectQuestionByID(@Param("id")int id);

    public boolean saveSolution(@Param("userID")int userID,@Param("questionID")int questionID,@Param("name")String name);
}
