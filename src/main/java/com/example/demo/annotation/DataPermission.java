package com.example.demo.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermission {
    /**
     * 权限条件表达式，如果为空则通过handler处理
     */
    String value() default "";
    
    /**
     * 是否忽略权限控制
     */
    boolean ignore() default false;
    
    /**
     * 权限处理器方法名
     * 方法必须返回String类型
     * 可以是当前类的方法，也可以是其他Bean的方法（需要指定bean）
     */
    String handler() default "";
    
    /**
     * 处理器所在的bean名称，为空则在当前类中查找handler方法
     */
    String bean() default "";
} 