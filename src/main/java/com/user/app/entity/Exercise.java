package com.user.app.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "questions")
@JsonIgnoreProperties(ignoreUnknown=true)
@Data
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String question;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String options;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String answer;

    @JoinColumn(name = "courses_id")
    private Long CoursesId;


    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @JoinColumn(name = "question_exercise_id")
    @ManyToOne
    private QuestionExercise questionExerciseList;

    private Date createdAt;
    private Date updatedAt;





}
