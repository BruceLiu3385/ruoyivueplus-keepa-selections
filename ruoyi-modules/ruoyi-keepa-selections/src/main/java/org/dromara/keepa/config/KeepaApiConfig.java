package org.dromara.keepa.config;

import com.keepa.api.backend.KeepaAPI;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Keepa API配置
 *
 * @author ruoyi
 * @date 2025-01-02
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "keepa.api")
public class KeepaApiConfig {

    /**
     * API密钥
     */
    private String key;

    /**
     * 每分钟Token恢复数量
     */
    private Integer tokensPerMinute = 250;

    /**
     * 单次请求最大ASIN数量
     */
    private Integer maxBatchSize = 100;

    /**
     * 请求间隔毫秒数
     */
    private Long requestDelayMs = 1000L;

    /**
     * 请求超时时间（毫秒）
     */
    private Long timeoutMs = 60000L;

    /**
     * 最大重试次数
     */
    private Integer maxRetryCount = 3;

    /**
     * 是否启用API限制检查
     */
    private Boolean enableRateLimit = true;

    /**
     * 创建KeepaAPI实例
     */
    @Bean
    public KeepaAPI keepaAPI() {
        if (key == null || key.trim().isEmpty()) {
            log.error("Keepa API密钥未配置，请设置keepa.api.key属性");
            throw new IllegalArgumentException("Keepa API密钥未配置");
        }
        
        log.info("初始化Keepa API，密钥前缀: {}...", key.substring(0, Math.min(10, key.length())));
        return new KeepaAPI(key);
    }
}
