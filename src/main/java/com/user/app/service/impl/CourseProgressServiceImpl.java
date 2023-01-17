package com.user.app.service.impl;

import com.user.app.dto.CourseProgressDto;
import com.user.app.entity.CourseProgress;
import com.user.app.repositories.CourseProgressRepository;
import com.user.app.service.CourseProgressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class CourseProgressServiceImpl implements CourseProgressService {

    @Autowired
    private CourseProgressRepository courseProgressRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CourseProgressDto createCourseProgress(CourseProgressDto courseProgressDto) {
        courseProgressDto.setCreatedAt(new Date());
        courseProgressDto.setUpdatedAt(new Date());
        CourseProgress courseProgress = modelMapper.map(courseProgressDto, CourseProgress.class);
        return modelMapper.map(courseProgressRepository.save(courseProgress), CourseProgressDto.class);
    }

    @Override
    public void updateCourseProgress(CourseProgressDto courseProgressDto, String type) {
        if (type.equals("lesson")) {
            courseProgressRepository.updateLesson(courseProgressDto.getCompletedLesson(),
                    courseProgressDto.getData(), courseProgressDto.getCourse(), courseProgressDto.getStudent());
        } else {
            courseProgressRepository.updateExercise(courseProgressDto.getCompletedExercise(),
                    courseProgressDto.getData(), courseProgressDto.getCourse(), courseProgressDto.getStudent());
        }

    }

    @Override
    public boolean checkCourseProgressExistOrNot(CourseProgressDto courseProgressDto) {
        return courseProgressRepository.checkRecords(courseProgressDto.getCourse().getId(), courseProgressDto.getStudent().getId());
    }

    @Override
    public CourseProgressDto getCourseProgressExistRecord(CourseProgressDto courseProgressDto) {
        return modelMapper.map(
                courseProgressRepository
                        .getSpecificData(courseProgressDto.getCourse().getId(),
                                courseProgressDto.getStudent().getId()),
                CourseProgressDto.class
        );
    }

    @Override
    public CourseProgressDto getByCourseId(Long courseId, Long studentId) {
        CourseProgress courseProgress = courseProgressRepository.getSpecificData(courseId, studentId);
        if(courseProgress==null) return null;
        return modelMapper.map(courseProgress,
                CourseProgressDto.class
        );
    }
}
