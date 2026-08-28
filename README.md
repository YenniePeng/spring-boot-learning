# Spring Boot Learning Journey

这是一个持续更新的 Spring Boot 学习仓库，用来保存课程示例、概念验证和练习代码。

每个知识点放在独立的 `lesson` 子目录中，并作为独立 Maven 模块运行。仓库根目录是 Maven 聚合项目，使用 IntelliJ 打开整个仓库即可同时管理所有课程。后续学习 Spring MVC、REST API、数据库、JPA、PostgreSQL 和 Docker 时，会继续按照编号追加。

## 当前课程

| Lesson | 主题 | 主要知识点 |
| --- | --- | --- |
| `lesson-01-beans-dependency-injection` | Beans & Dependency Injection | Spring 容器、`@Configuration`、`@Bean`、`@Primary`、`@Qualifier`、构造器注入、`@Value`、`@PropertySource` |

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

## 运行方式

需要安装 Lesson `pom.xml` 中指定的 JDK。Maven Wrapper 位于仓库根目录，可以在根目录运行 Lesson 1：

```bash
./mvnw -pl lesson-01-beans-dependency-injection spring-boot:run
```

在根目录运行所有 Lesson 的测试：

```bash
./mvnw test
```

也可以只测试 Lesson 1：

```bash
./mvnw -pl lesson-01-beans-dependency-injection test
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
├── .mvn/
├── mvnw
├── pom.xml
├── lesson-01-beans-dependency-injection/
├── lesson-02-topic-name/
├── lesson-03-topic-name/
└── README.md
```

每个 Lesson 保留自己的 Maven 配置和源码：

```text
pom.xml
src/main/
src/test/
```

根目录 `pom.xml` 通过 `<modules>` 注册所有课程。这样可以直接在 IntelliJ 中打开整个仓库，同时编译和测试所有 Lesson，而不同课程仍然拥有独立依赖及配置。

## 后续更新

每学完一个新的 Spring Boot 知识点，就新增一个编号 Lesson，并完成两项更新：

1. 在根目录 `pom.xml` 的 `<modules>` 中注册新 Lesson。
2. 在本 README 的课程列表中记录主题和主要知识点。
