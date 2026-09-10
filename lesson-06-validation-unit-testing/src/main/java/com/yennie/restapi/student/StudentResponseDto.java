package com.yennie.restapi.student;

public record StudentResponseDto(
       Integer id,
       String firstName,
       String lastName,
       String email,
       int age,
       Integer schoolId,
       String schoolName) {
}
