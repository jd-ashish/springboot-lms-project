package com.user.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.user.app.entity.QuestionExercise;
import com.user.app.entity.User;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.user.app.entity.Exercise} entity
 */
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExerciseDto  {

    private Long id;
    private String question;
    private String options;
    private String answer;



    private UserDto user;

    private Date createdAt;
    private Date updatedAt;
    private Long CoursesId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private QuestionExercise questionExerciseList;
}