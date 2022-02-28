package com.ten.advice;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ten.annotation.SecurityParameter;
import com.ten.utils.AesUtil;
import com.ten.utils.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author monkey
 * @desc 请求数据解密
 * @date 2018/10/29 20:17
 */
@Slf4j
@ControllerAdvice(basePackages = "com.ten.controller")
public class DecodeRequestBodyAdvice implements RequestBodyAdvice {

    private static final String SERVER_PRIVATE_KEY="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKcLqHIL9tusBAB3RH1z1HFM/ZU0B7/L8+q5+HKGu9jJMbHeH95n/b0IEiUTYip1aUgcNu1/V/dS/015XX3nXyBGXVfp9LeMM1AeFsHrQ4RXHKfk4UovPRur7+tKjziV9xpfdbamUF2/jFOXjGw6FSQMltsXBD8el9igVKA39T7rAgMBAAECgYAelsrsv3Yf+drtvuli07AmY0PYa6OzooRzCkUNq9CAPS+Fc7iAQKi9UdoIwgjq3388/jWsvfH8z00DAeN158WKVpBq6pg2nSWphYO7+EICbrgsDkVsMNTj2FAyOtmp6iJ7k2NWYpWsFBgbE1FpnxHYQLo8JPU4QKKlVLFGVNKOGQJBAN0WyJVq09KcyvZd2NZeAhXejHSNW1MG+C7z6yM91ld7YdoJ+YGSQHNLSgFATg+CwUmzbcEkRwKzQrtikbcNmLUCQQDBbD4T/JBEqfBdIY2BY6YedZLzNUUOO3UDWdkNRjt9KxKFknBqY5G1iu0koeSwVQINmjhNFBlYu5DPaLk9Pl0fAkEAiZhLX4prqBEC089WSxuYqqmlukfbRVH6FaaieX1br3hPTtaUXMp7qAV8Wkj+C/Mxfj/YF7MPKqQL7PkMfONOXQJBAL26TGZUfrsrqr/fsqsxsPf9wmhYX2Vngw8HafYwZxgPUPq+uxBQnNPj81vUhbtFl176YLGSvo4gZhpdEh719mcCQHlCa/7Dy0uiP2UNX48dmJAeDvK7QCTVIaJfNjgXA0UgrgQp9OB6MTdewxkZxEt7/MD0TkIopEku7k7M4MJcoqo=";

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        //是否开启入参解密 true 开启 false 关闭
        return true;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        try {
            boolean encode = false;
            if (methodParameter.getMethod().isAnnotationPresent(SecurityParameter.class)) {
                //获取注解配置的包含和去除字段
                SecurityParameter serializedField = methodParameter.getMethodAnnotation(SecurityParameter.class);
                //入参是否需要解密
                encode = serializedField.inDecode();
            }
            if (encode) {
                log.info("【入参解密】对方法method :【{}】请求数据进行解密",methodParameter.getMethod().getName());
                return new MyHttpInputMessage(inputMessage);
            }else{
                return inputMessage;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("【入参解密】对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行解密出现异常："+e.getMessage());
            return inputMessage;
        }
    }



    class MyHttpInputMessage implements HttpInputMessage {

        private HttpHeaders headers;

        private InputStream body;

        public MyHttpInputMessage(HttpInputMessage inputMessage) throws Exception {
            this.headers = inputMessage.getHeaders();
            this.body = IOUtils.toInputStream(
                    easpString(IOUtils.toString(inputMessage.getBody(),"utf-8")),"utf-8");
        }

        @Override
        public InputStream getBody(){
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }

        /**
         *
         * @param requestData
         * @return
         */
        public String easpString(String requestData) {
            if(StringUtils.isNotBlank(requestData)){
                Map<String,String> map = new Gson().fromJson(requestData,new TypeToken<Map<String,String>>() {
                }.getType());
                // 密文
                String data = map.get("requestData");
                // 加密的ase秘钥
                String encrypted = map.get("encryptedKey");
                if(StringUtils.isEmpty(data) || StringUtils.isEmpty(encrypted)){
                    throw new RuntimeException("参数【requestData】缺失异常！");
                }else{
                    String content = null ;
                    String aseKey = null;
                    try {
                        aseKey = RSAUtil.decryptDataOnJava(encrypted,SERVER_PRIVATE_KEY);
                    }catch (Exception e){
                        throw  new RuntimeException("参数【aseKey】解析异常！");
                    }
                    try {
                        content  = AesUtil.decrypt(data, aseKey,true);
                        System.out.println(JSON.toJSONString(content));
                    }catch (Exception e){
                        throw  new RuntimeException("参数【content】解析异常！");
                    }
                    if (StringUtils.isEmpty(content) || StringUtils.isEmpty(aseKey)){
                        throw  new RuntimeException("参数【requestData】解析参数空指针异常!");
                    }
                    return content;
                }
            }
            throw new RuntimeException("参数【requestData】不合法异常！");
        }
    }

}
