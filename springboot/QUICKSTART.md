# 快速启动指南

## 🚀 5分钟快速启动

### 步骤1：进入项目目录
```bash
cd d:\project\limite\springboot
```

### 步骤2：使用Maven启动
```bash
mvn spring-boot:run
```

**或者**编译后运行：
```bash
mvn clean package
java -jar target/rate-limiter-1.0.0.jar
```

### 步骤3：等待启动完成
看到以下日志表示启动成功：
```
Started RateLimiterApplication in 3.5 seconds
```

### 步骤4：打开浏览器访问
访问：http://localhost:8080

### 步骤5：登录系统
使用演示账号登录：
- **账号**：user
- **密码**：password

或者使用：
- **账号**：admin
- **密码**：admin123

### 步骤6：开始测试
在仪表板上点击各个按钮测试限流效果。

---

## 📝 默认配置速查表

| 项 | 值 |
|---|---|
| 应用端口 | 8080 |
| 上下文路径 | / |
| IP限流 | 每秒20个请求 |
| 令牌桶速率 | 每秒50个令牌 |
| 令牌桶容量 | 100个令牌 |
| 默认用户 | user / password |

---

## 🆘 启动不了？

### 提示"Port 8080 already in use"
说明8080端口被占用，修改 `src/main/resources/application.yml`：
```yaml
server:
  port: 8888  # 改为其他端口
```

### 提示"Maven command not found"
Maven没有安装或环境变量没配置，参考：
https://maven.apache.org/install.html

### 提示"Java 11 not found"
需要安装Java 11或更高版本，参考：
https://www.oracle.com/java/technologies/

---

## 📞 需要帮助？

查看详细文档：[README.md](README.md)
