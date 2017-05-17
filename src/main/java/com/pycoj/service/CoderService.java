package com.pycoj.service;

import com.pycoj.dao.QuestionDao;
import com.pycoj.dao.UserDao;
import com.pycoj.entity.Question;
import com.pycoj.entity.QuestionState;
import com.pycoj.entity.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Heyman on 2017/5/1.
 */
@Service("coderService")
public class CoderService {
    private static final Logger log=Logger.getLogger(CoderService.class);
    @Autowired private UserDao userDao;
    @Autowired @Qualifier("tokenMap") private Map map;

    /**
     * 注册的时候检查用户名是否重复，没有重复则返回真
     * @param username
     * @return
     */
    public boolean checkUsernameExist(String username) {
        if (userDao.selectUsernameByUsername(username)>0){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 注册业务
     * @param user
     * @return
     */
    public boolean register(User user) {
        try {
            if (userDao.selectUsernameByUsername(user.getUsername()) > 0) {
                //再次检测
                return false;
            } else {
                userDao.save(user);
                map.remove(user.getEmail());//移除token
                return true;
            }
        }catch (Exception e){
            return false;
        }
    }

    public boolean login(User user) {
        User result;
        if ((result=userDao.selectUserByUsernameAndPassword(user))!=null){
            user.setEmail(result.getEmail());
            user.setId(result.getId());
            return true;
        }else {
            return false;
        }
    }
}
