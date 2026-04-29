package com.example.ratelimiter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 全局QPS封顶限流器
 * 限制系统整体每秒请求数
 */
public class GlobalQPSLimiter implements RateLimiter {
    private final AtomicInteger globalCounter = new AtomicInteger(0);
    private volatile long lastResetTime = System.currentTimeMillis();
    private final int maxQps;

    public GlobalQPSLimiter(int maxQps) {
        this.maxQps = maxQps;
    }

    @Override
    public boolean tryAcquire(String key) {
        long currentTime = System.currentTimeMillis();
        long currentSecond = currentTime / 1000;
        long lastSecond = lastResetTime / 1000;

        if (currentSecond > lastSecond) {
            // 新秒开始，重置计数器
            synchronized (this) {
                if (currentSecond > lastResetTime / 1000) {
                    globalCounter.set(1);
                    lastResetTime = currentTime;
                    return true;
                }
            }
        }

        // 同一秒内，检查是否超过限制
        int currentCount = globalCounter.incrementAndGet();
        if (currentCount <= maxQps) {
            return true;
        } else {
            globalCounter.decrementAndGet(); // 回滚计数
            return false;
        }
    }

    @Override
    public String getName() {
        return "全局QPS封顶";
    }
}