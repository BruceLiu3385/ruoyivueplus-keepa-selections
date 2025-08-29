package org.dromara.keepa.service.ratelimit;

import org.dromara.keepa.config.KeepaApiConfig;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 简单的令牌桶，用于按分钟恢复 Keepa Token
 */
public class KeepaTokenBucket {
    private final KeepaApiConfig config;
    private final AtomicInteger tokens;
    private volatile long windowStartEpochMs;

    public KeepaTokenBucket(KeepaApiConfig config) {
        this.config = config;
        this.tokens = new AtomicInteger(config.getTokensPerMinute());
        this.windowStartEpochMs = Instant.now().toEpochMilli();
    }

    /**
     * 获取令牌，不足则返回 false
     */
    public synchronized boolean tryAcquire(int required) {
        refillIfNeeded();
        if (tokens.get() >= required) {
            tokens.addAndGet(-required);
            return true;
        }
        return false;
    }

    /**
     * 需要阻塞等待直到足够的令牌恢复
     */
    public synchronized void awaitTokens(int required) throws InterruptedException {
        while (true) {
            refillIfNeeded();
            if (tokens.get() >= required) {
                tokens.addAndGet(-required);
                return;
            }
            long elapsed = Instant.now().toEpochMilli() - windowStartEpochMs;
            long remain = Math.max(0, 60_000 - elapsed);
            wait(remain + 50);
        }
    }

    private void refillIfNeeded() {
        long now = Instant.now().toEpochMilli();
        long elapsed = now - windowStartEpochMs;
        if (elapsed >= 60_000) {
            windowStartEpochMs = now;
            tokens.set(config.getTokensPerMinute());
            notifyAll();
        }
    }
}


