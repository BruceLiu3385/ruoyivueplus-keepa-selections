package org.dromara.keepa.config;

import org.dromara.keepa.service.ratelimit.KeepaTokenBucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeepaBeans {

    @Bean
    public KeepaTokenBucket keepaTokenBucket(KeepaApiConfig config) {
        return new KeepaTokenBucket(config);
    }
}


