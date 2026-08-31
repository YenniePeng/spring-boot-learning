package com.yennie.restapi;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {

   List<Student> findAllByFirstNameContainingIgnoreCase(String firstName);
}
