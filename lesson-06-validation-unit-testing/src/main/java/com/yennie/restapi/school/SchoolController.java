package com.yennie.restapi.school;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Controller 处理 HTTP 请求，把业务交给 Service。 */
@RestController
public class SchoolController {
    private final SchoolService schoolService;

    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }


    @PostMapping("/schools")
    public SchoolResponseDto create(@RequestBody SchoolRequestDto dto) {
        return schoolService.create(dto);
    }


    @GetMapping("/schools")
    public List<SchoolResponseDto> findAll() {
        return schoolService.findAll();
    }
}
