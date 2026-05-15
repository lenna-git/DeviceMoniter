-- 创建设备维修表
CREATE TABLE IF NOT EXISTS device_repair (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    device_id BIGINT NOT NULL,
    repair_time DATETIME NOT NULL,
    end_repair_time DATETIME NULL,
    repair_reason VARCHAR(500) NULL,
    repair_record VARCHAR(1000) NULL,
    FOREIGN KEY (device_id) REFERENCES device(id)
);