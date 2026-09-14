# Lesson 10：Spring Data JPA 派生查询

本课基于上传的课程代码，保留之前的实体关系、TABLE_PER_CLASS 继承和 Embedded 示例，
重点学习 AuthorRepository 的方法命名查询，以及与自定义 JPQL 更新的区别。
这是独立 Maven 项目，不依赖其他 Lesson。

## 1. 方法名就是查询条件

`findAllByFirstNameContainingIgnoreCase` 可以拆成：

- `findAllBy`：查找符合条件的记录。
- `FirstName`：Author 的 Java 属性 firstName，不是数据库列名。
- `Containing`：包含指定字符串。
- `IgnoreCase`：忽略大小写。

只需声明接口方法，由 Spring Data 创建实现，不需要自己编写这些查询的 SQL。
参数名可以叫 fn，不影响解析；真正重要的是方法名中的属性名。

| 方法后缀（省略 findAllBy） | 含义 | 参数示例 |
| --- | --- | --- |
| FirstName | 精确相等；大小写行为取决于数据库排序规则 | "Yennie" |
| FirstNameIgnoreCase | 忽略大小写的精确相等 | "yennie" |
| FirstNameContainingIgnoreCase | 忽略大小写，包含片段 | "yen" |
| FirstNameStartsWithIgnoreCase | 忽略大小写，以片段开头 | "yen" |
| FirstNameEndsWithIgnoreCase | 忽略大小写，以片段结尾 | "nie" |
| FirstNameInIgnoreCase | 忽略大小写，等于集合内任一名字 | List.of("yennie", "bob") |

Containing/StartsWith/EndsWith 的参数不需要自行添加 %。
返回 List，没有匹配时为空列表；没有声明排序时不要依赖返回顺序。

## 2. JPQL 更新不是派生查询

`updateAuthor(age, id)` 和 `updateAllAuthorsAges(age)` 使用了：

- `@Query`：显式写出 JPQL，Author 是实体名，age/id 是实体属性。
- `@Param`：把参数绑定到 :age、:id。
- `@Modifying`：表示执行更新，而非查询结果集。
- `@Transactional`：提供更新所需的事务。

第一种有 WHERE，只更新指定 ID；第二种没有 WHERE，更新全部作者。
批量 JPQL 更新不会同步已经加载到持久化上下文中的对象；需要 clear/refresh 后重新查询。
测试中使用 clear 验证数据库中的实际结果。

## 3. 运行与数据安全

使用 JDK 26，IntelliJ 直接打开本目录。默认连接 PostgreSQL 的 data_jpa。
启动前通过运行配置的环境变量设置 DB_PASSWORD；可选 DB_USERNAME、DB_URL。
MySQL 使用 mysql Profile，并设置 MYSQL_DB_PASSWORD，可选 MYSQL_DB_USERNAME、MYSQL_DB_URL。
密码不写进 Git，默认 ddl-auto 为 update，不会每次启动删除重建表。
update 不等于数据备份，也不保证能自动迁移以前课程所有结构变化，建议用独立练习库。

```bash
./mvnw spring-boot:run
```

原始课程启动时会插入 50 条随机作者，再把所有作者年龄改为 99。
整理版默认关闭这段演示；确认连接的是可修改的练习库后，才手动开启：

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--lesson.demo.enabled=true"
```

每次开启都会追加 50 条，不会自动去重；年龄更新也会覆盖之前的作者。
JavaFaker 只是生成内存中的假数据，真正入库靠 repository.save。
在 DBeaver 的 SQL 编辑器中用 SELECT * FROM public.author 查看，而不是在表格过滤栏输入完整 SQL。

## 4. 自动测试

```bash
./mvnw clean verify
```

测试专用配置使用 H2 内存数据库、关闭随机演示，不连接本机 PostgreSQL/MySQL。
AuthorQueryTests 用固定数据覆盖六种派生查询及两种 JPQL 更新；另保留启动上下文测试。
H2 验证不能替代真实数据库的排序规则、方言和性能验证。
此前课程的关联/继承映射保持上传版本，本课不进行关系模型重构。
