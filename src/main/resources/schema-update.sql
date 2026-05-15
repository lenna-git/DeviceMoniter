-- 创建设备状态表
CREATE TABLE IF NOT EXISTS devicestate (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    state_detail VARCHAR(100) NOT NULL
);

-- 插入设备状态数据
INSERT INTO devicestate (id, state_detail) VALUES (1, '已录入待安检');
INSERT INTO devicestate (id, state_detail) VALUES (2, '已安检待借用');
INSERT INTO devicestate (id, state_detail) VALUES (3, '借用中待通过');
INSERT INTO devicestate (id, state_detail) VALUES (4, '借用中');
INSERT INTO devicestate (id, state_detail) VALUES (5, '借出中待修理');
INSERT INTO devicestate (id, state_detail) VALUES (6, '修理中');
INSERT INTO devicestate (id, state_detail) VALUES (7, '转借中待转借人通过');
INSERT INTO devicestate (id, state_detail) VALUES (8, '申请归还中待通过');
INSERT INTO devicestate (id, state_detail) VALUES (9, '已下架');
INSERT INTO devicestate (id, state_detail) VALUES (10, '已退回');
INSERT INTO devicestate (id, state_detail) VALUES (11, '转借中待管理员通过');

-- 修改 Device 表，添加外键字段
ALTER TABLE device 
ADD COLUMN devicestate_id BIGINT NULL;

-- 添加外键约束
ALTER TABLE device 
ADD CONSTRAINT fk_device_state FOREIGN KEY (devicestate_id) REFERENCES devicestate(id);

-- 更新现有数据的状态字段（设置默认状态为"已录入待安检"）
UPDATE device SET devicestate_id = 1 WHERE devicestate_id IS NULL;

-- 删除原来的字符串字段（可选，根据需要）
-- ALTER TABLE device DROP COLUMN devicestate;