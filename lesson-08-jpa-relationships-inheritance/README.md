# Lesson 08：实体关系与继承映射

独立 Maven 项目，使用 Java 26、Spring Boot 4.1.1、Hibernate 和 Lombok。

## 本课代码

| 示例 | 数据库存储方式 |
| --- | --- |
| Author ↔ Course | 多对多，Course 维护 authors_courses 中间表 |
| Course → Section → Lecture | 一对多，外键分别为 section.course_id、lecture.section_id |
| Lecture ↔ Resource | 一对一，Resource.lecture 维护 lecture_id，Lecture 使用 mappedBy |
| BaseEntity → Author/Course/Section/Lecture | @MappedSuperclass，共享字段进入各实体表，不创建 base_entity 表 |
| Resource → Video/Text/File | SINGLE_TABLE，共用 resource 表，通过 resource_type 的 V/T/F 区分类型 |

BaseEntity 包含 id、createdAt、lastModified、createdBy、lastModifiedBy。目前没有自动审计配置，时间与用户字段需要手动赋值。

Resource 自己也是实体，未继承 BaseEntity。Video 的 length、Text 的 content、File 的 type 均存入 resource 表。其他类型的专属列可以为空。

Lombok 的 @NoArgsConstructor 生成无参构造方法，@AllArgsConstructor 处理当前类字段，@SuperBuilder 支持父类与子类字段的链式构建。课程保留 @Data；实际扩展双向关系时，应避免生成的 toString/equals/hashCode 遍历循环关系。

## 运行

在 IntelliJ 中直接打开本目录。PostgreSQL 默认连接 localhost:5432/data_jpa，运行前确保数据库存在并设置环境变量：

```bash
export DB_PASSWORD=your_postgres_password
./mvnw spring-boot:run
```

DB_URL、DB_USERNAME 可以覆盖连接地址和用户名。切换 MySQL：

```bash
export MYSQL_DB_PASSWORD=your_mysql_password
./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
```

MYSQL_DB_URL、MYSQL_DB_USERNAME 可覆盖 MySQL 默认连接。启动时 CommandLineRunner 会插入一个名为 abc、length 为 5 的 Video；每次启动都会新增一条。

整理版将原始明文密码改为环境变量，将默认 create-drop 改为 update。DDL_AUTO=create-drop 可用于明确允许重建并删除表的临时练习数据库。update 不保证能迁移旧继承结构，切换策略时请使用新的练习数据库。

## 验证

```bash
./mvnw clean verify
```

测试使用独立 H2 内存数据库，验证应用上下文、单表继承的类型恢复与鉴别列、一对一关联，以及 MappedSuperclass 字段持久化。测试不连接本地 PostgreSQL/MySQL，因此不能替代这两种数据库的运行验证。

源码来自本次 spring-data-jpa.zip，独立保存为 lesson8；整理时修正了 Lecture 与 Resource 同时声明两个外键的问题。原始压缩包和原项目仍保留在本地。
