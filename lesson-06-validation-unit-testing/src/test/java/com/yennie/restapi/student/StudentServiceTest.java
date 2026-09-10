package com.yennie.restapi.student;

import com.yennie.restapi.school.School;
import com.yennie.restapi.school.SchoolRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** 真实 Service + Mock 依赖：只测试业务编排，不启动 Spring 或数据库。 */
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    // 创建真实 Service，并通过构造器注入下方声明的三个 Mock。
    @InjectMocks
    private StudentService studentService;

    // Mock 不会自动执行真实 Repository 或 Mapper 的方法。
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private StudentMapper studentMapper;
    @Mock
    private SchoolRepository schoolRepository;


    @Test
    public void should_successfully_save_a_student() {
        //Given
        StudentDto dto = new StudentDto(
                "Yennie",
                "Peng",
                "test123@gmail.com",
                30,
                2
        );
        Student student = new Student(
                30,
                "Yennie",
                "test123@gmail.com",
                "Peng"
        );
        Student savedStudent = new Student(
                30,
                "Yennie",
                "test123@gmail.com",
                "Peng"
        );
        savedStudent.setId(1);
        School school = new School(
                "tianzhong"
        );

        school.setId(dto.schoolId());
        student.setSchool(school);
        savedStudent.setSchool(school);

        // 模拟最终返回给客户端的 DTO
        StudentResponseDto expectedResponse = new StudentResponseDto(
                savedStudent.getId(),
                savedStudent.getFirstName(),
                savedStudent.getLastName(),
                savedStudent.getEmail(),
                savedStudent.getAge(),
                school.getId(),
                school.getName()
        );


        //Mock the calls
        // ① 查学校
        when(schoolRepository.findById(dto.schoolId()))
                .thenReturn(Optional.of(school));

        // ② 请求 DTO 转成实体
        when(studentMapper.toStudent(dto, school))
                .thenReturn(student);

        // ③ 保存实体，返回带 ID 的新对象
        when(studentRepository.save(student))
                .thenReturn(savedStudent);

        // ④ 将保存结果转换成响应 DTO
        when(studentMapper.toStudentResponseDto(savedStudent))
                .thenReturn(expectedResponse);
        //When
        StudentResponseDto responseDto = studentService.saveStudent(dto);

        //Then
        assertEquals(dto.firstName(), responseDto.firstName());
        assertEquals(dto.lastName(), responseDto.lastName());
        assertEquals(dto.email(), responseDto.email());
        assertEquals(dto.age(), responseDto.age());
        assertEquals(dto.schoolId(), responseDto.schoolId());
        assertEquals(savedStudent.getId(), responseDto.id());
        verify(studentMapper, times(1)).toStudent(dto, school);
        verify(studentRepository, times(1)).save(student);
        verify(studentMapper, times(1)).toStudentResponseDto(savedStudent);
    }

    @Test
    public void should_return_all_students() {
        // Given：用不同对象，避免固定 Mock 结果掩盖转换错对象的问题。
        var first = new Student(30, "Yennie", "first@example.com", "Peng");
        var second = new Student(22, "Lucy", "second@example.com", "Chen");
        first.setId(1);
        second.setId(2);
        var firstResponse = new StudentResponseDto(1, "Yennie", "Peng", "first@example.com", 30, null, null);
        var secondResponse = new StudentResponseDto(2, "Lucy", "Chen", "second@example.com", 22, null, null);
        when(studentRepository.findAll()).thenReturn(List.of(first, second));
        when(studentMapper.toStudentResponseDto(first)).thenReturn(firstResponse);
        when(studentMapper.toStudentResponseDto(second)).thenReturn(secondResponse);

        // When
        var responseDtos = studentService.findAllStudent();

        // Then：record 的 equals 会比较每一个字段。
        assertEquals(List.of(firstResponse, secondResponse), responseDtos);
        verify(studentRepository).findAll();
        verify(studentMapper).toStudentResponseDto(first);
        verify(studentMapper).toStudentResponseDto(second);
        verifyNoMoreInteractions(studentRepository, studentMapper);
    }

    @Test
    public void should_return_student_by_id() {
        //Given
        Integer studentId = 1;
        Student student = new Student(
                30,
                "yennie",
                "test123@gmail.com",
                "Peng"
        );
        student.setId(studentId);

        School school = new School("tianzhong");
        school.setId(2);
        student.setSchool(school);
        //Mock the calls
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentMapper.toStudentResponseDto(student)).thenReturn(new StudentResponseDto(
                1,
                "yennie",
                "Peng",
                "test123@gmail.com",
                30,
                2,
                "tianzhong"
        ));
        //when
        StudentResponseDto responseDto = studentService.findStudentById(studentId);
        //Then
        assertEquals(student.getFirstName(), responseDto.firstName());
        assertEquals(student.getLastName(), responseDto.lastName());
        assertEquals(student.getEmail(), responseDto.email());
        assertEquals(student.getAge(), responseDto.age());
        assertEquals(school.getId(), responseDto.schoolId());
        assertEquals(studentId, responseDto.id());
        verify(studentMapper).toStudentResponseDto(student);
        verify(studentRepository, times(1)).findById(studentId);

    }

    @Test
    public void should_return_student_by_name() {
        // Given：Service 应把搜索词原样交给 Repository，再转换每个结果。
        String studentName = "yen";
        var first = new Student(30, "Yennie", "first@example.com", "Peng");
        var second = new Student(25, "Yen", "second@example.com", "Li");
        first.setId(1);
        second.setId(2);
        var firstResponse = new StudentResponseDto(1, "Yennie", "Peng", "first@example.com", 30, null, null);
        var secondResponse = new StudentResponseDto(2, "Yen", "Li", "second@example.com", 25, null, null);
        when(studentRepository.findAllByFirstNameContainingIgnoreCase(studentName))
                .thenReturn(List.of(first, second));
        when(studentMapper.toStudentResponseDto(first)).thenReturn(firstResponse);
        when(studentMapper.toStudentResponseDto(second)).thenReturn(secondResponse);

        // When
        var responseDtos = studentService.findStudentByName(studentName);

        // Then
        assertEquals(List.of(firstResponse, secondResponse), responseDtos);
        verify(studentRepository).findAllByFirstNameContainingIgnoreCase(studentName);
        verify(studentMapper).toStudentResponseDto(first);
        verify(studentMapper).toStudentResponseDto(second);
        verifyNoMoreInteractions(studentRepository, studentMapper);
    }
    @Test
    void should_reject_missing_school_id_without_database_call() {
        var dto = new StudentDto("Yennie", "Peng", "missing@example.com", 30, null);
        var error = assertThrows(ResponseStatusException.class, () -> studentService.saveStudent(dto));
        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
        verifyNoInteractions(schoolRepository, studentRepository, studentMapper);
    }

    @Test
    void should_not_save_when_school_does_not_exist() {
        var dto = new StudentDto("Yennie", "Peng", "unknown@example.com", 30, 99);
        when(schoolRepository.findById(99)).thenReturn(Optional.empty());
        var error = assertThrows(ResponseStatusException.class, () -> studentService.saveStudent(dto));
        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        verifyNoInteractions(studentRepository, studentMapper);
    }

    @Test
    void should_return_404_when_student_does_not_exist() {
        when(studentRepository.findById(99)).thenReturn(Optional.empty());
        var error = assertThrows(ResponseStatusException.class, () -> studentService.findStudentById(99));
        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        verifyNoInteractions(studentMapper);
    }

    @Test
    void should_delete_student_by_id() {
        studentService.deleteStudentById(1);
        verify(studentRepository).deleteById(1);
        verifyNoInteractions(studentMapper, schoolRepository);
    }
}
