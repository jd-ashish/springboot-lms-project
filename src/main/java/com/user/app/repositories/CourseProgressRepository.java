package com.user.app.repositories;

import com.user.app.dto.CourseProgressDto;
import com.user.app.entity.Course;
import com.user.app.entity.CourseProgress;
import com.user.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CourseProgressRepository extends JpaRepository<CourseProgress, Long> {
    @Query("select (count(c) > 0) from CourseProgress c where c.course.id = ?1 and c.student.id = ?2")
    boolean checkRecords(Long course_id, Long student_id);

    @Query("select c from CourseProgress c where c.course.id = ?1 and c.student.id = ?2")
    CourseProgress getSpecificData(Long course_id, Long student_id);

    @Transactional
    @Modifying
    @Query("update CourseProgress c set c.completedLesson = ?1, c.data = ?2 " +
            "where c.course = ?3 and c.student = ?4")
    int updateLesson(int completedLesson , String data, Course course, User student);

    @Transactional
    @Modifying
    @Query("update CourseProgress c set c.completedExercise = ?1, c.data = ?2 " +
            "where c.course = ?3 and c.student = ?4")
    int updateExercise(int completedExercise, String data, Course course, User student);

}

