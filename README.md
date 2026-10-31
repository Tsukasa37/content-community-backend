# Content Community Backend（内容社区后端）

一个用 **Spring Boot 3 + MySQL + JWT** 实现的轻量内容社区 RESTful 后端服务，适合作为后端求职面试的作品集项目。包含完整的用户注册/登录鉴权、文章 CRUD、评论、分页查询、统一异常处理、参数校验与单元测试。

## ✨ 功能特性

- **用户认证**：注册、登录，BCrypt 加密存储密码，签发 JWT 令牌，无状态会话
- **文章管理**：发布、分页列表（创建时间倒序）、详情、仅作者可更新/删除
- **评论管理**：发表评论、按文章查询、仅评论作者可删除
- **健壮性**：统一响应包装、全局异常处理、参数校验（JSR-303）、SQL 注入防护（JPA 预编译）
- **工程化**：分层架构（Controller / Service / Repository）、DTO 隔离、OpenAPI 接口文档、单元测试（H2 内存库）

## 🛠 技术栈

| 分类 | 技术 |
|---|---|
| 框架 | Spring Boot 3.2、Spring Web、Spring Data JPA、Spring Security |
| 数据库 | MySQL 8（运行时）、H2（测试） |
| 认证 | JWT（jjwt 0.11）+ BCrypt |
| 校验 | Jakarta Bean Validation |
| 文档 | Springdoc OpenAPI（Swagger UI） |
| 构建 | Maven、Java 17 |

## 🚀 快速开始

前置要求：JDK 17+、Maven 3.6+、MySQL 8（本地 `community` 库可自动创建）。

1. 修改 `src/main/resources/application.yml` 中的数据库用户名/密码，以及 `jwt.secret`（正式使用请换成随机长密钥）。
2. 启动 MySQL，然后运行：

```bash
mvn spring-boot:run
```

3. 访问接口文档：http://localhost:8080/swagger-ui.html

运行测试（H2 内存库，无需 MySQL）：

```bash
mvn test
```

## 📡 API 一览

| 方法 | 路径 | 说明 | 鉴权 |
|---|---|---|---|
| POST | `/api/auth/register` | 注册 | 否 |
| POST | `/api/auth/login` | 登录，返回 JWT | 否 |
| POST | `/api/articles` | 发布文章 | 是 |
| GET | `/api/articles?page=1&size=10` | 分页查询文章 | 否 |
| GET | `/api/articles/{id}` | 文章详情 | 否 |
| PUT | `/api/articles/{id}` | 更新文章（作者本人） | 是 |
| DELETE | `/api/articles/{id}` | 删除文章（作者本人） | 是 |
| POST | `/api/comments/article/{articleId}` | 发表评论 | 是 |
| GET | `/api/comments/article/{articleId}` | 查询某文章的评论 | 否 |
| DELETE | `/api/comments/{id}` | 删除评论（作者本人） | 是 |

> 需要鉴权的接口：请求头携带 `Authorization: Bearer <token>`。

## 📂 项目结构

```
src/main/java/com/community/
├── CommunityApplication.java   启动类
├── config/                    JWT 工具、认证过滤器、安全配置、当前用户工具
├── controller/                REST 接口
├── service/                   业务逻辑层
├── repository/                JPA 数据访问层
├── entity/                    数据库实体
├── dto/                       请求/响应对象
└── exception/                 业务异常与全局异常处理
```

## 📌 可扩展方向（面试加分点）

- 关注 / 点赞 / 收藏功能
- Redis 缓存热门文章、实现接口限流
- 全文检索（如 Elasticsearch）
- 消息队列解耦点赞与计数
- Docker 化部署 + CI/CD

## 📄 License

MIT
