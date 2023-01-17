package com.user.app.dto;

import com.user.app.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link Exam} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamDto implements Serializable {
    private Long id;
    private User student;
    private User teacher;
    private QuestionExercise exercise;
    private Course course;
    private String question;
    private String answer;
    private Date createdAt;
    private Date updatedAt;
    private String timeTaken;
    private String totalQuestions;
    private String totalSolvedQuestions;
    private String totalUnsolvedQuestions;
    private double minimumMarks;
    private boolean isNegative;
    private int eachQuestionContains;
}