package com.example.demo.interceptor;

public interface SqlInterceptor {
    String processSql(String originalSql, Object... params);
} 