package com.pycoj.dao;

import com.pycoj.entity.State;
import com.pycoj.entity.Submit;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Heyman on 2017/6/13.
 */
@Repository
public interface SubmitDao {
    public boolean saveSubmit(@Param("submit")Submit submit);
    public boolean saveState(@Param("submit")Submit submit);
    public State[] selectStatesByCoderIdAndQuestionId(@Param("coderId")int coderId,
                                                      @Param("questionId")int questionId);

    /**
     * 查看该用户在某一个问题上的回答次数，用返回的数判定是否应该增加该用户的AC题目数量
     * @param coderId
     * @param questionId
     * @return
     */
    public int selectCountSubmitByCoderIdAndQuestionId(@Param("coderId")int coderId,
                                                       @Param("questionId")int questionId);
}
