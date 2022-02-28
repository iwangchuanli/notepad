package com.ten.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis接口
* @date 2021/03/31 11:17
 */
public interface RedisService {

    /**
     * 获取 key
     *
     * @param pattern 正则
     * @return Set
     */
    Set<String> getKeys(String pattern);

    /**
     * get命令
     *
     * @param key key
     * @return Object
     */
    Object get(String key);

    /**
     * set命令
     *
     * @param key   key
     * @param value value
     */
    void set(String key, Object value);


    /**
     * set 命令
     *
     * @param key         key
     * @param value       value
     * @param seconds 秒
     */
    void setSeconds(String key, Object value, Long seconds);

    /**
     * set 命令
     *
     * @param key         key
     * @param value       value
     * @param minutes 分钟
     */
    void setMinutes(String key, Object value, Long minutes);

    /**
     * set 命令
     *
     * @param key         key
     * @param value       value
     * @param day 天数
     */
    void setDay(String key, Object value, Long day);

    /**
     * set 命令
     *
     * @param key         key
     * @param value       value
     * @param time 毫秒
     */
    void set(String key, Object value, Long time,TimeUnit timeUnit);

    /**
     * 往Hash中存入数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @param value 值
     */
    void hPut(final String key, final String hKey, final Object value);

    /**
     * 删除 map里面的key
     */
    void hDel(String key, String... hKey);

   /**
    * Hash 存入对象
    * @param key: 对象的key
    * @param object: 对象信息
    * @param time: 过期时间
    * @param timeUnit: 过期类型
    * @return void
      * @date 2021/8/3 16:59
    */
    void hPutObject(final String key, Object object, Long time,TimeUnit timeUnit);

    /**
     * 往Hash中存入数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @param value 值
     * @param milliSeconds 毫秒
     */
    void hPutSeconds(String key, String hKey, Object value,Long milliSeconds);

    /**
     * 往Hash中存入数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @param value 值
     * @param time 时间
     * @param timeUnit 时间类型
     */
    void hPut(String key, String hKey, Object value,Long time, TimeUnit timeUnit);

    /**
     * 往Hash中存入多个数据
     *
     * @param key Redis键
     * @param values Hash键值对
     */
    void hPutAll(final String key, final Map<String, Object> values);

    /**
     * 往Hash中存入数据
     *
     * @param key Redis键
     * @param values 值
     * @param seconds 秒
     */
    void hPutAllSeconds(String key, final Map<String, Object> values,Long seconds);

    /**
     * 往Hash中存入数据
     *
     * @param key Redis键
     * @param values 值
     * @param time 时间
     * @param timeUnit 时间类型
     */
    void hPutAll(String key, final Map<String, Object> values,Long time, TimeUnit timeUnit);

    /**
     * 获取Hash中的数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    Object hGet(final String key, final String hKey);

    /**
     * 获取Hash中的对象
     * @param key: 标识
     * @param clz: 对象类型
     * @return T 返回的对象
        * @date 2021/8/3 17:51
     */
    <T> T hGet(final String key,Class<T> clz);

    /**
     * 获取 Hash中的所有的数据hk 和hv
     * @param key
     * @return java.util.Map<java.lang.Object,java.lang.Object>
        * @date 2020/5/12 15:12
     **/
    Map<String, Object> entries(final String key);

    /**
     * 获取多个Hash中的数据
     *
     * @param key Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    List<Object> hMultiGet(final String key, final Collection<Object> hKeys);

    /**
     * 往Set中存入数据
     *
     * @param key Redis键
     * @param values 值
     * @return 存入的个数
     */
    long sSet(final String key, final Object... values);

    /**
     * 删除Set中的数据
     *
     * @param key Redis键
     * @param values 值
     * @return 移除的个数
     */
    long sDel(final String key, final Object... values);

    /**
     * 往List中存入数据
     *
     * @param key Redis键
     * @param value 数据
     * @return 存入的个数
     */
    long lPush(final String key, final Object value);

    /**
     * 往List中存入多个数据
     *
     * @param key Redis键
     * @param values 多个数据
     * @return 存入的个数
     */
    long lPushAll(final String key, final Collection<Object> values);

    /**
     * 往List中存入多个数据
     *
     * @param key Redis键
     * @param values 多个数据
     * @return 存入的个数
     */
    long lPushAll(final String key, final Object... values);

    /**
     * 从List中获取begin到end之间的元素
     *
     * @param key Redis键
     * @param start 开始位置
     * @param end 结束位置（start=0，end=-1表示获取全部元素）
     * @return List对象
     */
    List<Object> lGet(final String key, final int start, final int end);

    /**
     * del命令
     *
     * @param key key
     * @return Long
     */
    Long del(String... key);

    /**
     * exists命令
     * 判断key是否存在
     * @param key key
     * @return Boolean
     */
    Boolean exists(String key);

    /**
     * 判断key是否过期
     * @param key key
     * @return Boolean
     */
    Boolean isExpire(String key);

    /**
     * 返回剩余时间 秒
     *
     * @param key key
     * @return Long
     */
    Long getExpireSeconds(String key);

    /**
     * 返回剩余时间 天
     *
     * @param key key
     * @return Long
     */
    Long getExpireDay(String key);

    /**
     * 设置key的生命周期
     *
     * @param key
     * @param time
     * @param timeUnit
     */
    void expireKey(String key, long time, TimeUnit timeUnit);


    /**
     * setnx 命令
     * 将 key 的值设为 value ，当且仅当 key 不存在。
     * 若给定的 key 已经存在，则 SETNX 不做任何动作。
     * SETNX 是『SET if Not eXists』(如果不存在，则 SET)的简写
        * @date 2019/6/20 11:17
     */
    Boolean setnx(String key, String value);

    /**
     * getSet 方法
        * @return java.lang.String
     * @date 2019/6/20 11:24
     */
    Object getSet(String key, String value);


    /**
     * 加锁
        * @param key :
     * @param value :  当前时间+过期时间
     * @return boolean
     * @date 2019/6/20 10:29
     */
    boolean lock(String key,String value);

    /**
     * 解锁
        * @param key :
     * @param value : 当前时间+过期时间
     * @return void
     * @date 2019/6/20 11:30
     */
    void unlock(String key,String value);


    /**
     * 递增
     * @param key 键
     * @param delta 要增加几(大于0)
     */
    long incr(String key, long delta);

    /**
     * redis 发布消息队列
     * @param topicName: 主题
     * @param message: 消息内容
     * @return void
        * @date 2021/10/21 14:47
     */
    void publish(String topicName, String message);


}

