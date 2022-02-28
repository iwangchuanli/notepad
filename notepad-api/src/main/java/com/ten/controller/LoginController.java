package com.ten.controller;

import com.google.common.collect.Maps;
import com.ten.annotation.SecurityParameter;
import com.ten.constant.CommonConstant;
import com.ten.entity.SysUser;
import com.ten.enums.ResultEnum;
import com.ten.from.ForgetPasswordFrom;
import com.ten.from.LoginFrom;
import com.ten.from.RegisterFrom;
import com.ten.from.UpdatePasswordFrom;
import com.ten.service.RedisService;
import com.ten.service.SysUserService;
import com.ten.utils.AesUtil;
import com.ten.utils.BeanConvertUtil;
import com.ten.utils.IPUtil;
import com.ten.utils.JwtUtil;
import com.ten.vo.BaseResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 登录
 *
* @date 2022/2/1 10:43
 */
@RestController
public class LoginController {

    @Autowired
    private SysUserService userService;

    @Autowired
    private RedisService redisService;

    @PostMapping("/login")
    @SecurityParameter
    public BaseResultVO login(@Valid @RequestBody LoginFrom loginFrom){
        SysUser user= this.userService.findByMobile(loginFrom.getMobile());
        if(ObjectUtils.isEmpty(user)){
            return BaseResultVO.fail(ResultEnum.USER_NOT_EXISTENT);
        }
        if(0!=user.getStatus()){
            return BaseResultVO.fail(ResultEnum.USER_STATUS_ERROR);
        }
        String encryptPassword=null;
        try {
            encryptPassword = AesUtil.encrypt(loginFrom.getPassword(), CommonConstant.PASSWORD_KEY, false);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResultVO.fail(e.getMessage());
        }
        if(!user.getPassword().equals(encryptPassword)){
            return BaseResultVO.fail(ResultEnum.USER_PASSWORD_FAIL);
        }
        String token = JwtUtil.getToken(user.getId());
        Map<String,Object> redisResult= Maps.newHashMap();
        redisResult.put("token",token);
        redisResult.put("isTemporary",2);
        this.redisService.hPutAll(CommonConstant.TOKEN_REDIS_KEY + user.getId(),
                redisResult,7L, TimeUnit.DAYS);
        Map<String,Object> result = Maps.newHashMap();
        user.setPassword(null);
        result.put("userInfo",user);
        result.put("token", token);
        result.put("isTemporary",2);
        return BaseResultVO.success(result);
    }

    @PostMapping("/temporary/login")
    public BaseResultVO temporaryLogin(){
        String token = JwtUtil.getToken(IPUtil.getIp());
        Map<String,Object> redisResult= Maps.newHashMap();
        redisResult.put("token",token);
        redisResult.put("isTemporary",1);
        this.redisService.hPutAll(CommonConstant.TOKEN_REDIS_KEY + IPUtil.getIp(),
                redisResult,2L, TimeUnit.HOURS);
        Map<String,Object> result = Maps.newHashMap();
        SysUser user = new SysUser();
        user.setNickname("临时账号");
        result.put("userInfo",user);
        result.put("token", token);
        result.put("isTemporary",1);
        return BaseResultVO.success(result);
    }

    /**
     * 注册
     */
    @PutMapping("/register")
    @SecurityParameter(outEncode = false)
    public BaseResultVO register(@Valid @RequestBody RegisterFrom registerFrom){
        SysUser user= this.userService.findByMobile(registerFrom.getMobile());
        if(!ObjectUtils.isEmpty(user)){
            return BaseResultVO.fail(ResultEnum.USER_MOBILE_EXISTENT);
        }
        if(!registerFrom.getPassword().equals(registerFrom.getPasswordRepetition())){
            return BaseResultVO.fail(ResultEnum.PASENTERED_PASSWORDS_DIFFERSWORD);
        }
        String encryptPassword=null;
        try {
            encryptPassword = AesUtil.encrypt(registerFrom.getPassword(), CommonConstant.PASSWORD_KEY, false);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResultVO.fail(e.getMessage());
        }
        SysUser sysUser = BeanConvertUtil.copy(registerFrom, SysUser.class);
        sysUser.setPassword(encryptPassword);
        sysUser.setStatus(0);
        sysUser.setCreateTime(new Date());
        this.userService.save(sysUser);
        return BaseResultVO.success();
    }

    /**
     * description  忘记密码
     * @param       forgetPasswordFrom
     * @return      com.ten.vo.BaseResultVO
     * @author      shisen
     * date         2022/2/2 17:13
     */
    @PostMapping("/forget/password")
    @SecurityParameter(outEncode = false)
    public BaseResultVO forgetPassword(@Valid @RequestBody ForgetPasswordFrom forgetPasswordFrom){
        SysUser user= this.userService.findByMobile(forgetPasswordFrom.getMobile());
        if(!ObjectUtils.isEmpty(user)){
            return BaseResultVO.fail(ResultEnum.USER_MOBILE_EXISTENT);
        }
        //从redis获取验证码
        Object code = this.redisService.get(CommonConstant.EMAIL_CODE_REDIS_KEY+user.getId());
        if (ObjectUtils.isEmpty(code)||!forgetPasswordFrom.getCode().equals(code.toString())){
            return BaseResultVO.fail(ResultEnum.EMAIL_CODE_ERROR);
        }
        String encryptPassword=null;
        try {
            encryptPassword = AesUtil.encrypt(forgetPasswordFrom.getPassword(), CommonConstant.PASSWORD_KEY, false);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResultVO.fail(e.getMessage());
        }
        user.setPassword(encryptPassword);
        user.setUpdateTime(new Date());
        this.userService.updateById(user);
        return BaseResultVO.success();
    }

    /**
     * description  修改密码
     * @param       updatePasswordFrom
     * @param       request
     * @return      com.ten.vo.BaseResultVO
     * @author      shisen
     * date         2022/2/2 17:13
     */
    @PostMapping("/user/update/password")
    @SecurityParameter(outEncode = false)
    public BaseResultVO forgetPassword(@Valid @RequestBody UpdatePasswordFrom updatePasswordFrom, HttpServletRequest request){
        SysUser user = this.userService.getById(JwtUtil.getUserId(request.getHeader("token")));
        if(ObjectUtils.isEmpty(user)){
            return BaseResultVO.fail(ResultEnum.USER_NOT_EXISTENT);
        }
        String encryptPassword=null;
        try {
            encryptPassword = AesUtil.encrypt(updatePasswordFrom.getOldPassword(), CommonConstant.PASSWORD_KEY, false);
            if(!user.getPassword().equals(encryptPassword)){
                return BaseResultVO.fail(ResultEnum.USER_OLD_PASSWORD_ERROR);
            }
            encryptPassword = AesUtil.encrypt(updatePasswordFrom.getNewPassword(), CommonConstant.PASSWORD_KEY, false);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResultVO.fail(e.getMessage());
        }
        user.setPassword(encryptPassword);
        user.setUpdateTime(new Date());
        this.userService.updateById(user);
        return BaseResultVO.success();
    }

    /**
     * description  退出
     * @param       request
     * @return      com.ten.vo.BaseResultVO
     * @author      shisen
     * date         2022/2/2 17:13
     */
    @PostMapping("/logout")
    public BaseResultVO logout(HttpServletRequest request){
        String userId = JwtUtil.getUserId(request.getHeader("token"));
        this.redisService.del(CommonConstant.TOKEN_REDIS_KEY +userId);
        return BaseResultVO.success();
    }
}
