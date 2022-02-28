package com.ten.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.ten.service.RedisService;
import com.ten.utils.BeanConvertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
  * Redis方法实现类
  * @date 2021/03/31 11:19
 **/
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public Set<String> getKeys(String pattern) {
        return (Set) redisTemplate.opsForHash().keys(pattern);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setSeconds(String key, Object value, Long seconds) {
        this.set(key,value,seconds,TimeUnit.SECONDS);
    }

    @Override
    public void setMinutes(String key, Object value, Long minutes) {
        this.set(key,value,minutes,TimeUnit.MINUTES);
    }

    @Override
    public void setDay(String key, Object value, Long day) {
        this.set(key,value,day,TimeUnit.DAYS);
    }

    @Override
    public void set(String key, Object value, Long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    @Override
    public void hPut(String key, String hKey, Object value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    @Override
    public void hDel(String key, String... hKey) {
        redisTemplate.opsForHash().delete(key,hKey);
    }

    @Override
    public void hPut(String key, String hKey, Object value,Long time, TimeUnit timeUnit) {
        redisTemplate.opsForHash().put(key, hKey, value);
        redisTemplate.expire(key,time,timeUnit);
    }

    @Override
    public void hPutSeconds(String key, String hKey, Object value,Long time) {
        this.hPut(key,hKey,value,time,TimeUnit.SECONDS);
    }

    @Override
    public void hPutAll(String key, Map<String, Object> values) {
        redisTemplate.opsForHash().putAll(key, values);
    }

    @Override
    public void hPutAllSeconds(String key, Map<String, Object> values, Long time) {
        this.hPutAll(key,values,time,TimeUnit.SECONDS);
    }

    @Override
    public void hPutAll(String key, Map<String, Object> values, Long time, TimeUnit timeUnit) {
        redisTemplate.opsForHash().putAll(key, values);
        redisTemplate.expire(key,time,timeUnit);
    }

    @Override
    public void hPutObject(String key, Object object, Long time, TimeUnit timeUnit) {
        this.hPutAll(key, BeanConvertUtil.objectToMap(object),time,timeUnit);
    }

    @Override
    public Object hGet(String key, String hKey) {
        return redisTemplate.opsForHash().get(key, hKey);
    }

    @Override
    public <T> T hGet(String key,Class<T> clz) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        if(CollectionUtil.isEmpty(entries)){
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(entries), clz);
    }

    @Override
    public Map<String, Object> entries(String key) {
        Map<Object, Object> objectObjectMap = redisTemplate.opsForHash().entries(key);
        Map<String,Object> result= Maps.newHashMap();
        objectObjectMap.forEach((k,v)->{
            result.put(String.valueOf(k),v);
        });
        return result;
    }

    @Override
    public List<Object> hMultiGet(String key, Collection<Object> hKeys) {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    @Override
    public long sSet(String key, Object... values) {
        Long count = redisTemplate.opsForSet().add(key, values);
        return count == null ? 0 : count;
    }

    @Override
    public long sDel(String key, Object... values) {
        Long count = redisTemplate.opsForSet().remove(key, values);
        return count == null ? 0 : count;
    }

    @Override
    public long lPush(String key, Object value) {
        Long count = redisTemplate.opsForList().rightPush(key, value);
        return count == null ? 0 : count;
    }

    @Override
    public long lPushAll(String key, Collection<Object> values) {
        Long count = redisTemplate.opsForList().rightPushAll(key, values);
        return count == null ? 0 : count;
    }

    @Override
    public long lPushAll(String key, Object... values) {
        Long count = redisTemplate.opsForList().rightPushAll(key, values);
        return count == null ? 0 : count;
    }

    @Override
    public List<Object> lGet(String key, int start, int end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    @Override
    public Long del(String... keys) {
        Set<String> kSet = Stream.of(keys).map(k -> k).collect(Collectors.toSet());
        return redisTemplate.delete(kSet);
    }

    @Override
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Long getExpireSeconds(String key) {
        return this.getExpireTime(key,TimeUnit.SECONDS);
    }

    @Override
    public Long getExpireDay(String key) {
        return this.getExpireTime(key,TimeUnit.DAYS);
    }

    @Override
    public Boolean isExpire(String key) {
        return this.getExpireSeconds(key) > 0L?false:true;
    }

    @Override
    public Boolean setnx(String key, String value) {
        return this.redisTemplate.opsForValue().setIfAbsent(key,value);
    }

    @Override
    public Object getSet(String key, String value) {
        return this.redisTemplate.opsForValue().getAndSet(key,value);
    }

    @Override
    public boolean lock(String key, String value) {
        if(this.setnx(key, value)){
            return true;
        }
        String currentValue = (String)this.get(key);
        //如果锁 过期
        if(StringUtils.isNotBlank(currentValue)&&Long.parseLong(currentValue)<System.currentTimeMillis()){
            String oldValue=(String)this.getSet(key, value);
            if(StringUtils.isNotBlank(oldValue)&&oldValue.equals(currentValue)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void unlock(String key, String value) {
        String currentValue = (String)this.get(key);
        if(StringUtils.isNotBlank(currentValue)&&currentValue.equals(value)){
            this.del(key);
        }
    }

    @Override
    public long incr(String key, long delta) {
        if(delta<0){
            throw new RuntimeException("递增因子必须大于0");
        }
        return this.redisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public void expireKey(String key, long time, TimeUnit timeUnit) {
        this.redisTemplate.expire(key, time, timeUnit);
    }

    /**
     * 获取key的剩余时间
     * @param key: key
     * @param timeUnit: 返回的时间类型
     * @return long
        * @date 2021/8/20 9:43
     */
    private long getExpireTime(String key,TimeUnit timeUnit) {
        return this.redisTemplate.getExpire(key,timeUnit);
    }


    @Override
    public void publish(String topicName, String message) {
        this.redisTemplate.convertAndSend(topicName,message);
    }
}
