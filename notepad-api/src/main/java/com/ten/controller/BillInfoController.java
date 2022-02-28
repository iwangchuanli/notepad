package com.ten.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ten.entity.BillInfo;
import com.ten.enums.BillTypeEnum;
import com.ten.enums.ResultEnum;
import com.ten.from.BillPageFrom;
import com.ten.from.BillSaveFrom;
import com.ten.from.IdFrom;
import com.ten.from.TimeFrom;
import com.ten.service.BillInfoService;
import com.ten.utils.BeanConvertUtil;
import com.ten.utils.JwtUtil;
import com.ten.vo.BaseResultVO;
import com.ten.vo.BillInfoVO;
import com.ten.vo.BillPageDetailsVO;
import com.ten.vo.BillPageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 账单
 *

 * @date 2021-11-29 16:23:21
 */
@RestController
@RequestMapping("/bill")
public class BillInfoController{

    @Autowired
    private BillInfoService billInfoService;

    /**
    * 查询账单分页
    * @param  pageParam 查询条件
      * @date 2021-11-29 16:23:21
    */
    @PostMapping("/page")
    public BaseResultVO findBillInfoByPage(@Valid @RequestBody BillPageFrom pageParam) {
        Page<BillInfo> page = this.billInfoService.findPage(pageParam);
        Map<String, List<BillInfo>> map = page.getRecords().stream().collect(Collectors.groupingBy(e -> DateUtil.formatDate(e.getUpdateTime())));
        List<BillPageVO> result = Lists.newArrayList();
        map.forEach((k,v)->{
            BillPageVO billPageVO = new BillPageVO();
            billPageVO.setUpdateTime(k);
            Map<Integer, List<BillInfo>> collect = v.stream().collect(Collectors.groupingBy(BillInfo::getType));
            if(collect.containsKey(1)){
                double earningMoney = collect.get(1).stream().mapToDouble(a -> a.getMoney().doubleValue()).sum();
                billPageVO.setEarningMoney(String.valueOf(earningMoney));
            }
            if(collect.containsKey(2)){
                double expenseMoney = collect.get(2).stream().mapToDouble(a -> a.getMoney().doubleValue()).sum();
                billPageVO.setExpenseMoney(String.valueOf(expenseMoney));
            }
            if(collect.containsKey(3)){
                double borrowMoney = collect.get(3).stream().mapToDouble(a -> a.getMoney().doubleValue()).sum();
                billPageVO.setBorrowMoney(String.valueOf(borrowMoney));
            }
            List<BillPageDetailsVO> details = BeanConvertUtil.copyList(v, BillPageDetailsVO.class);
            billPageVO.setDetail(details);
            result.add(billPageVO);
        });
        result.sort((o1, o2) -> o2.getUpdateTime().compareTo(o1.getUpdateTime()));
        return BaseResultVO.page(page,result);
    }

    /**
     * description  根据时间查询用户的收入，支出，借款 数据
     * @param       timeFrom
     * @return      com.ten.vo.BaseResultVO
     * @author      shisen
     * date         2021/12/12 3:17
     */
    @PostMapping("/getTimeData")
    public BaseResultVO findTimeData(@Valid @RequestBody TimeFrom timeFrom){
        List<BillInfo> dataList= this.billInfoService.findUserTimeData(timeFrom);
        Map<Integer, List<BillInfo>> collect = dataList.stream().collect(Collectors.groupingBy(BillInfo::getType));
        Map<String,String> result = Maps.newHashMap();
        if(collect.containsKey(1)){
            double earningMoney = collect.get(1).stream().mapToDouble(a -> a.getMoney().doubleValue()).sum();
            result.put("earningMoneyTotal",String.valueOf(earningMoney));
        }
        if(collect.containsKey(2)){
            double expenseMoney = collect.get(2).stream().mapToDouble(a -> a.getMoney().doubleValue()).sum();
            result.put("expenseMoneyTotal",String.valueOf(expenseMoney));
        }
        if(collect.containsKey(3)){
            double borrowMoney = collect.get(3).stream().mapToDouble(a -> a.getMoney().doubleValue()).sum();
            result.put("borrowMoneyTotal",String.valueOf(borrowMoney));
        }
        return BaseResultVO.success(result);
    }

    /**
    * 删除账单
    * @param  idFrom 主键id
      * @date 2021-11-29 16:23:21
    */
    @DeleteMapping("/delete")
    public BaseResultVO delete(@Valid @RequestBody IdFrom idFrom){
        this.billInfoService.delete(idFrom.getId());
        return BaseResultVO.success();
    }

    /**
     * 获取详情
     * @param  idFrom 主键id
        * @date 2021-11-29 16:23:21
     */
    @PostMapping("/info")
    public BaseResultVO getInfo(@Valid @RequestBody IdFrom idFrom){
        BillInfo info = this.billInfoService.getById(idFrom.getId());
        if(ObjectUtil.isEmpty(info)){
            return BaseResultVO.fail(ResultEnum.DATA_FAIL);
        }
        BillInfoVO result = BeanConvertUtil.copy(info, BillInfoVO.class);
        if(BillTypeEnum.BORROW_MONEY.getCode().equals(info.getType())){
            List<BillInfo> childList = this.billInfoService.findByParentId(idFrom.getId());
            List<BillInfoVO> copyList = BeanConvertUtil.copyList(childList, BillInfoVO.class);
            result.setChildData(copyList);
        }
        return BaseResultVO.success(result);
    }

    /**
     * 保存/编辑
     * @param  saveFrom 入参
        * @date 2021-11-29 16:23:21
     */
    @PutMapping("/save")
    public BaseResultVO save(@Valid @RequestBody BillSaveFrom saveFrom){
        if(StringUtils.isBlank(saveFrom.getId())){
            //添加
            BillInfo billInfo = BeanConvertUtil.copy(saveFrom, BillInfo.class);
            billInfo.setMoney(new BigDecimal(saveFrom.getMoney()));
            billInfo.setUserId(JwtUtil.getUserId());
            billInfo.setCreateTime(new Date());
            billInfo.setUpdateTime(new Date());
            this.billInfoService.save(billInfo);
        }else {
            //编辑
            BillInfo billInfo = this.billInfoService.getById(saveFrom.getId());
            if(ObjectUtil.isEmpty(billInfo)){
                return BaseResultVO.fail(ResultEnum.DATA_FAIL);
            }
            BeanConvertUtil.copy(saveFrom,billInfo);
            billInfo.setParentId(null);
            billInfo.setMoney(new BigDecimal(saveFrom.getMoney()));
            billInfo.setUpdateTime(new Date());
            this.billInfoService.updateById(billInfo);
        }
        return BaseResultVO.success();
    }

}
