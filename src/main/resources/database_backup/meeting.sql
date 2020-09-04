/*
 Navicat Premium Data Transfer

 Source Server         : 39.108.158.74_3306
 Source Server Type    : MySQL
 Source Server Version : 50728
 Source Host           : 39.108.158.74:3306
 Source Schema         : meeting

 Target Server Type    : MySQL
 Target Server Version : 50728
 File Encoding         : 65001

 Date: 05/09/2020 00:14:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for driver
-- ----------------------------
DROP TABLE IF EXISTS `driver`;
CREATE TABLE `driver`  (
  `driver_id` int(11) NOT NULL AUTO_INCREMENT,
  `car_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车牌号',
  `driver_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '司机姓名',
  `driver_tel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '司机电话',
  `car_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车型',
  `meeting_id` int(11) NOT NULL,
  `is_arrenge` int(1) NULL DEFAULT 1 COMMENT '是否分配，未分配为1，已分配为0',
  PRIMARY KEY (`driver_id`, `meeting_id`) USING BTREE,
  INDEX `c11`(`meeting_id`) USING BTREE,
  INDEX `driver_id`(`driver_id`) USING BTREE,
  CONSTRAINT `c11` FOREIGN KEY (`meeting_id`) REFERENCES `meeting` (`meetingid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of driver
-- ----------------------------
INSERT INTO `driver` VALUES (1, '渝C A42769', '张三', '15039465896', '商务车', 1, NULL);
INSERT INTO `driver` VALUES (3, '123456', 'jjj', '13216546513', 'SUV', 1, NULL);
INSERT INTO `driver` VALUES (4, '6413232', 'asdasd', '65123121', 'suv', 1, NULL);
INSERT INTO `driver` VALUES (5, '1324864132', 'asdsad', '15231651', '轿车', 1, NULL);

-- ----------------------------
-- Table structure for driver_guest_arengement
-- ----------------------------
DROP TABLE IF EXISTS `driver_guest_arengement`;
CREATE TABLE `driver_guest_arengement`  (
  `arg_id` int(11) NOT NULL AUTO_INCREMENT,
  `driver_id` int(11) NULL DEFAULT NULL,
  `guest_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`arg_id`) USING BTREE,
  INDEX `d2`(`driver_id`) USING BTREE,
  INDEX `g2`(`guest_id`) USING BTREE,
  CONSTRAINT `d2` FOREIGN KEY (`driver_id`) REFERENCES `driver` (`driver_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `g2` FOREIGN KEY (`guest_id`) REFERENCES `guest` (`guestid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of driver_guest_arengement
-- ----------------------------
INSERT INTO `driver_guest_arengement` VALUES (4, 1, 15);
INSERT INTO `driver_guest_arengement` VALUES (5, 1, 15);
INSERT INTO `driver_guest_arengement` VALUES (6, 1, 15);
INSERT INTO `driver_guest_arengement` VALUES (7, 3, 15);
INSERT INTO `driver_guest_arengement` VALUES (8, 1, 15);
INSERT INTO `driver_guest_arengement` VALUES (9, 3, 15);
INSERT INTO `driver_guest_arengement` VALUES (10, 4, 15);

-- ----------------------------
-- Table structure for guest
-- ----------------------------
DROP TABLE IF EXISTS `guest`;
CREATE TABLE `guest`  (
  `guestid` int(11) NOT NULL AUTO_INCREMENT COMMENT '嘉宾id',
  `meetingid` int(11) NOT NULL COMMENT '对应的会议id',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '嘉宾图片地址',
  `introduction` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '嘉宾简介',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名字',
  `position` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '职位',
  `flight_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '航班信息',
  `person_id` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `guest_tel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话',
  PRIMARY KEY (`guestid`, `meetingid`) USING BTREE,
  INDEX `g1`(`meetingid`) USING BTREE,
  INDEX `guestid`(`guestid`) USING BTREE,
  CONSTRAINT `g1` FOREIGN KEY (`meetingid`) REFERENCES `meeting` (`meetingid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of guest
-- ----------------------------
INSERT INTO `guest` VALUES (15, 1, 'c5f822d4-9fd5-4a24-a7c8-42ed4bad74c5.jpg', '重庆大学某学生', 'zao', '教授', '北京-》重庆', '132165642313165', '16165445611');
INSERT INTO `guest` VALUES (16, 1, NULL, 'asdasdasd', 'asd', 'asdasd', 'asfasfasd', '232131321', '1321321');
INSERT INTO `guest` VALUES (17, 1, NULL, '啊实打实大苏打', 'zao', '教授', 'sadasDsad4asd', '12321315613213', '1512121');

-- ----------------------------
-- Table structure for meeting
-- ----------------------------
DROP TABLE IF EXISTS `meeting`;
CREATE TABLE `meeting`  (
  `meetingid` int(11) NOT NULL AUTO_INCREMENT,
  `m_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会议名称',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '地点',
  `start_time` timestamp(6) NULL DEFAULT NULL COMMENT '开始时间',
  `close_time` timestamp(6) NULL DEFAULT NULL COMMENT '结束时间',
  `introduction` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '简介信息',
  `schedule` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日程信息',
  `needvolunteer` int(1) NULL DEFAULT NULL COMMENT '是否需要志愿者，0不需要、1需要',
  `typeid` int(8) NULL DEFAULT NULL COMMENT '会议类型的id，为meetingtype的主键',
  `organizer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主办单位',
  `hostedby` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '承办单位',
  `communicate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主办方联系方式',
  PRIMARY KEY (`meetingid`) USING BTREE,
  INDEX `typeid`(`typeid`) USING BTREE,
  CONSTRAINT `meeting_ibfk_1` FOREIGN KEY (`typeid`) REFERENCES `meetingtype` (`typeid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 150 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of meeting
-- ----------------------------
INSERT INTO `meeting` VALUES (1, 'cccc', 'dddd', '2019-12-21 22:11:03.000000', '2019-12-28 19:01:00.000000', 'fdfdfdfd', 'fffff', 0, 1, NULL, NULL, '123123123');
INSERT INTO `meeting` VALUES (133, 'asd', 'asd', '2020-06-04 00:00:00.000000', '2020-06-10 00:00:00.000000', 'asd', 'asd', NULL, NULL, '', NULL, '');
INSERT INTO `meeting` VALUES (134, 'test', 'asdasd', '2020-06-25 00:00:00.000000', '2020-06-26 00:00:00.000000', 'noa\'s', 'as啊实打实', 10, NULL, '啊实打实的', NULL, '1515151');
INSERT INTO `meeting` VALUES (135, 'nxp比赛', '重庆', '2020-07-03 00:00:00.000000', '2021-06-16 00:00:00.000000', '撒大苏打实打实的阿斯顿啊水水大苏打阿斯顿阿斯顿阿三的空间咯技术的喀什看来大家 撒打算肯定就卡死经济ask贷记卡上课阿斯顿阿三贷记卡时间打开拉萨', '撒打算阿三打算大撒d \n阿斯顿阿斯顿阿三打算d\n阿三打算大撒大撒d', NULL, NULL, 'nxp', NULL, '16512132165');
INSERT INTO `meeting` VALUES (136, 'nxp比赛', '重庆', '2020-07-03 00:00:00.000000', '2021-06-16 00:00:00.000000', '撒大苏打实打实的阿斯顿啊水水大苏打阿斯顿阿斯顿阿三的空间咯技术的喀什看来大家 撒打算肯定就卡死经济ask贷记卡上课阿斯顿阿三贷记卡时间打开拉萨', '撒打算阿三打算大撒d \n阿斯顿阿斯顿阿三打算d\n阿三打算大撒大撒d', 10, NULL, 'nxp', NULL, '16512132165');
INSERT INTO `meeting` VALUES (138, 'sadsd', 'asdasd', '2020-06-18 00:00:00.000000', '2020-06-30 00:00:00.000000', 'sadasd', 'asdasd', 10, NULL, 'asd', NULL, 'asdasd');
INSERT INTO `meeting` VALUES (139, 'sadsd', 'asdasd', '2020-06-18 00:00:00.000000', '2020-06-30 00:00:00.000000', 'sadasd', 'asdasd', 10, NULL, 'asd', NULL, 'asdasd');
INSERT INTO `meeting` VALUES (140, '212131', '1321321', '2020-06-12 00:00:00.000000', '2020-06-18 00:00:00.000000', 'asd', 'asd', 10, NULL, 'asd', NULL, '13131231');
INSERT INTO `meeting` VALUES (141, 'asdasd', 'asdasd', '2020-06-25 00:00:00.000000', '2020-06-18 00:00:00.000000', 'sadasd', 'sadasd', NULL, NULL, 'asdasd', NULL, 'asdasda');
INSERT INTO `meeting` VALUES (142, 'asdasd', 'asdasd', '2020-06-25 00:00:00.000000', '2020-06-18 00:00:00.000000', 'sadasd', 'sadasd', NULL, NULL, 'asdasd', NULL, 'asdasda');
INSERT INTO `meeting` VALUES (143, 'asdasdasd', 'asdasdasd', '2020-06-25 00:00:00.000000', '2020-06-30 00:00:00.000000', 'sadasdasdasd', 'sadasd', NULL, NULL, 'asdasd', NULL, 'asdasda');
INSERT INTO `meeting` VALUES (144, 'asdasdasd', 'asdasdasd', '2020-06-25 00:00:00.000000', '2020-06-30 00:00:00.000000', 'sadasdasdasd', 'sadasd', NULL, NULL, 'asdasd', NULL, 'asdasda');
INSERT INTO `meeting` VALUES (145, 'jsandkn', 'asdnalskd', '2020-06-10 00:00:00.000000', '2020-06-24 00:00:00.000000', 'sadkasndn', 'sadklnasndkl', NULL, NULL, 'asdasd', NULL, '15015121212');
INSERT INTO `meeting` VALUES (146, 'jsandkn', 'asdnalskd', '2020-06-10 00:00:00.000000', '2020-06-24 00:00:00.000000', 'sadkasndn', 'sadklnasndkl', 10, NULL, 'asdasd', NULL, '15015121212');
INSERT INTO `meeting` VALUES (147, 'jsandkn', 'asdnalskd', '2020-06-10 00:00:00.000000', '2020-06-24 00:00:00.000000', 'sadkasndn', 'sadklnasndkl', 10, NULL, 'asdasd', NULL, '15015121212');
INSERT INTO `meeting` VALUES (148, '阿手机电脑', '阿三开家里的卡上', '2020-09-09 00:00:00.000000', '2020-09-17 00:00:00.000000', '撒打算的', '阿斯顿阿斯顿', NULL, NULL, '阿斯顿卡上了', NULL, '爱睡觉的卡和手机');
INSERT INTO `meeting` VALUES (149, '阿手机电脑', '阿三开家里的卡上', '2020-09-09 00:00:00.000000', '2020-09-17 00:00:00.000000', '撒打算的', '阿斯顿阿斯顿', NULL, NULL, '阿斯顿卡上了', NULL, '爱睡觉的卡和手机');

-- ----------------------------
-- Table structure for meeting_sign_in_table
-- ----------------------------
DROP TABLE IF EXISTS `meeting_sign_in_table`;
CREATE TABLE `meeting_sign_in_table`  (
  `meetingId` int(11) NOT NULL,
  `sign_in_start_time` datetime(0) NULL DEFAULT NULL,
  `special_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`meetingId`) USING BTREE,
  CONSTRAINT `sign_m1` FOREIGN KEY (`meetingId`) REFERENCES `meeting` (`meetingid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of meeting_sign_in_table
-- ----------------------------
INSERT INTO `meeting_sign_in_table` VALUES (1, '2020-06-12 17:00:00', 'afd162318d3d6a016eb336d40d5466e6');

-- ----------------------------
-- Table structure for meeting_sign_in_user
-- ----------------------------
DROP TABLE IF EXISTS `meeting_sign_in_user`;
CREATE TABLE `meeting_sign_in_user`  (
  `meeting_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `sign_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`meeting_id`, `user_id`) USING BTREE,
  INDEX `si_u1`(`user_id`) USING BTREE,
  CONSTRAINT `si_m1` FOREIGN KEY (`meeting_id`) REFERENCES `meeting` (`meetingid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `si_u1` FOREIGN KEY (`user_id`) REFERENCES `user` (`userid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of meeting_sign_in_user
-- ----------------------------
INSERT INTO `meeting_sign_in_user` VALUES (1, 25, '2020-05-22 18:30:53');

-- ----------------------------
-- Table structure for meetingfile
-- ----------------------------
DROP TABLE IF EXISTS `meetingfile`;
CREATE TABLE `meetingfile`  (
  `fileid` int(11) NOT NULL AUTO_INCREMENT,
  `meetingid` int(11) NOT NULL COMMENT '会议id',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件路径',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  PRIMARY KEY (`fileid`) USING BTREE,
  INDEX `mf1`(`meetingid`) USING BTREE,
  CONSTRAINT `mf1` FOREIGN KEY (`meetingid`) REFERENCES `meeting` (`meetingid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of meetingfile
-- ----------------------------
INSERT INTO `meetingfile` VALUES (15, 1, '/usr/local/MeetingFile/1/code-wallpaper-18.png', 'code-wallpaper-18.png');
INSERT INTO `meetingfile` VALUES (16, 1, '/usr/local/MeetingFile/1/834383.jpg', '834383.jpg');
INSERT INTO `meetingfile` VALUES (17, 134, '/usr/local/MeetingFile/134/245484 (2).jpg', '245484 (2).jpg');
INSERT INTO `meetingfile` VALUES (18, 1, '/usr/local/MeetingFile/1/389474.png', '389474.png');
INSERT INTO `meetingfile` VALUES (19, 1, '/usr/local/MeetingFile/1/261652.jpg', '261652.jpg');
INSERT INTO `meetingfile` VALUES (20, 133, '/usr/local/MeetingFile/133/login-bac.png', 'login-bac.png');
INSERT INTO `meetingfile` VALUES (21, 134, '/usr/local/MeetingFile/134/zao 2.png', 'zao 2.png');
INSERT INTO `meetingfile` VALUES (22, 133, '/usr/local/MeetingFile/133/zao 2.png', 'zao 2.png');

-- ----------------------------
-- Table structure for meetingtask
-- ----------------------------
DROP TABLE IF EXISTS `meetingtask`;
CREATE TABLE `meetingtask`  (
  `id` int(11) NOT NULL,
  `meetingid` int(11) NULL DEFAULT NULL COMMENT '会议Id',
  `taskid` int(11) NULL DEFAULT NULL COMMENT '工作ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `mt1`(`meetingid`) USING BTREE,
  CONSTRAINT `mt1` FOREIGN KEY (`meetingid`) REFERENCES `meeting` (`meetingid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for meetingtype
-- ----------------------------
DROP TABLE IF EXISTS `meetingtype`;
CREATE TABLE `meetingtype`  (
  `typeid` int(8) NOT NULL AUTO_INCREMENT COMMENT '会议分类id',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会议分类，具体描述',
  PRIMARY KEY (`typeid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of meetingtype
-- ----------------------------
INSERT INTO `meetingtype` VALUES (1, '学术');
INSERT INTO `meetingtype` VALUES (2, '分享');
INSERT INTO `meetingtype` VALUES (3, '文艺');
INSERT INTO `meetingtype` VALUES (4, '招聘');
INSERT INTO `meetingtype` VALUES (5, '未知类型');

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `msgid` int(11) NOT NULL AUTO_INCREMENT,
  `sendfrom` int(16) NULL DEFAULT 0 COMMENT '发送方id，默认为系统id 0',
  `sendto` int(16) NOT NULL COMMENT '接收方用户id',
  `isread` int(2) NULL DEFAULT -1 COMMENT '是否已读，-1未读、1已读',
  `content` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息内容',
  PRIMARY KEY (`msgid`) USING BTREE,
  INDEX `m1`(`sendto`) USING BTREE,
  CONSTRAINT `m1` FOREIGN KEY (`sendto`) REFERENCES `user` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room`  (
  `room_id` int(11) NOT NULL AUTO_INCREMENT,
  `room_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '房型',
  `room_number` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
  `room_price` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '价格',
  `meeting_id` int(11) NOT NULL,
  `guest_id` int(11) NULL DEFAULT -1 COMMENT '住的嘉宾号，没人住就是-1',
  PRIMARY KEY (`room_id`, `meeting_id`) USING BTREE,
  INDEX `r1`(`meeting_id`) USING BTREE,
  CONSTRAINT `r1` FOREIGN KEY (`meeting_id`) REFERENCES `meeting` (`meetingid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `userid` int(11) NOT NULL AUTO_INCREMENT,
  `realname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '真实名',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `gender` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别',
  `emailaddr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮件地址',
  `phone` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
  `organization` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织名，比如:“重庆邮电大学”',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像地址',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  PRIMARY KEY (`userid`) USING BTREE,
  UNIQUE INDEX `phone`(`phone`) USING BTREE,
  UNIQUE INDEX `constraint_unique`(`phone`, `emailaddr`) USING BTREE,
  UNIQUE INDEX `emailaddr`(`emailaddr`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '林进波', '波波', '男', '123@qq.cpom', '123', '重邮', '1.jpg', '123');
INSERT INTO `user` VALUES (2, '吕进豪', 'lvlvlvlv', '男', '3032155055@qq.com', '15683487907', NULL, NULL, '1212121121');
INSERT INTO `user` VALUES (17, '吕进豪', 'L J H', '男', '1083775096@qq.com', '15086924104', '重邮', '4a84a2dc-ad96-4ccf-a605-4e5624235495.png', '123456');
INSERT INTO `user` VALUES (20, NULL, 'Lurker', '男', '15540922270@163.com', '15540922270', NULL, NULL, '123456');
INSERT INTO `user` VALUES (21, NULL, 'Lurker', '男', '15310610871@163.com', '15310610871', NULL, NULL, '123456');
INSERT INTO `user` VALUES (22, NULL, '121', '', NULL, '13658361535', NULL, NULL, '123456');
INSERT INTO `user` VALUES (24, NULL, 'hu', '', NULL, '18323221800', NULL, NULL, '123456');
INSERT INTO `user` VALUES (25, NULL, '邓书慧', '', NULL, '18225189933', NULL, NULL, '159357');
INSERT INTO `user` VALUES (27, NULL, '韩bobo', '', NULL, '15823489504', NULL, NULL, '13638308789?');
INSERT INTO `user` VALUES (28, NULL, 'zao23132', '男', '1064216064@qq.com', '15002320582', NULL, NULL, '123456789');

-- ----------------------------
-- Table structure for user_meeting
-- ----------------------------
DROP TABLE IF EXISTS `user_meeting`;
CREATE TABLE `user_meeting`  (
  `userid` int(11) NOT NULL,
  `meetingid` int(11) NOT NULL,
  `type` tinyint(3) NOT NULL COMMENT '类型：1 创建的会议；2 参加的会议；3 收藏的会议 ；4  志愿者申请中 ；5 志愿者； 6 申请志愿者失败',
  PRIMARY KEY (`type`, `meetingid`, `userid`) USING BTREE,
  INDEX `um1`(`userid`) USING BTREE,
  INDEX `um2`(`meetingid`) USING BTREE,
  CONSTRAINT `um1` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `um2` FOREIGN KEY (`meetingid`) REFERENCES `meeting` (`meetingid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_meeting
-- ----------------------------
INSERT INTO `user_meeting` VALUES (17, 1, 1);
INSERT INTO `user_meeting` VALUES (17, 135, 1);
INSERT INTO `user_meeting` VALUES (17, 136, 1);
INSERT INTO `user_meeting` VALUES (17, 138, 1);
INSERT INTO `user_meeting` VALUES (17, 139, 1);
INSERT INTO `user_meeting` VALUES (17, 140, 1);
INSERT INTO `user_meeting` VALUES (17, 141, 1);
INSERT INTO `user_meeting` VALUES (17, 142, 1);
INSERT INTO `user_meeting` VALUES (17, 143, 1);
INSERT INTO `user_meeting` VALUES (17, 144, 1);
INSERT INTO `user_meeting` VALUES (17, 145, 1);
INSERT INTO `user_meeting` VALUES (17, 146, 1);
INSERT INTO `user_meeting` VALUES (17, 147, 1);
INSERT INTO `user_meeting` VALUES (17, 148, 1);
INSERT INTO `user_meeting` VALUES (17, 149, 1);
INSERT INTO `user_meeting` VALUES (17, 133, 2);
INSERT INTO `user_meeting` VALUES (17, 134, 2);
INSERT INTO `user_meeting` VALUES (17, 136, 2);
INSERT INTO `user_meeting` VALUES (17, 133, 3);
INSERT INTO `user_meeting` VALUES (17, 136, 3);
INSERT INTO `user_meeting` VALUES (17, 1, 4);
INSERT INTO `user_meeting` VALUES (20, 1, 2);
INSERT INTO `user_meeting` VALUES (22, 1, 2);
INSERT INTO `user_meeting` VALUES (24, 1, 2);
INSERT INTO `user_meeting` VALUES (25, 1, 2);
INSERT INTO `user_meeting` VALUES (28, 133, 1);
INSERT INTO `user_meeting` VALUES (28, 134, 1);
INSERT INTO `user_meeting` VALUES (28, 1, 3);

-- ----------------------------
-- Table structure for volunt
-- ----------------------------
DROP TABLE IF EXISTS `volunt`;
CREATE TABLE `volunt`  (
  `meetid` int(11) NOT NULL COMMENT '志愿活动服务的会议编号',
  `introduction` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '志愿者要求介绍，不超过1024字',
  `number` int(11) NULL DEFAULT NULL COMMENT '需求人数',
  `isproof` int(1) NULL DEFAULT NULL COMMENT '是否提供时长证明  1提供，-1不提供',
  `full` int(3) NULL DEFAULT 1 COMMENT '是否需要停止招募，1 继续，-1 停止招募',
  PRIMARY KEY (`meetid`) USING BTREE,
  UNIQUE INDEX `meetingIdUnique`(`meetid`) USING BTREE,
  CONSTRAINT `vo1` FOREIGN KEY (`meetid`) REFERENCES `meeting` (`meetingid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for voluntask
-- ----------------------------
DROP TABLE IF EXISTS `voluntask`;
CREATE TABLE `voluntask`  (
  `taskid` int(11) NOT NULL AUTO_INCREMENT,
  `meetid` int(11) NOT NULL COMMENT '工作对应的会议ID',
  `taskinfo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工作的描述',
  `workingtime` int(11) NULL DEFAULT NULL COMMENT '工作时长',
  `numbers` int(11) NULL DEFAULT NULL COMMENT '需要的志愿者人数',
  PRIMARY KEY (`taskid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of voluntask
-- ----------------------------
INSERT INTO `voluntask` VALUES (-1, 75, '志愿活动', 10, 10);
INSERT INTO `voluntask` VALUES (1, 21, '测试是的 的的的的', 8, 2);
INSERT INTO `voluntask` VALUES (2, 21, '测试是的 的的的的', 8, 2);
INSERT INTO `voluntask` VALUES (3, 22, '测试是的 的的的的', 8, 2);
INSERT INTO `voluntask` VALUES (4, 22, '测试是的 的的的的', 8, 2);
INSERT INTO `voluntask` VALUES (5, 23, '测试是的 的的的的', 8, 2);
INSERT INTO `voluntask` VALUES (6, 23, '测试是的 的的的的', 8, 2);
INSERT INTO `voluntask` VALUES (7, 24, '会场管理', 8, 10);
INSERT INTO `voluntask` VALUES (8, 24, '机场迎接嘉宾', 8, 2);
INSERT INTO `voluntask` VALUES (9, 1, '测试是的 的的的的', 8, 2);
INSERT INTO `voluntask` VALUES (10, 1, '测试是的 的的的的', 8, 2);
INSERT INTO `voluntask` VALUES (11, 54, '测试', 8, 2);
INSERT INTO `voluntask` VALUES (12, 54, '测试', 8, 2);
INSERT INTO `voluntask` VALUES (13, 56, '志愿者一', 10, 10);
INSERT INTO `voluntask` VALUES (14, 56, '志愿二', 12, 12);
INSERT INTO `voluntask` VALUES (15, 57, '志愿者一', 10, 10);
INSERT INTO `voluntask` VALUES (16, 57, '志愿二', 12, 12);
INSERT INTO `voluntask` VALUES (17, 62, '志愿活动1', 12, 12);
INSERT INTO `voluntask` VALUES (18, 78, '测试', 12, 12);
INSERT INTO `voluntask` VALUES (19, 82, '志愿活动', 2, 1);
INSERT INTO `voluntask` VALUES (20, 83, '志愿活动一', 10, 10);
INSERT INTO `voluntask` VALUES (21, 84, '志愿活动一', 10, 10);
INSERT INTO `voluntask` VALUES (22, 88, '一', 1, 1);
INSERT INTO `voluntask` VALUES (23, 91, 'adffs', 5, 10);
INSERT INTO `voluntask` VALUES (24, 93, '引导嘉宾', 2, 5);
INSERT INTO `voluntask` VALUES (25, 93, '指导参会', 5, 5);
INSERT INTO `voluntask` VALUES (26, 96, '永真', 8, 1);

-- ----------------------------
-- Table structure for voluntinfo
-- ----------------------------
DROP TABLE IF EXISTS `voluntinfo`;
CREATE TABLE `voluntinfo`  (
  `userid` int(11) NOT NULL,
  `meetingid` int(11) NOT NULL,
  `taskid` int(11) NOT NULL COMMENT '工作内容对应的ID',
  `studentid` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学号',
  `personid` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  PRIMARY KEY (`userid`, `meetingid`, `taskid`) USING BTREE,
  INDEX `vi2`(`taskid`) USING BTREE,
  CONSTRAINT `vi1` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `vi2` FOREIGN KEY (`taskid`) REFERENCES `voluntask` (`taskid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of voluntinfo
-- ----------------------------
INSERT INTO `voluntinfo` VALUES (2, 24, 7, '2017210000', '123111111');
INSERT INTO `voluntinfo` VALUES (17, 21, 2, '2017210403', '5222661998');
INSERT INTO `voluntinfo` VALUES (17, 24, 8, '123123', '500226218651');

SET FOREIGN_KEY_CHECKS = 1;
