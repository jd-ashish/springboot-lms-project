package com.user.app.service.impl;

import com.user.app.dto.ExamDto;
import com.user.app.dto.LessonDto;
import com.user.app.entity.Exam;
import com.user.app.entity.Lesson;
import com.user.app.repositories.ExamRepository;
import com.user.app.service.ExamService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ExamServiceImpl implements ExamService {
    @Autowired private ExamRepository examRepository;
    @Autowired private ModelMapper modelMapper;

    @Override
    public ExamDto create(ExamDto examDto) {
        examDto.setUpdatedAt(new Date());
        examDto.setCreatedAt(new Date());

        Exam exam = modelMapper.map(examDto, Exam.class);
        Exam examDtoResponse = examRepository.save(exam);
        return modelMapper.map(examDtoResponse, ExamDto.class);
    }

    @Override
    public List<ExamDto> getExamsWithReports(String student_id, String teacher_id, String course_id, String question_exercise_id) {
        return examRepository.findByStudent_IdAndTeacher_IdAndCourse_IdAndQuestionExercise_IdOrderByCreatedAtDesc(
                Long.parseLong(student_id),Long.parseLong(teacher_id),Long.parseLong(course_id),Long.parseLong(question_exercise_id)
        ).stream()
                .map(exam -> modelMapper.map(exam,ExamDto.class))
                .collect(Collectors.toList());
    }


}
