package com.user.app.service;

import com.user.app.dto.ExamReportDto;
import com.user.app.entity.User;

import java.util.List;

public interface ExamReportService {
    ExamReportDto createReport(ExamReportDto examReportDto);

    List<ExamReportDto> findById(String examReportId);
    List<ExamReportDto> findByExamId(String examReportId);
    List<Object[]> getMaxFinalMarks(User user);
}
