package com.yennie.restapi.studentprofile;

import com.yennie.restapi.student.Student;
import jakarta.persistence.*;

/** 一对一关系的维护端：student_id 外键存放在资料表。 */
@Entity
public class StudentProfile {
    @Id
    @GeneratedValue
    private Integer id;
    private String bio;

    @OneToOne
    @JoinColumn(name = "student_id", unique = true)
    private Student student;

    public StudentProfile() {
    }

    public StudentProfile(String bio) {
        this.bio = bio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
