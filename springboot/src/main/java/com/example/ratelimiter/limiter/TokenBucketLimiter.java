package com.example.ratelimiter.limiter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 令牌桶限流器
 */
public class TokenBucketLimiter implements RateLimiter {
    private final double rate;
    private final long capacity;
    private final AtomicLong tokens;
    private volatile long lastRefillTime;

    public TokenBucketLimiter(double rate, long capacity) {
        this.rate = rate;
        this.capacity = capacity;
        this.tokens = new AtomicLong(capacity);
        this.lastRefillTime = System.nanoTime();
    }

    @Override
    public boolean tryAcquire(String key) {
        refillTokens();

        long currentTokens = tokens.get();
        if (currentTokens > 0) {
            if (tokens.compareAndSet(currentTokens, currentTokens - 1)) {
                return true;
            }
        }
        return false;
    }

    private void refillTokens() {
        long now = System.nanoTime();
        long timeElapsed = now - lastRefillTime;
        double tokensToAdd = (timeElapsed / 1_000_000_000.0) * rate;

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

    public long getTokenCount() {
        refillTokens();
        return tokens.get();
    }
}
