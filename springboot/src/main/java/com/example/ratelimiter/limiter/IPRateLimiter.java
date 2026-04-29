package com.example.ratelimiter.limiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 单IP限流器
 */
public class IPRateLimiter implements RateLimiter {
    private final ConcurrentHashMap<String, AtomicInteger> ipCounters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> ipTimestamps = new ConcurrentHashMap<>();
    private final int maxRequestsPerSecond;

    public IPRateLimiter(int maxRequestsPerSecond) {
        this.maxRequestsPerSecond = maxRequestsPerSecond;
    }

    @Override
    public boolean tryAcquire(String ip) {
        long currentTime = System.currentTimeMillis();
        long currentSecond = currentTime / 1000;

        ipCounters.putIfAbsent(ip, new AtomicInteger(0));
        ipTimestamps.putIfAbsent(ip, currentSecond);

        long lastSecond = ipTimestamps.get(ip);
        AtomicInteger counter = ipCounters.get(ip);

        if (currentSecond > lastSecond) {
            counter.set(1);
            ipTimestamps.put(ip, currentSecond);
            return true;
        } else if (currentSecond == lastSecond) {
            if (counter.incrementAndGet() <= maxRequestsPerSecond) {
                return true;
            } else {
                counter.decrementAndGet();
                return false;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "单IP限流";
    }
}
