# Spring Boot Learning Journey

这是一个持续更新的 Spring Boot 学习仓库，用来保存课程示例、概念验证和练习代码。

每个知识点放在独立的 `lesson` 子目录中。每个 Lesson 都是完整、独立的 Spring Boot Maven 项目，拥有自己的 `pom.xml` 和 Maven Wrapper，可以单独下载、用 IntelliJ 打开、运行和测试。后续学习 Spring MVC、REST API、数据库、JPA、PostgreSQL 和 Docker 时，会继续按照编号追加。

## 当前课程

| Lesson | 主题 | 主要知识点 |
| --- | --- | --- |
| `lesson-01-beans-dependency-injection` | Beans & Dependency Injection | Spring 容器、`@Configuration`、`@Bean`、`@Primary`、`@Qualifier`、构造器注入、`@Value`、`@PropertySource` |
| `lesson-02-spring-profiles` | Spring Profiles | `@Profile`、激活环境、环境专属 Bean、`application-{profile}.properties`、默认配置与配置优先级 |
| `lesson-03-rest-api-basics` | REST API Basics | `@RestController`、GET/POST、`@RequestBody`、`@PathVariable`、`@RequestParam`、Jackson、JavaBean 与 Record |

## Lesson 01：Beans & Dependency Injection

本课通过三个相同类型、不同名称的 `MyFirstClass` Bean 演示 Spring 如何创建、保存和选择对象。

- `ApplicationConfig`：使用 `@Bean` 注册对象，并演示 Bean 名称和 `@Primary`
- `MyFirstClass`：由配置类创建的普通 Java 对象
- `MyFirstService`：使用构造器注入和 `@Qualifier` 选择指定 Bean
- `ExampleApplication`：启动 Spring 容器并读取示例结果
- `custom.properties`：提供上午问候配置
- `custom-file-2.properties`：提供下午问候配置
- `ExampleApplicationTests`：验证默认 Bean、指定 Bean 和外部属性

核心依赖关系：

```text
ApplicationConfig
       ↓ 创建
MyFirstClass Beans
       ↓ 构造器注入
MyFirstService
       ↓ 从容器获取并调用
ExampleApplication
```

## Lesson 02：Spring Profiles

本课演示如何通过 Profile 在不同运行环境中选择不同的 Bean 和配置值。

- `ProfileApplication`：使用 `dev` 作为默认 Profile，同时允许外部配置覆盖
- `ApplicationConfig`：分别为 `dev` 和 `test` 创建名为 `profileBean` 的 Bean
- `ProfileService`：通过同一个 Bean 名称注入当前环境对应的对象
- `application-dev.properties`：开发环境配置
- `application-test.properties`：测试环境配置
- Profile 测试：分别验证 `dev` 和 `test` 环境加载的内容

运行默认的 `dev` 环境：

```bash
cd lesson-02-spring-profiles
./mvnw spring-boot:run
```

使用 `test` 环境运行：

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=test
```

## Lesson 03：REST API Basics

本课创建第一个 REST Controller，并通过 Postman 观察 HTTP 请求如何转换成 Java 数据。

- `FirstController`：GET、POST、路径变量、查询参数和请求体示例
- `Order`：使用 getter/setter 的传统 JavaBean
- `OrderRecord`：使用构造器接收数据的简洁 Record
- `api-test.http`：完整请求样例，也可以复制到 Postman 中测试

运行 Lesson 03：

```bash
cd lesson-03-rest-api-basics
./mvnw spring-boot:run
```

## 运行方式

需要安装 Lesson `pom.xml` 中指定的 JDK。以 Lesson 01 为例，先进入课程目录：

```bash
cd lesson-01-beans-dependency-injection
```

运行应用：

```bash
./mvnw spring-boot:run
```

运行这一课的测试：

```bash
./mvnw test
```

Windows 用户可以把 `./mvnw` 换成：

```text
mvnw.cmd
```

构建生成的 `target` 文件不会提交到 Git。

## 目录约定

后续课程继续使用统一命名方式：

```text
spring-boot-learning/
├── lesson-01-beans-dependency-injection/
│   ├── .mvn/
│   ├── mvnw
│   ├── pom.xml
│   └── src/
├── lesson-02-spring-profiles/
│   ├── .mvn/
│   ├── mvnw
│   ├── pom.xml
│   └── src/
├── lesson-03-rest-api-basics/
│   ├── .mvn/
│   ├── mvnw
│   ├── pom.xml
│   └── src/
└── README.md
```

每个 Lesson 都包含独立运行所需的文件：

```text
.mvn/
mvnw
mvnw.cmd
pom.xml
src/main/
src/test/
```

在 IntelliJ 中学习某一课时，直接打开对应的 Lesson 文件夹，而不是打开整个仓库。例如复习 REST API 时，打开 `lesson-03-rest-api-basics`。

> GitHub 的普通下载按钮会下载整个仓库。如果只需要某一课，可以使用 GitHub 的目录下载工具，或者克隆仓库后仅打开相应 Lesson；每个 Lesson 本身不依赖其他课程。

## 后续更新

每学完一个新的 Spring Boot 知识点，就新增一个编号 Lesson，并完成以下更新：

1. 创建包含独立 `pom.xml` 和 Maven Wrapper 的 Lesson 文件夹。
2. 确认该 Lesson 可以单独运行并通过测试。
3. 在本 README 的课程列表中记录主题和主要知识点。
