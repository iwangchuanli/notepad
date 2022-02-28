package com.ten.from;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页入参
* @date 2019/6/21 14:48
 */
@Data
public class PageParams implements Serializable {

    private static final long serialVersionUID = -4869594085374385813L;

    /** 当前页面数据量 */
    private int pageSize = 10;

    /** 当前页码 */
    private int pageNumber = 1;

    public PageParams() {
    }

    public PageParams(int pageSize, int pageNumber) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }
}
