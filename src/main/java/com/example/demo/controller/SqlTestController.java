package com.example.demo.controller;

import com.example.demo.service.DataPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sql")
public class SqlTestController {
    
    @Autowired
    private DataPermissionService dataPermissionService;
    
    @PostMapping("/process")
    public String processSql(@RequestBody SqlRequest request) {
        return dataPermissionService.processSQL(
            request.getSql(),
            request.getDataPermission()
        );
    }

    @GetMapping("/example/condition")
    public String conditionQueryExample() {
        String sql = "SELECT u.*, d.dept_name " +
                    "FROM users u " +
                    "LEFT JOIN departments d ON u.dept_id = d.id " +
                    "WHERE u.age > 20 AND u.status = 'active'";
        String dataPermission = "u.dept_id IN (1, 2, 3)";
        return dataPermissionService.processSQL(sql, dataPermission);
    }

    @GetMapping("/example/pagination")
    public String paginationQueryExample(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        String sql = String.format(
            "SELECT * FROM users ORDER BY id LIMIT %d OFFSET %d",
            size,
            (page - 1) * size
        );
        String dataPermission = "dept_id = 1";
        return dataPermissionService.processSQL(sql, dataPermission);
    }
}

class SqlRequest {
    private String sql;
    private String dataPermission;
    
    // getters and setters
    public String getSql() {
        return sql;
    }
    
    public void setSql(String sql) {
        this.sql = sql;
    }
    
    public String getDataPermission() {
        return dataPermission;
    }
    
    public void setDataPermission(String dataPermission) {
        this.dataPermission = dataPermission;
    }
} 