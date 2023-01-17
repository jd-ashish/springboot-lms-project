package com.user.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.user.app.entity.Exercise;
import com.user.app.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A DTO for the {@link com.user.app.entity.QuestionExercise} entity
 */
@Data
public class QuestionExerciseDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String exerciseName;
    private User user;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Exercise> exercise = new ArrayList<>();

    private Date createdAt;
    private Date updatedAt;

    private String time;
    private String numberOfQuestionPerStudent;
    private double minimumMarks;
    private boolean isNegative;
    private int eachQuestionContains;



    public QuestionExerciseDto() {

    }
}