package com.ten.controller;

import com.ten.constant.CommonConstant;
import com.ten.entity.SysUser;
import com.ten.enums.ResultEnum;
import com.ten.from.EmailPhoneFrom;
import com.ten.from.FeedbackFrom;
import com.ten.service.EmailService;
import com.ten.service.RedisService;
import com.ten.service.SysUserService;
import com.ten.utils.KeyUtil;
import com.ten.vo.BaseResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * description
 *
* @date 2022/2/1 15:02
 */
@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private SysUserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisService redisService;

    @PostMapping("/phone")
    public BaseResultVO sendPhone(@Valid @RequestBody EmailPhoneFrom emailPhoneFrom){
        SysUser user = this.userService.findByMobile(emailPhoneFrom.getPhone());
        if(ObjectUtils.isEmpty(user)){
            return BaseResultVO.fail(ResultEnum.USER_NOT_EXISTENT);
        }
        String randomString = KeyUtil.getRandomString(6);
        Boolean isEmail = this.emailService.sendMail(user.getEmail(), "验证码", String.format("您的验证码是 %s ,5分钟内有效。",randomString));
        if(isEmail){
            //添加到redis
            this.redisService.setMinutes(CommonConstant.EMAIL_CODE_REDIS_KEY+user.getId(),randomString,5L);
            return BaseResultVO.success();
        }else {
            return BaseResultVO.fail(ResultEnum.EMAIL_SEND_ERROR);
        }
    }

    @PostMapping("/feedback")
    public BaseResultVO sendPhone(@Valid @RequestBody FeedbackFrom feedbackFrom){
        Boolean isEmail = this.emailService.sendMail(
                feedbackFrom.getEmail(), "反馈",feedbackFrom.getMessage());
        if(isEmail){
            return BaseResultVO.success();
        }else {
            return BaseResultVO.fail(ResultEnum.EMAIL_SEND_ERROR);
        }
    }
}
