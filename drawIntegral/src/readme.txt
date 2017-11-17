--创建奖项表
CREATE TABLE `t_activity_award` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `award` varchar(2) NOT NULL DEFAULT '5' COMMENT '奖项等级：1-一等奖，2-二等奖，3-三等奖，4-四等奖，5-五等奖',
  `statu` varchar(2) NOT NULL DEFAULT '0' COMMENT '发放状态: 0-未发放;1-已发放',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=401 DEFAULT CHARSET=utf8;

--创建参加的用户
CREATE TABLE `t_activity_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `customerNo` varchar(20) NOT NULL COMMENT '客户编号',
  `customertimes` int(11) NOT NULL DEFAULT '0' COMMENT '客户抽奖次数',
  `createtime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `customerNo` (`customerNo`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;

--中奖记录
CREATE TABLE `t_activity_recordlist` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键流水号',
  `customerNo` varchar(20) NOT NULL COMMENT '中奖客户编号',
  `awardid` int(11) NOT NULL COMMENT '奖池编号',
  `award` varchar(2) NOT NULL COMMENT '奖项等级：1-一等奖，2-二等奖，3-三等奖，4-四等奖，5-五等奖',
  `createtime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `awardid` (`awardid`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

--中奖记录
INSERT INTO `t_activity_recordlist` VALUES (1, '00001', 372, '5', '2017-11-14 13:27:18');
INSERT INTO `t_activity_recordlist` VALUES (2, '00001', 38, '3', '2017-11-14 13:50:09');
INSERT INTO `t_activity_recordlist` VALUES (3, '00001', 145, '4', '2017-11-14 14:25:18');
INSERT INTO `t_activity_recordlist` VALUES (4, '00001', 196, '4', '2017-11-14 14:25:43');
INSERT INTO `t_activity_recordlist` VALUES (5, '00001', 220, '4', '2017-11-14 14:41:33');
INSERT INTO `t_activity_recordlist` VALUES (6, '00001', 13, '3', '2017-11-14 15:24:42');
INSERT INTO `t_activity_recordlist` VALUES (7, '00001', 325, '5', '2017-11-14 15:26:24');
INSERT INTO `t_activity_recordlist` VALUES (8, '00001', 387, '5', '2017-11-14 15:27:56');
INSERT INTO `t_activity_recordlist` VALUES (9, '00001', 73, '3', '2017-11-14 15:30:52');
INSERT INTO `t_activity_recordlist` VALUES (10, '00001', 186, '4', '2017-11-14 15:35:15');
INSERT INTO `t_activity_recordlist` VALUES (11, '00001', 200, '4', '2017-11-14 15:57:38');
INSERT INTO `t_activity_recordlist` VALUES (12, '00001', 212, '4', '2017-11-14 16:07:22');

--活动成员
INSERT INTO `t_activity_order` VALUES (1, '18186864873', '刘博', '701p00000008wtEAAQ', 'Test Campaign for XP', '已计划', '2016-6-25 17:08:15', '2016-6-25 17:08:15', '2016-6-25 17:08:15');
INSERT INTO `t_activity_order` VALUES (2, '13810264087', '吴佳奇', '701p00000008wtEAAQ', 'Test Campaign for XP', '已计划', '2016-6-25 17:14:03', '2016-6-25 17:14:03', '2016-6-25 17:14:03');
INSERT INTO `t_activity_order` VALUES (3, '13810264087', '吴佳奇', '701p00000008x0AAAQ', '2015年10月10日-上海静安分公司-艺术殿堂-父亲节雪茄品鉴活动', '进行中', '2016-6-25 17:15:13', '2016-6-25 17:15:13', '2016-6-25 17:15:13');
INSERT INTO `t_activity_order` VALUES (4, '18643009969', '胡越', '701p00000008wtEAAQ', 'Test Campaign for XP', '已计划', '2016-6-25 17:16:00', '2016-6-25 17:16:00', '2016-6-25 17:16:00');
INSERT INTO `t_activity_order` VALUES (5, '18186864873', '刘博', '701p00000008x0AAAQ', '2015年10月10日-上海静安分公司-艺术殿堂-父亲节雪茄品鉴活动', '进行中', '2016-6-27 19:39:28', '2016-6-27 19:39:28', '2016-6-27 19:39:28');
INSERT INTO `t_activity_order` VALUES (6, '18186864873', '刘博', '701p00000008zPtAAI', '百家讲坛', '计划中', '2016-6-27 19:39:46', '2016-6-27 19:39:46', '2016-6-27 19:39:46');
INSERT INTO `t_activity_order` VALUES (7, '18643009969', '胡越', '701p00000008x0AAAQ', '2015年10月10日-上海静安分公司-艺术殿堂-父亲节雪茄品鉴活动', '进行中', '2016-6-27 19:42:41', '2016-6-27 19:42:41', '2016-6-27 19:42:41');
INSERT INTO `t_activity_order` VALUES (8, '13810264087', '吴佳奇', '701p00000005gjiAAA', 'Wendy测试', NULL, '2016-9-6 09:31:35', '2016-9-6 09:31:35', '2016-9-6 09:31:35');


--有多少奖项就写多少条奖
INSERT INTO `t_activity_award` VALUES (1, '1', '0', '2017-11-14 13:22:20', NULL);
INSERT INTO `t_activity_award` VALUES (2, '2', '0', '2017-11-14 13:22:20', NULL);
INSERT INTO `t_activity_award` VALUES (3, '2', '0', '2017-11-14 13:22:20', NULL);
INSERT INTO `t_activity_award` VALUES (4, '2', '0', '2017-11-14 13:22:20', NULL);
INSERT INTO `t_activity_award` VALUES (5, '2', '0', '2017-11-14 13:22:20', NULL);
INSERT INTO `t_activity_award` VALUES (6, '2', '0', '2017-11-14 13:22:20', NULL);
INSERT INTO `t_activity_award` VALUES (7, '2', '0', '2017-11-14 13:22:20', NULL);
INSERT INTO `t_activity_award` VALUES (8, '2', '0', '2017-11-14 13:22:20', NULL);
INSERT INTO `t_activity_award` VALUES (9, '2', '0', '2017-11-14 13:22:20', NULL);
INSERT INTO `t_activity_award` VALUES (10, '2', '0', '2017-11-14 13:22:20', NULL);
INSERT INTO `t_activity_award` VALUES (11, '2', '0', '2017-11-14 13:22:20', NULL);
INSERT INTO `t_activity_award` VALUES (12, '3', '0', '2017-11-14 13:22:20', NULL);
INSERT INTO `t_activity_award` VALUES (13, '3', '1', '2017-11-14 13:22:20', '2017-11-14 15:24:42');
INSERT INTO `t_activity_award` VALUES (14, '3', '0', '2017-11-14 13:22:20', NULL);
INSERT INTO `t_activity_award` VALUES (15, '3', '0', '2017-11-14 13:22:20', NULL);

INSERT INTO `t_activity_award` VALUES (309, '4', '0', '2017-11-14 13:22:20', NULL);
INSERT INTO `t_activity_award` VALUES (310, '4', '0', '2017-11-14 13:22:20', NULL);
INSERT INTO `t_activity_award` VALUES (311, '4', '0', '2017-11-14 13:22:20', NULL);
INSERT INTO `t_activity_award` VALUES (312, '5', '0', '2017-11-14 13:22:20', NULL);
INSERT INTO `t_activity_award` VALUES (313, '5', '0', '2017-11-14 13:22:20', NULL);
INSERT INTO `t_activity_award` VALUES (314, '5', '0', '2017-11-14 13:22:20', NULL);
INSERT INTO `t_activity_award` VALUES (315, '5', '0', '2017-11-14 13:22:20', NULL);
