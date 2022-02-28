package com.ten.utils;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ten.constant.CommonConstant;
import com.ten.from.PageParams;
import org.apache.commons.lang3.StringUtils;

/**
 * 分页工具类
 *
* @date 2021/9/18 10:11
 */
@SuppressWarnings("unchecked")
public class SortUtil {
    /**
     * 处理排序（分页情况下） for mybatis-plus
     *
     * @param pageParam         分页参数
     * @param defaultSort       默认排序的字段
     * @param defaultOrder      默认排序规则
     * @param camelToUnderscore 是否开启驼峰转下划线
     * @param alias 表别名
     */
    public static <T> Page<T> handlePageSort(PageParams pageParam, String defaultSort, String defaultOrder, boolean camelToUnderscore, String alias) {
        Page<T> page = new Page<T>(pageParam.getPageNumber(), pageParam.getPageSize());
        boolean isDesc = StringUtils.equals(defaultOrder, CommonConstant.ORDER_ASC);
        if(camelToUnderscore){
            defaultSort= StrUtil.toUnderlineCase(defaultSort);
        }
        defaultSort=StringUtils.isNotBlank(alias)?alias+"."+defaultSort:defaultSort;
        page.addOrder(new OrderItem(defaultSort,isDesc));
        return page;
    }

    /**
     * 处理排序（分页情况下） for mybatis-plus
     *
     * @param pageParam         分页参数
     * @param defaultSort       默认排序的字段
     * @param defaultOrder      默认排序规则
     * @param camelToUnderscore 是否开启驼峰转下划线
     */
    public static <T> Page<T> handlePageSort(PageParams pageParam, String defaultSort, String defaultOrder, boolean camelToUnderscore) {
        return handlePageSort(pageParam, defaultSort, defaultOrder, camelToUnderscore,null);
    }

    /**
     * 处理排序（分页情况下） for mybatis-plus
     *
     * @param pageParam         分页参数
     * @param defaultSort       默认排序的字段
     * @param defaultOrder      默认排序规则
     */
    public static <T> Page<T> handlePageSortUnderline(PageParams pageParam, String defaultSort, String defaultOrder) {
        return handlePageSort(pageParam, defaultSort, defaultOrder, false,null);
    }

    /**
     * 处理排序 倒序
     *
     * @param pageParam         分页参数
     * @param defaultSort       默认排序的字段
     */
    public static <T> Page<T> handlePageSortDesc(PageParams pageParam, String defaultSort) {
        return handlePageSort(pageParam, defaultSort, CommonConstant.ORDER_DESC, false,null);
    }

    /**
     * 处理排序 正序
     *
     * @param pageParam         分页参数
     * @param defaultSort       默认排序的字段
     */
    public static <T> Page<T> handlePageSortAsc(PageParams pageParam, String defaultSort) {
        return handlePageSort(pageParam, defaultSort, CommonConstant.ORDER_ASC, false,null);
    }



}
