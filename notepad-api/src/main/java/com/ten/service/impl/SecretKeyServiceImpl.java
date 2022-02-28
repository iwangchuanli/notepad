package com.ten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ten.entity.SecretKey;
import com.ten.from.PageParams;
import com.ten.mapper.SecretKeyMapper;
import com.ten.service.SecretKeyService;
import com.ten.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * 密钥 实现
 *
* @date 2021-11-19 10:48:32
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SecretKeyServiceImpl extends ServiceImpl<SecretKeyMapper, SecretKey> implements SecretKeyService {


    @Override
    public Page<SecretKey> findPage(PageParams pageParams) {
        LambdaQueryWrapper<SecretKey> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SecretKey::getUserId, JwtUtil.getUserId());
        Page<SecretKey> page = new Page<>(pageParams.getPageNumber(), pageParams.getPageSize());
        return this.page(page, queryWrapper);
    }
}
