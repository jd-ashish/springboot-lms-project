package com.user.app.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @Column(name = "lessons_name", unique = true)
    @NotNull(message = "lesson can't be null")
    @NotEmpty(message = "Please mention lesson name.")
    private String lessonsName;

    @Column(unique = true, columnDefinition = "LONGTEXT")
    @NotNull(message = "lesson content can't be null")
    @NotEmpty(message = "Please mention lesson content.")
    private String lessonContent;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @NotNull(message = "Please enter course Id.")
    @JoinColumn(name = "course_id")
    @ManyToOne
    private Course course;

    private Date createdAt;
    private Date updatedAt;
}
