package com.yennie.springdatajpa;

import com.yennie.springdatajpa.models.Author;
import com.yennie.springdatajpa.repositories.AuthorRepository;
import com.yennie.springdatajpa.specification.AuthorSpecification;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.domain.Specification;

@SpringBootApplication
public class SpringDataJpaApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringDataJpaApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(AuthorRepository authorRepository) {
        return args -> {
            // 命名查询示例：只查询恰好 40 岁的作者。
            // authorRepository.findByNamedQuery(40).forEach(System.out::println);

            // 危险演示，保持关闭：没有 WHERE，会把全部作者的年龄改成 40。
            // authorRepository.updateByNamedQuery(40);

            // 保留课程原有分组：(年龄=40 AND 名字包含 Mi) OR 名字包含 en。
            // 所以名字包含 en 的作者不受年龄限制！
            Specification<Author> specification = Specification.where(AuthorSpecification.hasAge(40))
                    .and(AuthorSpecification.firstNameContains("Mi"))
                    .or(AuthorSpecification.firstNameContains("en"));
            authorRepository.findAll(specification).forEach(System.out::println);

            // 若所有结果都必须是 40 岁，要把两个名字条件先组合：
            // Specification<Author> ageAndNames = AuthorSpecification.hasAge(40).and(
            //         AuthorSpecification.firstNameContains("Mi")
            //                 .or(AuthorSpecification.firstNameContains("en")));
        };
    }
}
