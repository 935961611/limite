package com.example.ratelimiter;

/**
 * 限流配置类
 * 统一管理所有限流参数和开关
 */
public class Config {
    // 限流开关
    public static boolean ENABLE_IP_LIMIT = true; // 单IP限流开关
    public static boolean ENABLE_ACCOUNT_LIMIT = true; // 单账号限流开关
    public static boolean ENABLE_GLOBAL_QPS_LIMIT = true; // 全局QPS封顶开关
    public static boolean ENABLE_TOKEN_BUCKET_LIMIT = true; // 令牌桶限流开关
    public static boolean ENABLE_LEAKY_BUCKET_LIMIT = true; // 漏桶限流开关

    // 单IP限流参数
    public static int IP_MAX_REQUESTS_PER_SECOND = 10; // 单个IP每秒最大请求数

    // 单账号限流参数
    public static int ACCOUNT_MAX_REQUESTS_PER_SECOND = 5; // 单个账号每秒最大请求数

    // 全局QPS封顶参数
    public static int GLOBAL_MAX_QPS = 100; // 系统整体最大QPS

    // 令牌桶参数
    public static double TOKEN_BUCKET_RATE = 50.0; // 令牌生成速率（每秒）
    public static int TOKEN_BUCKET_CAPACITY = 100; // 令牌桶最大容量

    // 漏桶参数
    public static double LEAKY_BUCKET_RATE = 40.0; // 漏桶流出速率（每秒）
    public static int LEAKY_BUCKET_CAPACITY = 80; // 漏桶最大容量
}