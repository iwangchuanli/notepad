package com.ten.service;

/**
 * 发送邮件
 *
* @date 2022/2/1 14:52
 */
public interface  EmailService {

    /**
     *  发送简单邮件
     * @param to 收件人
     * @param subject  发送主题
     * @param content 邮件内容
     */
    Boolean sendMail(String to,String subject,String content);
}
