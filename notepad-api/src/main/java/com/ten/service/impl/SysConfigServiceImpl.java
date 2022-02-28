package com.ten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.ten.constant.CommonConstant;
import com.ten.entity.SysConfig;
import com.ten.mapper.SysConfigMapper;
import com.ten.service.RedisService;
import com.ten.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
* @author administator
* @description 针对表【sys_config(系统配置表)】的数据库操作Service实现
* @createDate 2022-02-12 10:44:31
*/
@Slf4j
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig>
    implements SysConfigService{

    @Autowired
    private RedisService redisService;

    /**
     * 初始化系统配置
     */
    @PostConstruct
    public void init() {
        redisService.hPutAll(CommonConstant.SYSTEM_CONFIG_KEY, this.findByListData(),7L, TimeUnit.DAYS);
        log.info("初始化系统配置成功");
    }

    @Override
    public SysConfig findByKey(String key) {
        Object o = this.redisService.hGet(CommonConstant.SYSTEM_CONFIG_KEY, key);
        SysConfig config=null;
        if(ObjectUtils.isEmpty(o)){
            config = this.getOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, key));
            if(!ObjectUtils.isEmpty(config)){
                this.redisService.hPut(CommonConstant.SYSTEM_CONFIG_KEY, key, config.getConfigValue());
            }
        }else {
            config= new SysConfig();
            config.setConfigKey(key);
            config.setConfigValue(o.toString());
        }
        return config;
    }

    /**
     * 获取所有的系统配置数据
        * @return java.util.Map<java.lang.String,java.lang.String>
     * @date 2020/1/18 18:32
     */
    private Map<String,Object> findByListData(){
        List<SysConfig> configs=this.list();
        Map<String, Object> result = Maps.newHashMap();
        configs.forEach(sysConfig->{
            result.put(sysConfig.getConfigKey(),sysConfig.getConfigValue());
        });
        return result;
    }
}




