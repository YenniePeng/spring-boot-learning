package com.yennie.springdatajpa;

import com.yennie.springdatajpa.models.Author;
import com.yennie.springdatajpa.models.Video;
import com.yennie.springdatajpa.repositories.AuthorRepository;
import com.yennie.springdatajpa.repositories.VideoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringDataJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataJpaApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(AuthorRepository authorRepository, VideoRepository videoRepository) {
         return args -> {
            /* var authors = Author.builder()
                     .firstName("Yennie")
                     .lastName("Peng")
                     .age(30)
                     .email("yennie@gmail.com").build();
             authorRepository.save(authors);*/

             var video = Video.builder()
                     .name("abc")
                     .length(5)
                     .build();
             videoRepository.save(video);
         };
    }

}
