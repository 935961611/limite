# 限流算法完整实战项目

## 🎯 项目简介

这是一个**限流算法**的完整教学项目，包含两个版本：
- **纯Java版本**：学习算法原理
- **Spring Boot版本**：学习企业级开发

无论你是初学者还是有经验的开发者，都能在这个项目中找到适合你的学习路径。

## 📦 项目结构

```
limite/
├── src/                          # 纯Java版本源代码
│   └── main/java/com/example/ratelimiter/
├── README.md                      # 纯Java版本说明
├── TECH_INTRO.md                  # 技术介绍（适合初学者）
├── VERSION_COMPARISON.md          # 两个版本对比说明
│
└── springboot/                    # Spring Boot版本
    ├── pom.xml                    # Maven依赖
    ├── README.md                  # 详细文档
    ├── QUICKSTART.md              # 快速启动指南
    └── src/
        └── main/
            ├── java/com/example/ratelimiter/
            └── resources/
                ├── application.yml
                ├── static/
                └── templates/
```

## 🚀 快速开始

### 方案A：学习算法原理（初学者推荐）

```bash
cd d:\project\limite
javac -encoding UTF-8 -d . src/main/java/com/example/ratelimiter/*.java
java com.example.ratelimiter.Simulation
```

✅ **优点**：5秒启动，代码简单，专注理论
⏱️ **耗时**：< 1分钟
📚 **文档**：[README.md](README.md)

### 方案B：学习企业级开发（进阶推荐）

```bash
cd d:\project\limite\springboot
mvn spring-boot:run
```

然后访问：http://localhost:8080

✅ **优点**：完整Web应用，有前端界面，贴近工作环境
⏱️ **耗时**：2-5分钟（首次需下载依赖）
📚 **文档**：[springboot/QUICKSTART.md](springboot/QUICKSTART.md)

## 📖 文档导航

### 对于初学者
1. **开始阅读**：[TECH_INTRO.md](TECH_INTRO.md) - 用生活化的语言介绍技术
2. **运行演示**：[README.md](README.md) - 纯Java版本说明
3. **深入学习**：修改代码，尝试不同参数

### 对于有经验的开发者
1. **快速了解**：[VERSION_COMPARISON.md](VERSION_COMPARISON.md) - 版本对比
2. **选择版本**：根据需求选择纯Java版或Spring Boot版
3. **深入代码**：研究限流算法实现和框架集成

### 对于想从零开始学的人
1. **技术介绍**：[TECH_INTRO.md](TECH_INTRO.md)
2. **纯Java版**：[README.md](README.md)
3. **升级进阶**：[springboot/README.md](springboot/README.md)

## 🎓 学习内容

### 纯Java版本学到的东西
- ✅ 限流算法原理（IP限流、令牌桶、漏桶）
- ✅ Java并发编程（原子操作、线程安全）
- ✅ 多线程模拟高并发
- ✅ 系统设计思路

### Spring Boot版本新增内容
- ✅ Spring Boot框架基础
- ✅ Spring Security认证授权
- ✅ REST API设计
- ✅ 前端与后端交互
- ✅ Web应用开发流程

## 💡 核心功能

### 限流算法（两个版本都有）
| 算法 | 说明 | 应用场景 |
|------|------|--------|
| 单IP限流 | 限制单个IP的请求频率 | 防止恶意攻击 |
| 令牌桶 | 固定速率生成令牌，请求消费令牌 | 允许突发流量 |
| 漏桶 | 固定速率流出请求 | 平滑流量输出 |
| 全局QPS | 限制系统总体请求数 | 保护系统 |

### Web功能（Spring Boot版本）
| 功能 | 说明 |
|------|------|
| 用户登录 | Spring Security内存认证 |
| 仪表板 | 实时显示限流效果 |
| 测试接口 | 4个演示API接口 |
| 压力测试 | 快速发送多个请求 |
| 实时日志 | 查看请求放行/拦截详情 |

## 🧪 测试场景

两个版本都内置了以下场景：
- 📊 平稳正常流量
- 💥 业务合理突发
- 🔴 恶意IP高频攻击
- 💬 账号刷屏请求
- ⚡ 突发流量测试

## 🔐 演示账号（Spring Boot版本）

| 账号 | 密码 | 说明 |
|------|------|------|
| user | password | 普通用户 |
| admin | admin123 | 管理员 |

## 📊 性能参数（可自定义）

```
单IP限流：每秒最多20个请求
令牌生成速率：每秒50个令牌
令牌桶容量：最多100个令牌
全局QPS：系统整体最多100/秒
```

修改位置：
- **纯Java版**：`src/main/java/com/example/ratelimiter/Config.java`
- **Spring Boot版**：`springboot/src/main/java/.../config/RateLimiterConfig.java`

## 🛠️ 环境要求

### 纯Java版本
- Java 8+
- （无其他依赖）

### Spring Boot版本
- Java 11+
- Maven 3.6+
- 网络连接（首次下载依赖）

## 🤔 常见问题

**Q: 我是初学者，应该从哪个版本开始？**
A: 从纯Java版本开始！它更简单，能帮你理解核心原理。

**Q: 两个版本的代码是什么关系？**
A: 限流算法完全相同，Spring Boot版本在此基础上加入了框架和Web界面。

**Q: 可以用在生产环境吗？**
A: 可以作为参考，但生产环境建议：
- 使用Redis实现分布式限流
- 加入更复杂的规则引擎
- 集成监控告警系统

**Q: 怎样修改限流参数？**
A: 找到配置文件，修改参数后重新编译/启动。详见各版本的README。

## 🚦 我应该学哪个版本？

### 选择纯Java版本 if：
- 你是编程初学者
- 你想快速看到效果
- 你只关心算法原理
- 你的电脑资源有限

### 选择Spring Boot版本 if：
- 你想学企业级开发
- 你想要完整的Web应用
- 你对Spring框架感兴趣
- 你想积累工作经验

**最佳方案：两个都学！** 从纯Java开始，然后升级到Spring Boot。

## 📈 学习路线

```
Day 1-2: 纯Java版本
  ↓
理解限流算法原理
  ↓
Day 3-4: Spring Boot版本
  ↓
学习企业级开发
  ↓
Day 5-7: 深化和扩展
  ↓
添加新功能、优化代码
  ↓
完成！🎉
```

## 🎯 项目目标

- ✅ 帮助你从零理解限流原理
- ✅ 教你如何用Java实现复杂算法
- ✅ 展示企业级应用的开发方式
- ✅ 为你的简历加分
- ✅ 给你在工作中的参考实例

## 💻 实际应用

这个项目学的知识可以应用到：
- 🌐 **Web应用**：API接口限流
- 📱 **移动应用**：请求频率控制
- 🛍️ **电商系统**：秒杀场景保护
- 💬 **社交平台**：防刷屏
- 🎮 **游戏服务**：并发用户控制
- 🔐 **安全防护**：DDoS防御

## 🤝 贡献和建议

如果你有改进建议，欢迎提出！

## 📝 许可证

本项目仅供学习使用。

---

## 🎬 立即开始

**第一步**：选择你的学习路径
- 初学者 → [纯Java版本](README.md)
- 进阶者 → [Spring Boot版本](springboot/QUICKSTART.md)

**第二步**：阅读对应文档

**第三步**：按照说明编译运行

**第四步**：修改代码，深入学习

**第五步**：开始构建自己的应用！

---

**准备好了吗？开始你的限流学习之旅！** 🚀

