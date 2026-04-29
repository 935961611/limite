package com.example.ratelimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 单账号限流器
 * 基于滑动窗口实现每秒请求数限制
 */
public class AccountRateLimiter implements RateLimiter {
    private final ConcurrentHashMap<String, AtomicInteger> accountCounters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> accountTimestamps = new ConcurrentHashMap<>();
    private final int maxRequestsPerSecond;

    public AccountRateLimiter(int maxRequestsPerSecond) {
        this.maxRequestsPerSecond = maxRequestsPerSecond;
    }

    @Override
    public boolean tryAcquire(String account) {
        long currentTime = System.currentTimeMillis();
        long currentSecond = currentTime / 1000;

        accountCounters.putIfAbsent(account, new AtomicInteger(0));
        accountTimestamps.putIfAbsent(account, currentSecond);

        long lastSecond = accountTimestamps.get(account);
        AtomicInteger counter = accountCounters.get(account);

        if (currentSecond > lastSecond) {
            // 新秒开始，重置计数器
            counter.set(1);
            accountTimestamps.put(account, currentSecond);
            return true;
        } else if (currentSecond == lastSecond) {
            // 同一秒内，检查是否超过限制
            if (counter.incrementAndGet() <= maxRequestsPerSecond) {
                return true;
            } else {
                counter.decrementAndGet(); // 回滚计数
                return false;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "单账号限流";
    }
}