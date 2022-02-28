package com.ten.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ten.entity.RecordInfo;
import com.ten.enums.ResultEnum;
import com.ten.from.IdFrom;
import com.ten.from.PageParams;
import com.ten.from.RecordSaveFrom;
import com.ten.service.RecordInfoService;
import com.ten.utils.BeanConvertUtil;
import com.ten.utils.JwtUtil;
import com.ten.vo.BaseResultVO;
import com.ten.vo.RecordPageVO;
import com.ten.vo.RecordVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 记事本
 *
* @date 2021-11-19 10:48:32
 */
@RestController
@RequestMapping("/record")
public class RecordInfoController{

    @Autowired
    private RecordInfoService recordInfoService;


    @GetMapping("/lastData")
    public BaseResultVO getLastData(){
        List<RecordInfo> resultList = this.recordInfoService.findByUserList();
        if(CollectionUtil.isEmpty(resultList)){
            return BaseResultVO.success(new RecordInfo());
        }
        RecordInfo recordInfo = resultList.get(0);
        recordInfo.setUserId(null);
        return BaseResultVO.success(recordInfo);
    }

    /**
    * 查询记事本分页
    * @param  pageParams 分页信息
      * @date 2021-11-19 10:48:32
    */
    @PostMapping("/page")
    public BaseResultVO page(@Valid @RequestBody PageParams pageParams) {
        Page<RecordInfo> page = this.recordInfoService.findPage(pageParams);
        return BaseResultVO.page(page, RecordPageVO.class);
    }

    /**
     * 获取记事本详细信息
     * @param  idFrom 主键
        * @date 2021-11-19 10:48:32
     */
    @PostMapping("/getInfo")
    public BaseResultVO getInfo(@Valid @RequestBody IdFrom idFrom) {
        RecordInfo recordInfo=this.recordInfoService.getById(idFrom.getId());
        RecordVO result = new RecordVO();
        if(ObjectUtil.isNotEmpty(recordInfo)){
            BeanConvertUtil.copy(recordInfo,result);
        }
        return BaseResultVO.success(result);
    }

    /**
     * 删除记事本
     * @param  idFrom 主键id
        * @date 2021-11-19 10:48:32
     */
    @DeleteMapping("/delete")
    public BaseResultVO deleteRecordInfo(@Valid @RequestBody IdFrom idFrom){
        this.recordInfoService.removeById(idFrom.getId());
        return BaseResultVO.success();
    }

    /**
     * description  保存或者更新记事本
     * @param       recordSaveFrom 入参
     * @return      com.ten.vo.BaseResultVO
     * @author      shisen
     * date         2021/11/19 17:08
     */
    @PutMapping("/save")
    public BaseResultVO addRecordInfo(@Valid @RequestBody RecordSaveFrom recordSaveFrom){
        RecordInfo result = new RecordInfo();
        if(StringUtils.isBlank(recordSaveFrom.getId())){
            //添加
            BeanConvertUtil.copy(recordSaveFrom,result);
            result.setCreateTime(DateUtil.parse(recordSaveFrom.getCreateTime(),"yyyy-MM-dd HH:mm"));
            result.setUserId(JwtUtil.getUserId());
            this.recordInfoService.save(result);
        }else {
            //编辑
            result = this.recordInfoService.getById(recordSaveFrom.getId());
            if(ObjectUtil.isEmpty(result)){
                return BaseResultVO.fail(ResultEnum.DATA_FAIL);
            }
            BeanConvertUtil.copy(recordSaveFrom,result);
            result.setCreateTime(DateUtil.parse(recordSaveFrom.getCreateTime(),"yyyy-MM-dd HH"));
            result.setUpdateTime(new Date());
            this.recordInfoService.updateById(result);
        }
        return BaseResultVO.success();
    }
}
