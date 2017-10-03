package com.pycoj.util;

import com.google.gson.Gson;
import com.pycoj.entity.Coder;
import com.pycoj.http.RedisHttpSession;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpSession;
import java.util.Random;

/**
 * Created by Heyman on 2017/5/2.
 */
public class MyUtil {
    private static final char[] characters={
        'q','w','e','r','t','y','u','i','o','p','a','s','d','f','g','h','j','k','l','z','x','c','v','b','n','m',
        'Q','W','E','R','T','Y','U','I','O','P','A','Z','D','F','G','H','J','K','L','Z','X','C','V','B','N','M',
        '1','2','3','4','5','6','7','8','9','0','_'
    };
    private static final int length=63;
    private static final Random rand=new Random();
    private static Gson gson=new Gson();

    public static long tokenToLong(byte[] array){
        long l=0L;
        for (int i=0;i<array.length;i++){
            l*=10;
            l+=(long)array[i]-48L;
        }
        return l;
    }

    public static String getRandomString(){
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<32;i++){
            sb.append(characters[rand.nextInt(length)]);
        }
        return sb.toString();
    }

    public static Coder getCurrentCoder(HttpSession session){
        /**
         * 如果是用的自定义session，就使用自己定义的代码，
         * 否则使用tomcat提供的api
         */
        if (session instanceof RedisHttpSession) {
            byte[] redisResult = (byte[]) session.getAttribute("coder");
            if (redisResult==null||redisResult.length==0){//可能当前sessionId已经过期了
                return null;
            }else if (redisResult.length == 1 && redisResult[0] == 48) {//未登录，初始化为0
                return null;
            } else {
                return gson.fromJson(new String(redisResult), Coder.class);
            }
        }else{
            return (Coder) session.getAttribute("coder");
        }
    }
}
