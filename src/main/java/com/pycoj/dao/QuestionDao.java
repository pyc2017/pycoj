package com.pycoj.dao;

import com.pycoj.entity.MatchQuestion;
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
     * 根据比赛id查询对应的题目
     * @param id
     * @return
     */
    public List<MatchQuestion> selectMatchQuestions(@Param("id")int id);

    /**
     * 添加题目
     * @param question
     * @return
     */
    public boolean save(@Param("question")Question question);

    /**
     * 新增某一个比赛的题目
     * @param question
     * @return
     */
    public boolean save2(@Param("question")MatchQuestion question);

    /**
     * 获取题目的总数
     * @return
     */
    public int selectAmountOfQuestions();

    public Question selectQuestionByID(@Param("id")int id);

    /**
     * 某一个用户第一AC，增加该题目的AC数
     * @param id
     * @return
     */
    public boolean updateAddSubmit(@Param("id")int id);

    /**
     * 查询比赛id与问题id是否相对应
     * @param matchId
     * @param questionId
     * @return
     */
    public int checkQuestionAndMatch(@Param("matchId")int matchId,@Param("questionId")int questionId,@Param("coderId") int coderId);

    public Integer[] selectMatchQuestionWhichAcIsZeroWithMatchIdAndCoderId(@Param("matchId")int matchId,@Param("coderId")int coderId);
}
