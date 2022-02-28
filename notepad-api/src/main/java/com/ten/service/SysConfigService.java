package com.ten.service;

import com.ten.entity.SysConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author administator
* @description 针对表【sys_config(系统配置表)】的数据库操作Service
* @createDate 2022-02-12 10:44:31
*/
public interface SysConfigService extends IService<SysConfig> {

    /**
     * description  根据key获取数据
     * @param       key key
     * @return      java.lang.String
     * @author      shisen
     * date         2022/2/12 13:36
     */
    SysConfig findByKey(String key);
}
