package com.yennie.springdatajpa;

import com.yennie.springdatajpa.models.Author;
import com.yennie.springdatajpa.repositories.AuthorRepository;
import com.yennie.springdatajpa.specification.AuthorSpecification;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AdvancedQueryTests {
    @Autowired AuthorRepository repository;
    @Autowired EntityManager entityManager;

    @BeforeEach
    void seed() {
        repository.save(Author.builder().firstName("Mike").age(40).build());
        repository.save(Author.builder().firstName("Ben").age(25).build());
        repository.save(Author.builder().firstName("Jenny").age(40).build());
        repository.save(Author.builder().firstName("Bob").age(60).build());
        repository.flush();
        entityManager.clear();
    }

    @Test
    void namedQueryUsesExactAge() {
        assertThat(repository.findByNamedQuery(40)).extracting(Author::getFirstName)
                .containsExactlyInAnyOrder("Mike", "Jenny");
    }

    @Test
    void originalGroupingAllowsOtherAges() {
        var spec = AuthorSpecification.hasAge(40)
                .and(AuthorSpecification.firstNameContains("Mi"))
                .or(AuthorSpecification.firstNameContains("en"));
        assertThat(repository.findAll(spec)).extracting(Author::getFirstName)
                .containsExactlyInAnyOrder("Mike", "Jenny", "Ben");
    }

    @Test
    void nestedGroupingRequiresAgeForBothNames() {
        var spec = AuthorSpecification.hasAge(40).and(
                AuthorSpecification.firstNameContains("Mi")
                        .or(AuthorSpecification.firstNameContains("en")));
        assertThat(repository.findAll(spec)).extracting(Author::getFirstName)
                .containsExactlyInAnyOrder("Mike", "Jenny");
    }

    @Test
    void absentConditionsDoNotRestrictResults() {
        assertThat(repository.findAll(AuthorSpecification.hasAge(-1)
                .and(AuthorSpecification.firstNameContains(null)))).hasSize(4);
    }

    @Test
    void namedUpdateAffectsAllRows() {
        repository.updateByNamedQuery(42);
        entityManager.clear();
        assertThat(repository.findAll()).hasSize(4).allMatch(a -> a.getAge() == 42);
    }

    @Test
    void jpqlUpdateIsMarkedModifying() {
        repository.updateAllAuthorsAges(50);
        entityManager.clear();
        assertThat(repository.findAll()).hasSize(4).allMatch(a -> a.getAge() == 50);
    }
}
