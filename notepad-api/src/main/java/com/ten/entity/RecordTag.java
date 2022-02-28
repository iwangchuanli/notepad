package com.ten.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 记事本标签关联表 实体类
* @date 2021-11-19 10:48:32
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("record_tag")
public class RecordTag implements Serializable  {

    /**
     * 记录id
     */
    @TableField("record_id")
    private String recordId;

    /**
     * 标签id
     */
    @TableField("tag_id")
    private String tagId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

}