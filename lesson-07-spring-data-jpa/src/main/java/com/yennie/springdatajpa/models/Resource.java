package com.yennie.springdatajpa.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Resource {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String size;
    private String url;


    @OneToOne
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;
}
