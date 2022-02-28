package com.ten.interceptor;

import cn.hutool.core.collection.CollectionUtil;
import com.ten.constant.CommonConstant;
import com.ten.entity.SysUser;
import com.ten.enums.ResultEnum;
import com.ten.exception.SysRunException;
import com.ten.service.RedisService;
import com.ten.service.SysUserService;
import com.ten.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token的校验
 *
* @date 2022/2/1 23:15
 */

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private SysUserService userService;

    @Autowired
    private RedisService redisService;

    /**
     * 外部http请求中 header中 token的 键值
     */
    private String headerTokenKey="token";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        String requestUri = request.getRequestURI();

        String token = request.getHeader(headerTokenKey);
        log.info("【token拦截器】请求地址:{},token:{}",requestUri,token);
        if(StringUtils.isBlank(token)){
            log.error("【token拦截器】token为空,请求地址:{}",requestUri);
            throw new SysRunException(ResultEnum.TOKEN_NOT_EXISTENT);
        }
        Claims tokenClaim = JwtUtil.getTokenClaim(token);
        String userId =tokenClaim.getSubject();

        Map<String,Object> redisResult = this.redisService.entries(CommonConstant.TOKEN_REDIS_KEY +userId);
        if(CollectionUtil.isEmpty(redisResult)){
            log.error("【token拦截器】-redis缓存过期，缓存中没有查询出token，redisKey:{}",CommonConstant.TOKEN_REDIS_KEY+tokenClaim.getSubject());
            throw new SysRunException(ResultEnum.TOKEN_LOSE_EFFICACY);
        }

        if(!JwtUtil.isTokenExpired(tokenClaim.getExpiration())){
            log.error("【token拦截器】根据token已经过期,刷新token,并返回");
            String refToken = JwtUtil.getToken(userId);
            this.redisService.hPut(CommonConstant.TOKEN_REDIS_KEY +userId,headerTokenKey,refToken);
            throw new SysRunException(ResultEnum.TOKEN_REF_TIME.getCode(),refToken);
        }

        String isTemporary = redisResult.get("isTemporary").toString();
        if("2".equals(isTemporary)){
            //正常登录
            SysUser user = this.userService.getById(userId);
            if(ObjectUtils.isEmpty(user)){
                log.error("【token拦截器】根据token查询不出数据,请求地址:{},用户id:{}",requestUri,userId);
                throw new SysRunException(ResultEnum.TOKEN_LOSE_EFFICACY);
            }
            if(1==user.getStatus()){
                log.error("【token拦截器】用户状态异常,请求地址:{},用户id:{}",requestUri,userId);
                throw new SysRunException(ResultEnum.USER_STATUS_ERROR);
            }
            this.redisService.expireKey(CommonConstant.TOKEN_REDIS_KEY + userId,7L, TimeUnit.DAYS);
        }
        return true;
    }
}
