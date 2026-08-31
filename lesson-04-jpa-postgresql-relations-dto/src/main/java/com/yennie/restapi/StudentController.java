package com.yennie.restapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/** 学生 API：Controller 只接收/返回 DTO，Repository 负责访问数据库。 */
@RestController
public class StudentController {
   private final StudentRepository repository;
   private final SchoolRepository schoolRepository;

   public StudentController(StudentRepository repository, SchoolRepository schoolRepository) {
      this.repository = repository;
      this.schoolRepository = schoolRepository;
   }

    @PostMapping("/students")
    public StudentResponseDto post(@RequestBody StudentDto dto) {
       var school = schoolRepository.findById(dto.schoolId())
              .orElseThrow(() -> new ResponseStatusException(
                      HttpStatus.NOT_FOUND,
                      "School not found: " + dto.schoolId()
              ));
       var student = toStudent(dto, school);
       var savedStudent = repository.save(student);
       return toStudentResponseDto(savedStudent);
    }

   private Student toStudent(StudentDto studentDto, School school) {
       var student = new Student();
       student.setFirstName(studentDto.firstName());
       student.setLastName(studentDto.lastName());
       student.setEmail(studentDto.email());
       student.setAge(studentDto.age());
       student.setSchool(school);
       return student;
   }

   private StudentResponseDto toStudentResponseDto(Student student) {
      return new StudentResponseDto(
              student.getId(),
              student.getFirstName(),
              student.getLastName(),
              student.getEmail(),
              student.getAge(),
              student.getSchool().getId(),
              student.getSchool().getName());
   }
    @GetMapping("/students")
   public List<StudentResponseDto> findAllStudent() {
       return repository.findAll().stream().map(this::toStudentResponseDto).toList();
    }


    @GetMapping("/students/{student-id}")
   public StudentResponseDto findStudentById(@PathVariable("student-id") Integer studentId) {
       return repository.findById(studentId)
               .map(this::toStudentResponseDto)
               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/students/search/{student-name}")
   public List<StudentResponseDto> findStudentByName(@PathVariable("student-name") String name) {
       return repository.findAllByFirstNameContainingIgnoreCase(name)
               .stream().map(this::toStudentResponseDto).toList();
    }

    @DeleteMapping("/students/{student-id}")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   public void deleteStudentById(@PathVariable("student-id") Integer studentId) {
       repository.deleteById(studentId);
    }


}
