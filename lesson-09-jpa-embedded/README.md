# Lesson 09：Embedded 值对象与复合主键

独立 Spring Boot 4.1.1 / Java 26 Maven 项目，来自本次上传的 spring-data-jpa.zip。

## 重点阅读

- `src/main/java/com/yennie/springdatajpa/models/embedded/Address.java`：@Embeddable 声明可嵌入值对象，没有独立 ID 或表。
- `Order.java`：@Embedded 将地址属性展开到订单表；@EmbeddedId 把另一组属性用作主键。
- `OrderId.java`：username 与 orderDate 共同标识一条订单；提供基于值的 equals/hashCode。
- `src/test/java/com/yennie/springdatajpa/EmbeddedMappingTests.java`：完整的保存、按复合主键查询与修改嵌入属性示例。

三个类都补充了中文注释。Address.ZipCode 规范为 zipCode，保留 getZipCode/setZipCode 用法。Order 中删除了无关的 PrivateKey import。

## Java 属性与表列

| Java 属性 | _order 表列 | 作用 |
| --- | --- | --- |
| orderId.username | username | 复合主键的一部分 |
| orderId.orderDate | order_date | 复合主键的一部分 |
| address.streetName | street_name | 普通地址列 |
| address.houseNumber | house_number | 普通地址列 |
| address.zipCode | zip_code | 普通地址列 |
| orderInfo / anotherField | order_info / another_field | 普通业务列 |

数据库中没有 address_id 外键、独立 address 表或 order_id 对象列。Address 的数据随 Order 保存。复用嵌入类是复用 Java 数据结构，各订单仍保存自己的地址值。

本课暂未新增订单 Repository。若后续使用，主键类型应为 `JpaRepository<Order, OrderId>`，不能写 Integer；`findById` 需要传入两项均已设置的 OrderId。

若同一个实体嵌入两个 Address，可用 @AttributeOverrides / @AttributeOverride 给两组属性指定不同列名。本课代码没有启用该扩展示例。

## 运行与测试

在 IntelliJ 直接打开此目录。测试使用 H2 临时数据库，不需要 Docker：

```bash
./mvnw clean verify
```

运行 PostgreSQL（先准备 data_jpa 数据库）：

```bash
export DB_PASSWORD=your_postgres_password
./mvnw spring-boot:run
```

可用 DB_URL、DB_USERNAME 覆盖 localhost:5432/data_jpa 与 yennie。MySQL：

```bash
export MYSQL_DB_PASSWORD=your_mysql_password
./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
```

可用 MYSQL_DB_URL、MYSQL_DB_USERNAME 覆盖 MySQL 地址及用户。

整理版使用环境变量密码，默认 DDL_AUTO=update。原课程的 create-drop 会重建表并在关闭时删表，仅在可丢弃的练习库中显式启用。继承结构改变时 update 不保证迁移正确，建议使用单独的新练习库。

压缩包中的其他课程示例仍保留：Resource 当前使用 TABLE_PER_CLASS；Lecture 与 Resource 各自声明关联（两个独立拥有方），此课未调整这部分映射。启动时原 CommandLineRunner 仍会新增一个 Video，embedded 的保存示例在测试中。H2 测试不能替代 PostgreSQL/MySQL 实测。
