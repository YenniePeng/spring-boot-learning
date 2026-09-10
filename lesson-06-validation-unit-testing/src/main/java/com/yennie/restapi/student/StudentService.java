package com.yennie.restapi.student;

import com.yennie.restapi.school.SchoolRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/** Service 编排查询、业务检查、保存和 DTO 转换；事务覆盖整个操作。 */
@Service
@Transactional(readOnly = true)
public class StudentService {
    private final StudentRepository repository;
    private final StudentMapper studentMapper;
    private final SchoolRepository schoolRepository;

    public StudentService(StudentRepository repository, SchoolRepository schoolRepository, StudentMapper studentMapper) {
        this.repository = repository;
        this.schoolRepository = schoolRepository;
        this.studentMapper = studentMapper;
    }

    @Transactional
    public StudentResponseDto saveStudent(StudentDto dto) {
        if (dto.schoolId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "schoolId is required");
        }
        // 先确认学校存在，而不是只 new School 并填写一个可能不存在的 ID。
        var school = schoolRepository.findById(dto.schoolId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "School not found: " + dto.schoolId()
                ));
        var student = studentMapper.toStudent(dto, school);
        var savedStudent = repository.save(student);
        return studentMapper.toStudentResponseDto(savedStudent);
    }

    public List<StudentResponseDto> findAllStudent() {
        return repository.findAll().stream()
                .map(studentMapper::toStudentResponseDto)
                .toList();
    }
    public StudentResponseDto findStudentById(Integer studentId) {
        return repository.findById(studentId)
                .map(studentMapper::toStudentResponseDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    public List<StudentResponseDto> findStudentByName(String name) {
        return repository.findAllByFirstNameContainingIgnoreCase(name)
                .stream().map(studentMapper::toStudentResponseDto).toList();
    }
    @Transactional
    public void deleteStudentById(Integer studentId) {
        repository.deleteById(studentId);
    }

}
