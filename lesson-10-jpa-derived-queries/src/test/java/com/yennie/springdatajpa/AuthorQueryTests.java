package com.yennie.springdatajpa;

import com.yennie.springdatajpa.models.Author;
import com.yennie.springdatajpa.repositories.AuthorRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AuthorQueryTests {
    @Autowired AuthorRepository repository;
    @Autowired EntityManager entityManager;
    private Integer yennieId;

    @BeforeEach
    void prepare() {
        // 固定数据而不是随机名字，保证测试结果可重复；每个测试完成后回滚。
        for (String name : List.of("Yennie", "YENNIE", "Jenny", "LittleYennie", "Yen", "Bob")) {
            var author = Author.builder().firstName(name).lastName("Test")
                    .email(name + "@example.com").age(30).build();
            repository.save(author);
            if (name.equals("Yennie")) yennieId = author.getId();
        }
        repository.flush();
        entityManager.clear();
    }

    @Test
    void exactName() {
        assertThat(repository.findAllByFirstName("Yennie")).extracting(Author::getFirstName)
                .containsExactly("Yennie");
        assertThat(repository.findAllByFirstName("Missing")).isEmpty();
    }

    @Test
    void exactNameIgnoringCase() {
        assertThat(repository.findAllByFirstNameIgnoreCase("yennie")).hasSize(2);
    }

    @Test
    void containingIgnoringCase() {
        assertThat(repository.findAllByFirstNameContainingIgnoreCase("YEN"))
                .extracting(Author::getFirstName)
                .containsExactlyInAnyOrder("Yennie", "YENNIE", "LittleYennie", "Yen");
    }

    @Test
    void prefixIgnoringCase() {
        assertThat(repository.findAllByFirstNameStartsWithIgnoreCase("yen"))
                .extracting(Author::getFirstName)
                .containsExactlyInAnyOrder("Yennie", "YENNIE", "Yen");
    }

    @Test
    void suffixIgnoringCase() {
        assertThat(repository.findAllByFirstNameEndsWithIgnoreCase("NIE"))
                .extracting(Author::getFirstName)
                .containsExactlyInAnyOrder("Yennie", "YENNIE", "LittleYennie");
    }

    @Test
    void namesInCollectionIgnoringCase() {
        assertThat(repository.findAllByFirstNameInIgnoreCase(List.of("yennie", "BOB")))
                .extracting(Author::getFirstName)
                .containsExactlyInAnyOrder("Yennie", "YENNIE", "Bob");
    }

    @Test
    void updateOnlySelectedAuthor() {
        repository.updateAuthor(22, yennieId);
        entityManager.clear();
        assertThat(repository.findById(yennieId).orElseThrow().getAge()).isEqualTo(22);
        assertThat(repository.findAllByFirstName("Bob").getFirst().getAge()).isEqualTo(30);
    }

    @Test
    void updateAllAuthors() {
        repository.updateAllAuthorsAges(99);
        entityManager.clear();
        assertThat(repository.findAll()).hasSize(6).allMatch(author -> author.getAge() == 99);
    }
}
