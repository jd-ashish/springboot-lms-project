package com.user.app.dto;

import com.user.app.commons.enumarizion.EnrollCourseStatus;
import com.user.app.entity.Course;
import com.user.app.entity.EnrollCourse;
import com.user.app.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link EnrollCourse} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollCourseDto implements Serializable {
    private Long id;
    private Course course;
    private User student;
    private User teacher;
    private boolean isEnrollment;
    private EnrollCourseStatus status;
    private Date createdAt;
    private Date updatedAt;
}