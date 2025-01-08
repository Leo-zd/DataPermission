# SQL数据权限处理组件

基于Spring Boot的SQL数据权限处理组件，通过动态解析和修改SQL语句来实现数据权限控制，并使用缓存优化性能。

## 功能特性

- 动态SQL解析和修改
- 支持条件查询和分页查询
- 基于指纹的SQL缓存机制
- 支持复杂SQL（JOIN、子查询等）
- RESTful API接口

## 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.6+
- Spring Boot 2.3.12.RELEASE

### 安装和运行

1. 克隆项目
```bash
git clone [项目地址]
cd [项目目录]
```

2. 编译项目
```bash
mvn clean install
```

3. 运行项目
```bash
mvn spring-boot:run
```

## API使用示例

### 1. 基础SQL处理
```bash
curl -X POST http://localhost:8080/sql/process \
-H "Content-Type: application/json" \
-d '{
    "sql": "SELECT * FROM users",
    "dataPermission": "dept_id = 1"
}'
```

### 2. 条件查询示例
```bash
curl http://localhost:8080/sql/example/condition
```

输出示例：
```sql
SELECT u.*, d.dept_name 
FROM users u 
LEFT JOIN departments d ON u.dept_id = d.id 
WHERE u.age > 20 AND u.status = 'active' AND u.dept_id IN (1, 2, 3)
```

### 3. 分页查询示例
```bash
curl "http://localhost:8080/sql/example/pagination?page=2&size=15"
```

输出示例：
```sql
SELECT * FROM users 
WHERE dept_id = 1 
ORDER BY id LIMIT 15 OFFSET 15
```

## 项目结构

```
demo/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           └── demo/
│       │               ├── cache/
│       │               │   └── SqlCache.java          # SQL缓存实现
│       │               ├── controller/
│       │               │   └── SqlTestController.java  # REST接口
│       │               ├── service/
│       │               │   └── DataPermissionService.java  # 业务逻辑
│       │               ├── sql/
│       │               │   └── SqlParser.java         # SQL解析处理
│       │               └── DemoApplication.java       # 启动类
│       └── resources/
│           └── application.properties                 # 配置文件
└── pom.xml                                           # 项目依赖
```

## 核心组件说明

### SqlParser
SQL解析和处理的核心类，负责：
- 解析原始SQL
- 添加数据权限条件
- 处理复杂SQL结构

### SqlCache
基于Guava Cache的缓存实现：
- 缓存已处理的SQL
- 使用SQL指纹作为缓存key
- 10分钟自动过期
- 最大缓存1000条

### DataPermissionService
业务逻辑处理服务：
- 整合SQL解析和缓存
- 处理业务异常
- 提供统一接口

## 性能优化

1. 缓存策略
   - 最大缓存条数：1000
   - 缓存时间：10分钟
   - 基于SQL指纹的缓存key

2. SQL解析优化
   - 支持批量处理
   - 智能SQL结构识别

## 使用示例
a. 直接指定条件
1. mybatis 使用示例
```java
@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users")
    @DataPermission("dept_id = #{deptId}")
    List<User> findAll();
}
```

2. 普通service使用示例
```java
@Service
public class UserService {
    @DataPermission("dept_id IN (1, 2, 3)")
    public List<User> findUsers() {
        return userMapper.findAll();
    }
}
```

b.使用当前类的处理器方法
```java
@Service
public class UserService {
    @DataPermission(handler = "getPermission")
    public List<User> findUsers() {
        return userMapper.findAll();
    }
    
    private String getPermission() {
        return "dept_id = " + getCurrentUserDeptId();
    }
}
```

c.使用其他Bean的处理器方法
```java
@Service
public class UserService {
    @DataPermission(handler = "getPermission", bean = "permissionHandler")
    public List<User> findUsers() {
        return userMapper.findAll();
    }
}

@Component("permissionHandler")
public class PermissionHandler {
    public String getPermission() {
        return "dept_id IN (SELECT dept_id FROM user_departments WHERE user_id = " + getCurrentUserId() + ")";
    }
}
```

d.实现接口方式
```java
@Service
public class UserService implements DataPermissionHandler {
    @DataPermission
    public List<User> findUsers() {
        return userMapper.findAll();
    }
    
    @Override
    public String getPermission() {
        return "dept_id = " + getCurrentUserDeptId();
    }
}
```

## 使用注意事项

1. SQL注入防护
   - 所有SQL参数需经过安全处理
   - 建议使用参数化查询

2. 性能考虑
   - 合理使用缓存
   - 避免过于复杂的SQL

3. 限制说明
   - 仅支持SELECT语句
   - 需要正确的SQL语法

## 常见问题

1. 缓存未生效
   - 检查缓存key生成是否正确
   - 确认缓存配置是否正确

2. SQL解析错误
   - 检查SQL语法
   - 确认数据权限条件格式

## 后续规划

- [ ] 支持更多SQL类型（INSERT、UPDATE、DELETE）
- [ ] 添加更多缓存策略
- [ ] 支持自定义权限规则
- [ ] 添加性能监控
- [ ] 支持分布式缓存

## 许可证

MIT License

## 贡献指南

1. Fork 项目
2. 创建特性分支
3. 提交变更
4. 发起 Pull Request

## 联系方式

如有问题，请提交 Issue 或 Pull Request。

