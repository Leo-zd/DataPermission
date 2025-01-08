package com.example.demo.context;

public class DataPermissionContext {
    private static final ThreadLocal<String> PERMISSION_CONTEXT = new ThreadLocal<>();
    
    public static void setPermission(String permission) {
        PERMISSION_CONTEXT.set(permission);
    }
    
    public static String getPermission() {
        return PERMISSION_CONTEXT.get();
    }
    
    public static void clear() {
        PERMISSION_CONTEXT.remove();
    }
} 