package com.example.demo.aspect;

import com.example.demo.annotation.DataPermission;
import com.example.demo.context.DataPermissionContext;
import com.example.demo.handler.DataPermissionHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

@Aspect
@Component
public class DataPermissionAspect implements ApplicationContextAware {
    
    private ApplicationContext applicationContext;
    
    @Around("@annotation(dataPermission)")
    public Object around(ProceedingJoinPoint point, DataPermission dataPermission) throws Throwable {
        try {
            if (!dataPermission.ignore()) {
                String permission = resolvePermission(point, dataPermission);
                DataPermissionContext.setPermission(permission);
            }
            return point.proceed();
        } finally {
            DataPermissionContext.clear();
        }
    }
    
    private String resolvePermission(ProceedingJoinPoint point, DataPermission dataPermission) throws Exception {
        // 1. 如果直接指定了权限条件，则直接使用
        if (!StringUtils.isEmpty(dataPermission.value())) {
            return dataPermission.value();
        }
        
        // 2. 如果指定了处理器方法
        if (!StringUtils.isEmpty(dataPermission.handler())) {
            Object target;
            
            // 2.1 如果指定了bean名称，从Spring容器获取
            if (!StringUtils.isEmpty(dataPermission.bean())) {
                target = applicationContext.getBean(dataPermission.bean());
            } else {
                // 2.2 否则使用当前类
                target = point.getTarget();
            }
            
            // 2.3 获取处理器方法
            Method method = findHandlerMethod(target.getClass(), dataPermission.handler());
            if (method != null) {
                method.setAccessible(true);
                Object result = method.invoke(target);
                if (result instanceof String) {
                    return (String) result;
                }
            }
        }
        
        // 3. 如果目标类实现了DataPermissionHandler接口
        if (point.getTarget() instanceof DataPermissionHandler) {
            return ((DataPermissionHandler) point.getTarget()).getPermission();
        }
        
        throw new IllegalStateException("No valid permission condition found");
    }
    
    private Method findHandlerMethod(Class<?> clazz, String methodName) {
        try {
            return clazz.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
} 