package com.ten.from;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * description
 *
* @date 2022/2/12 10:48
 */
@Data
public class AppVersionFrom {

    @NotBlank(message = "标识不能为空")
    private String appId;

    @NotBlank(message = "版本不能为空")
    private String version;

    /**
     * （1101是安卓，1102是IOS）
     */
    @NotNull(message = "平台不能为空")
    private Integer type;

}
