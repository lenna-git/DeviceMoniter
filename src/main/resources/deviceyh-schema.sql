-- 修改 Device 表，添加借用人外键字段
ALTER TABLE device 
ADD COLUMN deviceyh_id BIGINT NULL;

-- 添加外键约束
ALTER TABLE device 
ADD CONSTRAINT fk_device_user FOREIGN KEY (deviceyh_id) REFERENCES sys_user(id);
