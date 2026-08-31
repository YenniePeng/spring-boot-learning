package com.yennie.restapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class StudentRepositoryTests {

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void savesRelationshipAndSearchesNameIgnoringCase() {
        var school = schoolRepository.save(new School("Spring School"));

        var student = new Student(20, "Yennie", "yennie@example.com", "Peng");
        student.setSchool(school);
        studentRepository.save(student);

        var results = studentRepository.findAllByFirstNameContainingIgnoreCase("yen");

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getSchool().getId()).isEqualTo(school.getId());
    }
}
