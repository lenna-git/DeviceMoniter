-- =============================================
-- 数据库清理脚本
-- 按外键依赖顺序删除所有表数据
-- 执行前请确保已备份数据
-- =============================================

-- 使用test数据库
USE test;

-- 1. 删除密码重置令牌表（依赖 sys_user）
DELETE FROM password_reset_token;

-- 2. 删除转借记录表（依赖 device, sys_user）
DELETE FROM device_transfer_record;

-- 3. 删除维修记录表（依赖 device）
DELETE FROM device_repair;

-- 4. 删除借用记录表（依赖 device, sys_user）
DELETE FROM device_record;

-- 5. 删除设备表
DELETE FROM device;

-- 6. 删除用户表
DELETE FROM sys_user;

-- =============================================
-- 重置自增ID（可选）
-- =============================================
ALTER TABLE sys_user AUTO_INCREMENT = 1;
ALTER TABLE device AUTO_INCREMENT = 1;
ALTER TABLE device_record AUTO_INCREMENT = 1;
ALTER TABLE device_repair AUTO_INCREMENT = 1;
ALTER TABLE device_transfer_record AUTO_INCREMENT = 1;
ALTER TABLE password_reset_token AUTO_INCREMENT = 1;

-- =============================================
-- 添加默认管理员账户（可选）
-- 密码: Aa123456!
-- =============================================
-- INSERT INTO sys_user (sysusername, sysuserpassword, sysuserrole) 
-- VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', 1);

-- =============================================
-- 添加默认操作员账户（可选）
-- 密码: Aa123456!
-- =============================================
-- INSERT INTO sys_user (sysusername, sysuserpassword, sysuserrole) 
-- VALUES ('operator', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', 2);

-- =============================================
-- 执行完成提示
-- =============================================
SELECT '数据库清理完成' AS result;
