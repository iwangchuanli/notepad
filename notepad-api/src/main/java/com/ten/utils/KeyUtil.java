package com.ten.utils;

import java.util.Random;
import java.util.UUID;

public class KeyUtil {

    /**
     * 生成随机数
     */
    public static synchronized String getRandomString() {
        return System.currentTimeMillis()+getRandomString(16);
    }

    /**
     * 创建指定位数的随机字符串
     * @param length 表示生成字符串的长度
     * @return 字符串
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}



