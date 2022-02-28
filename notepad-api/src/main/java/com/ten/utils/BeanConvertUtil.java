package com.ten.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import javax.validation.constraints.Size;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * className: javaBean转换
 *
* date:     2019/6/22 0:09
 * description:
 */
@Slf4j
public class BeanConvertUtil {

    /**
     * sourceList --> targetList 转换
     *
     * @param sourceArray 源List
     * @param target      目标对象
     */
    public static <T> List<T> copyList(@Size(min = 1) List<?> sourceArray, Class<T> target) {
        return sourceArray.stream().map((bo) -> copy(bo, target)).collect(Collectors.toList());
    }

    /**
     * source --> target 的转换
     *
     * @param source 被转换的对象
     * @param target 转换成的对象
     * @return
     */
    public static void copy(Object source,Object target) {
        BeanUtils.copyProperties(source,target);
    }

    /**
     * source --> target 的转换
     *
     * @param source 被转换的对象
     * @param target 转换成的对象
     * @return
     */
    public static <T> T copy(Object source, Class<T> target) {
        if (null == source || null == target) {
            return null;
        }
        try {
            T t = target.newInstance();
            CustomBeanUtils.copyProperties(source, t);
            return t;
        } catch (Exception e) {
            log.error("【javaBean转换类(BeanConvertUtil)】方法:{},转换错误,异常类:{},错误信息:{}", "copy", e.getCause(), e.getMessage());
        }
        return null;
    }


    /**
     * source --> target 的转换，只复制不为空null的属性
     *
     * @param source 被转换的对象
     * @param target 转换成的对象
     * @param <T>
     * @return
     */
    public static <T> T copyNotNull(Object source, Class<T> target) {
        try {
            T t = target.newInstance();
            CustomBeanUtils.copyProperties(source, target);
            return t;
        } catch (Exception e) {
            log.error("【javaBean转换类(BeanConvertUtil)】方法:{},转换错误,异常类:{},错误信息:{}", "copyNotNull", e.getCause(), e.getMessage());
        }
        return null;
    }

    /**
     * map 转 javaBean
     *
     * @param map :
     * @param clz :
     * @return T
        * @date 2019/7/4 17:53
     */
    public static <T> T mapToObject(Map<String, Object> map, Class<T> clz) {
        return JSON.parseObject(JSON.toJSONString(map), clz);
    }


    /**
     * 方法说明：对象转化为Map
     *  
     *
     * @param object
     * @return
     */
    public static Map<String,Object> objectToMap(Object object) {
        return JSONObject.parseObject(JSON.toJSONString(object));
    }


    /**
     * 只复制不为空的属性
     */
    private static class CustomBeanUtils extends BeanUtils {
        public static void copyProperties(Object source, Object target) throws BeansException {
            copyProperties(source, target, null, (String[]) null);
        }

        private static void copyProperties(Object source, Object target, @Nullable Class<?> editable, @Nullable String... ignoreProperties) throws BeansException {
            Assert.notNull(source, "Source must not be null");
            Assert.notNull(target, "Target must not be null");
            Class<?> actualEditable = target.getClass();
            if (editable != null) {
                if (!editable.isInstance(target)) {
                    throw new IllegalArgumentException("Target class [" + target.getClass().getName() + "] not assignable to Editable class [" + editable.getName() + "]");
                }

                actualEditable = editable;
            }

            PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
            List<String> ignoreList = ignoreProperties != null ? Arrays.asList(ignoreProperties) : null;
            PropertyDescriptor[] var7 = targetPds;
            int var8 = targetPds.length;

            for (int var9 = 0; var9 < var8; ++var9) {
                PropertyDescriptor targetPd = var7[var9];
                Method writeMethod = targetPd.getWriteMethod();
                if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
                    PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                    if (sourcePd != null) {
                        Method readMethod = sourcePd.getReadMethod();
                        if (readMethod != null && ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                            try {
                                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                    readMethod.setAccessible(true);
                                }

                                Object value = readMethod.invoke(source);
                                // 只copy不为null的值
                                if (null != value) {
                                    if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                        writeMethod.setAccessible(true);
                                    }

                                    writeMethod.invoke(target, value);
                                }
                            } catch (Throwable var15) {
                                throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", var15);
                            }
                        }
                    }
                }
            }
        }
    }
}
