package com.ten.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ten.entity.RecordInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ten.from.PageParams;

import java.util.List;

/**
 * 记事本 接口
 *
* @date 2021-11-19 10:48:32
 */
public interface RecordInfoService extends IService<RecordInfo> {

    /**
     * 查询记事本所有
     * @return List<RecordInfo>
        * @date 2021-11-19 10:48:32
     */
    List<RecordInfo> findByUserList();

    /**
     * 查询记事本分页
     * @param pageParams 分页参数
        * @date 2021-11-19 10:48:32
     */
    Page<RecordInfo> findPage(PageParams pageParams);
}
