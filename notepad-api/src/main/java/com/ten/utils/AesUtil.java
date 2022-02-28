package com.ten.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 前后端数据传输加密工具类
 * @author monkey
 *
 */
public class AesUtil {

    /** 加密方式 */
    private static final String KEY_ALGORITHM = "AES";
    private static final String UTF8 = "UTF-8";

    /** 参数分别代表 算法名称/加密模式/数据填充方式 */
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    /**
     * 加密
     * @param content 加密的字符串
     * @param encryptKey key值
     * @param isDefaultKey 是否是默认key生成器
     */
    public static String encrypt(String content, String encryptKey,Boolean isDefaultKey) throws Exception {
        if(isDefaultKey){
            KeyGenerator kgen = KeyGenerator.getInstance(KEY_ALGORITHM);
            kgen.init(128);
        }
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        if(isDefaultKey){
            // 初始化为加密模式的默认密码器
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), KEY_ALGORITHM));
        }else {
            // 初始化为加密模式的自定义密码器
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(encryptKey));
        }
        // 加密
        byte[] result = cipher.doFinal(content.getBytes(UTF8));
        // 采用base64算法进行转码,避免出现中文乱码
        return Base64.encodeBase64String(result);
    }


    /**
     * 解密
     * @param encryptStr 解密的字符串
     * @param decryptKey 解密的key值
     * @param isDefaultKey 是否是默认key生成器
     */
    public static String decrypt(String encryptStr, String decryptKey,Boolean isDefaultKey) throws Exception {
        if(isDefaultKey){
            KeyGenerator kgen = KeyGenerator.getInstance(KEY_ALGORITHM);
            kgen.init(128);
        }
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        if(isDefaultKey){
            // 初始化为加密模式的默认密码器
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), KEY_ALGORITHM));
        }else {
            // 初始化为加密模式的自定义密码器
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(decryptKey));
        }
        // 采用base64算法进行转码,避免出现中文乱码
        return new String(cipher.doFinal(Base64.decodeBase64(encryptStr)), UTF8);
    }

    /**
     * 生成加密秘钥
     */
    private static SecretKeySpec getSecretKey(final String key) throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(key.getBytes());
        //AES 要求密钥长度为 128
        kg.init(128, random);
        //生成一个密钥
        SecretKey secretKey = kg.generateKey();
        // 转换为AES专用密钥
        return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
    }
}
