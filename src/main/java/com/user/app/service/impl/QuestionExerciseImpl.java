package com.user.app.service.impl;

import com.user.app.dto.QuestionExerciseDto;
import com.user.app.entity.QuestionExercise;
import com.user.app.entity.User;
import com.user.app.repositories.QuestionExerciseRepository;
import com.user.app.service.QuestionExerciseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionExerciseImpl implements QuestionExerciseService {

    @Autowired private QuestionExerciseRepository questionExerciseRepository;

    @Autowired private ModelMapper modelMapper;
    @Override
    public QuestionExerciseDto create(QuestionExerciseDto questionExerciseDto) {
        QuestionExercise  questionExercise = modelMapper.map(questionExerciseDto, QuestionExercise.class);
        QuestionExercise afterSaveQuestionResponse = questionExerciseRepository.save(questionExercise);

        return modelMapper.map(afterSaveQuestionResponse, QuestionExerciseDto.class);
    }

    @Override
    public QuestionExerciseDto getById(Long id) {
        return modelMapper.map(questionExerciseRepository.findById(id).get(), QuestionExerciseDto.class);
    }

    @Override
    public List<QuestionExerciseDto> findByName(String name) {
        return questionExerciseRepository.findByName(name).stream()
                .map((QuestionExercise questionExercise) ->
                        modelMapper.map(questionExercise, QuestionExerciseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionExerciseDto> findByName(String name, User user) {
        return questionExerciseRepository.findByNameSpecificUser(name, user.getId()).stream()
                .map((QuestionExercise questionExercise) ->
                        modelMapper.map(questionExercise, QuestionExerciseDto.class))
                .collect(Collectors.toList());

    }

    @Override
    public List<QuestionExerciseDto> getQuestionExerciseList() {
        return questionExerciseRepository.getQuestionExerciseList().stream()
                .map((QuestionExercise questionExercise) ->
                        modelMapper.map(questionExercise, QuestionExerciseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionExerciseDto> getQuestionExerciseListLimit(int limit, User user) {

        return questionExerciseRepository.getQuestionExerciseListLimit(limit,user.getId()).stream()
                .map((QuestionExercise questionExercise) ->
                        modelMapper.map(questionExercise, QuestionExerciseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        questionExerciseRepository.delete(questionExerciseRepository.findById(Long.parseLong(id)).get());
    }

    @Override
    public List<QuestionExerciseDto> getExerciseByCourseId(String courseId) {

        return questionExerciseRepository.getExerciseByCourseId(Long.parseLong(courseId)).stream()
                .map((QuestionExercise questionExercise) ->
                        modelMapper.map(questionExercise, QuestionExerciseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void updateQuestionExercise(QuestionExerciseDto questionExerciseDto) {


        questionExerciseDto.setUpdatedAt(new Date());
        questionExerciseRepository.save(modelMapper.map(questionExerciseDto, QuestionExercise.class));
    }

    @Override
    public List<QuestionExerciseDto> searchQuestionExerciseList(String search, User user) {
        return questionExerciseRepository.searchQuestionExerciseList(search, user.getId()).stream()
                .map((QuestionExercise questionExercise) ->
                        modelMapper.map(questionExercise, QuestionExerciseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionExerciseDto> getCountQuestionExerciseByTeacher(Long id) {
        return questionExerciseRepository.getCountQuestionExerciseByTeacher(id).stream()
                .map((QuestionExercise questionExercise) ->
                        modelMapper.map(questionExercise, QuestionExerciseDto.class))
                .collect(Collectors.toList());
    }


}
