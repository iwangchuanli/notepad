package com.ten.configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * className: MybatisPlusConfigure
 *
* date:     2019/6/27 22:21
 * description: 分页插件
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false
     * 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     * @author ZhouZhou
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //设置分页
        PaginationInnerInterceptor innerInterceptor =  new PaginationInnerInterceptor();
        //设置最大分页数量
        innerInterceptor.setMaxLimit(200L);
        //对于单一数据库类型来说,都建议配置该值,避免每次分页都去抓取数据库类型
        innerInterceptor.setDbType(DbType.MYSQL);
        interceptor.addInnerInterceptor(innerInterceptor);
        return interceptor;
    }
}
