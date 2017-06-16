package com.pycoj.dao;

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
}
