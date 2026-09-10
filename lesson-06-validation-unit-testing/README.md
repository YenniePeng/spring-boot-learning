# Lesson 06：参数校验、异常处理与单元测试

在 lesson-5 的 Service / Mapper 分层代码上继续学习：

- Bean Validation：@Valid、@NotEmpty、@Email。
- @ExceptionHandler：将字段校验错误转换成可读的 HTTP 400 JSON。
- JUnit：@Test、断言、测试生命周期、Given / When / Then。
- Mockito：@Mock、@InjectMocks、when / thenReturn、verify。

本课是独立 Maven / Spring Boot 项目，拥有自己的 pom.xml、Maven Wrapper、源码和测试，
不依赖其他 lesson 的项目文件。要求 JDK 26，沿用 Spring Boot 4.1.1。

## 从哪里开始阅读

1. StudentDto：声明客户端数据必须满足的规则。
2. StudentController：@Valid 触发请求体校验，异常处理方法返回字段错误。
3. StudentMapperTests：不使用 Mock，直接测试真实 Mapper 的字段转换。
4. StudentServiceTest：真实 Service 配合三个 Mock，测试业务流程。
5. StudentApiTests：启动随机端口的服务器，用真实 HTTP 请求检查校验和接口。

源码在 src/main/java/com/yennie/restapi；测试在 src/test/java/com/yennie/restapi/student。
school、student、studentprofile 仍按业务模块分包；layer.txt 保留之前的目录结构笔记。

## 参数校验

StudentDto 的 firstName 和 lastName 使用 @NotEmpty；email 同时使用 @NotEmpty 和 @Email。

- @NotEmpty：不能为 null 或空字符串，但允许纯空格；想禁止纯空格时可使用 @NotBlank。
- @Email：检查邮箱格式，不应该单独用于“必填”检查。
- @Valid：在本课 Controller 接收请求体时触发校验。

直接 new StudentDto(...) 或在 Mockito 测试中直接调用 Service，不会自动触发 Controller 的 @Valid。
因此必须用 HTTP 测试或显式 Validator 测试，才能证明输入校验有效。

本课修正了原练习中 @Email 的提示，并补充邮箱必填检查。年龄、姓名长度等更多业务规则仍可作为后续练习。

## 错误响应

POST /students 发送：

```json
{
  "firstName": "",
  "lastName": "",
  "email": "not-an-email",
  "age": 20,
  "schoolId": 1
}
```

返回 HTTP 400，响应内容为（JSON 字段顺序不重要）：

```json
{
  "firstName": "Firstname should not be empty",
  "lastName": "Lastname should not be empty",
  "email": "Email should be valid"
}
```

处理器使用 getFieldErrors() 获取字段错误，不把所有 ObjectError 强制转换为 FieldError。
当前每个字段只保留一条提示；该处理器属于 StudentController，并不是全局异常处理器。
它只处理字段校验异常，不负责所有 JSON 格式错误或业务异常。
学校不存在返回 404，缺少 schoolId 返回 400，这两种检查仍由 Service 负责。

## Mockito：测试 Service，不测试替身本身

StudentServiceTest 中：

- @InjectMocks 标注真实的 StudentService。
- @Mock 分别标注 StudentRepository、SchoolRepository、StudentMapper。
- @ExtendWith(MockitoExtension.class) 管理 Mock 生命周期，替代手动 openMocks()。
- when(...).thenReturn(...) 预先设置依赖方法的结果。
- assertEquals(expected, actual) 检查结果，verify(...) 检查重要调用。
- verifyNoInteractions(...) 检查错误情况下不应发生的操作。

保存场景需要区分待保存实体和保存结果：

```java
when(studentRepository.save(student)).thenReturn(savedStudent);
when(studentMapper.toStudentResponseDto(savedStudent)).thenReturn(expectedResponse);
```

savedStudent 包含模拟数据库生成的 ID。这样能验证 Service 把保存结果交给 Mapper，
而不是让一个固定 DTO 掩盖“转换了错误对象”的问题。
这些设置不会真的插入数据库，也不会执行 Mock Mapper 的真实方法。

列表与搜索测试使用不同对象和不同 DTO，检查完整内容，而不只检查返回数量。

## 测试的范围

| 测试类 | 真实执行的部分 | 数据库 |
| --- | --- | --- |
| StudentMapperTests | DTO / Entity 转换 | 无 |
| StudentServiceTest | Service 业务编排，其他依赖使用 Mock | 无 |
| StudentRepositoryTests | JPA 保存、关联和查询 | 独立 H2 内存库 |
| StudentApiTests | HTTP、校验、Controller、Service、Mapper、Repository | 独立 H2 内存库 |

错误场景覆盖空姓名、缺少邮箱、非法邮箱、缺少学校 ID、学校/学生不存在；
HTTP 校验失败时还检查数据库没有新增学生。
H2 PostgreSQL 兼容模式不等于 PostgreSQL，不能取代真正的 PostgreSQL 集成验证。

在本课目录运行所有测试：

```bash
./mvnw test
```

只运行你本次学习的 Service 测试：

```bash
./mvnw -Dtest=StudentServiceTest test
```

IntelliJ 中也可以点测试方法旁的绿色三角。测试正常启动、运行完毕且断言通过才算成功；
不要把 WARNING 当作断言失败。
如果 JDK 显示 Mockito 动态加载 Agent 的警告，那是测试工具兼容性提示，不等于业务测试失败；
未来升级 JDK 时可能需要按 Mockito 文档显式配置测试 JVM 的 -javaagent。

## JUnit 生命周期笔记

默认情况下，每个测试方法有自己的测试类实例：

- @BeforeAll：该测试类全部测试前执行一次，默认需为 static。
- @BeforeEach：每个测试前执行。
- @AfterEach：每个测试后执行。
- @AfterAll：该测试类全部测试后执行一次，默认需为 static。

StudentMapperTests 保留了你注释掉的生命周期打印示例，可按需取消注释观察；
不要依赖测试方法的默认执行顺序共享数据。

## 启动应用与 Postman

在 IntelliJ 中单独打开本课文件夹并导入 Maven。启动现有 PostgreSQL 后，在运行配置中设置 DB_PASSWORD。
或者在本课目录运行：

```bash
export DB_PASSWORD='替换为实际数据库密码'
./mvnw spring-boot:run
```

默认 DB_URL 为 jdbc:postgresql://localhost:5432/springboot_db，DB_USERNAME 为 yennie，
都可用同名环境变量覆盖。真实密码、.env、IDE 配置不提交到 Git。
IntelliJ 数据库工具的密码不会自动提供给 Spring Boot。

本课默认与 lesson-4、lesson-5 共用本地数据库，写入/删除会影响同一份数据；
需要隔离时先建立独立数据库，再修改 DB_URL。不要同时启动多个使用默认 8080 端口的应用。
ddl-auto: update 不会每次启动重建表，但仍可能修改表结构。
自动化测试使用独立 H2，不连接这份 PostgreSQL。

api-test.http 包含成功和校验失败请求，可以直接复制到 Postman：
先 POST /schools，取得真实学校 ID 后再 POST /students。
email 有唯一约束，重复创建请更换测试邮箱或只删除自己创建的测试数据。
