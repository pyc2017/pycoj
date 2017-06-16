package com.pycoj.dao;

import com.pycoj.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Heyman on 2017/5/1.
 */
@Repository
public interface UserDao {
    public int selectUsernameByUsername(@Param("username")String username);
    public boolean save(@Param("user")User user);
    public User selectUserByUsernameAndPassword(@Param("user")User user);
}
