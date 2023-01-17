package com.user.app.repositories;

import com.user.app.entity.QuestionExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface QuestionExerciseRepository extends JpaRepository<QuestionExercise, Long> {
    @Query("select q from QuestionExercise q where upper(q.exerciseName) = upper(?1)")
    List<QuestionExercise> findByName(String exerciseName);


    @Query("select q from QuestionExercise q")
    List<QuestionExercise> getQuestionExerciseList();

    @Query(value = "select * from exercise where user_id=?2 order by id desc limit ?1", nativeQuery=true)
    List<QuestionExercise> getQuestionExerciseListLimit(int limit, Long id);

    @Transactional
    @Modifying
    @Query("delete from QuestionExercise q where q.id = ?1")
    int deleteQuestionExercise(Long id);

    @Query("select q from QuestionExercise q where upper(q.exerciseName) = upper(?1) and q.user.id = ?2")
    List<QuestionExercise> findByNameSpecificUser(String exerciseName, Long id);
    @Query(value = "SELECT e.id, e.exercise_name , e.user_id, e.each_question_contains, e.is_negative,e.minimum_marks, e.created_at, e.updated_at, e.number_of_question_per_student , e.time FROM questions AS q, exercise e  WHERE q.courses_id =?1 and e.id=q.question_exercise_id GROUP BY e.exercise_name", nativeQuery = true)
    List<QuestionExercise> getExerciseByCourseId(Long id);

    @Transactional
    @Modifying
    @Query("update QuestionExercise q set q.time = :time, q.numberOfQuestionPerStudent = :numberOfQuestionPerStudent, q.updatedAt = :updatedAt " +
            "where q.id = :id")
    void updateQuestionExercise(@Param("time") String time, @Param("numberOfQuestionPerStudent") String numberOfQuestionPerStudent, @Param("updatedAt") Date updatedAt, @Param("id") Long id);

    @Query(value = "SELECT * FROM `exercise` WHERE user_id = ?2 and `exercise_name` LIKE %?1%", nativeQuery = true)
    List<QuestionExercise> searchQuestionExerciseList(String search, Long id);

    @Query("select q from QuestionExercise q where q.user.id = ?1")
    List<QuestionExercise> getCountQuestionExerciseByTeacher(Long id);
}