package com.ten.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * description
 *
* @date 2021/11/30 14:58
 */
@Data
public class BillInfoVO implements Serializable {
    /**
     *
     */
    private String id;

    /**
     * 金钱
     */
    private BigDecimal money;

    /**
     * 借款姓名
     */
    private String borrowerName;

    /**
     * 父id
     */
    private String parentId;

    /**
     * 类型 1收入 2支出 3借款
     */
    private Integer type;

    /**
     * 用途
     */
    private String purpose;

    /**
     * 支付方式 1支付宝 2微信 3银行卡 4信用卡
     */
    private Integer payType;

    /**
     * 是否还完类型 1是 2否
     */
    private Integer isPayOff;

    /**
     * 描述
     */
    private String details;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 子数据
     */
    List<BillInfoVO> childData;
}
