package com.ten.from;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * description
 *
* @date 2021/11/30 14:10
 */
@Data
public class BillPageFrom extends PageParams{

    @NotNull(message = "tab请选择")
    private Integer tabNum;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
}
