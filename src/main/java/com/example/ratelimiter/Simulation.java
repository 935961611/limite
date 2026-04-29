package com.example.ratelimiter;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限流模拟测试类
 * 模拟各种高并发场景测试限流效果
 */
public class Simulation {
    private static final RateLimitManager manager = new RateLimitManager();
    private static final AtomicInteger totalRequests = new AtomicInteger(0);
    private static final AtomicInteger allowedRequests = new AtomicInteger(0);
    private static final Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 限流算法演示项目启动 ===");
        printConfig();

        // 场景1：平稳正常流量
        System.out.println("\n=== 场景1：平稳正常流量（50并发，持续10秒） ===");
        simulateSteadyTraffic(50, 10);

        // 场景2：业务合理突发流量
        System.out.println("\n=== 场景2：业务合理突发流量（100并发，持续5秒） ===");
        simulateBurstTraffic(100, 5);

        // 场景3：恶意单IP高频攻击
        System.out.println("\n=== 场景3：恶意单IP高频攻击（同一IP高频请求） ===");
        simulateMaliciousIPAttack();

        // 场景4：同一账号刷屏请求
        System.out.println("\n=== 场景4：同一账号刷屏请求（同一账号高频请求） ===");
        simulateAccountSpam();

        // 场景5：令牌桶攒满后超大突发流量
        System.out.println("\n=== 场景5：令牌桶攒满后超大突发流量（200并发，持续3秒） ===");
        simulateTokenBucketBurst(200, 3);

        printSummary();
    }

    /**
     * 模拟平稳流量
     */
    private static void simulateSteadyTraffic(int concurrency, int durationSeconds) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(concurrency);
        for (int i = 0; i < concurrency; i++) {
            executor.submit(() -> {
                for (int j = 0; j < durationSeconds * 10; j++) { // 每秒10次请求
                    sendRequest("192.168.1." + random.nextInt(255), "user" + random.nextInt(100));
                    try {
                        Thread.sleep(100); // 100ms间隔
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(durationSeconds + 2, TimeUnit.SECONDS);
    }

    /**
     * 模拟突发流量
     */
    private static void simulateBurstTraffic(int concurrency, int durationSeconds) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(concurrency);
        for (int i = 0; i < concurrency; i++) {
            executor.submit(() -> {
                for (int j = 0; j < durationSeconds * 20; j++) { // 每秒20次请求
                    sendRequest("192.168.2." + random.nextInt(255), "user" + random.nextInt(100));
                    try {
                        Thread.sleep(50); // 50ms间隔
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(durationSeconds + 2, TimeUnit.SECONDS);
    }

    /**
     * 模拟恶意IP攻击
     */
    private static void simulateMaliciousIPAttack() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(20);
        String maliciousIP = "192.168.3.1";
        for (int i = 0; i < 20; i++) {
            executor.submit(() -> {
                for (int j = 0; j < 50; j++) { // 每个线程50次请求
                    sendRequest(maliciousIP, "user" + random.nextInt(100));
                    try {
                        Thread.sleep(10); // 10ms间隔，高频
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    /**
     * 模拟账号刷屏
     */
    private static void simulateAccountSpam() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(20);
        String spamAccount = "spammer";
        for (int i = 0; i < 20; i++) {
            executor.submit(() -> {
                for (int j = 0; j < 50; j++) {
                    sendRequest("192.168.4." + random.nextInt(255), spamAccount);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    /**
     * 模拟令牌桶攒满后突发
     */
    private static void simulateTokenBucketBurst(int concurrency, int durationSeconds) throws InterruptedException {
        // 先等待令牌桶攒满
        System.out.println("等待令牌桶攒满...");
        Thread.sleep(3000); // 等待3秒让令牌攒满

        ExecutorService executor = Executors.newFixedThreadPool(concurrency);
        for (int i = 0; i < concurrency; i++) {
            executor.submit(() -> {
                for (int j = 0; j < durationSeconds * 50; j++) { // 高频请求
                    sendRequest("192.168.5." + random.nextInt(255), "user" + random.nextInt(100));
                    try {
                        Thread.sleep(5); // 5ms间隔
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(durationSeconds + 2, TimeUnit.SECONDS);
    }

    /**
     * 发送请求并记录结果
     */
    private static void sendRequest(String ip, String account) {
        totalRequests.incrementAndGet();
        RateLimitManager.CheckResult result = manager.check(ip, account);

        if (result.allowed) {
            allowedRequests.incrementAndGet();
            System.out.println("请求放行 - IP: " + ip + ", 账号: " + account);
        } else {
            System.out.println("请求拦截 - IP: " + ip + ", 账号: " + account + " | 被拦截层: " + result.blockedBy + " | 规则: " + result.rule);
        }
    }

    /**
     * 打印配置信息
     */
    private static void printConfig() {
        System.out.println("当前配置:");
        System.out.println("单IP限流: " + (Config.ENABLE_IP_LIMIT ? "开启" : "关闭") + " (每秒最大: " + Config.IP_MAX_REQUESTS_PER_SECOND + ")");
        System.out.println("单账号限流: " + (Config.ENABLE_ACCOUNT_LIMIT ? "开启" : "关闭") + " (每秒最大: " + Config.ACCOUNT_MAX_REQUESTS_PER_SECOND + ")");
        System.out.println("全局QPS封顶: " + (Config.ENABLE_GLOBAL_QPS_LIMIT ? "开启" : "关闭") + " (最大QPS: " + Config.GLOBAL_MAX_QPS + ")");
        System.out.println("令牌桶限流: " + (Config.ENABLE_TOKEN_BUCKET_LIMIT ? "开启" : "关闭") + " (速率: " + Config.TOKEN_BUCKET_RATE + "/s, 容量: " + Config.TOKEN_BUCKET_CAPACITY + ")");
        System.out.println("漏桶限流: " + (Config.ENABLE_LEAKY_BUCKET_LIMIT ? "开启" : "关闭") + " (速率: " + Config.LEAKY_BUCKET_RATE + "/s, 容量: " + Config.LEAKY_BUCKET_CAPACITY + ")");
    }

    /**
     * 打印总结
     */
    private static void printSummary() {
        int total = totalRequests.get();
        int allowed = allowedRequests.get();
        int blocked = total - allowed;
        System.out.println("\n=== 测试总结 ===");
        System.out.println("总请求数: " + total);
        System.out.println("放行请求: " + allowed + " (" + String.format("%.2f", (double)allowed/total*100) + "%)");
        System.out.println("拦截请求: " + blocked + " (" + String.format("%.2f", (double)blocked/total*100) + "%)");
    }
}