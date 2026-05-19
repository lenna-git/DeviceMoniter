/*
 Navicat Premium Dump SQL

 Source Server         : 本机
 Source Server Type    : MySQL
 Source Server Version : 80041 (8.0.41)
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 80041 (8.0.41)
 File Encoding         : 65001

 Date: 19/05/2026 16:02:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for devcpu
-- ----------------------------
DROP TABLE IF EXISTS `devcpu`;
CREATE TABLE `devcpu`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cpuname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK7y067bcrgjqthlktc5f86qekf`(`cpuname` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of devcpu
-- ----------------------------
INSERT INTO `devcpu` VALUES (1, '龙芯3A5000', '龙芯3A5000');
INSERT INTO `devcpu` VALUES (2, '飞腾D2000', '飞腾D2000');

-- ----------------------------
-- Table structure for device
-- ----------------------------
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `deviceajdata` datetime NULL DEFAULT '2001-02-02 12:00:00',
  `deviceghdata` datetime NULL DEFAULT '2002-03-03 18:30:00',
  `deviceno` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `devicescdata` datetime NULL DEFAULT '2000-01-01 00:00:00',
  `devicesn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `devtype_id` bigint NULL DEFAULT NULL,
  `devcpu_id` bigint NULL DEFAULT NULL,
  `devmanufacturer_id` bigint NULL DEFAULT NULL,
  `devicestate_id` bigint NULL DEFAULT NULL,
  `devicexh` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `deviceyh_id` bigint NULL DEFAULT NULL,
  `transfer_target_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKf5otfppdt50bit2h8imn0n0wm`(`devtype_id` ASC) USING BTREE,
  INDEX `FKjwaq8d84ra4a3ct63ido57ntm`(`devcpu_id` ASC) USING BTREE,
  INDEX `FKhw95hhc4y7biuq23s0j8b8w06`(`devmanufacturer_id` ASC) USING BTREE,
  INDEX `FKsyhtoevasmdtxpp8kof5yubcl`(`devicestate_id` ASC) USING BTREE,
  INDEX `FKcfejtipq74dc99bv934e8lvd5`(`deviceyh_id` ASC) USING BTREE,
  CONSTRAINT `FKcfejtipq74dc99bv934e8lvd5` FOREIGN KEY (`deviceyh_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKf5otfppdt50bit2h8imn0n0wm` FOREIGN KEY (`devtype_id`) REFERENCES `devtype` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKhw95hhc4y7biuq23s0j8b8w06` FOREIGN KEY (`devmanufacturer_id`) REFERENCES `devmanufacturer` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKjwaq8d84ra4a3ct63ido57ntm` FOREIGN KEY (`devcpu_id`) REFERENCES `devcpu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKsyhtoevasmdtxpp8kof5yubcl` FOREIGN KEY (`devicestate_id`) REFERENCES `devicestate` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 110 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device
-- ----------------------------

-- ----------------------------
-- Table structure for device_record
-- ----------------------------
DROP TABLE IF EXISTS `device_record`;
CREATE TABLE `device_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `borror_date` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `return_date` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `user_id` bigint NULL DEFAULT NULL,
  `device_id` bigint NULL DEFAULT NULL,
  `sys_user_id` bigint NULL DEFAULT NULL,
  `approval_date` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `return_approval_date` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `return_approval_user_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKn0yqsi74inipgfphg5gjdfv3c`(`device_id` ASC) USING BTREE,
  INDEX `FKd76n26itx0hiehshykp5xc9vw`(`sys_user_id` ASC) USING BTREE,
  CONSTRAINT `FKd76n26itx0hiehshykp5xc9vw` FOREIGN KEY (`sys_user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKn0yqsi74inipgfphg5gjdfv3c` FOREIGN KEY (`device_id`) REFERENCES `device` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device_record
-- ----------------------------

-- ----------------------------
-- Table structure for device_repair
-- ----------------------------
DROP TABLE IF EXISTS `device_repair`;
CREATE TABLE `device_repair`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `end_repair_time` datetime(6) NULL DEFAULT NULL,
  `repair_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `repair_record` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `repair_time` datetime(6) NOT NULL,
  `device_id` bigint NOT NULL,
  `repair_person_id` bigint NULL DEFAULT NULL,
  `reporter_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK5ndc6knax8vy3hlfa65729hjn`(`device_id` ASC) USING BTREE,
  CONSTRAINT `FK5ndc6knax8vy3hlfa65729hjn` FOREIGN KEY (`device_id`) REFERENCES `device` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device_repair
-- ----------------------------

-- ----------------------------
-- Table structure for device_transfer_record
-- ----------------------------
DROP TABLE IF EXISTS `device_transfer_record`;
CREATE TABLE `device_transfer_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `admin_approval_date` datetime(6) NULL DEFAULT NULL,
  `approval_date` datetime(6) NULL DEFAULT NULL,
  `detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT NULL COMMENT 'Status 1（申请中） ：借用人发起转借申请，等待新借用人确认是否同意接收该设备。\n\nStatus 2（新借用人已同意） ：新借用人已同意接收该设备，等待管理员最终审批确认。\n\nStatus 3（管理员已同意） ：管理员已批准转借申请，转借流程完成，设备正式归属新借用人使用。\n\nStatus 4（已拒绝） ：转借申请被新借用人或管理员拒绝，设备仍归原借用人使用。',
  `transfer_date` datetime(6) NULL DEFAULT NULL,
  `admin_approval_user_id` bigint NULL DEFAULT NULL,
  `device_id` bigint NULL DEFAULT NULL,
  `from_user_id` bigint NULL DEFAULT NULL,
  `to_user_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKiamudulleowmx2c6xcs8du813`(`admin_approval_user_id` ASC) USING BTREE,
  INDEX `FK96cdwhsm3tflidwu3897fwlbd`(`device_id` ASC) USING BTREE,
  INDEX `FKm4rbd228iodvk6x2xsixjfcun`(`from_user_id` ASC) USING BTREE,
  INDEX `FKseihedfxbob6u7q4gsqj3dj3r`(`to_user_id` ASC) USING BTREE,
  CONSTRAINT `FK96cdwhsm3tflidwu3897fwlbd` FOREIGN KEY (`device_id`) REFERENCES `device` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKiamudulleowmx2c6xcs8du813` FOREIGN KEY (`admin_approval_user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKm4rbd228iodvk6x2xsixjfcun` FOREIGN KEY (`from_user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKseihedfxbob6u7q4gsqj3dj3r` FOREIGN KEY (`to_user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device_transfer_record
-- ----------------------------

-- ----------------------------
-- Table structure for devicestate
-- ----------------------------
DROP TABLE IF EXISTS `devicestate`;
CREATE TABLE `devicestate`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `state_detail` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of devicestate
-- ----------------------------
INSERT INTO `devicestate` VALUES (1, '已录入待安检');
INSERT INTO `devicestate` VALUES (2, '已安检待借用');
INSERT INTO `devicestate` VALUES (3, '借用中待通过');
INSERT INTO `devicestate` VALUES (4, '借用中');
INSERT INTO `devicestate` VALUES (5, '借出中待修理');
INSERT INTO `devicestate` VALUES (6, '修理中');
INSERT INTO `devicestate` VALUES (7, '转借中待转借人通过');
INSERT INTO `devicestate` VALUES (8, '申请归还中待通过');
INSERT INTO `devicestate` VALUES (9, '已下架');
INSERT INTO `devicestate` VALUES (10, '已退回');
INSERT INTO `devicestate` VALUES (11, '转借中待管理员批准');

-- ----------------------------
-- Table structure for devmanufacturer
-- ----------------------------
DROP TABLE IF EXISTS `devmanufacturer`;
CREATE TABLE `devmanufacturer`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `manufacturername` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UKkst159n5vbarfte01sl1ly4k9`(`manufacturername` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of devmanufacturer
-- ----------------------------
INSERT INTO `devmanufacturer` VALUES (1, NULL, '同方');
INSERT INTO `devmanufacturer` VALUES (2, NULL, '中兴');
INSERT INTO `devmanufacturer` VALUES (3, NULL, '浪潮');
INSERT INTO `devmanufacturer` VALUES (4, NULL, '联想');

-- ----------------------------
-- Table structure for devtype
-- ----------------------------
DROP TABLE IF EXISTS `devtype`;
CREATE TABLE `devtype`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `typename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UKjaxnarmoyeev8smj2n2eh9hss`(`typename` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of devtype
-- ----------------------------
INSERT INTO `devtype` VALUES (1, '台式机', '台式机');
INSERT INTO `devtype` VALUES (2, '笔记本', '笔记本电脑');
INSERT INTO `devtype` VALUES (3, '服务器', '服务器');

-- ----------------------------
-- Table structure for password_reset_token
-- ----------------------------
DROP TABLE IF EXISTS `password_reset_token`;
CREATE TABLE `password_reset_token`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `expiry_date` datetime(6) NOT NULL,
  `token` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `used` bit(1) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UKg0guo4k8krgpwuagos61oc06j`(`token` ASC) USING BTREE,
  INDEX `FK5hogdhidcrr50bdo347pmpv0t`(`user_id` ASC) USING BTREE,
  CONSTRAINT `FK5hogdhidcrr50bdo347pmpv0t` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of password_reset_token
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sysusername` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sysuserpassword` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sysuserrole` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin1', '$2a$10$Tow5d9y8HLzszQPY9.vDp.IW8Ch95FwyJ7Cfm3esLWxiM2RSH.k8W', 1);

SET FOREIGN_KEY_CHECKS = 1;
