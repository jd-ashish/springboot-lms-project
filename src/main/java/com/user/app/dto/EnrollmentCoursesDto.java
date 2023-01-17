package com.user.app.dto;

import com.user.app.entity.EnrollCourse;
import lombok.Data;

@Data
public class EnrollmentCoursesDto extends EnrollCourseDto {
    private String course_name;
    private String course_topic;
    private Long course_id;
    private boolean is_enrollment;
    private Long enroll_id;
    private String name;
    private String email;




}
