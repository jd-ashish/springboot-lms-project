package com.user.app.service.impl;

import com.user.app.dto.ExamReportDto;
import com.user.app.entity.ExamReport;
import com.user.app.entity.User;
import com.user.app.repositories.ExamReportRepository;
import com.user.app.service.ExamReportService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamReportServiceImpl implements ExamReportService {
    @Autowired private ModelMapper modelMapper;
    @Autowired private ExamReportRepository examReportRepository;

    @Override
    public ExamReportDto createReport(ExamReportDto examReportDto) {
        examReportDto.setCreatedAt(new Date());
        examReportDto.setUpdatedAt(new Date());
        ExamReport examReport = modelMapper.map(examReportDto, ExamReport.class);
        ExamReport examReportResponse = examReportRepository.save(examReport);
        return modelMapper.map(examReportResponse, ExamReportDto.class);
    }

    @Override
    public List<ExamReportDto> findById(String examReportId) {
        return examReportRepository.findById(Long.parseLong(examReportId)).stream()
                .map(exercise -> modelMapper.map(exercise, ExamReportDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ExamReportDto> findByExamId(String examId) {
        return examReportRepository.findByExamId(Long.parseLong(examId)).stream()
                .map(exercise -> modelMapper.map(exercise, ExamReportDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Object[]> getMaxFinalMarks(User user) {
        return examReportRepository.getMaxFinalMarks(user.getId());
    }
}
