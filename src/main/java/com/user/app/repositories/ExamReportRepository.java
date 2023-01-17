package com.user.app.repositories;

import com.user.app.entity.ExamReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExamReportRepository extends JpaRepository<ExamReport, Long> {
    @Query("select e from ExamReport e where e.exam.id = ?1")
    List<ExamReport> findByExamId(Long id);


    @Query(value = "SELECT max(CAST(final_marks AS UNSIGNED)) as final_marks , id , exam_id FROM exam_report where student_id=?1" , nativeQuery = true)
    List<Object[]> getMaxFinalMarks(Long id);


}