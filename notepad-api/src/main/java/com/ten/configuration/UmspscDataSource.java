package com.ten.configuration;

import com.ten.utils.AesUtil;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 解密数据库连接账号密码连接
*/
@Slf4j
public class UmspscDataSource extends HikariDataSource {
    private String userNameDis;
    private String passwordDis;

     private final static String USERNAME_PKEY ="bUQyLWJFNyRnVTV+aEMyIWJPOSNvQzJAYk0xI3JE";
     private final static String PASSWORD_PKEY ="a0QwQGlDNiFnRTEhbFUwJWJDMV9rSDJAbQ==";

    @Override
    public String getPassword() {
        if(StringUtils.isNotBlank(passwordDis)){return passwordDis;}
        String encPassword = super.getPassword();
        if(null==encPassword){return null;}
        log.info("数据库密码加解密，{}",encPassword);
        try {
            passwordDis=AesUtil.decrypt(encPassword,PASSWORD_PKEY,false);
            return passwordDis;
        } catch (Exception e) {
            log.error("数据库密码解密出错，密码:{},错误信息:{}",encPassword,e);
            throw new RuntimeException("数据库密码解密失败！", e);
        }
    }

    @Override
    public String getUsername() {
        if(StringUtils.isNotBlank(userNameDis)){return userNameDis;}
        String encUsername = super.getUsername();
        if(null==encUsername){return null;}
        log.info("数据库用户加解密，{}",encUsername);
        try {
            userNameDis=AesUtil.decrypt(encUsername,USERNAME_PKEY,false);
            return userNameDis;
        } catch (Exception e) {
            log.error("数据库用户解密出错，用户:{},错误信息:{}",userNameDis,e);
            throw new RuntimeException("数据库用户解密失败！", e);
        }
    }

    public static void main(String[] args) throws Exception {
        String userDis=AesUtil.encrypt("root",USERNAME_PKEY,false);
        System.out.println(userDis);
        String user=AesUtil.decrypt("iJzW/yOyq3QnK1NFQBIApg==",USERNAME_PKEY,false);
        System.out.println(user);
        String passwordDis=AesUtil.encrypt("password",PASSWORD_PKEY,false);
        System.out.println(passwordDis);
    }
}
