package com.user.app.service.impl;

import com.user.app.dto.LessonDto;
import com.user.app.entity.Course;
import com.user.app.entity.Lesson;
import com.user.app.entity.User;
import com.user.app.repositories.LessonRepo;
import com.user.app.service.CourseService;
import com.user.app.service.LessonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonRepo lessonRepo;
    @Autowired
    private CourseService courseService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<LessonDto> getLessonList() {
        return lessonRepo.findAll().stream().map((Lesson lesson) ->
                modelMapper.map(lesson, LessonDto.class)).collect(Collectors.toList());
    }

    @Override
    public void createLesson(Map<String, Object> map, User user) {
        LessonDto lessonDto = new LessonDto();
        lessonDto.setLessonContent(map.get("content").toString());
        lessonDto.setLessonsName(map.get("lessonsName").toString());
        lessonDto.setUser(user);
        Course course = new Course();
        course.setId(Long.parseLong(map.get("courseId").toString()));
        lessonDto.setCourse(course);
        lessonDto.setCreatedAt(new Date());
        lessonDto.setUpdatedAt(new Date());
        if (!map.get("id").equals("0")) {
            lessonDto.setId(Long.valueOf(map.get("id").toString()));
        }


        modelMapper.map(
                lessonRepo.save(modelMapper.map(
                        lessonDto, Lesson.class)), LessonDto.class);


        if (map.get("id").equals("0")) {
            courseService.updateTotalLesson(
                    Long.parseLong(String.valueOf(lessonRepo.getLessonListByCourseId(lessonDto.getCourse().getId()).size())),
                    course.getId());
        }
    }

    @Override
    public LessonDto getLessonByLessonName(String lessonName) {
        Lesson lesson = lessonRepo.getLessonByLessonName(lessonName);
        if (lesson == null) return null;
        return modelMapper.map(lesson, LessonDto.class);
    }

    @Override
    public List<LessonDto> getLessonListByCourseId(String courseId) {
        return lessonRepo.getLessonListByCourseId(Long.parseLong(courseId))
                .stream()
                .map(lesson -> modelMapper.map(lesson, LessonDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<LessonDto> getLastFiveLessonList() {
        return lessonRepo.getLastFiveLessonList()
                .stream()
                .map(lesson -> modelMapper.map(lesson, LessonDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<LessonDto> searchLessonList(String search, User user) {
        return lessonRepo.searchLessonList(search,user.getId())
                .stream()
                .map(lesson -> modelMapper.map(lesson, LessonDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<LessonDto> getLessonListByCourseIdOrUserId(String courseId, Long userId) {
        return lessonRepo.getLessonListByCourseIdOrUserId(Long.parseLong(courseId), userId)
                .stream()
                .map(lesson -> modelMapper.map(lesson, LessonDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<LessonDto> getLessonByLessonId(String lessonId) {
        return lessonRepo.findById(Long.parseLong(lessonId))
                .stream()
                .map(lesson -> modelMapper.map(lesson, LessonDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<LessonDto> getRangeLesson(String courseId, String start, String end) {
        return lessonRepo.getRangeLesson(Long.parseLong(courseId), Long.parseLong(start), Long.parseLong(end))
                .stream()
                .map(lesson -> modelMapper.map(lesson, LessonDto.class)).collect(Collectors.toList());
    }

    @Override
    public Long getNextId(String courseId, String lessonId, int limit) {
        List<Lesson> lesson = lessonRepo.getNextId(courseId, lessonId, limit);
        if (lesson.size() == 0) return 0L;
        return lesson.get(0).getId();
    }

    @Override
    public void delete(Long id) {
        lessonRepo.deleteById(id);
    }

    @Override
    public List<LessonDto> getLatestNotice(int limit) {
        return lessonRepo.getLatestNotice(limit).stream().map((Lesson lesson) ->
                modelMapper.map(lesson, LessonDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<LessonDto> getLessonByTeacher(Long userId) {
        return lessonRepo.getLessonByTeacher(userId).stream().map((Lesson lesson) ->
                modelMapper.map(lesson, LessonDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<LessonDto> getLastFiveLessonListByTeacher(Long userId) {
        return lessonRepo.getLastFiveLessonListByTeacher(userId)
                .stream()
                .map(lesson -> modelMapper.map(lesson, LessonDto.class)).collect(Collectors.toList());
    }
}
