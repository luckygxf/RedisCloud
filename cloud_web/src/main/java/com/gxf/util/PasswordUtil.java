package com.gxf.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Random;

/**
 * Created by 58 on 2017/7/16.
 */
public class PasswordUtil {
    private static Logger logger = LoggerFactory.getLogger(PasswordUtil.class);

    public static void main(String[] args) {
        System.out.println("appkey = " + getAppkey());
    }

    /**
     * 生成随机密码
     * */
    public static String genRandomNum(int pwdLen){
        int maxNum = 36;
        char letters[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
                't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuilder password = new StringBuilder();
        int countLetter = 0;
        Random random = new Random();

        while(countLetter < pwdLen){
            int index = Math.abs(random.nextInt(maxNum));
            password.append(letters[index]);
            countLetter ++;
        }

        return password.toString();
    }

    /**
     * 获取应用的appkey
     * */
    public static String getAppkey(){
        String plainText = genRandomNum(128) + System.currentTimeMillis();
        return getMd5(plainText);
    }

    /**
     * 获取文本的MD5值
     * */
    public static String getMd5(String plainText){
        StringBuilder md5 = new StringBuilder();
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.digest(plainText.getBytes());
            byte bytes[] = md.digest();
            int i = 0;
            for(int offset = 0; offset < bytes.length; offset ++){
                i = bytes[offset];
                if(i < 0){
                    i += 256;
                }
                if(i < 16){
                    md5.append("0");
                }
                md5.append(Integer.toHexString(i));
            } //for
        } catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return md5.toString();
    }
}
