package com.yennie.restapi.student;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Controller 只处理 HTTP，业务流程委托 StudentService。 */
@RestController
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @PostMapping("/students")
    public StudentResponseDto saveStudent(@RequestBody StudentDto dto) {
        return studentService.saveStudent(dto);
    }


    @GetMapping("/students")
    public List<StudentResponseDto> findAllStudent() {
        return studentService.findAllStudent();
    }


    @GetMapping("/students/{student-id}")
    public StudentResponseDto findStudentById(@PathVariable("student-id") Integer studentId) {
        return studentService.findStudentById(studentId);
    }

    @GetMapping("/students/search/{student-name}")
    public List<StudentResponseDto> findStudentByName(@PathVariable("student-name") String name) {
        return studentService.findStudentByName(name);
    }

    @DeleteMapping("/students/{student-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudentById(@PathVariable("student-id") Integer studentId) {
        studentService.deleteStudentById(studentId);
    }


}
