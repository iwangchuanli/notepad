package com.ten.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * description
 *
* date    2021/11/22 14:58
 */
@Data
public class SecretKeyPageVO {

    /**
     *
     */
    private String id;

    /**
     * 描述
     */
    @JsonProperty("desc")
    private String description;
}
