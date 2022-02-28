package com.ten.from;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 主键入参
 * @author administator
 */
@Data
public class IdFrom {

    @NotBlank(message = "主键不能为空")
    private String id;
}
