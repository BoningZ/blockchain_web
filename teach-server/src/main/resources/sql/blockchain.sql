/*
 Navicat Premium Data Transfer

 Source Server         : localmysql
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : blockchain

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 29/06/2023 11:36:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `aid` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `user_id` int(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK1xup2h5adgbt0uiwqteihl8he`(`aid`) USING BTREE,
  INDEX `admin_user`(`user_id`) USING BTREE,
  CONSTRAINT `admin_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, '1919810', '测试管理员1', 2);
INSERT INTO `admin` VALUES (2, '5411145', '测试管理员2', 3);

-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `description` varchar(300) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `operate_state` int(0) NOT NULL,
  `operate_time` datetime(6) NOT NULL,
  `operate_id` int(0) NOT NULL,
  `user_id` int(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKgo6y2vee44qlswythsuhekc5n`(`operate_id`) USING BTREE,
  INDEX `FK3wxdofviqe2smmvh1w1yf98o1`(`user_id`) USING BTREE,
  CONSTRAINT `FK3wxdofviqe2smmvh1w1yf98o1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKgo6y2vee44qlswythsuhekc5n` FOREIGN KEY (`operate_id`) REFERENCES `operate` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of log
-- ----------------------------

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `mid` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `user_id` int(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UKjb2n85855mxchcq7a6qqby66s`(`mid`) USING BTREE,
  INDEX `member_user`(`user_id`) USING BTREE,
  CONSTRAINT `member_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of member
-- ----------------------------
INSERT INTO `member` VALUES (1, '114514', '测试成员2', 1);

-- ----------------------------
-- Table structure for operate
-- ----------------------------
DROP TABLE IF EXISTS `operate`;
CREATE TABLE `operate`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of operate
-- ----------------------------

-- ----------------------------
-- Table structure for right
-- ----------------------------
DROP TABLE IF EXISTS `right`;
CREATE TABLE `right`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) NULL DEFAULT NULL,
  `name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `update_time` datetime(6) NULL DEFAULT NULL,
  `admin_id` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK_right_admin`(`admin_id`) USING BTREE,
  CONSTRAINT `FK_right_admin` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of right
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `depth` int(0) NOT NULL,
  `icon` varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `order_in_siblings` int(0) NOT NULL,
  `url` varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `parent_id` int(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK2jrf4gb0gjqi8882gxytpxnhe`(`parent_id`) USING BTREE,
  CONSTRAINT `FK2jrf4gb0gjqi8882gxytpxnhe` FOREIGN KEY (`parent_id`) REFERENCES `sys_menu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 1, '', '用户资料', 100, 'MemberProfile', 1);
INSERT INTO `sys_menu` VALUES (2, 1, NULL, '修改密码', 102, 'ChangePassword', 2);
INSERT INTO `sys_menu` VALUES (3, 1, NULL, '管理员资料', 101, 'AdminProfile', 3);

-- ----------------------------
-- Table structure for type_menu
-- ----------------------------
DROP TABLE IF EXISTS `type_menu`;
CREATE TABLE `type_menu`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `sys_menu_id` int(0) NOT NULL,
  `user_type_id` int(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKj2qksumdqehapivmmhqv6vu2y`(`sys_menu_id`) USING BTREE,
  INDEX `FKyrcq9yvih21mlxfbtqeaymkm`(`user_type_id`) USING BTREE,
  CONSTRAINT `FKj2qksumdqehapivmmhqv6vu2y` FOREIGN KEY (`sys_menu_id`) REFERENCES `sys_menu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKyrcq9yvih21mlxfbtqeaymkm` FOREIGN KEY (`user_type_id`) REFERENCES `user_type` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of type_menu
-- ----------------------------
INSERT INTO `type_menu` VALUES (1, 1, 1);
INSERT INTO `type_menu` VALUES (2, 2, 1);
INSERT INTO `type_menu` VALUES (3, 2, 2);
INSERT INTO `type_menu` VALUES (4, 3, 2);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int(0) NOT NULL AUTO_INCREMENT,
  `last_login_time` datetime(6) NULL DEFAULT NULL,
  `login_count` int(0) NULL DEFAULT NULL,
  `password` varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `username` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `user_type_id` int(0) NULL DEFAULT NULL,
  `join_time` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `UKsb8bbouer5wak8vyiiy4pf2bx`(`username`) USING BTREE,
  INDEX `FKlrk9xrdps0emd6d5rx5x3ib6h`(`user_type_id`) USING BTREE,
  CONSTRAINT `FKlrk9xrdps0emd6d5rx5x3ib6h` FOREIGN KEY (`user_type_id`) REFERENCES `user_type` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '2023-06-28 19:44:15.585000', 1, '$2a$10$M4IyU4/K17WCCuY0i/3qdu0a2LRmrCrm7oGJfDu/CgB.IXgLLNkfe', 'Test_member1', 1, NULL);
INSERT INTO `user` VALUES (2, '2023-06-29 10:16:59.757000', 2, '$2a$10$aXqDWP5AUGOgJqAIuYlQdOrZs4kJdO0JRDxBn64yGOjXK6yCyajCm', 'Test_admin1', 2, NULL);
INSERT INTO `user` VALUES (3, '2023-06-29 10:17:34.544000', 1, '$2a$10$X5Ryf/pgTTAlu6.iwn8AIej7.6t/k0fLw2pIxReBqLo6fnWZF9/tG', 'Test_admin2', 2, NULL);

-- ----------------------------
-- Table structure for user_right
-- ----------------------------
DROP TABLE IF EXISTS `user_right`;
CREATE TABLE `user_right`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) NULL DEFAULT NULL,
  `update_time` datetime(6) NULL DEFAULT NULL,
  `member_id` int(0) NOT NULL,
  `right_id` int(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UKiijer69i6th4kh9qdmvxfkq81`(`member_id`, `right_id`) USING BTREE,
  INDEX `FK_USER_RIGHT`(`right_id`) USING BTREE,
  CONSTRAINT `FKtghim00w97ans7xwqu2fotxgg` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_USER_RIGHT` FOREIGN KEY (`right_id`) REFERENCES `right` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_right
-- ----------------------------

-- ----------------------------
-- Table structure for user_type
-- ----------------------------
DROP TABLE IF EXISTS `user_type`;
CREATE TABLE `user_type`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_type
-- ----------------------------
INSERT INTO `user_type` VALUES (1, 'ROLE_MEMBER');
INSERT INTO `user_type` VALUES (2, 'ROLE_ADMIN');

SET FOREIGN_KEY_CHECKS = 1;
