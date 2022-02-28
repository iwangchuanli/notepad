package com.ten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ten.entity.SysUser;
import com.ten.service.SysUserService;
import com.ten.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

/**
* @author administator
* @description 针对表【sys_user(用户)】的数据库操作Service实现
* @createDate 2022-02-01 10:40:02
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService{

    @Override
    public SysUser findByMobile(String mobile) {
        return this.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getMobile,mobile));
    }
}




