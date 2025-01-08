package com.example.demo.config;

import com.example.demo.interceptor.mybatis.MybatisSqlInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrmConfig {
    
    @Autowired(required = false)
    private MybatisSqlInterceptor mybatisInterceptor;
    
    @Bean
    @ConditionalOnClass(name = "org.apache.ibatis.session.SqlSessionFactory")
    public MybatisSqlInterceptor mybatisInterceptor() {
        return new MybatisSqlInterceptor();
    }
}