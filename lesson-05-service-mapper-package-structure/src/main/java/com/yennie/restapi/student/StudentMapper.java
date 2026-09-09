package com.yennie.restapi.student;

import com.yennie.restapi.school.School;
import org.springframework.stereotype.Component;

/** School 由 Service 查询后传入，Mapper 本身不依赖 Repository。 */
@Component
public class StudentMapper {
    public Student toStudent(StudentDto studentDto, School school) {
        var student = new Student();
        student.setFirstName(studentDto.firstName());
        student.setLastName(studentDto.lastName());
        student.setEmail(studentDto.email());
        student.setAge(studentDto.age());
        student.setSchool(school);
        return student;
    }

    public StudentResponseDto toStudentResponseDto(Student student) {
        return new StudentResponseDto(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getAge(),
                student.getSchool() == null ? null : student.getSchool().getId(),
                student.getSchool() == null ? null : student.getSchool().getName());
    }
}
