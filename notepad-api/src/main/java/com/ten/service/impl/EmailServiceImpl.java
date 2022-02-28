package com.ten.service.impl;

import com.ten.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * description
 *
* @date 2022/2/1 14:52
 */
@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
    /**
     * 默认编码
     */
    private static final String DEFAULT_ENCODING = "UTF-8";
    /**
     * 发送邮件邮箱
     */
    @Value("${spring.mail.username}")
    private String userName;

    /**
     * 发送邮件姓名
     */
    @Value("${spring.mail.nickname}")
    private String nickname;
    /**
     * 注入JavaMailSender
     */
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public Boolean sendMail(String to, String subject, String content) {
        log.info("【发送简单邮件】 开始----接收人:{},标题:{},内容:{}",to,subject,content);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,DEFAULT_ENCODING);
        try {
            //设置发件人
            helper.setFrom(nickname+'<'+userName+'>');
            //设置邮件的收件人
            helper.setTo(to);
            //设置邮件的主题
            helper.setSubject(subject);
            //设置邮件的内容
            helper.setText(content);
        } catch (MessagingException e) {
            log.error("【发送简单邮件】 发送失败，收件人:{},内容:{}",to,content,e);
            return false;
        }
        javaMailSender.send(mimeMessage);
        log.info("【发送简单邮件】 发送成功，收件人:{},内容:{}",to,content);
        return true;
    }
}
