package com.ten.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ten.entity.SecretKey;
import com.ten.from.PageParams;

/**
 * 密钥 接口
 *
* @date 2021-11-19 10:48:32
 */
public interface SecretKeyService extends IService<SecretKey> {

    /**
     * 查询密钥分页
     * @param pageParams 分页参数
        * @date 2021-11-19 10:48:32
     */
    Page<SecretKey> findPage(PageParams pageParams);
}
