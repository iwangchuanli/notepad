package com.ten.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ten.annotation.SecurityParameter;
import com.ten.constant.CommonConstant;
import com.ten.entity.SecretKey;
import com.ten.enums.ResultEnum;
import com.ten.exception.SysRunException;
import com.ten.from.IdFrom;
import com.ten.from.PageParams;
import com.ten.from.SecretKeySaveFrom;
import com.ten.service.SecretKeyService;
import com.ten.utils.AesUtil;
import com.ten.utils.BeanConvertUtil;
import com.ten.utils.JwtUtil;
import com.ten.vo.BaseResultVO;
import com.ten.vo.SecretKeyPageVO;
import com.ten.vo.SecretKeyVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * 密钥
 *
* @date 2021-11-19 10:48:32
 */
@RestController
@RequestMapping("/secret")
public class SecretKeyController{

    @Autowired
    private SecretKeyService secretKeyService;

    /**
    * 查询密钥分页
    *
    * @param  pageParams
      * @date 2021-11-19 10:48:32
    */
    @PostMapping("/page")
    @SecurityParameter(inDecode=false)
    public BaseResultVO page(@Valid @RequestBody PageParams pageParams) {
        Page<SecretKey> page = secretKeyService.findPage(pageParams);
        return BaseResultVO.page(page, SecretKeyPageVO.class);
    }

    /**
     * 获取密钥详细信息
     *
     * @param  idFrom 主键
        * @date 2021-11-19 10:48:32
     */
    @PostMapping("/info")
    @SecurityParameter
    public BaseResultVO findInfo(@Valid @RequestBody IdFrom idFrom) {
        SecretKey secretKey=this.secretKeyService.getById(idFrom.getId());
        SecretKeyVO secretKeyVO = new SecretKeyVO();
        BeanConvertUtil.copy(secretKey, secretKeyVO);
        String password=null;
        try {
            password = AesUtil.decrypt(secretKey.getPassword(), CommonConstant.PASSWORD_KEY, false);
        } catch (Exception e) {
            System.out.println("密码加密失败");
        }
        secretKeyVO.setPassword(password);
        return BaseResultVO.success(secretKeyVO);
    }

    /**
    * 新增密钥/编辑
    *
    * @param  saveFrom
      * @date 2021-11-19 10:48:32
    */
    @PutMapping("/save")
    @SecurityParameter(outEncode = false)
    public BaseResultVO save(@Valid @RequestBody SecretKeySaveFrom saveFrom){
        if(StringUtils.isBlank(saveFrom.getId())){
            //添加
            SecretKey secretKey = BeanConvertUtil.copy(saveFrom, SecretKey.class);
            String password=null;
            try {
                password = AesUtil.encrypt(secretKey.getPassword(), CommonConstant.PASSWORD_KEY, false);
            } catch (Exception e) {
                System.out.println("密码加密失败");
            }
            secretKey.setPassword(password);
            secretKey.setUserId(JwtUtil.getUserId());
            secretKey.setCreateTime(new Date());
            this.secretKeyService.save(secretKey);
        }else {
            //编辑
            SecretKey secretKey = this.secretKeyService.getById(saveFrom.getId());
            if(ObjectUtil.isEmpty(secretKey)){
                return BaseResultVO.fail(ResultEnum.DATA_FAIL);
            }
            BeanConvertUtil.copy(saveFrom,secretKey);
            String password=null;
            try {
                password = AesUtil.encrypt(secretKey.getPassword(), CommonConstant.PASSWORD_KEY, false);
            } catch (Exception e) {
                System.out.println("密码加密失败");
            }
            secretKey.setPassword(password);
            secretKey.setUpdateTime(new Date());
            this.secretKeyService.updateById(secretKey);
        }
        return BaseResultVO.success();
    }

    /**
    * 删除密钥
    * @param  idFrom 主键id
      * @date 2021-11-19 10:48:32
    */
    @DeleteMapping("/delete")
    @SecurityParameter(outEncode = false)
    public BaseResultVO deleteSecretKey(@Valid @RequestBody IdFrom idFrom){
        this.secretKeyService.removeById(idFrom.getId());
        return BaseResultVO.success();
    }
}
