-- ----------------------------
-- Keepa选品工具数据库表结构
-- ----------------------------

-- ----------------------------
-- 1、选品批次表
-- ----------------------------
DROP TABLE IF EXISTS `ks_selection_batch`;
CREATE TABLE `ks_selection_batch` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '批次ID',
    `batch_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '批次名称',
    `batch_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '批次编码',
    `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '批次描述',
    `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '状态(0初始,1配置中,2抓取ASIN中,3抓取详情中,4分析中,5完成,9失败)',
    `amazon_domain` int NOT NULL DEFAULT '1' COMMENT '亚马逊站点域名(1美国,2德国,3英国,4法国,5日本,6加拿大,7意大利,8西班牙,9印度,10中国,11墨西哥,12澳大利亚,13巴西,14土耳其)',
    `category_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '选择的类目ID列表(JSON格式)',
    `max_asin_count` int DEFAULT 5000 COMMENT '最大ASIN数量',
    `current_asin_count` int DEFAULT 0 COMMENT '当前ASIN数量',
    `current_detail_count` int DEFAULT 0 COMMENT '当前详情数量',
    `start_time` datetime DEFAULT NULL COMMENT '开始时间',
    `end_time` datetime DEFAULT NULL COMMENT '结束时间',
    `error_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '错误信息',
    `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_batch_code` (`batch_code`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='选品批次表';

-- ----------------------------
-- 2、亚马逊域名站点表
-- ----------------------------
DROP TABLE IF EXISTS `ks_amazon_domain`;
CREATE TABLE `ks_amazon_domain` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT '域名ID',
    `domain_code` int NOT NULL COMMENT '域名编码',
    `domain_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '域名名称',
    `country_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '国家代码',
    `domain_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '域名URL',
    `currency` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '货币代码',
    `language` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '语言代码',
    `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '状态(0正常,1停用)',
    `sort_order` int DEFAULT 0 COMMENT '排序',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_domain_code` (`domain_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='亚马逊域名站点表';

-- 插入默认亚马逊站点数据
INSERT INTO `ks_amazon_domain` VALUES 
(1, 1, '美国', 'US', 'amazon.com', 'USD', 'en-US', '0', 1),
(2, 2, '德国', 'DE', 'amazon.de', 'EUR', 'de-DE', '0', 2),
(3, 3, '英国', 'UK', 'amazon.co.uk', 'GBP', 'en-GB', '0', 3),
(4, 4, '法国', 'FR', 'amazon.fr', 'EUR', 'fr-FR', '0', 4),
(5, 5, '日本', 'JP', 'amazon.co.jp', 'JPY', 'ja-JP', '0', 5),
(6, 6, '加拿大', 'CA', 'amazon.ca', 'CAD', 'en-CA', '0', 6),
(7, 7, '意大利', 'IT', 'amazon.it', 'EUR', 'it-IT', '0', 7),
(8, 8, '西班牙', 'ES', 'amazon.es', 'EUR', 'es-ES', '0', 8),
(9, 9, '印度', 'IN', 'amazon.in', 'INR', 'en-IN', '0', 9),
(10, 10, '中国', 'CN', 'amazon.cn', 'CNY', 'zh-CN', '1', 10),
(11, 11, '墨西哥', 'MX', 'amazon.com.mx', 'MXN', 'es-MX', '0', 11),
(12, 12, '澳大利亚', 'AU', 'amazon.com.au', 'AUD', 'en-AU', '0', 12),
(13, 13, '巴西', 'BR', 'amazon.com.br', 'BRL', 'pt-BR', '0', 13),
(14, 14, '土耳其', 'TR', 'amazon.com.tr', 'TRY', 'tr-TR', '0', 14);

-- ----------------------------
-- 3、亚马逊类目表
-- ----------------------------
DROP TABLE IF EXISTS `ks_amazon_category`;
CREATE TABLE `ks_amazon_category` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '类目ID',
    `category_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Keepa类目ID',
    `parent_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '父类目ID',
    `category_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类目名称',
    `category_path` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类目路径',
    `level` int DEFAULT 1 COMMENT '层级',
    `domain_code` int NOT NULL COMMENT '所属域名',
    `product_count` bigint DEFAULT 0 COMMENT '商品数量',
    `has_children` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '是否有子类目(0否,1是)',
    `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '状态(0正常,1停用)',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_category_domain` (`category_id`, `domain_code`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_domain_code` (`domain_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='亚马逊类目表';

-- ----------------------------
-- 4、商品ASIN表
-- ----------------------------
DROP TABLE IF EXISTS `ks_product_asin`;
CREATE TABLE `ks_product_asin` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `batch_id` bigint NOT NULL COMMENT '批次ID',
    `asin` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ASIN',
    `category_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类目ID',
    `domain_code` int NOT NULL COMMENT '域名编码',
    `sales_rank` int DEFAULT NULL COMMENT '销售排名',
    `detail_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '详情状态(0未抓取,1抓取中,2已完成,9失败)',
    `analysis_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '分析状态(0未分析,1已分析)',
    `error_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '错误信息',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_batch_asin` (`batch_id`, `asin`),
    KEY `idx_asin` (`asin`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_detail_status` (`detail_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品ASIN表';

-- ----------------------------
-- 5、商品详情表
-- ----------------------------
DROP TABLE IF EXISTS `ks_product_detail`;
CREATE TABLE `ks_product_detail` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `asin_id` bigint NOT NULL COMMENT 'ASIN记录ID',
    `asin` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ASIN',
    `title` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商品标题',
    `brand` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '品牌',
    `manufacturer` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '制造商',
    `model` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '型号',
    `category_tree` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '类目树(JSON)',
    `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '主图链接',
    `features` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '产品特性(JSON)',
    `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '商品描述',
    `dimensions` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '尺寸',
    `weight` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '重量',
    `package_dimensions` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '包装尺寸',
    `package_weight` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '包装重量',
    `color` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '颜色',
    `size` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '尺码',
    `availability` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '库存状态',
    `is_adult` tinyint(1) DEFAULT 0 COMMENT '是否成人产品',
    `binding` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '装订类型',
    `edition` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '版本',
    `language` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '语言',
    `publication_date` date DEFAULT NULL COMMENT '发布日期',
    `release_date` date DEFAULT NULL COMMENT '发行日期',
    `hazmat` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '危险品标识',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_asin_id` (`asin_id`),
    KEY `idx_asin` (`asin`),
    KEY `idx_brand` (`brand`),
    KEY `idx_manufacturer` (`manufacturer`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品详情表';

-- ----------------------------
-- 6、商品价格历史表
-- ----------------------------
DROP TABLE IF EXISTS `ks_product_price_history`;
CREATE TABLE `ks_product_price_history` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `asin_id` bigint NOT NULL COMMENT 'ASIN记录ID',
    `asin` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ASIN',
    `price_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '价格类型(AMAZON,NEW,USED,SALES,LISTPRICE,等)',
    `history_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '历史数据(JSON格式,包含时间戳和价格)',
    `current_price` decimal(15,2) DEFAULT NULL COMMENT '当前价格',
    `min_price` decimal(15,2) DEFAULT NULL COMMENT '最低价格',
    `max_price` decimal(15,2) DEFAULT NULL COMMENT '最高价格',
    `avg_price` decimal(15,2) DEFAULT NULL COMMENT '平均价格',
    `data_points` int DEFAULT 0 COMMENT '数据点数量',
    `first_date` datetime DEFAULT NULL COMMENT '首次记录时间',
    `last_date` datetime DEFAULT NULL COMMENT '最新记录时间',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_asin_price_type` (`asin_id`, `price_type`),
    KEY `idx_asin` (`asin`),
    KEY `idx_price_type` (`price_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品价格历史表';

-- ----------------------------
-- 7、商品评论和评级历史表
-- ----------------------------
DROP TABLE IF EXISTS `ks_product_rating_history`;
CREATE TABLE `ks_product_rating_history` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `asin_id` bigint NOT NULL COMMENT 'ASIN记录ID',
    `asin` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ASIN',
    `rating_history` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '评分历史数据(JSON格式)',
    `review_count_history` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '评论数量历史(JSON格式)',
    `current_rating` decimal(3,1) DEFAULT NULL COMMENT '当前评分',
    `current_review_count` int DEFAULT NULL COMMENT '当前评论数',
    `avg_rating` decimal(3,1) DEFAULT NULL COMMENT '平均评分',
    `min_rating` decimal(3,1) DEFAULT NULL COMMENT '最低评分',
    `max_rating` decimal(3,1) DEFAULT NULL COMMENT '最高评分',
    `total_reviews` bigint DEFAULT 0 COMMENT '总评论数',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_asin_id` (`asin_id`),
    KEY `idx_asin` (`asin`),
    KEY `idx_current_rating` (`current_rating`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品评论和评级历史表';

-- ----------------------------
-- 8、商品offer历史表
-- ----------------------------
DROP TABLE IF EXISTS `ks_product_offer_history`;
CREATE TABLE `ks_product_offer_history` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `asin_id` bigint NOT NULL COMMENT 'ASIN记录ID',
    `asin` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ASIN',
    `offer_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Offer类型(COUNT_NEW,COUNT_USED,COUNT_REFURBISHED,等)',
    `history_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '历史数据(JSON格式)',
    `current_count` int DEFAULT NULL COMMENT '当前数量',
    `min_count` int DEFAULT NULL COMMENT '最小数量',
    `max_count` int DEFAULT NULL COMMENT '最大数量',
    `avg_count` decimal(10,2) DEFAULT NULL COMMENT '平均数量',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_asin_offer_type` (`asin_id`, `offer_type`),
    KEY `idx_asin` (`asin`),
    KEY `idx_offer_type` (`offer_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品offer历史表';

-- ----------------------------
-- 9、选品分析结果表
-- ----------------------------
DROP TABLE IF EXISTS `ks_selection_result`;
CREATE TABLE `ks_selection_result` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `batch_id` bigint NOT NULL COMMENT '批次ID',
    `asin_id` bigint NOT NULL COMMENT 'ASIN记录ID',
    `asin` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ASIN',
    `title` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商品标题',
    `brand` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '品牌',
    `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '主图链接',
    
    -- 价格相关分析
    `current_price` decimal(15,2) DEFAULT NULL COMMENT '当前价格',
    `min_price_30d` decimal(15,2) DEFAULT NULL COMMENT '30天最低价',
    `max_price_30d` decimal(15,2) DEFAULT NULL COMMENT '30天最高价',
    `avg_price_30d` decimal(15,2) DEFAULT NULL COMMENT '30天平均价',
    `price_volatility` decimal(5,2) DEFAULT NULL COMMENT '价格波动率(%)',
    `price_trend` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '价格趋势(上升/下降/稳定)',
    `discount_rate` decimal(5,2) DEFAULT NULL COMMENT '折扣率(%)',
    
    -- 销量排名分析
    `current_sales_rank` int DEFAULT NULL COMMENT '当前销量排名',
    `best_sales_rank_30d` int DEFAULT NULL COMMENT '30天最佳排名',
    `worst_sales_rank_30d` int DEFAULT NULL COMMENT '30天最差排名',
    `avg_sales_rank_30d` int DEFAULT NULL COMMENT '30天平均排名',
    `rank_volatility` decimal(5,2) DEFAULT NULL COMMENT '排名波动率(%)',
    `rank_trend` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '排名趋势(上升/下降/稳定)',
    
    -- 评论评分分析
    `current_rating` decimal(3,1) DEFAULT NULL COMMENT '当前评分',
    `current_review_count` int DEFAULT NULL COMMENT '当前评论数',
    `review_growth_30d` int DEFAULT NULL COMMENT '30天评论增长数',
    `review_growth_rate` decimal(5,2) DEFAULT NULL COMMENT '评论增长率(%)',
    
    -- 竞争分析
    `competitor_count` int DEFAULT NULL COMMENT '竞争对手数量',
    `new_offer_count` int DEFAULT NULL COMMENT '新品offer数量',
    `used_offer_count` int DEFAULT NULL COMMENT '二手offer数量',
    `fba_offer_count` int DEFAULT NULL COMMENT 'FBA offer数量',
    
    -- 库存分析
    `availability_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '库存状态',
    `stock_out_frequency` int DEFAULT 0 COMMENT '缺货频次',
    
    -- 综合评分
    `selection_score` decimal(5,2) DEFAULT NULL COMMENT '选品评分(0-100)',
    `profit_potential` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '盈利潜力(高/中/低)',
    `market_saturation` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '市场饱和度(高/中/低)',
    `entry_difficulty` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '进入难度(高/中/低)',
    `recommendation_level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '推荐等级(强烈推荐/推荐/一般/不推荐)',
    
    -- 分析结果
    `analysis_summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '分析摘要',
    `advantages` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '优势分析',
    `risks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '风险分析',
    `suggestions` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '建议',
    
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_batch_asin` (`batch_id`, `asin_id`),
    KEY `idx_batch_id` (`batch_id`),
    KEY `idx_asin` (`asin`),
    KEY `idx_selection_score` (`selection_score`),
    KEY `idx_recommendation_level` (`recommendation_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='选品分析结果表';

-- ----------------------------
-- 10、API Token使用记录表
-- ----------------------------
DROP TABLE IF EXISTS `ks_api_token_usage`;
CREATE TABLE `ks_api_token_usage` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `api_key_hash` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'API密钥哈希',
    `request_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求类型',
    `tokens_consumed` int NOT NULL DEFAULT 1 COMMENT '消耗Token数量',
    `tokens_remaining` int DEFAULT NULL COMMENT '剩余Token数量',
    `request_time` datetime NOT NULL COMMENT '请求时间',
    `batch_id` bigint DEFAULT NULL COMMENT '关联批次ID',
    `request_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求参数',
    `response_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '响应状态',
    `error_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '错误信息',
    PRIMARY KEY (`id`),
    KEY `idx_api_key_hash` (`api_key_hash`),
    KEY `idx_request_time` (`request_time`),
    KEY `idx_batch_id` (`batch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='API Token使用记录表';

-- ----------------------------
-- 11、异步任务队列表
-- ----------------------------
DROP TABLE IF EXISTS `ks_task_queue`;
CREATE TABLE `ks_task_queue` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `task_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务类型(FETCH_ASIN,FETCH_DETAIL,ANALYZE)',
    `task_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'PENDING' COMMENT '任务状态(PENDING,RUNNING,SUCCESS,FAILED,CANCELLED)',
    `batch_id` bigint NOT NULL COMMENT '批次ID',
    `task_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务参数(JSON格式)',
    `priority` int DEFAULT 0 COMMENT '优先级(数字越大优先级越高)',
    `retry_count` int DEFAULT 0 COMMENT '重试次数',
    `max_retry_count` int DEFAULT 3 COMMENT '最大重试次数',
    `progress` int DEFAULT 0 COMMENT '进度(0-100)',
    `start_time` datetime DEFAULT NULL COMMENT '开始时间',
    `end_time` datetime DEFAULT NULL COMMENT '结束时间',
    `error_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '错误信息',
    `result_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '结果数据',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_task_type` (`task_type`),
    KEY `idx_task_status` (`task_status`),
    KEY `idx_batch_id` (`batch_id`),
    KEY `idx_priority` (`priority`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='异步任务队列表';

-- ----------------------------
-- 12、系统配置表
-- ----------------------------
DROP TABLE IF EXISTS `ks_system_config`;
CREATE TABLE `ks_system_config` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置键',
    `config_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '配置值',
    `config_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'STRING' COMMENT '配置类型',
    `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '配置描述',
    `is_encrypted` tinyint(1) DEFAULT 0 COMMENT '是否加密',
    `sort_order` int DEFAULT 0 COMMENT '排序',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统配置表';

-- 插入默认系统配置
INSERT INTO `ks_system_config` VALUES 
(1, 'keepa.api.key', 'cqqkcu49mds3vp4u4v8om3qtmmfl4l63038r1httn5tun9tu155u39k4kpkbsj2k', 'STRING', 'Keepa API密钥', 1, 1, NOW(), NOW()),
(2, 'keepa.api.tokens.per.minute', '250', 'INTEGER', '每分钟恢复Token数量', 0, 2, NOW(), NOW()),
(3, 'keepa.api.max.batch.size', '100', 'INTEGER', '单次请求最大ASIN数量', 0, 3, NOW(), NOW()),
(4, 'keepa.api.request.delay.ms', '1000', 'INTEGER', '请求间隔毫秒数', 0, 4, NOW(), NOW()),
(5, 'keepa.selection.score.weights', '{"price_volatility":0.2,"rank_trend":0.25,"review_growth":0.15,"competitor_count":0.2,"profit_potential":0.2}', 'JSON', '选品评分权重配置', 0, 5, NOW(), NOW());
