package com.yennie.springdatajpa;

import com.github.javafaker.Faker;
import com.yennie.springdatajpa.models.Author;
import com.yennie.springdatajpa.repositories.AuthorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringDataJpaApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringDataJpaApplication.class, args);
    }

    // 默认关闭；仅在练习数据库上显式开启。每次执行都会再插入 50 条。
    @Bean
    @ConditionalOnProperty(name = "lesson.demo.enabled", havingValue = "true")
    public CommandLineRunner commandLineRunner(AuthorRepository authorRepository) {
        return args -> {
            Faker faker = new Faker();
            for (int i = 0; i < 50; i++) {
                var author = Author.builder()
                        .firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .age(faker.number().numberBetween(20, 80))
                        .email(faker.name().username() + "@gmail.com")
                        .build();
                authorRepository.save(author);
            }
            // JPQL 更新不是派生查询。不存在 ID=1 时，第一条更新不影响任何行。
            authorRepository.updateAuthor(22, 1);
            // 注意：没有 WHERE，会覆盖所有作者的年龄，包括之前的数据！
            authorRepository.updateAllAuthorsAges(99);
        };
    }
}
