package com.yennie.restapi.student;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

/** @NotEmpty 检查非 null、非空；@Email 检查格式，不能单独保证必填。 */
public record StudentDto(
        @NotEmpty(message = "Firstname should not be empty")
        String firstName,
        @NotEmpty(message = "Lastname should not be empty")
        String lastName,
        @NotEmpty(message = "Email should not be empty")
        @Email(message = "Email should be valid")
        String email,
        int age,
        Integer schoolId) {
}
