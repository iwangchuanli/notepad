package com.ten.mapper;

import com.ten.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author administator
* @description 针对表【sys_user(用户)】的数据库操作Mapper
* @createDate 2022-02-01 10:40:02
* @Entity com.ten.entity.SysUser
*/
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}




