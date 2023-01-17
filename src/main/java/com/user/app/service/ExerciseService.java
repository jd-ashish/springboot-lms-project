package com.user.app.service;

import com.user.app.dto.ExamDto;
import com.user.app.dto.ExerciseDto;
import com.user.app.entity.Exercise;
import com.user.app.entity.User;

import java.util.List;

public interface ExerciseService {
    ExerciseDto create(ExerciseDto exercise, User user);
    ExerciseDto update(ExerciseDto exercise, User user, String questionId);

    List<Exercise> findById(Long i);
    List<ExerciseDto> findAll();

    List<ExerciseDto> findAllByQuestionExerciseId(Long parseLong);
    List<ExerciseDto> findAllByQuestionById(Long parseLong);
    List<ExerciseDto> findAllByQuestionExerciseIdWithLimit(Long parseLong, String numberOfQuestionPerStudent);

    void delete(String questionId);

    void emptyExerciseQuestion(String exerciseId);

    List<ExerciseDto> getExerciseByCourseId(String courseId);
    ExerciseDto getById(String id);

    List<ExerciseDto> getLatestNotice(int limit);

    List<ExerciseDto> getByTeacher(Long userId);

    List<ExerciseDto> getList();
}
