package com.user.app.service;

import com.user.app.dto.CourseProgressDto;

public interface CourseProgressService {

    CourseProgressDto createCourseProgress(CourseProgressDto courseProgressDto);
    void updateCourseProgress(CourseProgressDto courseProgressDto, String type);
    boolean checkCourseProgressExistOrNot(CourseProgressDto courseProgressDto);
    CourseProgressDto getCourseProgressExistRecord(CourseProgressDto courseProgressDto);

    CourseProgressDto getByCourseId(Long courseId, Long studentId);
}
