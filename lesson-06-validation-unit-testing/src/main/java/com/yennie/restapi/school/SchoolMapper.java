package com.yennie.restapi.school;

import org.springframework.stereotype.Component;

/** Mapper 只转换数据，不查询数据库，也不处理 HTTP。 */
@Component
public class SchoolMapper {
    public School toSchool(SchoolRequestDto dto) {
        return new School(dto.name());
    }

    public SchoolResponseDto toSchoolDto(School school) {
        return new SchoolResponseDto(school.getId(), school.getName());
    }
}
