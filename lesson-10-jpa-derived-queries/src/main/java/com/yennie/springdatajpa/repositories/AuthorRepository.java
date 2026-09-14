package com.yennie.springdatajpa.repositories;

import com.yennie.springdatajpa.models.Author;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
    // JPQL 使用实体名 Author、Java 属性 age/id，不是数据库的表名/列名。
    // 批量更新绕过已加载实体的状态；后续读取需 clear/refresh，避免旧值。
    @Modifying
    @Transactional
    @Query("update Author a set a.age = :age where a.id = :id")
    void updateAuthor(@Param("age") int age, @Param("id") int id);

    // 没有 WHERE：更新所有作者，不要在需要保留数据的库中随意执行。
    @Modifying
    @Transactional
    @Query("update Author a set a.age = :age")
    void updateAllAuthorsAges(@Param("age") int age);

    // 以下是派生查询，Spring Data 按方法名解析实体的 firstName 属性。
    // 精确匹配；大小写行为还取决于数据库/列的排序规则。
    List<Author> findAllByFirstName(String fn);

    // 忽略大小写的精确匹配。
    List<Author> findAllByFirstNameIgnoreCase(String fn);

    // 包含匹配，传 "yen" 即可，不需要自己添加 %。
    List<Author> findAllByFirstNameContainingIgnoreCase(String fn);

    // 忽略大小写的前缀匹配，如 "yen" 匹配 "Yennie"。
    List<Author> findAllByFirstNameStartsWithIgnoreCase(String fn);

    // 忽略大小写的后缀匹配，如 "nie" 匹配 "Yennie"。
    List<Author> findAllByFirstNameEndsWithIgnoreCase(String fn);

    // 名字等于集合中的任意一项，忽略大小写，不是包含这些片段。
    List<Author> findAllByFirstNameInIgnoreCase(List<String> firstNames);
}
