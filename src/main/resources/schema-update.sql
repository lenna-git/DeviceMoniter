-- 先查看现有数据中不符合日期格式的记录
SELECT id, devicescdata, deviceajdata, deviceghdata 
FROM device 
WHERE deviceajdata NOT REGEXP '^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$'
   OR devicescdata NOT REGEXP '^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$'
   OR deviceghdata NOT REGEXP '^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$';

-- 将无效的日期数据更新为默认值
UPDATE device 
SET devicescdata = '2000-01-01 00:00:00' 
WHERE devicescdata IS NULL OR devicescdata NOT REGEXP '^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$';

UPDATE device 
SET deviceajdata = '2001-02-02 12:00:00' 
WHERE deviceajdata IS NULL OR deviceajdata NOT REGEXP '^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$';

UPDATE device 
SET deviceghdata = '2002-03-03 18:30:00' 
WHERE deviceghdata IS NULL OR deviceghdata NOT REGEXP '^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$';

-- 然后修改字段类型
ALTER TABLE device 
CHANGE COLUMN devicescdata devicescdata DATETIME NULL DEFAULT '2000-01-01 00:00:00',
CHANGE COLUMN deviceajdata deviceajdata DATETIME NULL DEFAULT '2001-02-02 12:00:00',
CHANGE COLUMN deviceghdata deviceghdata DATETIME NULL DEFAULT '2002-03-03 18:30:00';