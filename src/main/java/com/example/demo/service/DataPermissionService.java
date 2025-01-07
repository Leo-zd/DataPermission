package com.example.demo.service;

import com.example.demo.cache.SqlCache;
import com.example.demo.sql.SqlParser;
import net.sf.jsqlparser.JSQLParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataPermissionService {
    
    @Autowired
    private SqlCache sqlCache;
    
    private final SqlParser sqlParser = new SqlParser();
    
    public String processSQL(String originalSql, String dataPermission) {
        // 生成缓存key
        String cacheKey = sqlCache.getFingerprint(originalSql, dataPermission);
        
        // 尝试从缓存获取
        String cachedSql = sqlCache.get(cacheKey);
        if (cachedSql != null) {
            return cachedSql;
        }
        
        try {
            // 解析SQL并添加数据权限
            String processedSql = sqlParser.addDataPermission(originalSql, dataPermission);
            
            // 存入缓存
            sqlCache.put(cacheKey, processedSql);
            
            return processedSql;
        } catch (JSQLParserException e) {
            throw new RuntimeException("SQL解析错误", e);
        }
    }
} 