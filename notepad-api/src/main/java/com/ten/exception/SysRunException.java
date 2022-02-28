package com.ten.exception;

import com.ten.enums.ResultEnum;
import lombok.Data;

/**
 * 自定义异常
*/
@Data
public class SysRunException extends RuntimeException{

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String message;

    public SysRunException() {
        super();
    }

    public SysRunException(Integer code,String message) {
        super(message);
        this.code=code;
        this.message=message;
    }

    public SysRunException(ResultEnum resultEnum) {
        this.code=resultEnum.getCode();
        this.message=resultEnum.getMessage();
    }
}

