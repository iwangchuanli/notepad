package com.ten.from;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * description 注册入参
 *
* @date 2022/2/1 14:20
 */
@Data
public class RegisterFrom {

    @NotBlank(message = "昵称不能为空")
    private String nickname;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$",message = "手机号格式不正确")
    private String mobile;

    @NotBlank(message = "邮箱不能为空")
    @Email(regexp="^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$",message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    private String passwordRepetition;
}
