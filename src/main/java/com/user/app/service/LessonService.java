package com.user.app.service;

import com.user.app.dto.LessonDto;
import com.user.app.entity.User;

import java.util.List;
import java.util.Map;

public interface LessonService {
    List<LessonDto> getLessonList();

    void createLesson(Map<String, Object> map, User user);

    LessonDto getLessonByLessonName(String lessonName);

    List<LessonDto> getLessonListByCourseId(String courseId);

    List<LessonDto> getLastFiveLessonList();
    List<LessonDto> searchLessonList(String search, User user);

    List<LessonDto> getLessonListByCourseIdOrUserId(String courseId, Long userId);

    List<LessonDto>  getLessonByLessonId(String lessonId);

    List<LessonDto> getRangeLesson(String courseId,String start, String end);

    Long getNextId(String courseId, String lessonId, int limit);

    void delete(Long id);

    List<LessonDto> getLatestNotice(int limit);

    List<LessonDto> getLessonByTeacher(Long id);

    List<LessonDto> getLastFiveLessonListByTeacher(Long userId);
}
