package com.example.ratelimiter.config;

import com.example.ratelimiter.limiter.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 限流算法配置
 */
@Configuration
public class RateLimiterConfig {

    // 限流参数
    public static final int IP_MAX_REQUESTS_PER_SECOND = 20;
    public static final double TOKEN_BUCKET_RATE = 50.0;
    public static final long TOKEN_BUCKET_CAPACITY = 100;

    @Bean
    public IPRateLimiter ipRateLimiter() {
        return new IPRateLimiter(IP_MAX_REQUESTS_PER_SECOND);
    }

    @Bean
    public TokenBucketLimiter tokenBucketLimiter() {
        return new TokenBucketLimiter(TOKEN_BUCKET_RATE, TOKEN_BUCKET_CAPACITY);
    }
}
