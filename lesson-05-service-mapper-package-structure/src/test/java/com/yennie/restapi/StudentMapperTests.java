package com.yennie.restapi;

import com.yennie.restapi.school.School;
import com.yennie.restapi.student.Student;
import com.yennie.restapi.student.StudentDto;
import com.yennie.restapi.student.StudentMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StudentMapperTests {
    private final StudentMapper mapper = new StudentMapper();

    @Test
    void mapsRequestAndResponseWithoutLosingFields() {
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
        var response = mapper.toStudentResponseDto(new Student());
        assertThat(response.schoolId()).isNull();
        assertThat(response.schoolName()).isNull();
    }
}
