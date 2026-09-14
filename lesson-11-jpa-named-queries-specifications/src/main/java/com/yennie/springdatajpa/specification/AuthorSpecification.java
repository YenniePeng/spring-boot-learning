package com.yennie.springdatajpa.specification;

import com.yennie.springdatajpa.models.Author;
import org.springframework.data.jpa.domain.Specification;

/** 把可复用条件封装为 Specification，再通过 and/or 组合。 */
public class AuthorSpecification {
    public static Specification<Author> hasAge(int age) {
        // root 表示 Author；query 是当前查询；builder 用来构造比较条件。
        return (root, query, builder) -> {
            // 沿用课程约定：负数表示不添加年龄限制，并不是查负年龄。
            if (age < 0) return null;
            // get 使用 Java 属性名，equal 表示等于，不是大于等于。
            return builder.equal(root.get("age"), age);
        };
    }

    public static Specification<Author> firstNameContains(String firstName) {
        return (root, query, builder) -> {
            // null 表示不贡献此条件；组合时它不会作为一个 false 条件。
            if (firstName == null) return null;
            // SQL LIKE：% 匹配任意长度，_ 匹配一个字符。
            // 此处不忽略大小写、不转义输入中的通配符；空字符串相当于 LIKE '%%'。
            return builder.like(root.get("firstName"), "%" + firstName + "%");
        };
    }
}
