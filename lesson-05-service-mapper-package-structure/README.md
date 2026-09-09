# Lesson 05：Service、Mapper 与业务分包

在 lesson-4 的 Entity、Repository、DTO 基础上，把 Controller 中的业务和转换逻辑拆出来。
本课是独立 Maven 项目，不依赖其他 lesson。需要 JDK 26，Maven Wrapper 会使用 Maven 3.9.16。

## 每种类负责什么

| 角色 | 职责 | 本课示例 |
| --- | --- | --- |
| Controller | 接收 HTTP 参数、返回响应 DTO，委托 Service | StudentController |
| Service | 检查学校是否存在，组织查询、保存及转换，管理事务 | StudentService |
| Mapper | 在 DTO 和 Entity 之间转换，不访问数据库 | StudentMapper |
| Repository | 访问数据库 | StudentRepository |
| Entity | 映射表和关联关系 | Student、School |
| DTO | 定义请求和响应的数据格式，不等于数据库表 | StudentDto、StudentResponseDto |

例如创建学生：Controller 接收 StudentDto，Service 查询学校，Mapper 创建 Student，
Repository 保存，Mapper 把保存结果转换成 StudentResponseDto，最后返回给客户端。
Mapper 是 Java 手写类，不需要额外映射框架。它使用 @Component 注册；
@Service 也是组件注解，但留给承担业务逻辑的 Service 更清晰。

## 按业务模块分包

```text
com.yennie.restapi
├── RestApiApplication.java
├── school
│   ├── School.java
│   ├── SchoolController.java
│   ├── SchoolService.java
│   ├── SchoolMapper.java
│   ├── SchoolRepository.java
│   ├── SchoolRequestDto.java
│   └── SchoolResponseDto.java
├── student
│   ├── Student.java
│   ├── StudentController.java
│   ├── StudentService.java
│   ├── StudentMapper.java
│   ├── StudentRepository.java
│   ├── StudentDto.java
│   └── StudentResponseDto.java
└── studentprofile
    └── StudentProfile.java
```

先按 school、student 分业务，再在模块中按类的职责分层。
layer.txt 保留课程中的几种组织方式作为比较；这些文本目录树不会自动创建包。
Java 包名统一使用小写，因此 studentProfile 整理为 studentprofile。

## 相比原始练习的整理

- Controller 不再直接访问 Repository，依赖通过构造器注入。
- 学校接口统一使用 SchoolRequestDto 和 SchoolResponseDto，移除重复的 SchoolDto。
  POST /schools 返回真实的学校 ID，避免创建学生时猜 ID。
- 学校 ID 缺失返回 400，学校或学生查询不到返回 404。
- Service 查询在只读事务内完成，写入方法单独标注 @Transactional。
  关闭 open-in-view 后，读取关联与 DTO 转换仍在 Service 事务中完成。
- 响应 Mapper 兼容旧数据中没有学校的学生，schoolId 和 schoolName 返回 null。
- 密码通过环境变量提供，不写入源码。ddl-auto: update 不会每次启动重建表，
  但它仍可能修改表结构；生产项目通常使用 Flyway/Liquibase 管理迁移。

本课保留学习用的简单错误处理；尚未覆盖全部输入校验、重复邮箱的友好错误提示等生产需求。
StudentProfile 保留实体关系示例，本课没有新增其 HTTP 接口。

## 本地运行

在 IntelliJ 用 File → Open 打开本课目录，等待 Maven 导入。
先启动原有 PostgreSQL 容器。默认连接 localhost:5432/springboot_db，用户名 yennie。
本课和 lesson-4 默认使用同一个数据库，操作会影响同一份数据；不要同时启动两个默认 8080 端口的应用。
需要隔离时，先创建独立数据库，再用 DB_URL 指向它。

终端进入本课目录后：

```bash
export DB_PASSWORD='替换为实际数据库密码'
./mvnw spring-boot:run
```

可用 DB_USERNAME、DB_URL 覆盖默认用户名和连接地址。
若从 IntelliJ 的运行按钮启动，请在 RestApiApplication 的运行配置环境变量中设置 DB_PASSWORD。
数据库工具中的密码配置不会自动提供给 Spring Boot。

## 用 Postman 复习

api-test.http 中的请求可以复制到 Postman，无须 IntelliJ Ultimate。

1. POST /schools，JSON：{"name":"Spring School"}。
2. 复制响应中的 id，填入 POST /students 的 schoolId：

```json
{
  "firstName": "Yennie",
  "lastName": "Peng",
  "email": "yennie@example.com",
  "age": 20,
  "schoolId": 1
}
```

3. GET /students、GET /students/{id}、GET /students/search/yen 查看响应。
4. DELETE /students/{id} 删除刚创建的测试学生，返回 204。

上面的 1 只是示例；每次使用实际学校 ID。email 有唯一约束，重复测试请使用不同邮箱。

## 自动化测试

```bash
./mvnw test
```

测试使用独立的 H2 内存库（PostgreSQL 兼容模式），不需要 Docker，不会访问本地 PostgreSQL。
包含 Repository 关系测试、Mapper 测试，以及真实 HTTP 请求的创建/列表/搜索/查询/删除和错误场景。
H2 测试不能替代 PostgreSQL 本身的集成验证。
