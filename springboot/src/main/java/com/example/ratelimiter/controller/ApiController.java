package com.example.ratelimiter.controller;

import com.example.ratelimiter.service.RateLimiterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 页面控制器
 */
@Slf4j
@Controller
public class PageController {

    /**
     * 首页
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }

    /**
     * 登录页面
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 仪表板页面
     */
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}

/**
 * API 控制器
 */
@Slf4j
@RestController
@RequestMapping("/api")
class ApiController {
    private final RateLimiterService rateLimiterService;

    public ApiController(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    /**
     * 测试接口1：获取用户信息
     */
    @GetMapping("/test/user-info")
    public ApiResponse getUserInfo(HttpServletRequest request) {
        String ip = getClientIp(request);
        RateLimiterService.CheckResult result = rateLimiterService.checkRequest(ip);

        if (!result.isAllowed()) {
            log.warn("请求被限流: IP={}, 原因={}", ip, result.getBlockedBy());
            return ApiResponse.fail(result.getMessage());
        }

        log.info("请求通过: IP={}, 操作=获取用户信息", ip);
        return ApiResponse.success("用户信息获取成功", new UserInfo("张三", "user@example.com"));
    }

    /**
     * 测试接口2：获取数据列表
     */
    @GetMapping("/test/data-list")
    public ApiResponse getDataList(HttpServletRequest request) {
        String ip = getClientIp(request);
        RateLimiterService.CheckResult result = rateLimiterService.checkRequest(ip);

        if (!result.isAllowed()) {
            log.warn("请求被限流: IP={}, 原因={}", ip, result.getBlockedBy());
            return ApiResponse.fail(result.getMessage());
        }

        log.info("请求通过: IP={}, 操作=获取数据列表", ip);
        return ApiResponse.success("数据列表获取成功", 
            new java.util.ArrayList<String>() {{
                add("项目1");
                add("项目2");
                add("项目3");
            }}
        );
    }

    /**
     * 测试接口3：发送消息
     */
    @PostMapping("/test/send-message")
    public ApiResponse sendMessage(HttpServletRequest request, @RequestParam String message) {
        String ip = getClientIp(request);
        RateLimiterService.CheckResult result = rateLimiterService.checkRequest(ip);

        if (!result.isAllowed()) {
            log.warn("请求被限流: IP={}, 原因={}", ip, result.getBlockedBy());
            return ApiResponse.fail(result.getMessage());
        }

        log.info("请求通过: IP={}, 操作=发送消息, 内容={}", ip, message);
        return ApiResponse.success("消息发送成功", new MessageResult("msg_" + System.currentTimeMillis(), "已发送"));
    }

    /**
     * 测试接口4：耗时操作（模拟）
     */
    @PostMapping("/test/heavy-operation")
    public ApiResponse heavyOperation(HttpServletRequest request) {
        String ip = getClientIp(request);
        RateLimiterService.CheckResult result = rateLimiterService.checkRequest(ip);

        if (!result.isAllowed()) {
            log.warn("请求被限流: IP={}, 原因={}", ip, result.getBlockedBy());
            return ApiResponse.fail(result.getMessage());
        }

        log.info("请求通过: IP={}, 操作=耗时操作", ip);
        try {
            Thread.sleep(500); // 模拟耗时操作
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return ApiResponse.success("耗时操作完成", new OperationResult("成功", "用时500ms"));
    }

    /**
     * 获取系统状态
     */
    @GetMapping("/status")
    public ApiResponse getStatus() {
        return ApiResponse.success("系统运行正常", 
            new SystemStatus(rateLimiterService.getAvailableTokens())
        );
    }

    /**
     * 获取客户端IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip.split(",")[0].trim();
    }

    // ============ 内部类定义 ============

    static class UserInfo {
        public String name;
        public String email;
        public UserInfo(String name, String email) {
            this.name = name;
            this.email = email;
        }
    }

    static class MessageResult {
        public String messageId;
        public String status;
        public MessageResult(String messageId, String status) {
            this.messageId = messageId;
            this.status = status;
        }
    }

    static class OperationResult {
        public String result;
        public String detail;
        public OperationResult(String result, String detail) {
            this.result = result;
            this.detail = detail;
        }
    }

    static class SystemStatus {
        public long availableTokens;
        public SystemStatus(long availableTokens) {
            this.availableTokens = availableTokens;
        }
    }
}

/**
 * API 通用响应
 */
class ApiResponse {
    public boolean success;
    public String message;
    public Object data;
    public long timestamp;

    private ApiResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public static ApiResponse success(String message, Object data) {
        return new ApiResponse(true, message, data);
    }

    public static ApiResponse fail(String message) {
        return new ApiResponse(false, message, null);
    }
}
