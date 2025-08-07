# Hospital Admin System

基于Spring Boot + MyBatis-Plus + Spring Security + JWT的医院管理系统后端API

## 技术栈

- **Java**: 11
- **Spring Boot**: 2.7.18
- **Spring Security**: 集成JWT认证
- **MyBatis-Plus**: 3.5.3.1
- **MySQL**: 8.0+
- **Lombok**: 简化代码
- **JWT**: 无状态认证

## 功能特性

- ✅ JWT Token认证
- ✅ 用户登录/注销
- ✅ 密码加密存储
- ✅ Token黑名单机制
- ✅ 用户状态管理
- ✅ 角色权限管理（RBAC）
- ✅ 权限树形结构
- ✅ 基于注解的权限控制
- ✅ 逻辑删除
- ✅ 统一响应格式

## 快速开始

### 1. 环境要求

- JDK 11+
- MySQL 8.0+
- Maven 3.6+

### 2. 数据库配置

1. 创建数据库：
```sql
CREATE DATABASE hospital_admin DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行初始化脚本：
```bash
mysql -u root -p hospital_admin < src/main/resources/sql/init.sql
```

3. 修改数据库连接配置（`application.yml`）：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hospital_admin?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 3. 启动应用

```bash
# 编译项目
mvn clean compile

# 运行应用
mvn spring-boot:run
```

应用将在 `http://localhost:8080` 启动

## API接口文档

### 认证相关接口

#### 1. 用户登录
- **URL**: `POST /api/auth/login`
- **请求体**:
```json
{
    "username": "admin",
    "password": "123456"
}
```
- **响应**:
```json
{
    "code": 200,
    "message": "登录成功",
    "data": {
        "token": "eyJhbGciOiJIUzUxMiJ9...",
        "username": "admin",
        "realName": "系统管理员",
        "message": "登录成功"
    }
}
```

#### 2. 用户注销
- **URL**: `POST /api/auth/logout`
- **Headers**: `Authorization: Bearer {token}`
- **响应**:
```json
{
    "code": 200,
    "message": "操作成功",
    "data": null
}
```

#### 3. 获取用户信息
- **URL**: `GET /api/auth/info`
- **Headers**: `Authorization: Bearer {token}`
- **响应**:
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "id": 1,
        "username": "admin",
        "realName": "系统管理员",
        "email": "admin@hospital.com",
        "phone": "13800138000",
        "status": 1,
        "createTime": "2024-01-01T00:00:00",
        "updateTime": "2024-01-01T00:00:00"
    }
}
```

### 测试接口

#### 1. 公开接口
- **URL**: `GET /api/test/public`
- **无需认证**
- **响应**:
```json
{
    "code": 200,
    "message": "操作成功",
    "data": "这是一个公开的接口，无需认证"
}
```

#### 2. 受保护接口
- **URL**: `GET /api/test/protected`
- **Headers**: `Authorization: Bearer {token}`
- **响应**:
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "message": "这是一个受保护的接口，需要JWT认证",
        "username": "admin",
        "authorities": ["ROLE_SUPER_ADMIN", "system:user:query", "system:role:query"]
    }
}
```

#### 3. 角色权限测试接口
- **URL**: `GET /api/test/super-admin`
- **Headers**: `Authorization: Bearer {token}`
- **权限要求**: `ROLE_SUPER_ADMIN`
- **响应**:
```json
{
    "code": 200,
    "message": "操作成功",
    "data": "这是超级管理员专用接口"
}
```

#### 4. 具体权限测试接口
- **URL**: `GET /api/test/user-query`
- **Headers**: `Authorization: Bearer {token}`
- **权限要求**: `system:user:query`
- **响应**:
```json
{
    "code": 200,
    "message": "操作成功",
    "data": "这是用户查询权限接口"
}
```

## 测试账号

| 用户名 | 密码 | 状态 | 角色 | 主要权限 |
|--------|------|------|------|----------|
| admin | 123456 | 启用 | 超级管理员 | 所有权限 |
| doctor | 123456 | 启用 | 医生 | 患者管理、科室查询、医生查询 |
| nurse | 123456 | 启用 | 护士 | 患者查询、患者修改、科室查询 |
| test | 123456 | 禁用 | 普通用户 | 患者查询（禁用状态） |

## 项目结构

```
src/main/java/com/hospital/
├── HospitalAdminApplication.java    # 主启动类
├── config/
│   └── SecurityConfig.java         # Spring Security配置
├── controller/
│   ├── AuthController.java         # 认证控制器
│   ├── RoleController.java         # 角色管理控制器
│   ├── PermissionController.java   # 权限管理控制器
│   └── TestController.java         # 测试控制器
├── dto/
│   ├── ApiResponse.java            # 统一响应格式
│   ├── LoginRequest.java           # 登录请求DTO
│   └── LoginResponse.java          # 登录响应DTO
├── entity/
│   ├── User.java                   # 用户实体类
│   ├── Role.java                   # 角色实体类
│   ├── Permission.java             # 权限实体类
│   ├── UserRole.java               # 用户角色关联实体
│   └── RolePermission.java         # 角色权限关联实体
├── mapper/
│   ├── UserMapper.java             # 用户Mapper接口
│   ├── RoleMapper.java             # 角色Mapper接口
│   ├── PermissionMapper.java       # 权限Mapper接口
│   ├── UserRoleMapper.java         # 用户角色关联Mapper
│   └── RolePermissionMapper.java   # 角色权限关联Mapper
├── security/
│   ├── CustomUserDetailsService.java # 自定义UserDetailsService
│   └── JwtAuthenticationFilter.java  # JWT认证过滤器
├── service/
│   ├── UserService.java            # 用户服务接口
│   ├── RoleService.java            # 角色服务接口
│   ├── PermissionService.java      # 权限服务接口
│   └── impl/
│       ├── UserServiceImpl.java    # 用户服务实现
│       ├── RoleServiceImpl.java    # 角色服务实现
│       └── PermissionServiceImpl.java # 权限服务实现
└── util/
    └── JwtUtil.java                # JWT工具类
```

## 安全特性

1. **密码加密**: 使用BCrypt算法加密存储密码
2. **JWT认证**: 无状态JWT Token认证
3. **Token黑名单**: 注销后的Token会被加入黑名单
4. **用户状态**: 支持禁用/启用用户
5. **角色权限管理**: 基于RBAC模型的权限控制
6. **权限注解**: 支持@PreAuthorize注解进行方法级权限控制
7. **权限树形结构**: 支持菜单和按钮权限的层级管理
8. **逻辑删除**: 数据不会物理删除，只标记删除状态

## 注意事项

1. 生产环境中建议使用Redis存储Token黑名单
2. JWT密钥应该定期更换
3. 密码策略应该更加严格
4. 建议添加接口访问频率限制
5. 生产环境需要配置HTTPS

