package com.ten.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ten.entity.BillInfo;
import com.ten.enums.BillTypeEnum;
import com.ten.enums.ResultEnum;
import com.ten.exception.SysRunException;
import com.ten.from.BillPageFrom;
import com.ten.from.PageParams;
import com.ten.from.TimeFrom;
import com.ten.mapper.BillInfoMapper;
import com.ten.service.BillInfoService;
import com.ten.utils.BeanConvertUtil;
import com.ten.utils.JwtUtil;
import com.ten.utils.SortUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 账单 实现
 *
* @date 2021-11-29 16:23:21
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class BillInfoServiceImpl extends ServiceImpl<BillInfoMapper, BillInfo> implements BillInfoService {


    @Override
    public Page<BillInfo> findPage(BillPageFrom pageParam) {
        LambdaQueryWrapper<BillInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.apply(StrUtil.isNotBlank(pageParam.getStartTime()),
                "date_format(create_time,'%Y-%m') >= {0}" , pageParam.getStartTime())
                .apply(StrUtil.isNotBlank(pageParam.getEndTime()),
                        "date_format(create_time,'%Y-%m') <= {0}", pageParam.getEndTime());

        queryWrapper.eq(BillInfo::getUserId,JwtUtil.getUserId());
        queryWrapper.eq(BillInfo::getParentId,0);
        if(null!=pageParam.getTabNum()&& 0!=pageParam.getTabNum()){
            queryWrapper.eq(BillInfo::getType,pageParam.getTabNum());
        }
        PageParams pageParams = BeanConvertUtil.copy(pageParam, PageParams.class);
        Page<BillInfo> page = SortUtil.handlePageSortDesc(pageParams, "update_time");
        return this.page(page,queryWrapper);
    }


    @Override
    @Transactional
    public void delete(String id) {
        log.info("【删除订单】 入参:{}",id);
        BillInfo billInfo = this.getById(id);
        if(ObjectUtil.isEmpty(billInfo)){
            log.error("【删除订单】根据id查询数据不存在，id:{}",id);
            throw new SysRunException(ResultEnum.DATA_FAIL);
        }
        if(BillTypeEnum.BORROW_MONEY.getCode().equals(billInfo.getType())){
            List<BillInfo> childList = this.findByParentId(id);
            log.info("【删除订单】借款查询该订单（{}）下有没有子订单,子订单数据:{}",billInfo,childList);
            if (CollectionUtil.isNotEmpty(childList)){
                List<String> childIdList = childList.stream().map(e -> e.getId()).collect(Collectors.toList());
                log.info("【删除订单】获取子订单的所有数据id,子订单数量:{}",childIdList);
                this.removeByIds(childIdList);
            }
        }
        this.removeById(id);
	}

    @Override
    public List<BillInfo> findByParentId(String parentId) {
        return this.list(new LambdaQueryWrapper<BillInfo>()
                .eq(BillInfo::getParentId,parentId)
                .orderByAsc(BillInfo::getUpdateTime));
    }

    @Override
    public List<BillInfo> findUserTimeData(TimeFrom timeFrom) {
        LambdaQueryWrapper<BillInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.apply(StrUtil.isNotBlank(timeFrom.getStartTime()),
                        "date_format(create_time,'%Y-%m') >= {0}" , timeFrom.getStartTime())
                .apply(StrUtil.isNotBlank(timeFrom.getEndTime()),
                        "date_format(create_time,'%Y-%m') <= {0}", timeFrom.getEndTime());
        queryWrapper.eq(BillInfo::getUserId,JwtUtil.getUserId());
        queryWrapper.eq(BillInfo::getParentId,0);
        return this.list(queryWrapper);
    }
}
