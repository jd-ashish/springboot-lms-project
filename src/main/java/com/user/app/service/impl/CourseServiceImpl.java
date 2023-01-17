package com.user.app.service.impl;

import com.google.gson.Gson;
import com.user.app.dto.CourseDto;
import com.user.app.entity.Course;
import com.user.app.Model.CourseModal;
import com.user.app.repositories.CourseRepo;
import com.user.app.repositories.UserRepo;
import com.user.app.service.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CourseDto createCourse(CourseDto courseDto) {
        courseDto.setCreatedAt(new Date());
        return modelMapper.map(
                courseRepo.save(modelMapper.map(
                        courseDto, Course.class)), CourseDto.class);
    }

    @Override
    public List<CourseDto> getCourseList() {
        List<CourseDto> coursesList = courseRepo.findAll().stream().map((Course course) ->
                modelMapper.map(course, CourseDto.class)).collect(Collectors.toList());

        return coursesList;
    }

    @Override
    public List<CourseModal> getCourseListWithIsStudentInRollOrNot(Long id) {
        List<CourseModal> courseModals = new ArrayList<>();

        for (Object[] obj : courseRepo.getCourseListWithIsStudentInRollOrNot(id)) {
            CourseModal courseModal = new CourseModal();
            courseModal.setCourse_name((String) obj[0]);
            courseModal.setCourse_topic((String) obj[1]);
            courseModal.setCourse_id(Long.parseLong(String.valueOf(obj[2])));
            courseModal.setEnroll_course_id(Long.parseLong(String.valueOf((obj[3] == null) ? 0 : String.valueOf(obj[3]))));
            courseModal.setIs_enrollment((obj[4] == null) ? "null" : ((Boolean) obj[4]) ? "true" : "false");
            courseModal.setUser(userRepo.findById(Long.parseLong(String.valueOf(obj[5]))).get());
            if (obj[6] != null)
                courseModal.setStudentDetails(userRepo.findById(Long.parseLong(String.valueOf(obj[6]))).get());
            courseModals.add(courseModal);
        }
        return courseModals;
    }

    @Override
    public void deleteCourse(Integer id) {
        courseRepo.deleteById(Long.parseLong(String.valueOf(id)));
//        courseRepo.deleteCourseById(Long.parseLong(String.valueOf(id)));
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepo.findByIds(id).get(0);
    }

    @Override
    public void updateTotalLesson(Long totalLesson, Long courseId) {
        courseRepo.updateTotalLesson(totalLesson, courseId);
    }

    @Override
    public void updateTotalQuestion(Long totalQuestion, Long courseId) {
        courseRepo.updateTotalQuestion(totalQuestion, courseId);
    }

    @Override
    public List<CourseModal> getCourseListWithIsStudentInRollOrNotByUserId(Long userId) {
        List<CourseModal> courseModals = new ArrayList<>();
        for (Object[] obj : courseRepo.getCourseListWithIsStudentInRollOrNotByUserId(userId)) {
            CourseModal courseModal = new CourseModal();
            courseModal.setCourse_name((String) obj[0]);
            courseModal.setCourse_topic((String) obj[1]);
            courseModal.setCourse_id(Long.parseLong(String.valueOf(obj[2])));
            courseModal.setEnroll_course_id(Long.parseLong(String.valueOf((obj[3] == null) ? 0 : String.valueOf(obj[3]))));
            courseModal.setIs_enrollment((obj[4] == null) ? "null" : ((Boolean) obj[4]) ? "true" : "false");
            courseModal.setUser(userRepo.findById(Long.parseLong(String.valueOf(obj[5]))).get());

            if (obj[3] != null)
                courseModal.setStudentDetails(userRepo.findById(Long.parseLong(String.valueOf(obj[6]))).get());
            if (obj[7] != null) courseModal.setStatus((String) obj[1]);
            if (obj[3] != null) courseModals.add(courseModal);
        }
        return courseModals;
    }

    @Override
    public List<CourseDto> getLatestCourse(int limit) {

        return courseRepo.getLatestCourse(limit)
                .stream().map((Course course) ->
                        modelMapper.map(course, CourseDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> getCourseByTeacherId(Long id) {
        return courseRepo.getCourseByTeacherId(id)
                .stream().map((Course course) ->
                        modelMapper.map(course, CourseDto.class)).collect(Collectors.toList());
    }

    @Override
    public void updateCourse(CourseDto courseDto) {
        courseDto.setUpdatedAt(new Date());
        Course course = courseRepo.findByIds(courseDto.getId()).get(0);

        courseDto.setCreatedAt(course.getCreatedAt());
        courseDto.setTotal_lesson(course.getTotal_lesson());
        courseDto.setTotal_questions(course.getTotal_questions());
        courseRepo.save(modelMapper.map(
                courseDto, Course.class));
    }
}
