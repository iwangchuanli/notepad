package com.ten.vo;

import lombok.Data;

import java.util.Date;

/**
 *
 * @author shien
 */
@Data
public class RecordPageVO {

    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 创建时间
     */
    private Date createTime;
}
