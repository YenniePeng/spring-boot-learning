package com.yennie.restapi;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
//@Table(name = "Students")
public class Student {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "c_fname",length = 20)
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private int age;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    private StudentProfile studentProfile;



    @ManyToOne
    @JoinColumn(name =  "school_id")
    @JsonBackReference
    private School school;

    public Student() {
    }

    public Student(int age, String firstName, String email, String lastName) {
        this.age = age;
        this.firstName = firstName;
        this.email = email;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public StudentProfile getStudentProfile() {
        return studentProfile;
    }

    public void setStudentProfile(StudentProfile studentProfile) {
        this.studentProfile = studentProfile;
    }
}
