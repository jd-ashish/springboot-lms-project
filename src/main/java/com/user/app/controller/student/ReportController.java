package com.user.app.controller.student;

import com.user.app.commons.utils.EncodeDecode;
import com.user.app.commons.utils.Utils;
import com.user.app.dto.CourseProgressDto;
import com.user.app.dto.ExamDto;
import com.user.app.dto.ExamReportDto;
import com.user.app.dto.LessonDto;
import com.user.app.entity.Course;
import com.user.app.entity.User;
import com.user.app.repositories.UserRepo;
import com.user.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.user.app.commons.utils.Utils.getAuthUsername;

@Controller
@RequestMapping("/report/")
public class ReportController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private EnrollCourseService enrollCourseService;

    @Autowired
    private QuestionExerciseService questionExerciseService;

    @Autowired
    private CourseService courseService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private CourseProgressService courseProgressService;

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamReportService examReportService;

    @RequestMapping("{courseName}/{courseId}")
    public String report(Model model, Principal principal,
                         @PathVariable String courseName,
                         @PathVariable Integer courseId) {
        if (principal == null) return "redirect:/";
        try {
            model.addAttribute("user", userRepo.findByEmail(getAuthUsername()).get());
            model.addAttribute("name", principal.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("utils", Utils.class);
        model.addAttribute("courseId", courseId);
        return "admin/report/report";
    }

    @RequestMapping("get-layout/{courseId}")
    public String getLayout(Model model, Principal principal,
                            @PathVariable("courseId") String courseId) {


        User user = userRepo.findByEmail(getAuthUsername()).get();
        Course course = courseService.getCourseById(Long.parseLong(courseId));
        List<LessonDto> lessonList = lessonService.getLessonListByCourseId(courseId);
        List<Long> exerciseId = new ArrayList<>();
        List<Long> lessonId = new ArrayList<>();
        Map<Long, ExamReportDto> exerciseExam = new HashMap<>();
        if (user.getRole().equals("ROLE_STUDENT")) {
            CourseProgressDto courseProgress = courseProgressService.getByCourseId(course.getId(), user.getId());
            int CompleteProgress = 0;
            if (courseProgress != null) {
                double tc = courseProgress.getCompletedLesson() + courseProgress.getCompletedExercise();
                double tt = Utils.isNullToX(course.getTotal_lesson(), 0L) + Utils.isNullToX(course.getTotal_questions(), 0L);
                CompleteProgress = (int) ((tc / tt) * 100);
            }
            String[] progressData = new String[0];
            if (courseProgress != null) {
                progressData = courseProgress.getData().split(",");
            }

            for (String data : progressData) {
                if (data.contains("exercise")) exerciseId.add(Long.parseLong(data.split("_")[1]));
                if (data.contains("lesson")) lessonId.add(Long.parseLong(data.split("_")[1]));
                if (data.contains("exercise")) {
                    List<ExamDto> exam = examService
                            .getExamsWithReports(String.valueOf(user.getId()),
                                    String.valueOf(course.getUser().getId()), courseId, data.split("_")[1]);

                    if (exam.size() > 0) {
                        exerciseExam.put(Long.parseLong(data.split("_")[1]), examReportService
                                .findByExamId(String.valueOf(exam.get(0).getId())).get(0));
                    }
                }
            }
            model.addAttribute("totalProgress", 100);
            model.addAttribute("completeProgress", CompleteProgress);
        }

        model.addAttribute("exerciseExamReport", exerciseExam);
        model.addAttribute("exerciseId", exerciseId);
        model.addAttribute("user", user);
        model.addAttribute("lessonId", lessonId);
        model.addAttribute("title", "Lesson list");
        model.addAttribute("isCourse", true);
        model.addAttribute("isContent", false);
        model.addAttribute("isCreateLesson", false);
        model.addAttribute("lessonList", lessonList);
        model.addAttribute("course", course);
        model.addAttribute("encodeDecode", EncodeDecode.class);
        model.addAttribute("utils", Utils.class);
        model.addAttribute("exerciseQuestion",
                questionExerciseService.getExerciseByCourseId(courseId));

        model.addAttribute("obj", this);
        model.addAttribute("int", Integer.class);
//        model.addAttribute("exam", examReportService.findById(examReportId).get(0).getExam());

        model.addAttribute("utils", Utils.class);
        return "admin/report/report-layout";
    }

    //    public ExamReportDto getExam(Long student_id, Long teacher_id, Long course_id, Long question_exercise_id) {
//        List<ExamDto> examDto = examService.getExamsWithReports(String.valueOf(student_id),
//                String.valueOf(teacher_id),
//                String.valueOf(course_id), String.valueOf(question_exercise_id));
//        return examReportService.findById(String.valueOf(examDto.get(0).getId())).get(0).getExam();
//    }
    public ExamReportDto getExamReport(Long student_id, Long teacher_id, Long course_id, Long question_exercise_id) {
        List<ExamDto> examDto = examService.getExamsWithReports(String.valueOf(student_id),
                String.valueOf(teacher_id),
                String.valueOf(course_id), String.valueOf(question_exercise_id));
        if (examDto.size() == 0)
            return null;
        return (examReportService.findById(String.valueOf(examDto.get(0)
                .getId()))==null)? null:examReportService.findById(String.valueOf(examDto.get(0).getId())).get(0);
    }

    @RequestMapping("course-list")
    public String reportByCourse(Model model, Principal principal) {
        if (principal == null) return "redirect:/";

        try {
            model.addAttribute("user", userRepo.findByEmail(getAuthUsername()).get());
            model.addAttribute("name", principal.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("title", "My Course list");
        model.addAttribute("courseList", enrollCourseService.getEnrollmentCoursesByStudentId(userRepo.findByEmail(getAuthUsername()).get().getId()));
        model.addAttribute("utils", Utils.class);
        return "admin/report/report-index";
    }
}
