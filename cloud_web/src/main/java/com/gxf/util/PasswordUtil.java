package com.gxf.util;

import java.util.Random;

/**
 * Created by 58 on 2017/7/16.
 */
public class PasswordUtil {

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
}
