# Spring Boot版本 - 完整安装指南

## 📋 前置条件检查

### 检查Java版本
打开命令行，输入：
```bash
java -version
```

需要 **Java 11** 或更高版本。

**如果没有安装或版本太低：**
1. 下载Java 11+：https://www.oracle.com/java/technologies/
2. 安装JDK（不要安装JRE）
3. 配置环境变量

**简单验证方式：**
```bash
javac -version  # 应该显示 11.0.x 或更高
```

### 检查Maven版本
打开命令行，输入：
```bash
mvn -version
```

需要 **Maven 3.6.x** 或更高版本。

**如果没有安装：**
1. 下载Maven：https://maven.apache.org/download.cgi
2. 解压到某个文件夹（如 `C:\tools\maven-3.8.0`）
3. 配置环境变量：
   - 新增 `MAVEN_HOME = C:\tools\maven-3.8.0`
   - 编辑 `PATH`，添加 `%MAVEN_HOME%\bin`
4. 重新打开命令行，验证：`mvn -version`

### 检查网络连接
Maven需要下载依赖，确保网络正常：
```bash
ping baidu.com
```

## 🔧 环境变量配置（Windows）

如果Java或Maven还没配置，按以下步骤配置：

### 1. 打开系统属性
- 右键"此电脑" → "属性"
- 或直接搜索"编辑系统环境变量"

### 2. 新增/编辑环境变量
点击"环境变量..."按钮，在"系统变量"中：

**新增JAVA_HOME（如果没有）：**
- 变量名：`JAVA_HOME`
- 变量值：`C:\Program Files\Java\jdk-11` （根据实际路径调整）

**新增MAVEN_HOME：**
- 变量名：`MAVEN_HOME`
- 变量值：`C:\tools\maven-3.8.0` （根据实际路径调整）

**编辑PATH：**
- 找到 `Path` 变量，点"编辑"
- 添加两行：
  ```
  %JAVA_HOME%\bin
  %MAVEN_HOME%\bin
  ```

### 3. 验证
关闭所有命令窗口，重新打开一个新窗口，输入：
```bash
java -version
mvn -version
```

都显示版本信息就成功了！

## 📥 下载项目（如果还没有）

项目已经在 `d:\project\limite\springboot\` 中。

如果需要重新下载或更新，可以从Git获取（如果有Git的话）。

## 🚀 启动应用

### 方法1：使用Maven直接启动（推荐）

#### 步骤1：进入项目目录
```bash
cd d:\project\limite\springboot
```

#### 步骤2：启动应用
```bash
mvn spring-boot:run
```

**第一次运行会比较慢**（需要下载依赖），**耐心等待**（可能需要3-10分钟）。

看到以下日志说明启动成功：
```
2024-04-29 10:30:45.123  INFO 12345 --- [main] c.e.r.RateLimiterApplication : Started RateLimiterApplication in 3.5 seconds
```

#### 步骤3：访问应用
打开浏览器，访问：
```
http://localhost:8080
```

应该看到登录页面。

### 方法2：编译打包后运行

#### 步骤1：进入项目目录
```bash
cd d:\project\limite\springboot
```

#### 步骤2：编译打包
```bash
mvn clean package
```

成功后会生成 `target\rate-limiter-1.0.0.jar` 文件。

#### 步骤3：运行JAR包
```bash
java -jar target/rate-limiter-1.0.0.jar
```

### 方法3：使用IDE运行（最简单）

#### IntelliJ IDEA
1. 打开IDEA，选择 "Open Project"
2. 选择 `d:\project\limite\springboot`
3. IDEA会自动识别Maven项目
4. 右键 `RateLimiterApplication.java` → "Run"

#### Visual Studio Code
1. 安装插件：
   - Spring Boot Extension Pack
   - Maven for Java
2. 打开 `d:\project\limite\springboot` 文件夹
3. 在 `RateLimiterApplication.java` 上，点击 "Run" 按钮

#### Eclipse
1. 导入项目：File → Import → Maven → Existing Maven Projects
2. 选择 `d:\project\limite\springboot`
3. 右键项目 → Run As → Maven build...
4. 输入 goals: `spring-boot:run`

## 🔑 登录应用

启动后，访问 http://localhost:8080

在登录页面输入凭证：

**选项1：普通用户**
- 用户名：`user`
- 密码：`password`

**选项2：管理员**
- 用户名：`admin`
- 密码：`admin123`

登录后进入仪表板，可以开始测试！

## ⚙️ 常见问题排查

### 问题1：命令行提示"mvn: 无法识别"

**原因**：Maven环境变量没配好

**解决方案**：
1. 确认Maven安装了
2. 确认 `MAVEN_HOME` 环境变量配置正确
3. 重新打开命令行（重要！）
4. 输入 `mvn -version` 验证

### 问题2：启动时提示"Port 8080 already in use"

**原因**：8080端口被其他应用占用

**解决方案1**：改用其他端口
编辑 `src/main/resources/application.yml`：
```yaml
server:
  port: 8888  # 改为其他端口
