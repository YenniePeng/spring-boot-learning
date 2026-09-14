# Lesson 11：命名查询与 Specification（JPA 最后一章）

独立 Maven 项目，使用 JDK 26。保留上传代码的实体关系、继承和 Embedded 映射。

## Specification 是什么？

它表示一个可以复用、组合的查询条件。适合筛选页面：年龄、姓名等条件由用户选择，
不必为每一种组合声明一个新 Repository 方法。

1. AuthorSpecification.hasAge(40) 构造“年龄等于 40”。
2. firstNameContains("Mi") 构造“名字包含 Mi”。
3. and/or 组合条件。
4. repository.findAll(specification) 才执行数据库查询。

Repository 需要继承 JpaSpecificationExecutor<Author>。
lambda 的 root 代表 Author 实体，query 代表当前查询，builder 用于创建 equal、like 等条件。
root.get("firstName") 指 Java 属性，而不是数据库列 first_name。

## and/or 的括号决定结果

课程原表达式 A.and(B).or(C) 表示 (A AND B) OR C：

```java
var spec = AuthorSpecification.hasAge(40)
        .and(AuthorSpecification.firstNameContains("Mi"))
        .or(AuthorSpecification.firstNameContains("en"));
```

Mike（40 岁）、Jenny（40 岁）、Ben（25 岁）都可以匹配。
如果要求所有作者都是 40 岁，则使用 A.and(B.or(C))：

```java
var spec = AuthorSpecification.hasAge(40).and(
        AuthorSpecification.firstNameContains("Mi")
                .or(AuthorSpecification.firstNameContains("en")));
```

此时 Ben（25 岁）不会匹配。整理版保留原分组，并在源码展示另一种写法。

本例负年龄或 null 名字返回 null Predicate，表示不贡献该条件，不是 false。
LIKE 未做忽略大小写处理，大小写行为受数据库排序规则影响。
输入中的 % 和 _ 仍具有通配符意义，空字符串变成 LIKE '%%'；
正式筛选功能应根据需求补充空白处理、通配符转义与参数校验。

## 命名查询

Author 上的 @NamedQueries 预先定义 JPQL：

- Author.findByNamedQuery：age = :age，只查恰好这个年龄。
- Author.updateByNamedQuery：没有 WHERE，更新所有作者年龄，默认示例不调用。

Repository 方法按“实体名.方法名”找到定义；@Param 绑定参数。
更新操作需要 @Modifying 和事务。整理时恢复了 updateAllAuthorsAges 遗漏的 @Modifying。
JPQL 批量更新后，已加载对象可能仍保留旧值，需要 clear/refresh 后读取。
Author 关联集合排除在 toString/equals/hashCode 之外，避免打印触发懒加载及递归。

## 运行

在 IntelliJ 中打开本目录，环境变量设置 DB_PASSWORD。
默认 PostgreSQL localhost:5432/data_jpa，可通过 DB_URL、DB_USERNAME 覆盖。
MySQL 激活 mysql Profile，并设置 MYSQL_DB_PASSWORD，可选 MYSQL_DB_URL、MYSQL_DB_USERNAME。
密码不提交 Git，DDL_AUTO 默认 update；建议使用独立练习数据库。

```bash
./mvnw spring-boot:run
./mvnw clean verify
```

启动仅执行课程的 Specification 查询，不插入数据、不执行全表更新。
已删除原来 save 被注释的无效 Faker 循环；没有匹配数据时不打印作者是正常的。
测试使用独立 H2 内存数据库，不连接用户的 PostgreSQL/MySQL。
覆盖命名查询、两种分组、可选条件及批量更新，真实数据库方言与性能需另外验证。
