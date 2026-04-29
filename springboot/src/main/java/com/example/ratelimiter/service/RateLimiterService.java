package com.example.ratelimiter.service;

import com.example.ratelimiter.limiter.IPRateLimiter;
import com.example.ratelimiter.limiter.TokenBucketLimiter;
import lombok.Data;
import org.springframework.stereotype.Service;

/**
 * 限流服务
 */
@Service
public class RateLimiterService {
    private final IPRateLimiter ipRateLimiter;
    private final TokenBucketLimiter tokenBucketLimiter;

    public RateLimiterService(IPRateLimiter ipRateLimiter, TokenBucketLimiter tokenBucketLimiter) {
        this.ipRateLimiter = ipRateLimiter;
        this.tokenBucketLimiter = tokenBucketLimiter;
    }

    /**
     * 检查请求是否被允许（结合IP限流和令牌桶）
     */
    public CheckResult checkRequest(String ip) {
        // 先检查IP限流
        if (!ipRateLimiter.tryAcquire(ip)) {
            return CheckResult.builder()
                .allowed(false)
                .blockedBy(ipRateLimiter.getName())
                .message("单个IP每秒最多20个请求")
                .build();
        }

        // 再检查令牌桶
        if (!tokenBucketLimiter.tryAcquire(null)) {
            return CheckResult.builder()
                .allowed(false)
                .blockedBy(tokenBucketLimiter.getName())
                .message("系统令牌已耗尽，请稍后重试")
                .build();
        }

        return CheckResult.builder()
            .allowed(true)
            .build();
    }

    /**
     * 获取令牌桶剩余令牌数
     */
    public long getAvailableTokens() {
        return tokenBucketLimiter.getTokenCount();
    }

    @Data
    public static class CheckResult {
        private boolean allowed;
        private String blockedBy;
        private String message;

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private boolean allowed;
            private String blockedBy;
            private String message;

            public Builder allowed(boolean allowed) {
                this.allowed = allowed;
                return this;
            }

            public Builder blockedBy(String blockedBy) {
                this.blockedBy = blockedBy;
                return this;
            }

            public Builder message(String message) {
                this.message = message;
                return this;
            }

            public CheckResult build() {
                CheckResult result = new CheckResult();
                result.allowed = this.allowed;
                result.blockedBy = this.blockedBy;
                result.message = this.message;
                return result;
            }
        }
    }
}
