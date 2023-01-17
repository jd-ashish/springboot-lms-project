package com.user.app.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "exercise")
@Data
public class QuestionExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String exerciseName;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    private Date createdAt;
    private Date updatedAt;

    private String time;
    private String numberOfQuestionPerStudent;

    private double minimumMarks;
    private boolean isNegative;
    private int eachQuestionContains;


    @OneToMany(mappedBy = "questionExerciseList",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Exercise> exercise = new ArrayList<>();
}
