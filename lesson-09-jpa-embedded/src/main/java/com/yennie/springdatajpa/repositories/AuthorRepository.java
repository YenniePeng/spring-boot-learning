package com.yennie.springdatajpa.repositories;

import com.yennie.springdatajpa.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
