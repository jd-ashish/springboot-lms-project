package com.user.app.repositories;

import com.user.app.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public interface CourseRepo extends JpaRepository<Course, Long> {
    @Query("select c from Course c where c.id = ?1")
    List<Course> findByIds(Long id);

    @Transactional
    @Modifying
    @Query("delete from Course c where c.id = ?1")
    void deleteCourseById(Long id);

    @Query(value = "SELECT course.course_name, course.course_topic,course.id as course_id , enroll_course.id as enroll_course_id , enroll_course.is_enrollment , course.user_id\n" +
            ", enroll_course.student_id FROM course\n" +
            "LEFT JOIN enroll_course ON course.id = enroll_course.course_id AND enroll_course.student_id=?1\n" +
            "ORDER BY course.course_name", nativeQuery = true)
    List<Object[]> getCourseListWithIsStudentInRollOrNot(Long student_id);

    @Query(value = "SELECT course.course_name, course.course_topic,course.id as course_id , enroll_course.id as enroll_course_id , enroll_course.is_enrollment , course.user_id\n" +
            ", enroll_course.student_id , enroll_course.status FROM course\n" +
            "LEFT JOIN enroll_course ON course.id = enroll_course.course_id WHERE course.user_id= ?1\n" +
            "ORDER BY course.course_name", nativeQuery = true)
    List<Object[]> getCourseListWithIsStudentInRollOrNotByUserId(Long userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE `course` SET `total_lesson` = ?1 WHERE `course`.`id` = ?2", nativeQuery = true)
    void updateTotalLesson(Long totalLesson, Long courseId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE `course` SET `total_questions` = ?1 WHERE `course`.`id` = ?2", nativeQuery = true)
    void updateTotalQuestion(Long total_questions, Long courseId);

    @Query(value = "select * from course ORDER BY created_at DESC LIMIT ?1", nativeQuery = true)
    List<Course> getLatestCourse(int limit);

    @Query("select c from Course c where c.user.id = ?1")
    List<Course> getCourseByTeacherId(Long id);


}
