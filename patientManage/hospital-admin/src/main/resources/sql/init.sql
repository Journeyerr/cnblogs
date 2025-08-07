-- 创建数据库
CREATE DATABASE IF NOT EXISTS hospital_admin DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE hospital_admin;

-- 创建用户表
CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密后的）',
    real_name VARCHAR(100) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    INDEX idx_username (username),
    INDEX idx_status (status),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- 插入测试数据（密码为：123456，使用BCrypt加密）
INSERT INTO user (username, password, real_name, email, phone, status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '系统管理员', 'admin@hospital.com', '13800138000', 1),
('doctor', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '张医生', 'doctor@hospital.com', '13800138001', 1),
('nurse', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '李护士', 'nurse@hospital.com', '13800138002', 1),
('test', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '测试用户', 'test@hospital.com', '13800138003', 0);

-- 查看表结构
DESCRIBE user;

-- 创建角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    role_name VARCHAR(50) NOT NULL UNIQUE COMMENT '角色名称',
    role_code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码',
    description VARCHAR(200) COMMENT '角色描述',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    INDEX idx_role_code (role_code),
    INDEX idx_status (status),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统角色表';

-- 创建权限表
CREATE TABLE IF NOT EXISTS sys_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    permission_name VARCHAR(100) NOT NULL COMMENT '权限名称',
    permission_code VARCHAR(100) NOT NULL UNIQUE COMMENT '权限编码',
    permission_type TINYINT NOT NULL COMMENT '权限类型：1-菜单，2-按钮，3-接口',
    parent_id BIGINT DEFAULT 0 COMMENT '父权限ID',
    path VARCHAR(200) COMMENT '路径',
    component VARCHAR(200) COMMENT '组件',
    icon VARCHAR(100) COMMENT '图标',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    INDEX idx_permission_code (permission_code),
    INDEX idx_parent_id (parent_id),
    INDEX idx_status (status),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统权限表';

-- 创建用户角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_role (user_id, role_id),
    INDEX idx_user_id (user_id),
    INDEX idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 创建角色权限关联表
CREATE TABLE IF NOT EXISTS sys_role_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_role_permission (role_id, permission_id),
    INDEX idx_role_id (role_id),
    INDEX idx_permission_id (permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- 插入角色数据
INSERT INTO sys_role (role_name, role_code, description, status) VALUES
('超级管理员', 'SUPER_ADMIN', '系统超级管理员，拥有所有权限', 1),
('系统管理员', 'ADMIN', '系统管理员，拥有大部分权限', 1),
('医生', 'DOCTOR', '医生角色，拥有医生相关权限', 1),
('护士', 'NURSE', '护士角色，拥有护士相关权限', 1),
('普通用户', 'USER', '普通用户，拥有基本权限', 1);

-- 插入权限数据
INSERT INTO sys_permission (permission_name, permission_code, permission_type, parent_id, path, component, icon, sort_order, status) VALUES
-- 系统管理
('系统管理', 'system:manage', 1, 0, '/system', 'Layout', 'setting', 1, 1),
('用户管理', 'system:user:manage', 1, 1, '/system/user', 'system/user/index', 'user', 1, 1),
('角色管理', 'system:role:manage', 1, 1, '/system/role', 'system/role/index', 'peoples', 2, 1),
('权限管理', 'system:permission:manage', 1, 1, '/system/permission', 'system/permission/index', 'tree-table', 3, 1),

-- 用户管理权限
('用户查询', 'system:user:query', 2, 2, NULL, NULL, NULL, 1, 1),
('用户新增', 'system:user:add', 2, 2, NULL, NULL, NULL, 2, 1),
('用户修改', 'system:user:edit', 2, 2, NULL, NULL, NULL, 3, 1),
('用户删除', 'system:user:delete', 2, 2, NULL, NULL, NULL, 4, 1),
('用户导出', 'system:user:export', 2, 2, NULL, NULL, NULL, 5, 1),

-- 角色管理权限
('角色查询', 'system:role:query', 2, 3, NULL, NULL, NULL, 1, 1),
('角色新增', 'system:role:add', 2, 3, NULL, NULL, NULL, 2, 1),
('角色修改', 'system:role:edit', 2, 3, NULL, NULL, NULL, 3, 1),
('角色删除', 'system:role:delete', 2, 3, NULL, NULL, NULL, 4, 1),
('角色分配权限', 'system:role:assign', 2, 3, NULL, NULL, NULL, 5, 1),

-- 权限管理权限
('权限查询', 'system:permission:query', 2, 4, NULL, NULL, NULL, 1, 1),
('权限新增', 'system:permission:add', 2, 4, NULL, NULL, NULL, 2, 1),
('权限修改', 'system:permission:edit', 2, 4, NULL, NULL, NULL, 3, 1),
('权限删除', 'system:permission:delete', 2, 4, NULL, NULL, NULL, 4, 1),

-- 医院管理
('医院管理', 'hospital:manage', 1, 0, '/hospital', 'Layout', 'hospital', 2, 1),
('患者管理', 'hospital:patient:manage', 1, 5, '/hospital/patient', 'hospital/patient/index', 'user', 1, 1),
('科室管理', 'hospital:department:manage', 1, 5, '/hospital/department', 'hospital/department/index', 'tree', 2, 1),
('医生管理', 'hospital:doctor:manage', 1, 5, '/hospital/doctor', 'hospital/doctor/index', 'peoples', 3, 1),

-- 患者管理权限
('患者查询', 'hospital:patient:query', 2, 6, NULL, NULL, NULL, 1, 1),
('患者新增', 'hospital:patient:add', 2, 6, NULL, NULL, NULL, 2, 1),
('患者修改', 'hospital:patient:edit', 2, 6, NULL, NULL, NULL, 3, 1),
('患者删除', 'hospital:patient:delete', 2, 6, NULL, NULL, NULL, 4, 1),

-- 科室管理权限
('科室查询', 'hospital:department:query', 2, 7, NULL, NULL, NULL, 1, 1),
('科室新增', 'hospital:department:add', 2, 7, NULL, NULL, NULL, 2, 1),
('科室修改', 'hospital:department:edit', 2, 7, NULL, NULL, NULL, 3, 1),
('科室删除', 'hospital:department:delete', 2, 7, NULL, NULL, NULL, 4, 1),

-- 医生管理权限
('医生查询', 'hospital:doctor:query', 2, 8, NULL, NULL, NULL, 1, 1),
('医生新增', 'hospital:doctor:add', 2, 8, NULL, NULL, NULL, 2, 1),
('医生修改', 'hospital:doctor:edit', 2, 8, NULL, NULL, NULL, 3, 1),
('医生删除', 'hospital:doctor:delete', 2, 8, NULL, NULL, NULL, 4, 1);

-- 分配用户角色
INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1), -- admin -> 超级管理员
(2, 3), -- doctor -> 医生
(3, 4), -- nurse -> 护士
(4, 5); -- test -> 普通用户

