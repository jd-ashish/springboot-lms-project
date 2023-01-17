package com.user.app.dto;

import com.user.app.entity.Course;
import com.user.app.entity.CourseProgress;
import com.user.app.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link CourseProgress} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseProgressDto implements Serializable {
    private Long id;
    private Course course;
    private int completedLesson;
    private int completedExercise;
    private User student;
    private String data;
    private Date createdAt;
    private Date updatedAt;
}