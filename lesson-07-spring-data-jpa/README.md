# Lesson 07：深入 Spring Data JPA

本课使用 `Author`、`Course`、`Section`、`Lecture` 和 `Resource` 学习实体映射与 Spring Data JPA Repository。

## 主要内容

- `@Entity`、`@Id` 与 `@GeneratedValue`
- `JpaRepository<Author, Integer>` 提供的 CRUD 能力
- `Author` 与 `Course` 的多对多关系及 `@JoinTable`
- `Course`、`Section`、`Lecture` 的一对多/多对一关系
- `Lecture` 与 `Resource` 的一对一关系
- PostgreSQL 为默认数据库，`mysql` Profile 可切换到 MySQL
- 测试使用内存 H2，不依赖本地 Docker 数据库

## 关系结构

```text
Author   * ───── * Course
                    │ 1
                    │
                    * Section
                        │ 1
                        │
                        * Lecture 1 ───── 1 Resource
```

关系中保存外键的一侧是 owning side：`Course` 维护多对多中间表，`Section` 保存 `course_id`，`Lecture` 保存 `section_id`，`Resource` 保存 `lecture_id`。

## 运行 PostgreSQL 版本

确保 PostgreSQL 中已经存在 `data_jpa` 数据库，然后设置密码：

```bash
export DB_PASSWORD=your_postgres_password
./mvnw spring-boot:run
```

默认连接为 `jdbc:postgresql://localhost:5432/data_jpa`，用户名为 `yennie`。也可以用 `DB_URL` 和 `DB_USERNAME` 覆盖。

## 运行 MySQL 版本

```bash
export MYSQL_DB_PASSWORD=your_mysql_password
./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
```

## 测试

```bash
./mvnw test
```

测试会使用 H2 临时数据库，结束后自动清理。
