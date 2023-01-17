package com.user.app.repositories;

import com.user.app.commons.enumarizion.EnrollCourseStatus;
import com.user.app.entity.EnrollCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EnrollCourseRepository extends JpaRepository<EnrollCourse, Long> {
    @Query("select (count(e) > 0) from EnrollCourse e " +
            "where e.isEnrollment = ?1 and e.course.id = ?2 and e.student.id = ?3 and e.teacher.id = ?4")
    boolean checkCourseEnrollment(boolean isEnrollment, Long course_id, Long student_id, Long teacher_id);

    @Query(value = "SELECT c.course_name , c.course_topic , c.id as course_id , e.is_enrollment , e.id as enroll_id , u.name , u.email " +
            ",e.status , e.created_at FROM user as u , course as c , enroll_course as e " +
            "WHERE c.id=e.course_id and e.student_id= ?1 and u.id=e.teacher_id", nativeQuery = true)
    List<Object[]>  getEnrollmentCoursesByStudentId(Integer studentId);

    @Transactional
    @Modifying
    @Query("update EnrollCourse e set e.status = ?1, e.isEnrollment = ?2 where e.id = ?3")
    void acceptEnrollmentCourses(EnrollCourseStatus status, boolean isEnrollment, Long id);

    @Query(value = "select * from enroll_course WHERE id in " +
            "(select max(id) from enroll_course WHERE teacher_id=?1 GROUP BY student_id) ORDER BY created_at DESC" , nativeQuery = true)
    List<EnrollCourse> findByTeacher_IdOrderByCreatedAtDesc(Long id);

//
}