```
然后重新启动。

**解决方案2**：关闭占用该端口的应用
```bash
netstat -ano | findstr :8080  # 查看谁占用了8080
taskkill /PID 占用的进程号 /F  # 关闭该进程
```

### 问题3：启动很慢或超时

**原因**：首次启动需要下载很多依赖，网络不好

**解决方案**：
1. 耐心等待（可能需要5-10分钟）
2. 或者配置阿里云Maven镜像加速

**配置镜像加速：**

编辑 Maven 配置文件（`%MAVEN_HOME%\conf\settings.xml`），找到 `<mirrors>` 部分，添加：

```xml
<mirror>
    <id>aliyun</id>
    <name>aliyun</name>
    <url>https://maven.aliyun.com/repository/public/</url>
    <mirrorOf>*</mirrorOf>
</mirror>
```

### 问题4：无法连接到 http://localhost:8080

**原因**：应用没成功启动

**解决方案**：
1. 查看命令行是否有错误信息
2. 查看是否显示了"Started RateLimiterApplication"
3. 检查防火墙是否阻止了
4. 试试访问 `http://127.0.0.1:8080`

### 问题5：登录后显示空白页面

**原因**：前端资源加载出问题

**解决方案**：
1. 按 `F12` 打开浏览器开发者工具
2. 查看 Console 标签是否有错误信息
3. 清空浏览器Cache：Ctrl+Shift+Delete
4. 使用隐身窗口重新尝试

### 问题6：点击测试按钮没反应

**原因**：后端API返回错误

**解决方案**：
1. 按 `F12` 打开开发者工具
2. 切换到 Network 标签
3. 再点一次按钮
4. 查看请求的 Status Code 和 Response
5. 检查服务器日志的错误信息

## 📊 验证启动成功

看以下日志说明启动成功：

```
2024-04-29 10:30:40.123  INFO 12345 --- [main] c.e.r.c.SecurityConfig : Spring Security Configuration loaded
2024-04-29 10:30:40.456  INFO 12345 --- [main] c.e.r.c.RateLimiterConfig : Rate Limiter initialized
2024-04-29 10:30:45.123  INFO 12345 --- [main] c.e.r.RateLimiterApplication : Started RateLimiterApplication in 3.5 seconds
Tomcat started on port(s): 8080
```

然后访问 http://localhost:8080 应该看到登录页面。

## 🛑 停止应用

### 在命令行中运行的应用
按 `Ctrl + C` 停止

### 在IDE中运行的应用
点击IDE中的"Stop"按钮

## 🔄 重新启动

修改代码后，需要重新启动才能生效。

## 💾 常用命令速查

```bash
# 进入项目目录
cd d:\project\limite\springboot

# 清理上次编译的结果
mvn clean

# 编译项目
mvn compile

# 编译并打包
mvn clean package

# 只运行不打包
mvn spring-boot:run

# 跳过测试运行
mvn spring-boot:run -DskipTests

# 安装依赖
mvn dependency:resolve

# 查看依赖树
mvn dependency:tree
```

## 📚 下一步

- 访问 [QUICKSTART.md](QUICKSTART.md) 了解功能
- 访问 [README.md](README.md) 查看详细文档
- 开始修改代码，尝试新功能
- 参考源代码学习Spring Boot开发

## 🆘 还是有问题？

1. 查看项目的 README.md 文件
2. 检查Java和Maven版本是否满足要求
3. 查看命令行的错误信息
4. 尝试使用IDE运行（可能有更友好的错误提示）

---

**祝你成功启动！** 🎉
