package com.yennie.restapi.student;

public record StudentDto(
       String firstName,
       String lastName,
       String email,
       int age,
       Integer schoolId) {
}
