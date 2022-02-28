package com.ten.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ten.annotation.SecurityParameter;
import com.ten.utils.AesUtil;
import com.ten.utils.KeyUtil;
import com.ten.utils.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author monkey
 * @desc 返回数据加密
 * @date 2018/10/25 20:17
 */
@Slf4j
@ControllerAdvice(basePackages = "com.ten.controller")
public class EncodeResponseBodyAdvice implements ResponseBodyAdvice {

    private static final String CLIENT_PUBLIC_KEY="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKChtvlneNph4MLHV+exPfTpnAwQpVzsgbVcfq1BfAqlPdIEci5VY0p0MRa5FMiCMlLTXHObmyHLenmMqcq2p+q2Jz6D8P+21Z/42N2JsdBpTOxqGrBbmTuCyNN8JW/1a4+zhkAEqQIDWCfgZoVQnhPOtoCFhgR41ZvZxv09yDZwIDAQAB";

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        //是否开启返回数据加密  true 开启  false 关闭
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        boolean encode = false;
        if (methodParameter.getMethod().isAnnotationPresent(SecurityParameter.class)) {
            //获取注解配置的包含和去除字段
            SecurityParameter serializedField = methodParameter.getMethodAnnotation(SecurityParameter.class);
            //出参是否需要加密
            encode = serializedField.outEncode();
        }
        if (encode) {
            log.info("【返回数据加密】对方法method :【{}】返回数据进行加密",methodParameter.getMethod().getName());
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
                // 生成aes秘钥
                String aseKey = KeyUtil.getRandomString(16);
                // rsa公钥加密
                String encrypted = RSAUtil.encryptedDataOnJava(aseKey, CLIENT_PUBLIC_KEY);
                // aes加密
                String requestData = AesUtil.encrypt(result, aseKey,true);
                Map<String, String> map = new HashMap<>();
                map.put("encryptedKey", encrypted);
                map.put("responseData", requestData);
                return map;
            } catch (Exception e) {
                log.error("对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行解密出现异常：" + e.getMessage());
            }
        }
        return body;

    }
}
