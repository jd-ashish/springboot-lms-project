package com.user.app.dto;

import com.user.app.entity.Exam;
import com.user.app.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.user.app.entity.ExamReport} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamReportDto implements Serializable {
    private Long id;
    private User student;
    private Exam exam;
    private String totalCorrectQuestion;
    private String totalIncorrectQuestion;
    private String finalMarks;
    private String questions;
    private String answers;
    private String correctAnswer;
    private String studentAnswer;
    private Date createdAt;
    private Date updatedAt;
}