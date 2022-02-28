package com.ten.from;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * description  保存如阿灿
 * @author      shisen
 * date         2021/11/19 17:10
 */
@Data
public class RecordSaveFrom {
    /**
     *
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空")
    private String details;

    /**
     * 创建时间
     */
    @NotBlank(message = "时间不能为空")
    private String createTime;
}
