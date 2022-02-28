package com.ten.service;

import com.ten.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author administator
* @description 针对表【sys_user(用户)】的数据库操作Service
* @createDate 2022-02-01 10:40:02
*/
public interface SysUserService extends IService<SysUser> {

    /**
     * description  根据手机号查询信息
     * @param       mobile 手机号
     * @return      com.ten.entity.SysUser
     * @author      shisen
     * date         2022/2/1 10:49
     */
    SysUser findByMobile(String mobile);
}
