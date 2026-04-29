package com.example.ratelimiter.limiter;

/**
 * 限流器接口
 */
public interface RateLimiter {
    /**
     * 尝试获取许可
     * @param key 限流键
     * @return true表示允许通过，false表示被限流
     */
    boolean tryAcquire(String key);

    /**
     * 获取限流器名称
     */
    String getName();
}
