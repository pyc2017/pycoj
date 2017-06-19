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

    public boolean deleteSubmitAndStateByCoderIdAndQuestionId(@Param("coderId")int coderId,
                                                              @Param("questionId")int questionId);
}
