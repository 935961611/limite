package com.example.ratelimiter;

/**
 * 限流器接口
 * 定义限流器的基本行为
 */
public interface RateLimiter {
    /**
     * 尝试获取许可
     * @param key 限流键（如IP、账号等）
     * @return true表示允许通过，false表示被限流
     */
    boolean tryAcquire(String key);

    /**
     * 获取限流器名称，用于日志输出
     * @return 限流器名称
     */
    String getName();
}