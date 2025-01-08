-- 用户表
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 部门表
CREATE TABLE `sys_dept` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `dept_name` varchar(50) NOT NULL COMMENT '部门名称',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父部门ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 数据权限表
CREATE TABLE `sys_data_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  `permission_type` tinyint(4) NOT NULL COMMENT '权限类型(1:本部门,2:本部门及子部门,3:自定义)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据权限表';


-- 插入部门数据
INSERT INTO `sys_dept` (`id`, `dept_name`, `parent_id`) VALUES
(1, '总公司', NULL),
(2, '研发部', 1),
(3, '市场部', 1),
(4, '研发一组', 2),
(5, '研发二组', 2);

-- 插入用户数据
INSERT INTO `sys_user` (`id`, `username`, `dept_id`) VALUES
(1, 'admin', 1),
(2, 'dev1', 4),
(3, 'dev2', 5),
(4, 'sales1', 3);

-- 插入数据权限
INSERT INTO `sys_data_permission` (`id`, `user_id`, `dept_id`, `permission_type`) VALUES
(1, 1, 1, 1),  -- admin可以查看总公司数据
(2, 2, 4, 2),  -- dev1可以查看研发一组及子部门数据
(3, 3, 5, 3);  -- dev2可以自定义查看权限

