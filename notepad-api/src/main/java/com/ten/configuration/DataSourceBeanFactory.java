package com.ten.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
public class DataSourceBeanFactory {

    @Bean(name = "dataSource")
    @Primary
    @ConfigurationProperties(ignoreUnknownFields = false,prefix="spring.datasource")
    public HikariDataSource dataSource() {
        HikariDataSource druidDataSource = new UmspscDataSource();
        return druidDataSource;
    }
}
