package com.example.ratelimiter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 漏桶限流器
 * 漏桶算法：请求进入桶中，以固定速率流出
 * 特点：平滑流量，削峰填谷，防止突发流量击垮系统
 */
public class LeakyBucketLimiter implements RateLimiter {
    private final double rate; // 流出速率（每秒）
    private final long capacity; // 漏桶最大容量
    private final AtomicLong water; // 当前水量（请求数）
    private volatile long lastLeakTime; // 上次漏水时间

    public LeakyBucketLimiter(double rate, long capacity) {
        this.rate = rate;
        this.capacity = capacity;
        this.water = new AtomicLong(0); // 初始空桶
        this.lastLeakTime = System.nanoTime();
    }

    @Override
    public boolean tryAcquire(String key) {
        leakWater(); // 先漏水

        long currentWater = water.get();
        if (currentWater < capacity) {
            // 尝试放入请求
            if (water.compareAndSet(currentWater, currentWater + 1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 漏水操作
     * 根据时间间隔计算应漏出的水量
     */
    private void leakWater() {
        long now = System.nanoTime();
        long timeElapsed = now - lastLeakTime;
        double waterToLeak = (timeElapsed / 1_000_000_000.0) * rate; // 纳秒转秒

        if (waterToLeak > 0) {
            long leakAmount = (long) waterToLeak;
            if (leakAmount > 0) {
                water.updateAndGet(current -> Math.max(0, current - leakAmount));
                lastLeakTime = now;
            }
        }
    }

    @Override
    public String getName() {
        return "漏桶限流";
    }
}