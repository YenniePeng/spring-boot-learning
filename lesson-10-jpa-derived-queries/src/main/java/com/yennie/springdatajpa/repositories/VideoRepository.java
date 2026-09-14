package com.yennie.springdatajpa.repositories;

import com.yennie.springdatajpa.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Integer> {
}
