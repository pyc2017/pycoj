package com.pycoj;

import com.google.gson.Gson;
import com.pycoj.entity.Match;
import com.pycoj.entity.MatchSubmit;
import com.pycoj.service.CommentService;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 潘毅烦 on 2017/7/30.
 */
public class NoSpringTest {
    @Test
    public void getTransformAfterPandocTest() {
        try {
            Connection con = null;
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/oj?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT * FROM coder");
            stmt.close();
            con.close();
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void jedisTest(){
        Jedis jedis=new Jedis("127.0.0.1",6379);
        List<String> list=jedis.lrange("14",0,100);
        Gson gson=new Gson();
        for (String s:list){
            System.out.println(gson.fromJson(s, MatchSubmit.class));
        }
    }
}
