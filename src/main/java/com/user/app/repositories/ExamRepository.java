package com.user.app.repositories;

import com.user.app.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    @Query("select e from Exam e " +
            "where e.student.id = ?1 and e.teacher.id = ?2 and e.course.id = ?3 and e.questionExercise.id = ?4 " +
            "order by e.createdAt DESC")
    List<Exam> findByStudent_IdAndTeacher_IdAndCourse_IdAndQuestionExercise_IdOrderByCreatedAtDesc(Long student_id,
                                                                                                   Long teacher_id,
                                                                                                   Long course_id,
                                                                                                   Long question_exercise_id);

}