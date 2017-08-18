package com.pycoj.util;

import com.pycoj.entity.Coder;

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
        return (Coder) session.getAttribute("coder");
    }
}
