package com.yennie.restapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SchoolController {
    private final SchoolRepository schoolRepository;

    public SchoolController(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    @PostMapping("/schools")
    public SchoolResponseDto create(@RequestBody SchoolRequestDto dto) {
        var savedSchool = schoolRepository.save(new School(dto.name()));
        return toSchoolResponseDto(savedSchool);
    }

    private SchoolResponseDto toSchoolResponseDto(School school) {
        return new SchoolResponseDto(school.getId(), school.getName());
    }
    @GetMapping("/schools")
    public List<SchoolResponseDto> findAll() {
        return schoolRepository.findAll()
                .stream()
                .map(this::toSchoolResponseDto)
                .toList();
    }
}
