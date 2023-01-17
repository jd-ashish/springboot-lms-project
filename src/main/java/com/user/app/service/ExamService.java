package com.user.app.service;

import com.user.app.dto.ExamDto;

import java.util.List;

public interface ExamService {
    ExamDto create(ExamDto examDto);

    List<ExamDto> getExamsWithReports(String student_id, String teacher_id, String course_id, String question_exercise_id);


}
