package com.yennie.restapi.student;

import com.yennie.restapi.school.School;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

class StudentMapperTests {
    private final StudentMapper mapper = new StudentMapper();
//    @BeforeAll
//    static void beforeAll() {
//        System.out.println("Inside the before all test");
//    }
//    @AfterAll
//    static void afterAll() {
//        System.out.println("Inside the after all test");
//    }
//    @BeforeEach
//    void setUp() {
//        System.out.println("Inside the before each test");
//    }
//    @AfterEach
//    void tearDown() {
//        System.out.println("Inside the after each test");
//    }
    @Test
    void mapsRequestAndResponseWithoutLosingFields() {
//        System.out.println("My first test method");
        var school = new School("Spring School");
        school.setId(7);
        var dto = new StudentDto("Yennie", "Peng", "mapper@example.com", 20, 7);
        var student = mapper.toStudent(dto, school);
        student.setId(9);
        var response = mapper.toStudentResponseDto(student);

        assertThat(student.getSchool()).isSameAs(school);
        assertThat(response.id()).isEqualTo(9);
        assertThat(response.firstName()).isEqualTo(dto.firstName());
        assertThat(response.lastName()).isEqualTo(dto.lastName());
        assertThat(response.email()).isEqualTo(dto.email());
        assertThat(response.age()).isEqualTo(dto.age());
        assertThat(response.schoolId()).isEqualTo(7);
        assertThat(response.schoolName()).isEqualTo("Spring School");
    }

    @Test
    void mapsLegacyStudentWithoutSchool() {
//        System.out.println("My Second test method");
        var response = mapper.toStudentResponseDto(new Student());
        assertThat(response.schoolId()).isNull();
        assertThat(response.schoolName()).isNull();
    }
}
