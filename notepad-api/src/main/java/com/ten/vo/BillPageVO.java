package com.ten.vo;

import lombok.Data;

import java.util.List;

/**
 * description
 *
* @date 2021/11/30 14:23
 */
@Data
public class BillPageVO {

    private String updateTime;

    /**
     * 收入价格
     */
    private String earningMoney;

    /**
     * 支出价格
     */
    private String expenseMoney;

    /**
     * 借款价格
     */
    private String borrowMoney;

    private List<BillPageDetailsVO> detail;
}
