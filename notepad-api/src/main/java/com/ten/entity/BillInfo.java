package com.ten.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 账单 实体类
* @date 2021-11-29 16:23:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("bill_info")
public class BillInfo implements Serializable  {

    /**
     * 
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 父id
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 金钱
     */
    @TableField("money")
    private BigDecimal money;

    /**
     * 借款姓名
     */
    @TableField("borrower_name")
    private String borrowerName;

    /**
     * 类型 1收入 2支出 3借款
     */
    @TableField("type")
    private Integer type;

    /**
     * 用途
     */
    @TableField("purpose")
    private String purpose;

    /**
     * 类型 1支付宝 2微信 3银行卡 4信用卡
     */
    @TableField("pay_type")
    private Integer payType;

    /**
     * 是否还完 1是 2否
     */
    @TableField("is_pay_off")
    private Integer isPayOff;

    /**
     * 描述
     */
    @TableField("details")
    private String details;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;

}