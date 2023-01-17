package com.user.app.service.impl;

import com.user.app.dto.ExamDto;
import com.user.app.dto.ExerciseDto;
import com.user.app.dto.QuestionExerciseDto;
import com.user.app.dto.UserDto;
import com.user.app.entity.Exam;
import com.user.app.entity.Exercise;
import com.user.app.entity.QuestionExercise;
import com.user.app.entity.User;
import com.user.app.repositories.ExerciseRepository;
import com.user.app.service.CourseService;
import com.user.app.service.ExerciseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ExerciseDto create(ExerciseDto exerciseDto, User user) {

        UserDto userDto = modelMapper.map(user, UserDto.class);
        exerciseDto.setUser(userDto);
        exerciseDto.setCreatedAt(new Date());
        exerciseDto.setUpdatedAt(new Date());
        Exercise exercise = null;


        exercise = exerciseRepository.save(modelMapper.map(exerciseDto, Exercise.class));

        courseService.updateTotalQuestion(
                (long) (exerciseRepository.getExerciseByCourseIdWithGroup(exerciseDto.getCoursesId()).size()),
                exerciseDto.getCoursesId());

        return modelMapper.map(exercise, ExerciseDto.class);

    }

    @Override
    public ExerciseDto update(ExerciseDto exercise, User user, String questionId) {

        exerciseRepository.update(exercise.getQuestion(),
                exercise.getOptions(),
                exercise.getAnswer(),
                exercise.getCoursesId(),
                exercise.getQuestionExerciseList(),
                new Date(), Long.parseLong(questionId));
        Exercise exerciseEntity = exerciseRepository.findById(Long.parseLong(questionId)).get();
        return modelMapper.map(exerciseEntity, ExerciseDto.class);
    }

    @Override
    public List<Exercise> findById(Long i) {
        return exerciseRepository.findById(i).stream().filter(new Predicate<Exercise>() {
            @Override
            public boolean test(Exercise exercise) {
                return false;
            }
        }).collect(Collectors.toList());
    }


    @Override
    public List<ExerciseDto> findAll() {
        return exerciseRepository.findAll().stream()
                .map(exercise -> modelMapper.map(exercise, ExerciseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ExerciseDto> findAllByQuestionExerciseId(Long id) {
        return exerciseRepository.findAllByQuestionExerciseId(id).stream()
                .map(exercise -> modelMapper.map(exercise, ExerciseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ExerciseDto> findAllByQuestionById(Long id) {
        return exerciseRepository.findById(id)
                .stream()
                .map(exercise -> modelMapper.map(exercise, ExerciseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ExerciseDto> findAllByQuestionExerciseIdWithLimit(Long id, String numberOfQuestionPerStudent) {
        Pageable topTwenty = PageRequest.of(0, Integer.parseInt(numberOfQuestionPerStudent));

        return exerciseRepository.findAllByQuestionExerciseIdWithLimit(id, Integer.parseInt(numberOfQuestionPerStudent)).stream()
                .map(exercise -> modelMapper.map(exercise, ExerciseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String questionId) {
        exerciseRepository.deleteById(Long.parseLong(questionId));
    }

    @Override
    public void emptyExerciseQuestion(String exerciseId) {
        QuestionExercise questionExercise = new QuestionExercise();
        questionExercise.setId(Long.parseLong(exerciseId));
        exerciseRepository.emptyExerciseQuestion(questionExercise);
    }

    @Override
    public List<ExerciseDto> getExerciseByCourseId(String courseId) {
        return exerciseRepository.getExerciseByCourseId(Long.valueOf(courseId)).stream()
                .map((Exercise exercise) ->
                        modelMapper.map(exercise, ExerciseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ExerciseDto getById(String id) {
        return
                modelMapper.map(exerciseRepository.getById(Long.valueOf(id)), ExerciseDto.class);
    }
    @Override
    public List<ExerciseDto> getLatestNotice(int limit) {
        return exerciseRepository.getLatestNotice(limit).stream().map((Exercise exercise) ->
                modelMapper.map(exercise, ExerciseDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<ExerciseDto> getByTeacher(Long userId) {
        return exerciseRepository.getByTeacher(userId).stream().map((Exercise exercise) ->
                modelMapper.map(exercise, ExerciseDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<ExerciseDto> getList() {
        return exerciseRepository.findAll().stream().map((Exercise exercise) ->
                modelMapper.map(exercise, ExerciseDto.class)).collect(Collectors.toList());
    }
}
