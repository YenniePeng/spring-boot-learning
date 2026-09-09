package com.yennie.restapi.school;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SchoolService {
    private final SchoolMapper schoolMapper;
    private final SchoolRepository schoolRepository;

    public SchoolService(SchoolMapper schoolMapper, SchoolRepository schoolRepository) {
        this.schoolMapper = schoolMapper;
        this.schoolRepository = schoolRepository;
    }

    @Transactional
    public SchoolResponseDto create(SchoolRequestDto dto) {
        var school = schoolMapper.toSchool(dto);
        var savedSchool = schoolRepository.save(school);
        // 返回保存后的对象，响应才包含数据库生成的 ID。
        return schoolMapper.toSchoolDto(savedSchool);
    }

    public List<SchoolResponseDto> findAll() {
        return schoolRepository.findAll()
                .stream()
                .map(schoolMapper::toSchoolDto)
                .toList();
    }
}
