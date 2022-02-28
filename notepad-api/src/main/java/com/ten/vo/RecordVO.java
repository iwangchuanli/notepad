package com.ten.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * description  显示对象
 * @author      shisen
 * date         2021/11/19 16:51
 */
@Data
public class RecordVO {

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
    private String details;

    /**
     * 创建时间
     */
    private Date createTime;

}
