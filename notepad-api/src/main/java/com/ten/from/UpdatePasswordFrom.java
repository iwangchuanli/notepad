package com.ten.from;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * description
 *
* @date 2022/2/1 15:31
 */
@Data
public class UpdatePasswordFrom {

    @NotBlank(message = "原密码不能为空")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}
