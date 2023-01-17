package com.user.app.service;

import com.user.app.dto.EnrollCourseDto;
import com.user.app.dto.EnrollmentCoursesDto;

import java.util.List;

public interface EnrollCourseService {
    EnrollCourseDto createEnrollCourse(EnrollCourseDto enrollCourseDto);

    boolean checkCourseEnrollment(String isEnrollment, Long course_id, Long student_id, Long teacher_id);

    List<EnrollmentCoursesDto> getEnrollmentCoursesByStudentId(Long student_id);

    boolean acceptEnrollmentCourses(boolean isEnrollment , Long course_enrollment_id);

    List<EnrollCourseDto> findByTeacherId(Long teacher_id);

}
