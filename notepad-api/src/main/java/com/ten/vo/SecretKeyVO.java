package com.ten.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * description
 *
* @date 2021/11/22 15:02
 */
@Data
public class SecretKeyVO {

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
    @JsonProperty("desc")
    private String description;

    /**
     * 创建时间
     */
    private Date createTime;
}
