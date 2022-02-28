package com.ten.from;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 反馈
 *
* @date 2022/2/1 15:27
 */
@Data
public class FeedbackFrom {

    @NotBlank(message = "邮箱不能为空")
    @Email(regexp="^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$",message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "反馈意见不能为空")
    private String message;
}
