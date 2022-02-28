package com.ten.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ten.enums.ResultEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 统一反参
* @date 2021/8/3 16:44
 */
@Data
public class BaseResultVO implements Serializable {

    /**
     * 结果编码
     */
    private Integer code;

    /**
     * 描述信息
     */
    private String message;

    /**
     * 业务数据
     */
    private Object data;

    /**
     * 时间戳
     */
    private Long time;

    /**
     * 生成成功结果的方法
     */
    public static BaseResultVO success() {
        return success(ResultEnum.SUCCESS);
    }

    public static BaseResultVO success(Object data) {
        BaseResultVO baseResVO = success(ResultEnum.SUCCESS);
        baseResVO.setData(data);
        return baseResVO;
    }
    /**
     * 生成成功结果的方法
     */
    public static BaseResultVO success(Integer code, String message) {
        BaseResultVO baseResponseDto = new BaseResultVO();
        baseResponseDto.setCode(code);
        baseResponseDto.setMessage(message);
        baseResponseDto.setTime(System.currentTimeMillis());
        return baseResponseDto;
    }

    /**
     * 生成成功结果的方法
     */
    public static BaseResultVO success(String message) {
        BaseResultVO baseResponseDto = new BaseResultVO();
        baseResponseDto.setCode(ResultEnum.SUCCESS.getCode());
        baseResponseDto.setMessage(message);
        baseResponseDto.setTime(System.currentTimeMillis());
        return baseResponseDto;
    }


    /**
     * 生成成功结果的方法
     */
    public static BaseResultVO success(ResultEnum resultEnum) {
        BaseResultVO baseResponseDto = new BaseResultVO();
        baseResponseDto.setCode(resultEnum.getCode());
        baseResponseDto.setMessage(resultEnum.getMessage());
        baseResponseDto.setTime(System.currentTimeMillis());
        return baseResponseDto;
    }



    /**
     * 生成失败结果的方法
     */
    public static BaseResultVO fail() {
        return fail(ResultEnum.FAIL);
    }

    /**
     * 生成失败结果的方法
     */
    public static BaseResultVO fail(String message) {
        BaseResultVO baseResponseDto = new BaseResultVO();
        baseResponseDto.setCode(ResultEnum.FAIL.getCode());
        baseResponseDto.setMessage(message);
        baseResponseDto.setTime(System.currentTimeMillis());
        return baseResponseDto;
    }

    /**
     * 生成失败结果的方法
     */
    public static BaseResultVO fail(Integer code, String message) {
        BaseResultVO baseResponseDto = new BaseResultVO();
        baseResponseDto.setCode(code);
        baseResponseDto.setMessage(message);
        baseResponseDto.setTime(System.currentTimeMillis());
        return baseResponseDto;
    }

    /**
     * 生成成功结果的方法
     */
    public static BaseResultVO fail(ResultEnum resultEnum) {
        BaseResultVO baseResponseDto = new BaseResultVO();
        baseResponseDto.setCode(resultEnum.getCode());
        baseResponseDto.setMessage(resultEnum.getMessage());
        baseResponseDto.setTime(System.currentTimeMillis());
        return baseResponseDto;
    }


    /**
     * 生成异常的方法
     *
     * @return
     */
    public static BaseResultVO error() {
        BaseResultVO baseResponseDto = new BaseResultVO();
        baseResponseDto.setCode(ResultEnum.ERROR.getCode());
        baseResponseDto.setMessage(ResultEnum.ERROR.getMessage());
        baseResponseDto.setTime(System.currentTimeMillis());
        return baseResponseDto;
    }

    /**
     * 分页
        * @date 2019/9/5 16:54
     */
    public static BaseResultVO page(Page<?> info) {
        return BaseResultVO.success(new PageResult(info));
    }

    public static BaseResultVO page(Long total, Long pages, List<?> rows) {
        return BaseResultVO.success(new PageResult(total,pages,rows));
    }

    public static BaseResultVO page(Page<?> info, List<?> rows) {
        return BaseResultVO.success(new PageResult(info,rows));
    }
    public static BaseResultVO page(Page<?> info, Class<?> target) {
        return BaseResultVO.success(new PageResult(info,target));
    }

}
