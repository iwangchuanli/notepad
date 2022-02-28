package com.ten.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义配置
 *
* @date 2021/05/31 12:06
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    /**
     * 面校验地址
     */
    private String  anonUrl;

    private String fileUploadPath;
}