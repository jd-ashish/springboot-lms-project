package com.user.app.service.impl;

import com.user.app.commons.enumarizion.EnrollCourseStatus;
import com.user.app.dto.CourseDto;
import com.user.app.dto.EnrollmentCoursesDto;
import com.user.app.entity.Course;
import com.user.app.entity.EnrollCourse;
import com.user.app.dto.EnrollCourseDto;
import com.user.app.repositories.EnrollCourseRepository;
import com.user.app.service.EnrollCourseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class EnrollCourseServiceImpl implements EnrollCourseService {

    @Autowired
    private EnrollCourseRepository enrollCourseRepository;


    @Autowired
    private ModelMapper modelMapper;

    @Override
    public EnrollCourseDto createEnrollCourse(EnrollCourseDto enrollCourseDto) {
        enrollCourseDto.setStatus(EnrollCourseStatus.PENDING);
        enrollCourseRepository.save(modelMapper.map(enrollCourseDto, EnrollCourse.class));
        return null;
    }

    @Override
    public boolean checkCourseEnrollment(String isEnrollment, Long course_id, Long student_id, Long teacher_id) {

        String[] isEnrolls = isEnrollment.split(",");
        List<Boolean> list = new ArrayList<>();
        for (String isEnroll : isEnrolls) {
            System.out.println(isEnroll);
            boolean isData = enrollCourseRepository
                    .checkCourseEnrollment(Boolean.getBoolean(isEnroll), course_id, student_id, teacher_id);
            if (isData) list.add(true);
        }
        return list.size() == 2;
    }

    @Override
    public List<EnrollmentCoursesDto> getEnrollmentCoursesByStudentId(Long student_id) {

        List<EnrollmentCoursesDto> list = new ArrayList<>();
        for (Object[] obj : enrollCourseRepository
                .getEnrollmentCoursesByStudentId(Integer.valueOf(String.valueOf(student_id)))) {
            if (obj[0] != null) {
                EnrollmentCoursesDto enrollmentCoursesDto = new EnrollmentCoursesDto();
                if (obj[7] != null) {
                    enrollmentCoursesDto.setStatus(EnrollCourseStatus.valueOf(String.valueOf(obj[7])));
                }else{
                    enrollmentCoursesDto.setStatus(EnrollCourseStatus.valueOf(EnrollCourseStatus.NULL.toString()));
                }
                SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd");
                Date date= null;
                try {
                    date = formatter6.parse(obj[8].toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                enrollmentCoursesDto.setCreatedAt(date);

                enrollmentCoursesDto.setStatus(EnrollCourseStatus.valueOf(String.valueOf(obj[7])));
                enrollmentCoursesDto.setEmail(String.valueOf(obj[6]));
                enrollmentCoursesDto.setName(String.valueOf(obj[5]));
                enrollmentCoursesDto.setEnroll_id(Long.parseLong(String.valueOf(obj[4])));
                enrollmentCoursesDto.set_enrollment((Boolean) obj[3]);
                enrollmentCoursesDto.setCourse_id(Long.parseLong(String.valueOf(obj[2])));
                enrollmentCoursesDto.setCourse_topic((String) obj[1]);
                enrollmentCoursesDto.setCourse_name((String) obj[0]);
                list.add(enrollmentCoursesDto);
            }
        }
        return list;
    }

    @Override
    public boolean acceptEnrollmentCourses(boolean isEnrollment, Long course_enrollment_id) {
        if(isEnrollment){
            enrollCourseRepository.acceptEnrollmentCourses(EnrollCourseStatus.ACTIVE, true, course_enrollment_id);
        }else{
            enrollCourseRepository.acceptEnrollmentCourses(EnrollCourseStatus.PENDING, false, course_enrollment_id);
        }

        return false;
    }

    @Override
    public List<EnrollCourseDto> findByTeacherId(Long teacher_id) {
        return enrollCourseRepository.findByTeacher_IdOrderByCreatedAtDesc(teacher_id)
                .stream().map((EnrollCourse enrollCourse) ->
                        modelMapper.map(enrollCourse, EnrollCourseDto.class)).collect(Collectors.toList());
    }
}
