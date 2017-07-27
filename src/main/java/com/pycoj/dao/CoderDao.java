package com.pycoj.dao;

import com.pycoj.entity.Coder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Heyman on 2017/5/1.
 */
@Repository
public interface CoderDao {
    public int selectUsernameByUsername(@Param("username")String username);
    public boolean save(@Param("coder") Coder coder);
    public Coder selectCoderByUsernameAndPassword(@Param("coder") Coder coder);
    public boolean updateHeadImage(@Param("head")String head,@Param("id")int id);
    public boolean updateACCount(@Param("id")int id);
}
