package com.ten.enums;

import lombok.Getter;

/**
 * description
 *
* @date 2021/11/30 14:32
 */
@Getter
public enum BillTypeEnum {
    INCOME(1, "收入"),
    EXPENDITURE(2, "支出"),
    BORROW_MONEY(3, "借款"),

    ;
    private Integer code;
    private String message;

    BillTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
