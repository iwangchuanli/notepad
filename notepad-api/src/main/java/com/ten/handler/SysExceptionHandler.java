package com.ten.handler;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.ten.enums.ResultEnum;
import com.ten.exception.SysRunException;
import com.ten.vo.BaseResultVO;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.List;
import java.util.Set;

/**
 * 全局监控异常
* @date 2021/03/31 15:13
 **/
@Slf4j
@RestControllerAdvice
public class SysExceptionHandler {


    @ExceptionHandler(value = Exception.class)
    public BaseResultVO handleException(Exception e) {
        log.error("【全局监控异常】系统内部异常，异常类:Exception, 错误信息:{}", e.getMessage(), e);
        return BaseResultVO.error();
    }

    /**
     * 系统自定义异常
     * @param e :
     * @return BaseResVO
        * @date 2019/8/30 15:51
     */
    @ExceptionHandler(value = {SysRunException.class,})
    public BaseResultVO handleSysRunException(SysRunException e) {
        log.error("【全局监控异常】自定义异常，异常类:SysRunException, 状态码:{},错误信息:{},e：{}", e.getCode(), e.getMessage(),e);
        return BaseResultVO.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class,})
    public BaseResultVO handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("【全局监控异常】传参为空，异常类:MissingServletRequestParameterException, 错误信息:{}", e.getMessage());
        return BaseResultVO.fail(ResultEnum.PARAMETER_NOT_BLACK);
    }

    /**
     * 请求方式的异常
     *
     * @param e :
     * @return BaseResVO
        * @date 2019/8/30 15:50
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public BaseResultVO handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("【全局监控异常】 请求方式不正确，异常类:HttpRequestMethodNotSupportedException, 错误信息:{}", e.getMessage());
        return BaseResultVO.fail(ResultEnum.REQUEST_METHOD_FAIL);
    }

    /**
     * 统一处理请求参数校验(实体对象传参)
     *
     * @param e
     * @return BaseResVO
     */
    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public BaseResultVO validExceptionHandler(Exception e) {
        StringBuilder message = new StringBuilder();
        List<FieldError> fieldErrors = null;
        if (e instanceof MethodArgumentNotValidException) {
            fieldErrors = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
        } else if (e instanceof BindException) {
            fieldErrors = ((BindException) e).getBindingResult().getFieldErrors();
        } else if (e instanceof HttpMessageNotReadableException) {
            log.error("【全局监控异常】json入参参数不正确，异常类:HttpMessageNotReadableException, 错误信息:{}", e.getMessage());
            return BaseResultVO.fail(HttpStatus.BAD_REQUEST.value(), "入参为json格式且不能为空");
        }
        for (FieldError error : fieldErrors) {
            message.append(error.getDefaultMessage()).append(StringPool.COMMA);
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        log.error("【全局监控异常】json入参参数不正确，异常类:(BindException,MethodArgumentNotValidException), 错误信息:{}", message);
        return BaseResultVO.fail(ResultEnum.PARAMETER_NOT_BLACK.getCode(), message.toString());
    }

    /**
     * 统一处理请求参数校验(普通传参)
     *
     * @param e ConstraintViolationException
     * @return BaseResVO
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public BaseResultVO handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            Path path = violation.getPropertyPath();
            String[] pathArr = StringUtils.splitByWholeSeparatorPreserveAllTokens(path.toString(), ".");
            message.append(violation.getMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        log.error("【全局监控异常】 from入参参数不正确，异常类:ConstraintViolationException, 错误信息:{}", message);
        return BaseResultVO.fail(HttpStatus.BAD_REQUEST.value(), message.toString());
    }

    /**
     * description  JWT异常处理
     * @param       e
     * @return      com.ten.vo.BaseResultVO
     * @author      shisen
     * date         2022/2/16 15:18
     */
    @ExceptionHandler(value = {UnsupportedJwtException.class,
            MalformedJwtException.class, SignatureException.class})
    public BaseResultVO jwtExceptionHandler(Exception e) {
        log.error("【jwt异常】异常信息：{}",e.getMessage(),e);
        String message ="";
        if (e instanceof UnsupportedJwtException) {
            message="不支持该token";
            log.error("【jwt异常】具体异常UnsupportedJwtException信息：{}",e.getMessage(),e);
        } else if (e instanceof MalformedJwtException) {
            message="token格式错误";
            log.error("【jwt异常】具体异常MalformedJwtException信息：{}",e.getMessage(),e);
        } else if (e instanceof SignatureException) {
            message="token签名异常";
            log.error("【jwt异常】具体异常MalformedJwtException信息:{}", e.getMessage(),e);
        }
        return BaseResultVO.fail(ResultEnum.TOKEN_LOSE_EFFICACY.getCode(), message);
    }
}