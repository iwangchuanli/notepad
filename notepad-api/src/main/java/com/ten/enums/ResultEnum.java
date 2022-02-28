package com.ten.enums;

import lombok.Getter;

/**
 * 功能描述: 返回枚举
 *
* @date 2019/6/3 23:05
 */
@Getter
public enum ResultEnum {
    SUCCESS(2000, "成功"),
    FAIL(1000,"失败"),
    ERROR(5000,"网络错误"),
    //    参数编码
    PARAMETER_NOT_BLACK(1100,"参数不能为空"),
    REQUEST_METHOD_FAIL(1120,"请求方式不正确"),
    // 暂无数据
    NO_OVERALL_DATA(1200,"暂无数据"),
    DATA_FAIL(1210,"数据错误"),

    //token 系类
    TOKEN_LOSE_EFFICACY(1300,"令牌已失效"),
    TOKEN_NOT_EXISTENT(1310,"令牌不能为空"),
    TOKEN_REF_TIME(1320,"令牌刷新时间"),

    //用户
    USER_NOT_EXISTENT(1400,"用户信息不存在"),
    USER_STATUS_ERROR(1410,"用户状态异常"),
    USER_PASSWORD_FAIL(1420,"用户密码错误"),
    USER_MOBILE_EXISTENT(1430,"该手机号已存在"),
    PASENTERED_PASSWORDS_DIFFERSWORD(1440,"两次密码不一致"),
    USER_OLD_PASSWORD_ERROR(1450,"原密码错误"),

    //邮箱类
    EMAIL_SEND_ERROR(1500,"邮件发送失败"),
    EMAIL_CODE_ERROR(1510,"验证码错误"),
    UPLOAD_NOT_EXISTENT(1520,"文件不能为空"),
    UPLOAD_FILE_TYPE_ERROR(1530,"不支持该文件类型上传"),
    ;
    private Integer code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }




}
