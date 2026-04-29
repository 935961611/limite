package com.example.ratelimiter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 令牌桶限流器
 * 令牌桶算法：以固定速率生成令牌，请求到来时尝试获取令牌
 * 特点：允许一定程度的突发流量，但有上限
 */
public class TokenBucketLimiter implements RateLimiter {
    private final double rate; // 令牌生成速率（每秒）
    private final long capacity; // 令牌桶最大容量
    private final AtomicLong tokens; // 当前令牌数
    private volatile long lastRefillTime; // 上次补充令牌时间

    public TokenBucketLimiter(double rate, long capacity) {
        this.rate = rate;
        this.capacity = capacity;
        this.tokens = new AtomicLong(capacity); // 初始满桶
        this.lastRefillTime = System.nanoTime();
    }

    @Override
    public boolean tryAcquire(String key) {
        refillTokens(); // 先补充令牌

        long currentTokens = tokens.get();
        if (currentTokens > 0) {
            // 尝试获取令牌
            if (tokens.compareAndSet(currentTokens, currentTokens - 1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 补充令牌
     * 根据时间间隔计算应补充的令牌数
     */
    private void refillTokens() {
        long now = System.nanoTime();
        long timeElapsed = now - lastRefillTime;
        double tokensToAdd = (timeElapsed / 1_000_000_000.0) * rate; // 纳秒转秒

        if (tokensToAdd > 0) {
            long newTokens = (long) tokensToAdd;
            if (newTokens > 0) {
                tokens.updateAndGet(current -> Math.min(capacity, current + newTokens));
                lastRefillTime = now;
            }
        }
    }

    @Override
    public String getName() {
        return "令牌桶限流";
    }
}