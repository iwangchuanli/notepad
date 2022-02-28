package com.ten.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.ten.entity.SysConfig;
import com.ten.from.AppVersionFrom;
import com.ten.service.SysConfigService;
import com.ten.vo.BaseResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * 检测版本
 *
* @date 2022/2/12 10:45
 */
@RestController
@RequestMapping("version")
public class VersionController {

    @Autowired
    private SysConfigService configService;

    @PostMapping("app")
    public BaseResultVO getAppVersion(@Valid @RequestBody AppVersionFrom appVersionFrom){
        Map<String,Object> result= Maps.newHashMap();
        result.put("isUpdate",false);
        if(1101==appVersionFrom.getType()){
            //安卓
            SysConfig config = this.configService.findByKey("android-" + appVersionFrom.getAppId() + "-version");
            JSONObject jsonObject = JSON.parseObject(config.getConfigValue());
            if(!jsonObject.getString("version").equals(appVersionFrom.getVersion())){
                result.put("isUpdate",true);
                result.put("versionCode", jsonObject.getOrDefault("versionCode", null));
                result.put("versionName",jsonObject.getString("version"));
                result.put("versionInfo",jsonObject.getString("versionInfo"));
                result.put("updateType",jsonObject.getString("updateType"));
                result.put("downloadUrl",jsonObject.getString("downloadUrl"));
            }
        }else if(1102==appVersionFrom.getType()){
            //IOS
        }
        return BaseResultVO.success(result);
    }
}
