package com.yennie.springdatajpa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import com.yennie.springdatajpa.models.*;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpringDataJpaApplicationTests {
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void contextLoads() {
    }

    @Test
    @Transactional
    void singleTableStoresTypesAndRestoresLectureRelationship() {
        var lecture = Lecture.builder().name("Inheritance").build();
        entityManager.persist(lecture);
        var video = Video.builder().name("Video").length(60).lecture(lecture).build();
        var text = Text.builder().name("Notes").content("JPA").build();
        var file = com.yennie.springdatajpa.models.File.builder().name("Slides").type("PDF").build();
        entityManager.persist(video);
        entityManager.persist(text);
        entityManager.persist(file);
        entityManager.flush();
        entityManager.clear();

        var loaded = entityManager.find(Resource.class, video.getId());
        assertInstanceOf(Video.class, loaded);
        assertEquals(60, ((Video) loaded).getLength());
        assertInstanceOf(Text.class, entityManager.find(Resource.class, text.getId()));
        assertInstanceOf(com.yennie.springdatajpa.models.File.class,
                entityManager.find(Resource.class, file.getId()));
        assertEquals("V", entityManager.createNativeQuery(
                "select resource_type from resource where id = :id")
                .setParameter("id", video.getId()).getSingleResult());
        assertEquals(video.getId(), entityManager.find(Lecture.class, lecture.getId()).getResource().getId());
    }

    @Test
    @Transactional
    void mappedSuperclassFieldsPersistInAuthorTable() {
        var time = LocalDateTime.of(2026, 9, 13, 12, 0);
        var author = Author.builder().firstName("Yennie").createdAt(time).createdBy("lesson8").build();
        entityManager.persist(author);
        entityManager.flush();
        entityManager.clear();
        var loaded = entityManager.find(Author.class, author.getId());
        assertEquals(time, loaded.getCreatedAt());
        assertEquals("lesson8", loaded.getCreatedBy());
        assertEquals("lesson8", entityManager.createNativeQuery(
                "select created_by from author where id = :id")
                .setParameter("id", author.getId()).getSingleResult());
    }

}
