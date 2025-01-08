package com.example.demo.handler;

public interface DataPermissionHandler {
    /**
     * 获取数据权限条件
     * @return SQL条件语句
     */
    String getPermission();
} 