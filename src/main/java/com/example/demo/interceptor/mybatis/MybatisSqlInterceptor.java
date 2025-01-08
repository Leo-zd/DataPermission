package com.example.demo.interceptor.mybatis;

import com.example.demo.context.DataPermissionContext;
import com.example.demo.service.DataPermissionService;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Properties;

@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
@Component
public class MybatisSqlInterceptor implements Interceptor {

    @Resource
    private DataPermissionService dataPermissionService;

    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();
        
        // 从上下文获取数据权限条件
        String dataPermission = DataPermissionContext.getPermission();
        if (dataPermission != null) {
            // 处理SQL
            String processedSql = dataPermissionService.processSQL(sql, dataPermission);
            // 使用反射修改BoundSql中的sql字段
            try {
                Field sqlField = BoundSql.class.getDeclaredField("sql");
                sqlField.setAccessible(true);
                sqlField.set(boundSql, processedSql);
            } catch (Exception e) {
                throw new RuntimeException("Failed to modify SQL", e);
            }
        }
        
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}