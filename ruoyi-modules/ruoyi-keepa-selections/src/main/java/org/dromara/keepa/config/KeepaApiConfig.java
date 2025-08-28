package org.dromara.keepa.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Keepa API配置
 *
 * @author ruoyi
 * @date 2025-01-02
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "keepa.api")
public class KeepaApiConfig {

    /**
     * API密钥
     */
    private String key;

    /**
     * 每分钟Token数量
     */
    private int tokensPerMinute = 250;

    /**
     * 单次请求最大ASIN数量
     */
    private int maxBatchSize = 100;

    /**
     * 请求间隔毫秒数
     */
    private int requestDelayMs = 1000;

    /**
     * 请求超时时间（毫秒）
     */
    private int timeoutMs = 60000;

    /**
     * 最大重试次数
     */
    private int maxRetryCount = 3;

    /**
     * 是否启用API限制检查
     */
    private boolean enableRateLimit = true;

    /**
     * API基础URL
     */
    private String baseUrl = "https://api.keepa.com";

    /**
     * 是否启用缓存
     */
    private boolean cacheEnabled = true;

    /**
     * 缓存过期时间(小时)
     */
    private int cacheExpireHours = 24;

    /**
     * 是否启用API监控
     */
    private boolean monitoringEnabled = true;


}