package com.ten.vo;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.ten.utils.BeanConvertUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 统一分页返回类
* @date 2021/8/3 16:45
 */
@Data
public class PageResult implements Serializable {

    /**
     * 总条数
     */
    private Long total;

    /**
     * 总页数
     */
    private Long pages;

    /**
     * 数据
     */
    private List<?> rows = Lists.newArrayList();

    public PageResult() {
    }

    public PageResult(Long total, Long pages) {
        this.total = total;
        this.pages = pages;
    }

    public PageResult(Long total, Long pages, List<?> rows) {
        this.total = total;
        this.rows = rows;
        this.pages = pages;
    }

    public PageResult(Page<?> page) {
        this.total = page.getTotal();
        if (CollectionUtil.isNotEmpty(page.getRecords())) {
            this.rows = page.getRecords();
        }
        this.pages = page.getPages();
    }

    public PageResult(Page<?> page, List<?> rows) {
        this.total = page.getTotal();
        this.rows = rows;
        this.pages = page.getPages();
    }
    public PageResult(Page<?> page, Class<?> target) {
        this.total = page.getTotal();
        if(CollectionUtil.isNotEmpty(page.getRecords())){
            this.rows=BeanConvertUtil.copyList(page.getRecords(),target);
        }
        this.pages = page.getPages();
    }

    public void setRows(List<Object> rows) {
        if (CollectionUtil.isNotEmpty(rows)) {
            this.rows = rows;
        }
    }
}
