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
     * 请求超时时间(秒)
     */
    private int timeoutSeconds = 30;

    /**
     * 最大重试次数
     */
    private int maxRetries = 3;

    /**
     * 批处理大小
     */
    private int batchSize = 100;

    /**
     * 最大等待时间(分钟)
     */
    private int maxWaitMinutes = 10;

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