package com.ten.from;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * description
 *
* @date 2021/11/22 15:09
 */
@Data
public class SecretKeySaveFrom {

    private String id;

    /**
     * 账号
     */

    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 来源地址
     */
    private String refererUrl;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空")
    @Size(min = 2,max = 120,message = "描述最少2个字最多120个字")
    @JsonProperty("desc")
    private String description;
}
