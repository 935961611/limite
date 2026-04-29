# 限流系统 Spring Boot 版本

## 🚀 项目概述

这是一个**Spring Boot + Spring Security + 前端界面**的限流系统演示项目，基于之前的纯Java限流算法进行改造，使其成为一个完整的Web应用。

### 核心特性
- ✅ **Spring Boot 框架**：现代化Java Web框架
- ✅ **Spring Security**：内存用户认证系统
- ✅ **前端界面**：美观的登录和测试界面
- ✅ **REST API**：多个测试接口
- ✅ **限流算法**：IP限流 + 令牌桶算法
- ✅ **实时反馈**：请求成功/被拦截的详细日志
- ✅ **压力测试**：内置压力测试功能

## 📁 项目结构

```
springboot/
├── pom.xml                                 # Maven依赖管理
├── src/main/java/com/example/ratelimiter/
│   ├── RateLimiterApplication.java         # Spring Boot启动类
│   ├── config/
│   │   ├── RateLimiterConfig.java         # 限流配置
│   │   └── SecurityConfig.java            # Spring Security配置
│   ├── controller/
│   │   └── ApiController.java             # 页面和API控制器
│   ├── service/
│   │   └── RateLimiterService.java        # 限流业务逻辑
│   └── limiter/
│       ├── RateLimiter.java               # 限流器接口
│       ├── IPRateLimiter.java             # IP限流实现
│       └── TokenBucketLimiter.java        # 令牌桶限流实现
├── src/main/resources/
│   ├── application.yml                     # 应用配置
│   ├── static/                             # 静态资源（如有）
│   └── templates/
│       ├── login.html                      # 登录页面
│       └── dashboard.html                  # 仪表板页面（测试界面）
```

## 🔐 登录凭证

### 演示账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| user | password | USER |
| admin | admin123 | ADMIN |

## 📦 安装和运行

### 前置要求
- Java 11+
- Maven 3.6+

### 编译和运行

#### 方式1：使用Maven直接运行
```bash
cd d:\project\limite\springboot

# 编译并运行
mvn spring-boot:run
```

#### 方式2：先编译后运行
```bash
cd d:\project\limite\springboot

# 编译打包
mvn clean package

# 运行
java -jar target/rate-limiter-1.0.0.jar
```

#### 方式3：使用IDE运行
在IDE中打开项目，找到 `RateLimiterApplication.java`，右键选择"Run"。

### 访问应用

启动成功后，访问以下地址：

- **登录页面**：http://localhost:8080/
- **仪表板**：http://localhost:8080/dashboard（需要登录）
- **API根路径**：http://localhost:8080/api/

## 🧪 功能介绍

### 1. 登录系统
- 基于Spring Security的内存认证
- 支持账号密码登录
- 登出后返回登录页

### 2. 仪表板
登录后进入仪表板，可以看到：
- **实时状态**：可用令牌数、总请求、成功请求、被拦截请求
- **测试按钮**：4个不同的测试接口
- **压力测试**：快速发送大量请求测试限流效果
- **日志面板**：实时显示请求结果

### 3. 测试接口

#### 接口1：获取用户信息
```
GET /api/test/user-info
```
- 模拟获取用户信息的操作
- 返回用户名和邮箱

#### 接口2：获取数据列表
```
GET /api/test/data-list
```
- 模拟获取数据列表的操作
- 返回项目列表

#### 接口3：发送消息
```
POST /api/test/send-message?message=xxx
```
- 模拟发送消息的操作
- 需要消息参数

#### 接口4：耗时操作
```
POST /api/test/heavy-operation
```
- 模拟耗时操作（耗时500ms）
- 用来测试系统在处理慢请求时的限流表现

### 4. 系统状态接口
```
GET /api/status
```
- 获取系统当前状态
- 返回可用令牌数

## 🎯 限流原理

### 限流流程

每个请求进来时：
1. **检查IP限流**：单个IP每秒最多20个请求
2. **检查令牌桶**：如果令牌桶中有令牌，则通过；否则拒绝

### 令牌桶参数

在 `RateLimiterConfig.java` 中配置：

```java
// IP限流：每秒最多20个请求
public static final int IP_MAX_REQUESTS_PER_SECOND = 20;

// 令牌生成速率：每秒50个令牌
public static final double TOKEN_BUCKET_RATE = 50.0;

// 令牌桶容量：最多积攒100个令牌
public static final long TOKEN_BUCKET_CAPACITY = 100;
```

