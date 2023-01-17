package com.user.app.service;

import com.user.app.dto.CourseDto;
import com.user.app.entity.Course;
import com.user.app.Model.CourseModal;

import java.util.List;

public interface CourseService {
    CourseDto createCourse(CourseDto course);

    List<CourseDto> getCourseList();
    List<CourseModal> getCourseListWithIsStudentInRollOrNot(Long id);

    void deleteCourse(Integer id);

    Course getCourseById(Long id);
    void updateTotalLesson(Long totalLesson, Long courseId);
    void updateTotalQuestion(Long totalQuestion, Long courseId);

    List<CourseModal> getCourseListWithIsStudentInRollOrNotByUserId(Long userId);

    List<CourseDto> getLatestCourse(int limit);

    List<CourseDto> getCourseByTeacherId(Long id);

    void updateCourse(CourseDto courseDto);
}
