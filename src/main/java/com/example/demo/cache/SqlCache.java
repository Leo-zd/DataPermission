package com.example.demo.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class SqlCache {
    
    private final Cache<String, String> sqlCache;
    
    public SqlCache() {
        sqlCache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }
    
    public String get(String key) {
        return sqlCache.getIfPresent(key);
    }
    
    public void put(String key, String value) {
        sqlCache.put(key, value);
    }
    
    public String getFingerprint(String sql, String dataPermission) {
        return String.format("%s_%s", sql.hashCode(), dataPermission.hashCode());
    }
} 