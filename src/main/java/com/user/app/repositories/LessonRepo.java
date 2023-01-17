package com.user.app.repositories;

import com.user.app.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LessonRepo extends JpaRepository<Lesson,Long> {
    @Query("select l from Lesson l where l.course.id = ?1")
    List<Lesson> getLessonListByCourseId(Long id);

    @Query(value = "SELECT * FROM lesson WHERE lessons_name= '1?'", nativeQuery = true)
    Lesson getLessonByLessonName(String lessonName);

    @Query(value = "select * from lesson order by created_at desc limit 5", nativeQuery=true)
    List<Lesson> getLastFiveLessonList();

    @Query("select l from Lesson l where l.course.id = ?1 and l.user.id = ?2")
    List<Lesson> getLessonListByCourseIdOrUserId(Long courseId, Long userId);


    @Query(value = "select * from lesson WHERE lesson.course_id=?1 and lesson.id BETWEEN ?2 and ?3" , nativeQuery = true)
    List<Lesson> getRangeLesson(Long course_id, Long parseLong, Long parseLong1);

    @Query(value = "select * from lesson WHERE lesson.course_id=?1 and lesson.id>?2 LIMIT ?3", nativeQuery = true)
    List<Lesson> getNextId(String courseId, String lessonId, int limit);


    @Query(value = "SELECT * FROM `lesson` WHERE user_id=?2 and `lessons_name` LIKE %?1%", nativeQuery = true)
    List<Lesson> searchLessonList(String search, Long id);
    @Query(value = "select * from lesson ORDER BY created_at DESC LIMIT ?1", nativeQuery = true)
    List<Lesson> getLatestNotice(int limit);

    @Query("select l from Lesson l where l.user.id = ?1")
    List<Lesson> getLessonByTeacher(Long id);


    @Query(value = "select * from lesson where user_id=?1 order by created_at desc limit 5", nativeQuery=true)
    List<Lesson> getLastFiveLessonListByTeacher(Long userId);
}
