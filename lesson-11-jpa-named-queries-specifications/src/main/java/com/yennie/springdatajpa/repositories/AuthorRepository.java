package com.yennie.springdatajpa.repositories;

import com.yennie.springdatajpa.models.Author;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// JpaSpecificationExecutor 提供 findAll(Specification) 等动态查询入口。
public interface AuthorRepository extends JpaRepository<Author, Integer>, JpaSpecificationExecutor<Author> {

    // 对应 Author 上的 Author.findByNamedQuery，不是按方法名推导条件。
    @Transactional
    List<Author> findByNamedQuery(@Param("age") int age);

    @Modifying
    @Transactional
    void updateByNamedQuery(@Param("age") int age);

    //update author a set a.age = 22 where a.id = 1
    @Modifying
    @Transactional
    @Query("update Author a set a.age = :age where a.id = :id")
            void updateAuthor(@Param("age") int age, @Param("id") int id);

    // JPQL UPDATE 必须标记为修改操作；事务负责提交。
    @Modifying
    @Transactional
    @Query("update Author a set a.age = :age")
   void updateAllAuthorsAges(@Param("age") int age);

    // select * from author where first_name = 'Yennie'
    List<Author> findAllByFirstName(String fn);

    // select * from author where first_name = 'yennie'
    List<Author> findAllByFirstNameIgnoreCase(String fn);

    // 包含匹配：传 yen，无需手动添加 %。
    List<Author> findAllByFirstNameContainingIgnoreCase(String fn);

    // 前缀匹配，忽略大小写。
    List<Author> findAllByFirstNameStartsWithIgnoreCase(String fn);

    // 后缀匹配，忽略大小写。
    List<Author> findAllByFirstNameEndsWithIgnoreCase(String fn);

    // select * from author where first_name in ('yen','nie')
    List<Author> findAllByFirstNameInIgnoreCase(List<String> firstNames);
}
