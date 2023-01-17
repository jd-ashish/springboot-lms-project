package com.user.app.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String course_name;
    private String course_topic;


    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "course", cascade = CascadeType.PERSIST)
    private List<EnrollCourse> enrollCourse;

//    @OneToMany(mappedBy = "course")
//    private Exam exam;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Lesson> lesson;
//
//    @OneToMany(mappedBy = "course")
//    private CourseProgress courseProgress;

    private Date createdAt;
    private Date updatedAt;

    private Long total_lesson;

    private Long total_questions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
