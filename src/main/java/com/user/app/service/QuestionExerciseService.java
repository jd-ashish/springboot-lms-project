package com.user.app.service;

import com.user.app.dto.QuestionExerciseDto;
import com.user.app.entity.User;

import java.util.List;

public interface QuestionExerciseService {
    QuestionExerciseDto create(QuestionExerciseDto questionExerciseDto);
    QuestionExerciseDto getById(Long id);
    List<QuestionExerciseDto> findByName(String name);
    List<QuestionExerciseDto> findByName(String name, User user);
    List<QuestionExerciseDto> getQuestionExerciseList();
    List<QuestionExerciseDto> getQuestionExerciseListLimit(int limit, User user);


    void delete(String id);

    List<QuestionExerciseDto>  getExerciseByCourseId(String courseId);

    void updateQuestionExercise(QuestionExerciseDto questionExerciseDto);

    List<QuestionExerciseDto> searchQuestionExerciseList(String search, User user);

    List<QuestionExerciseDto> getCountQuestionExerciseByTeacher(Long id);
}
