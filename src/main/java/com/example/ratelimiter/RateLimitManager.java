package com.example.ratelimiter;

/**
 * 限流管理器
 * 按固定顺序依次校验各层限流
 * 顺序：单IP/单账号 → 全局QPS → 令牌桶 → 漏桶
 */
public class RateLimitManager {
    private final IPRateLimiter ipLimiter;
    private final AccountRateLimiter accountLimiter;
    private final GlobalQPSLimiter globalQpsLimiter;
    private final TokenBucketLimiter tokenBucketLimiter;
    private final LeakyBucketLimiter leakyBucketLimiter;

    public RateLimitManager() {
        this.ipLimiter = new IPRateLimiter(Config.IP_MAX_REQUESTS_PER_SECOND);
        this.accountLimiter = new AccountRateLimiter(Config.ACCOUNT_MAX_REQUESTS_PER_SECOND);
        this.globalQpsLimiter = new GlobalQPSLimiter(Config.GLOBAL_MAX_QPS);
        this.tokenBucketLimiter = new TokenBucketLimiter(Config.TOKEN_BUCKET_RATE, Config.TOKEN_BUCKET_CAPACITY);
        this.leakyBucketLimiter = new LeakyBucketLimiter(Config.LEAKY_BUCKET_RATE, Config.LEAKY_BUCKET_CAPACITY);
    }

    /**
     * 检查请求是否允许通过
     * @param ip 请求IP
     * @param account 请求账号
     * @return 检查结果
     */
    public CheckResult check(String ip, String account) {
        // 1. 单IP限流
        if (Config.ENABLE_IP_LIMIT && !ipLimiter.tryAcquire(ip)) {
            return new CheckResult(false, ipLimiter.getName(), "单IP每秒最大请求数: " + Config.IP_MAX_REQUESTS_PER_SECOND);
        }

        // 2. 单账号限流
        if (Config.ENABLE_ACCOUNT_LIMIT && !accountLimiter.tryAcquire(account)) {
            return new CheckResult(false, accountLimiter.getName(), "单账号每秒最大请求数: " + Config.ACCOUNT_MAX_REQUESTS_PER_SECOND);
        }

        // 3. 全局QPS封顶
        if (Config.ENABLE_GLOBAL_QPS_LIMIT && !globalQpsLimiter.tryAcquire(null)) {
            return new CheckResult(false, globalQpsLimiter.getName(), "全局最大QPS: " + Config.GLOBAL_MAX_QPS);
        }

        // 4. 令牌桶限流
        if (Config.ENABLE_TOKEN_BUCKET_LIMIT && !tokenBucketLimiter.tryAcquire(null)) {
            return new CheckResult(false, tokenBucketLimiter.getName(), "令牌生成速率: " + Config.TOKEN_BUCKET_RATE + "/s, 桶容量: " + Config.TOKEN_BUCKET_CAPACITY);
        }

        // 5. 漏桶限流（兜底）
        if (Config.ENABLE_LEAKY_BUCKET_LIMIT && !leakyBucketLimiter.tryAcquire(null)) {
            return new CheckResult(false, leakyBucketLimiter.getName(), "流出速率: " + Config.LEAKY_BUCKET_RATE + "/s, 桶容量: " + Config.LEAKY_BUCKET_CAPACITY);
        }

        return new CheckResult(true, null, null);
    }

    /**
     * 检查结果类
     */
    public static class CheckResult {
        public final boolean allowed;
        public final String blockedBy;
        public final String rule;

        public CheckResult(boolean allowed, String blockedBy, String rule) {
            this.allowed = allowed;
            this.blockedBy = blockedBy;
            this.rule = rule;
        }
    }
}