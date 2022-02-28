package com.ten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ten.entity.RecordInfo;
import com.ten.from.PageParams;
import com.ten.mapper.RecordInfoMapper;
import com.ten.service.RecordInfoService;
import com.ten.utils.JwtUtil;
import com.ten.utils.SortUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 记事本 实现
 *
* @date 2021-11-19 10:48:32
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RecordInfoServiceImpl extends ServiceImpl<RecordInfoMapper, RecordInfo> implements RecordInfoService {


    @Override
    public List<RecordInfo> findByUserList() {
        LambdaQueryWrapper<RecordInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RecordInfo::getUserId, JwtUtil.getUserId());
        queryWrapper.orderByDesc(RecordInfo::getCreateTime);
        return this.list(queryWrapper);
    }

    @Override
    public Page<RecordInfo> findPage(PageParams pageParams) {
        LambdaQueryWrapper<RecordInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RecordInfo::getUserId,JwtUtil.getUserId());
        Page<RecordInfo> page = SortUtil.handlePageSortDesc(pageParams, "create_time");
        return this.page(page, queryWrapper);
    }
}
