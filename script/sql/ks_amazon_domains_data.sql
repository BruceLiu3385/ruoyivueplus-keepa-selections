-- ======================================
-- Keepa选品工具 - 亚马逊域名基础数据
-- ======================================

-- 清空现有数据
DELETE FROM ks_amazon_domain WHERE 1=1;

-- 插入亚马逊各站点域名数据
INSERT INTO `ks_amazon_domain` (`domain_id`, `domain_code`, `domain_name`, `country_code`, `country_name`, `base_url`, `currency_code`, `language`, `status`, `sort_order`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(1, 1, 'Amazon.com', 'US', '美国', 'https://www.amazon.com', 'USD', 'en-US', '0', 1, 1, NOW(), 1, NOW(), '美国亚马逊站点'),
(2, 2, 'Amazon.co.uk', 'GB', '英国', 'https://www.amazon.co.uk', 'GBP', 'en-GB', '0', 2, 1, NOW(), 1, NOW(), '英国亚马逊站点'),
(3, 3, 'Amazon.de', 'DE', '德国', 'https://www.amazon.de', 'EUR', 'de-DE', '0', 3, 1, NOW(), 1, NOW(), '德国亚马逊站点'),
(4, 4, 'Amazon.fr', 'FR', '法国', 'https://www.amazon.fr', 'EUR', 'fr-FR', '0', 4, 1, NOW(), 1, NOW(), '法国亚马逊站点'),
(5, 5, 'Amazon.co.jp', 'JP', '日本', 'https://www.amazon.co.jp', 'JPY', 'ja-JP', '0', 5, 1, NOW(), 1, NOW(), '日本亚马逊站点'),
(6, 6, 'Amazon.ca', 'CA', '加拿大', 'https://www.amazon.ca', 'CAD', 'en-CA', '0', 6, 1, NOW(), 1, NOW(), '加拿大亚马逊站点'),
(7, 7, 'Amazon.it', 'IT', '意大利', 'https://www.amazon.it', 'EUR', 'it-IT', '0', 7, 1, NOW(), 1, NOW(), '意大利亚马逊站点'),
(8, 8, 'Amazon.es', 'ES', '西班牙', 'https://www.amazon.es', 'EUR', 'es-ES', '0', 8, 1, NOW(), 1, NOW(), '西班牙亚马逊站点'),
(9, 9, 'Amazon.in', 'IN', '印度', 'https://www.amazon.in', 'INR', 'en-IN', '0', 9, 1, NOW(), 1, NOW(), '印度亚马逊站点'),
(10, 10, 'Amazon.com.br', 'BR', '巴西', 'https://www.amazon.com.br', 'BRL', 'pt-BR', '0', 10, 1, NOW(), 1, NOW(), '巴西亚马逊站点'),
(11, 11, 'Amazon.com.mx', 'MX', '墨西哥', 'https://www.amazon.com.mx', 'MXN', 'es-MX', '0', 11, 1, NOW(), 1, NOW(), '墨西哥亚马逊站点'),
(12, 12, 'Amazon.com.au', 'AU', '澳大利亚', 'https://www.amazon.com.au', 'AUD', 'en-AU', '0', 12, 1, NOW(), 1, NOW(), '澳大利亚亚马逊站点'),
(13, 13, 'Amazon.nl', 'NL', '荷兰', 'https://www.amazon.nl', 'EUR', 'nl-NL', '0', 13, 1, NOW(), 1, NOW(), '荷兰亚马逊站点'),
(14, 14, 'Amazon.se', 'SE', '瑞典', 'https://www.amazon.se', 'SEK', 'sv-SE', '0', 14, 1, NOW(), 1, NOW(), '瑞典亚马逊站点'),
(15, 15, 'Amazon.pl', 'PL', '波兰', 'https://www.amazon.pl', 'PLN', 'pl-PL', '0', 15, 1, NOW(), 1, NOW(), '波兰亚马逊站点'),
(16, 16, 'Amazon.com.tr', 'TR', '土耳其', 'https://www.amazon.com.tr', 'TRY', 'tr-TR', '0', 16, 1, NOW(), 1, NOW(), '土耳其亚马逊站点'),
(17, 17, 'Amazon.sg', 'SG', '新加坡', 'https://www.amazon.sg', 'SGD', 'en-SG', '0', 17, 1, NOW(), 1, NOW(), '新加坡亚马逊站点'),
(18, 18, 'Amazon.ae', 'AE', '阿联酋', 'https://www.amazon.ae', 'AED', 'ar-AE', '0', 18, 1, NOW(), 1, NOW(), '阿联酋亚马逊站点'),
(19, 19, 'Amazon.sa', 'SA', '沙特阿拉伯', 'https://www.amazon.sa', 'SAR', 'ar-SA', '0', 19, 1, NOW(), 1, NOW(), '沙特阿拉伯亚马逊站点'),
(20, 20, 'Amazon.eg', 'EG', '埃及', 'https://www.amazon.eg', 'EGP', 'ar-EG', '0', 20, 1, NOW(), 1, NOW(), '埃及亚马逊站点');

-- 重置自增主键
ALTER TABLE `ks_amazon_domain` AUTO_INCREMENT = 21;

-- 验证数据插入
SELECT COUNT(*) as '总站点数量' FROM ks_amazon_domain;
SELECT domain_code, domain_name, country_name, currency_code FROM ks_amazon_domain ORDER BY sort_order LIMIT 10;
