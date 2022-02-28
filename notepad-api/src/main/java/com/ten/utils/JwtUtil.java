package com.ten.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * description jwt 工具类
 *
* @date 2022/2/1 11:57
 */
@Slf4j
public class JwtUtil {

    /**
     * 失效时间： 7 天 =24*3600*1000*7 ms
     */
    private static final long EXPIRE_TIME = 24*3600*1000*7;
    /**
     * jwt 密钥
     */
    private static final String SECRET = "9fR80!R8#&ioqoh#ggkA2Swj7ImZEgJK2$7kj@rZk4OgGT35khG$0Puc053NEs9pZJ6LrX6TCf6s2HqM5gyPHMP6OCYBhMOhkxy";

    /**
     * token id
     */
    public static final String JWTID = "tokenId";

    /**
     * 外部http请求中 header中 token的 键值
     */
    private final static String headerTokenKey="token";

    /**
     * 获取token
     * @return      java.lang.String
     * @author      shisen
     * date         2022/2/1 12:39
     */
    public static String getToken(String userId) {
        String jwt = Jwts.builder().setSubject(userId)
                .setId(JWTID)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, generalKey())
                .compact();
            //jwt前面一般都会加Bearer
        return jwt;
    }

    /**
     * Token 是否过期验证
     */
    public static boolean isTokenExpired(String token) {
        Claims tokenClaim = getTokenClaim(token);
        return tokenClaim.getExpiration().after(new Date());
    }

    /**
     * Token 是否过期验证
     */
    public static boolean isTokenExpired(Date expiration) {
        return expiration.after(new Date());
    }



    /**
     * 根据token获取userId
     * @param token
     * @return
     */
    public static String getUserId(String token) {
        Claims tokenClaim = getTokenClaim(token);
        return tokenClaim.getSubject();
    }

    /**
     * 根据token获取userId
     * @return
     */
    public static String getUserId() {
        HttpServletRequest request = HttpContextUtil.getRequest();
        Claims body = getTokenClaim(request.getHeader(headerTokenKey));
        return body.getSubject();
    }

    /**
     * 获取token中注册信息
     *
     * @param token
     * @return
     */
    public static Claims getTokenClaim(String token) {
        try {
            return Jwts.parser().setSigningKey(generalKey()).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }


    /**
     * 由字符串生成加密key
     *
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.decodeBase64(SECRET);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }
}