## 🧪 测试场景

### 场景1：正常访问
- 在登录后，点击各个按钮，每个按钮都应该能正常返回成功

### 场景2：快速点击
- 在短时间内快速点击同一个按钮，会看到部分请求被限流拦截

### 场景3：压力测试
- 输入一个数字（如50），点击"开始压力测试"
- 系统会快速发送50个请求，大部分会被限流拦截

## 🔧 自定义配置

### 修改限流参数

编辑 `src/main/java/com/example/ratelimiter/config/RateLimiterConfig.java`：

```java
// 修改IP限流阈值
public static final int IP_MAX_REQUESTS_PER_SECOND = 10; // 改为10

// 修改令牌生成速率
public static final double TOKEN_BUCKET_RATE = 100.0; // 改为100/秒

// 修改令牌桶容量
public static final long TOKEN_BUCKET_CAPACITY = 200; // 改为200
```

修改后重新编译运行。

### 修改登录账号

编辑 `src/main/java/com/example/ratelimiter/config/SecurityConfig.java`：

```java
@Bean
public UserDetailsService userDetailsService() {
    UserDetails user = User.builder()
        .username("新用户名")
        .password(passwordEncoder().encode("新密码"))
        .roles("USER")
        .build();
    
    // ...
}
```

### 修改应用端口

编辑 `src/main/resources/application.yml`：

```yaml
server:
  port: 8888  # 改为其他端口
```

## 📊 日志查看

启动应用后，控制台会输出详细的日志：

```
2024-04-29 10:30:45 - 请求通过: IP=192.168.1.1, 操作=获取用户信息
2024-04-29 10:30:46 - 请求被限流: IP=192.168.1.1, 原因=单IP限流
2024-04-29 10:30:47 - 请求通过: IP=192.168.1.2, 操作=获取数据列表
```

在仪表板的"请求结果日志"中也能看到所有请求的结果。

## 🚨 故障排除

### 问题1：项目启动失败，提示Maven依赖下载失败
**解决方案**：
- 检查网络连接
- 手动配置Maven源：编辑 `%MAVEN_HOME%/conf/settings.xml`
- 配置阿里云镜像

### 问题2：启动成功但无法访问
**解决方案**：
- 检查防火墙设置
- 确认应用确实启动（看是否有"Started RateLimiterApplication"的日志）
- 检查端口是否被占用：`netstat -ano | findstr :8080`

### 问题3：登录后仍然显示登录页面
**解决方案**：
- 清空浏览器Cookie
- 使用隐身窗口重新尝试
- 检查浏览器控制台的错误信息

## 📚 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.2.0 | Web框架 |
| Spring Security | 6.x | 安全认证 |
| Thymeleaf | 3.x | 模板引擎 |
| Lombok | 1.x | 代码简化 |
| Maven | 3.6+ | 依赖管理 |

## 🎓 学习要点

1. **Spring Boot基础**：自动配置、起步依赖
2. **Spring Security**：认证、授权、配置
3. **REST API设计**：接口设计最佳实践
4. **前端基础**：HTML、CSS、JavaScript交互
5. **限流算法**：IP限流、令牌桶原理
6. **并发编程**：原子操作、线程安全

## 💡 后续扩展方向

1. **集成数据库**：使用JPA/Mybatis存储用户信息
2. **Redis支持**：使用Redis实现分布式限流
3. **监控面板**：增加实时监控和数据可视化
4. **API权限**：不同用户享受不同的限流额度
5. **黑白名单**：支持IP黑白名单功能
6. **限流规则热更新**：不重启应用修改限流参数

## 📝 常见问题

**Q: 这个项目支持分布式部署吗？**
A: 当前版本是单机版，限流数据存储在内存中。要支持分布式，需要改用Redis等分布式缓存。

**Q: 可以修改限流算法吗？**
A: 当然可以！所有限流算法都在 `limiter` 包中，可以新增或修改。

**Q: 如何增加更多测试接口？**
A: 在 `ApiController.java` 中添加新的 `@GetMapping` 或 `@PostMapping` 方法即可。

## 📞 获取帮助

遇到问题可以：
1. 查看应用日志
2. 检查浏览器开发者工具（F12）
3. 参考项目中的注释代码

## 📄 许可证

本项目仅供学习使用。

---

**祝你学习愉快！** 🎉
