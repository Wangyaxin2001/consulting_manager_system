/*
 Navicat Premium Data Transfer

 Source Server         : MySql
 Source Server Type    : MySQL
 Source Server Version : 50529
 Source Host           : localhost:3306
 Source Schema         : 230308-cms

 Target Server Type    : MySQL
 Target Server Version : 50529
 File Encoding         : 65001

 Date: 07/03/2023 10:07:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cms_slideshow
-- ----------------------------
DROP TABLE IF EXISTS `cms_slideshow`;
CREATE TABLE `cms_slideshow`  (
  `id` int(255) NOT NULL AUTO_INCREMENT COMMENT '轮播图编号',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '轮播图片url',
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '启用' COMMENT '图片状态',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  `upload_time` datetime NOT NULL COMMENT '上传时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

insert into cms_slideshow
values(1,'风景图片','http://pic1.win4000.com/wallpaper/2019-07-19/5d31808febeba.jpg','启用',0,'2023-01-12 12:00:00');
insert into cms_slideshow
values(2,'人物','https://img.shuicaimi.com/c2021/09/26/eoz4muma3mw.jpg','启用',0,'2023-03-07 12:00:00');
insert into cms_slideshow
values(3,'搞笑','https://lmg.jj20.com/up/allimg/tp03/1Z9201IJ360U-0-lp.jpg','启用',0,'2023-03-06 12:00:00');

-- ----------------------------
-- Table structure for cms_role
-- ----------------------------
DROP TABLE IF EXISTS `cms_role`;
CREATE TABLE `cms_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色描述',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE COMMENT '角色名唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of cms_role
-- ----------------------------
INSERT INTO `cms_role` VALUES (1, '超级管理员', '管理所有用户，审核用户发表的资讯文章，栏目、资讯、评论管理');
INSERT INTO `cms_role` VALUES (2, '普通管理员', '管理普通用户，审核用户发表的资讯文章，栏目、资讯、评论管理');
INSERT INTO `cms_role` VALUES (3, '普通用户', '浏览、发布、评论文章');

-- ----------------------------
-- Table structure for cms_user
-- ----------------------------
DROP TABLE IF EXISTS `cms_user`;
CREATE TABLE `cms_user`  (
  `id` bigint(20) NOT NULL COMMENT '用户id',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名称',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户密码',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `gender` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '男' COMMENT '性别',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户电话',
  `register_time` datetime NOT NULL COMMENT '注册时间',
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '启用' COMMENT '用户状态',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `role_id` int(11) NOT NULL DEFAULT 3 COMMENT '角色id',
  `is_vip` int(1) NOT NULL DEFAULT 0 COMMENT '是否为会员',
  `expires_time` datetime NULL DEFAULT NULL COMMENT '会员到期时间',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '用户删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;


-- ----------------------------
-- Table structure for cms_category
-- ----------------------------
DROP TABLE IF EXISTS `cms_category`;
CREATE TABLE `cms_category`  (
  `id` int(255) NOT NULL AUTO_INCREMENT COMMENT '栏目编号',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '栏目名称',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '栏目描述',
  `order_num` int(255) NOT NULL COMMENT '栏目序号',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '栏目删除状态',
  `parent_id` int(255) COMMENT '父栏目id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;


-- ----------------------------
-- Table structure for cms_article
-- ----------------------------
DROP TABLE IF EXISTS `cms_article`;
CREATE TABLE `cms_article`  (
  `id` bigint(20) NOT NULL COMMENT '文章id',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文章标题',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文章内容',
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '未审核' COMMENT '文章审核状态',
  `read_num` int(255) NULL DEFAULT 0 COMMENT '阅读量',
  `like_num` int(255) NULL DEFAULT 0 COMMENT '点赞量',
  `dislike_num` int(255) NULL DEFAULT 0 COMMENT '拉踩量',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `category_id` int(255) NOT NULL COMMENT '类别id',
  `charged` int(1) NOT NULL DEFAULT 0 COMMENT '是否收费，默认0不收费',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '文章删除状态',
  `publish_time` datetime NOT NULL COMMENT '文章发表时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;


-- ----------------------------
-- Table structure for cms_fullcomment
-- ----------------------------
/*
DROP TABLE IF EXISTS `cms_fullcomment`;
CREATE TABLE `cms_fullcomment`  (
  `id` bigint(20) NOT NULL COMMENT '评论id',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评论内容',
  `publish_time` datetime NOT NULL COMMENT '评论发表时间',
  `user_id` bigint(20) NOT NULL COMMENT '评论所属用户id',
  `article_id` bigint(20) NOT NULL COMMENT '评论所属文章id',
  `parent_id` bigint(20) COMMENT '二级评论所属父评论id',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  `reply_id` bigint(20) COMMENT '回复评论id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;
*/

-- ----------------------------
-- Table structure for cms_comment
-- ----------------------------
DROP TABLE IF EXISTS `cms_comment`;
CREATE TABLE `cms_comment`  (
  `id` bigint(20) NOT NULL,
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `publish_time` datetime NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `article_id` bigint(20) NOT NULL,
  `deleted` int(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;


DROP TABLE IF EXISTS `cms_subcomment`;
CREATE TABLE `cms_subcomment`  (
  `id` bigint(20) NOT NULL,
  `content` varchar(255) NOT NULL,
  `publish_time` datetime NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `parent_id` bigint(20) NOT NULL COMMENT '一级评论id',
  `reply_id` bigint(20) COMMENT '回复评论id',
  `deleted` int(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

DROP TABLE IF EXISTS `cms_log`;
CREATE TABLE `cms_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问用户账号',
  `business_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口描述信息',
  `request_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求的地址',
  `request_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求的方式，get post delete put',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip',
  `source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip来源',
  `spend_time` bigint(20) NULL DEFAULT NULL COMMENT '请求接口耗时',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `params_json` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求的参数',
  `result_json` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '响应参数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


SET FOREIGN_KEY_CHECKS = 1;
