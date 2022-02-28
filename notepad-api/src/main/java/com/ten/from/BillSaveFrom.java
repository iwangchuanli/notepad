package com.ten.from;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * description
 *
* @date 2021/11/30 16:22
 */
@Data
public class BillSaveFrom {


    private String id;

    /**
     * 父id
     */
    @NotNull(message = "父id不能为空")
    private String parentId;

    /**
     * 金钱
     */
    @NotBlank(message = "请输入价格")
    private String money;

    /**
     * 借款姓名
     */
    private String borrowerName;

    /**
     * 类型 1收入 2支出 3借款
     */
    @NotNull(message = "请选择类型")
    private Integer type;

    /**
     * 用途
     */
    @NotBlank(message = "请输入用途")
    @Size(min = 2,max = 250,message = "最少2个字最长250个字")
    private String purpose;

    /**
     * 支付类型 1支付宝 2微信 3银行卡 4信用卡
     */
    @NotNull(message = "请选择支付类型")
    private Integer payType;

    /**
     * 是否还完 1是 2否
     */
    private Integer isPayOff;

    /**
     * 描述
     */
    private String details;
}