-- 分配角色权限
-- 超级管理员拥有所有权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, id FROM sys_permission WHERE status = 1;

-- 系统管理员权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 2, id FROM sys_permission WHERE status = 1 AND permission_code NOT LIKE 'system:permission:%';

-- 医生权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 3, id FROM sys_permission WHERE status = 1 AND (
    permission_code LIKE 'hospital:patient:%' OR
    permission_code LIKE 'hospital:department:query' OR
    permission_code LIKE 'hospital:doctor:query'
);

-- 护士权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 4, id FROM sys_permission WHERE status = 1 AND (
    permission_code LIKE 'hospital:patient:query' OR
    permission_code LIKE 'hospital:patient:edit' OR
    permission_code LIKE 'hospital:department:query'
);

-- 普通用户权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 5, id FROM sys_permission WHERE status = 1 AND permission_code LIKE 'hospital:patient:query';

-- 查看表结构
DESCRIBE user;
DESCRIBE sys_role;
DESCRIBE sys_permission;
DESCRIBE sys_user_role;
DESCRIBE sys_role_permission;

-- 查看测试数据
SELECT id, username, real_name, email, phone, status, create_time FROM user WHERE deleted = 0;
SELECT id, role_name, role_code, description, status FROM sys_role WHERE deleted = 0;
SELECT id, permission_name, permission_code, permission_type, parent_id, status FROM sys_permission WHERE deleted = 0;



-- 创建待办事项表
CREATE TABLE IF NOT EXISTS user_todo (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                         create_user_id BIGINT NOT NULL COMMENT '创建用户ID',
                                         target_user_id BIGINT NOT NULL COMMENT '目标用户ID',
                                         title VARCHAR(100) NOT NULL COMMENT '待办事项名称',
    urgency INT(1) COMMENT '紧急程度（3-高、2-中、1-低）',
    status VARCHAR(1) COMMENT '状态（0-待办、1-进行中、2-已完成）',
    content TEXT COMMENT '代办事项内容',
    execute_time DATETIME COMMENT '执行时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    operation_ids VARCHAR(100) COMMENT '操作id集合',
    INDEX idx_creator_user_id (create_user_id),
    INDEX idx_target_user_id (target_user_id),
    INDEX idx_status (status),
    INDEX idx_urgency (urgency),
    INDEX idx_deleted (deleted)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='待办事项表';