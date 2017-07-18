package com.pycoj.service;

import com.pycoj.dao.CoderDao;
import com.pycoj.entity.Coder;
import com.pycoj.util.MyUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Heyman on 2017/5/1.
 */
@Service("coderService")
public class CoderService {
    private static final Logger log=Logger.getLogger(CoderService.class);
    @Autowired private CoderDao coderDao;
    @Autowired @Qualifier("tokenMap") private Map map;
    @Autowired @Qualifier("headImage") private String headImageDir;

    /**
     * 注册的时候检查用户名是否重复，没有重复则返回真
     * @param username
     * @return
     */
    public boolean checkUsernameExist(String username) {
        if (coderDao.selectUsernameByUsername(username)>0){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 注册业务
     * @param coder
     * @return
     */
    public boolean register(Coder coder) {
        try {
            if (coderDao.selectUsernameByUsername(coder.getUsername()) > 0) {
                //再次检测
                return false;
            } else {
                coderDao.save(coder);
                map.remove(coder.getEmail());//移除token
                return true;
            }
        }catch (Exception e){
            return false;
        }
    }

    @Transactional(readOnly = true)
    public boolean login(Coder coder) {
        Coder result;
        if ((result= coderDao.selectCoderByUsernameAndPassword(coder))!=null){
            coder.setEmail(result.getEmail());
            coder.setId(result.getId());
            coder.setHeadImage(result.getHeadImage());
            coder.setNickname(result.getNickname());
            coder.setAcAmount(result.getAcAmount());
            return true;
        }else {
            return false;
        }
    }

    public boolean uploadHeadImage(MultipartFile file, Coder coder) throws IOException {
        StringBuilder builder=new StringBuilder();
        String fileOriginalName=file.getOriginalFilename();
        builder.append(MyUtil.getRandomString());
        builder.append(fileOriginalName.substring(fileOriginalName.lastIndexOf('.')));
        String fileFinalName=builder.toString();
        File targetFile=new File(headImageDir, fileFinalName);
        targetFile.createNewFile();
        //存储到磁盘上
        file.transferTo(targetFile);
        //存储名字到数据库中
        coderDao.updateHeadImage(fileFinalName,coder.getId());
        coder.setHeadImage(fileFinalName);
        return true;
    }
}
