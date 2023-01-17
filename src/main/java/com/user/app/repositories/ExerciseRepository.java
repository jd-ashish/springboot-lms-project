package com.user.app.repositories;

import com.user.app.entity.Exam;
import com.user.app.entity.Exercise;
import com.user.app.entity.QuestionExercise;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {



    @Query("select (count(q) > 0) from questions q where upper(q.question) = upper(?1) and q.user.id = ?2")
    boolean CheckDuplicateQuestions(String question, Long id);



    @Query("select q from questions q where q.questionExerciseList.id = ?1")
    List<Exercise> findAllByQuestionExerciseId(Long id);

    @Transactional
    @Modifying
    @Query("update questions q set q.question = ?1, q.options = ?2, q.answer = ?3, q.CoursesId = ?4, " +
            "q.questionExerciseList = ?5, q.updatedAt = ?6 " +
            "where q.id = ?7")
    int update(@NonNull String question, @NonNull String options, @NonNull String answer,
               @NonNull Long CoursesId, @NonNull QuestionExercise questionExerciseList, @NonNull Date updatedAt, Long id);

    @Transactional
    @Modifying
    @Query("delete from questions q where q.questionExerciseList = ?1")
    int emptyExerciseQuestion(QuestionExercise questionExerciseList);

    @Query(value = "select * from questions where courses_id= ?1", nativeQuery = true)
    List<Exercise> getExerciseByCourseId(Long CoursesId);

    @Query(value = "select * from questions where courses_id= ?1 GROUP by question_exercise_id", nativeQuery = true)
    List<Exercise> getExerciseByCourseIdWithGroup(Long CoursesId);

//    @Query("select q from questions q where q.questionExerciseList.id = ?1 ORDER BY RAND() LIMIT ?2")
//    List<Exercise>  findAllByQuestionExerciseIdWithLimit(Long id, String numberOfQuestionPerStudent);

    @Query(value = "select * from questions WHERE question_exercise_id=?1 ORDER BY RAND() LIMIT ?2" , nativeQuery = true)
    List<Exercise> findAllByQuestionExerciseIdWithLimit(Long id, Integer pageable);

    @Query(value = "select * from questions ORDER BY created_at DESC LIMIT ?1", nativeQuery = true)
    List<Exercise> getLatestNotice(int limit);

    @Query("select q from questions q where q.user.id = ?1")
    List<Exercise> getByTeacher(Long userId);

}