package com.ten.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * description
 *
* @date 2021/12/5 14:10
 */
@Data
public class BillPageDetailsVO {
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
     * 类型 1收入 2支出 3借款
     */
    private Integer type;

    /**
     * 用途
     */
    private String purpose;

    /**
     * 支付方式类型 1支付宝 2微信 3银行卡 4信用卡
     */
    private Integer payType;

    /**
     * 是否还完 1是 2否
     */
    private Integer isPayOff;
}
