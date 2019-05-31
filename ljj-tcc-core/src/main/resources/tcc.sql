SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tcc_transaction`
-- ----------------------------
DROP TABLE IF EXISTS `tcc_transaction`;
CREATE TABLE `tcc_transaction` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `domain` varchar(100) DEFAULT NULL,
  `global_tx_id` varchar(32) NOT NULL COMMENT '全局事务ID',
  `branch_qualifier` varchar(32) NOT NULL COMMENT '分支限定符',
  `content` varbinary(8000) DEFAULT NULL COMMENT '序列化后事务实例',
  `status` tinyint(1) DEFAULT NULL COMMENT '事务阶段: 1=try,2=confirm,3=cancel',
  `transaction_type` tinyint(1) DEFAULT NULL COMMENT '事务类型: 1=root, 2=branch',
  `deleted` tinyint(1) DEFAULT NULL COMMENT '是否删除:1=删除,0=没有删除',
  `retried_count` tinyint(3) DEFAULT NULL COMMENT '重试次数',
  `version` smallint(5) DEFAULT '1' COMMENT '事务版本号',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UX_TX_BQ` (`global_tx_id`,`branch_qualifier`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tcc_transaction
-- ----------------------------