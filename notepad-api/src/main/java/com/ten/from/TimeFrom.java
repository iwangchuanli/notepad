package com.ten.from;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * description
 *
* @date 2021/12/12 3:14
 */
@Data
public class TimeFrom {

    /**
     * 开始时间
     */
    @NotBlank(message = "开始时间不能为空")
    private String startTime;

    /**
     * 结束时间
     */
    @NotBlank(message = "结束时间不能为空")
    private String endTime;
}
