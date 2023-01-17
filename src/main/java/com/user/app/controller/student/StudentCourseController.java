package com.user.app.controller.student;

import com.user.app.commons.utils.Utils;
import com.user.app.dto.CourseProgressDto;
import com.user.app.dto.EnrollCourseDto;
import com.user.app.entity.Course;
import com.user.app.entity.User;
import com.user.app.repositories.QuestionExerciseRepository;
import com.user.app.repositories.UserRepo;
import com.user.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.user.app.commons.utils.Utils.getAuthUsername;

@Controller
@RequestMapping("/student/")
public class StudentCourseController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private QuestionExerciseService questionExerciseService;

    @Autowired
    private EnrollCourseService enrollCourseService;

    @Autowired
    private CourseService courseService;
    @Autowired
    private LessonService lessonService;

    @Autowired
    private QuestionExerciseRepository questionExerciseRepository;
    @Autowired
    private CourseProgressService courseProgressService;

    CourseProgressDto data = null;

    @GetMapping("all-course")
    public String course(Model model, Principal principal) {
        if (principal == null) return "redirect:/";

        User user = userRepo.findByEmail(getAuthUsername()).get();
        try {
            model.addAttribute("user", user);
            model.addAttribute("name", principal.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("title", "Course list");
        model.addAttribute("courseList", courseService.getCourseListWithIsStudentInRollOrNot(user.getId()));
        model.addAttribute("utils", Utils.class);
        return "admin/course/student/index";
    }

    @GetMapping("my-course")
    public String myCourse(Model model, Principal principal) {
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
        return "admin/course/student/myCourse";
    }

    @GetMapping("enrollment-request")
    public String enrollmentRequest(Model model, Principal principal) {
        if (principal == null) return "redirect:/";

        try {
            model.addAttribute("user", userRepo.findByEmail(getAuthUsername()).get());
            model.addAttribute("name", principal.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("title", "My Course list");
        model.addAttribute("courseList", courseService.getCourseListWithIsStudentInRollOrNotByUserId(userRepo.findByEmail(getAuthUsername()).get().getId()));
        model.addAttribute("utils", Utils.class);
        return "admin/course/enrollment-request";
    }

    @PostMapping("in-role-course")
    @ResponseBody
    public String InRoleCourse(Model model, @RequestBody Map<String, String> map) {

        EnrollCourseDto enrollCourseDto = new EnrollCourseDto();

        enrollCourseDto.setCourse(courseService.getCourseById(Long.parseLong(map.get("courseId"))));
        enrollCourseDto.setStudent(userRepo.findByEmail(getAuthUsername()).get());
        enrollCourseDto.setTeacher(userRepo.findByEmail(map.get("uEmail")).get());
        enrollCourseDto.setEnrollment(false);
        enrollCourseDto.setCreatedAt(new Date());
        enrollCourseDto.setUpdatedAt(new Date());
        if (enrollCourseService.checkCourseEnrollment("true,false",
                Long.parseLong(map.get("courseId")),
                userRepo.findByEmail(getAuthUsername()).get().getId(),
                userRepo.findByEmail(map.get("uEmail")).get().getId())) {
            return "{\"error\":true, \"message\":\"This course is already enrolled\"}";
        }
        enrollCourseService.createEnrollCourse(enrollCourseDto);
        return "{\"error\":false, \"message\":\"Course successfully enrolled!\"}";
    }

    @PostMapping("accept-enroll-request")
    @ResponseBody
    public String acceptEnrollRequest(Model model, @RequestBody Map<String, String> map) {

        String enrollmentId = map.get("enrollmentId").toString();
        enrollCourseService.acceptEnrollmentCourses(true, Long.parseLong(enrollmentId));
        return "{\"error\":false, \"message\":\"Course enrollment successfully!\"}";
    }

    @PostMapping("cancel-enroll-request")
    @ResponseBody
    public String cancelEnrollRequest(Model model, @RequestBody Map<String, String> map) {

        String enrollmentId = map.get("enrollmentId").toString();
        enrollCourseService.acceptEnrollmentCourses(false, Long.parseLong(enrollmentId));
        return "{\"error\":false, \"message\":\"Course enrollment cancelled \"}";
    }

    @PostMapping("progress-update")
    @ResponseBody
    public String updateProgress(Model model, @RequestBody Map<String, String> map) {

        User user = userRepo.findByEmail(getAuthUsername()).get();
        Course course = courseService.getCourseById(Long.parseLong(map.get("courseId")));

        if (user.getRole().equals("ROLE_STUDENT")) {
            Long course_id = course.getId();
            CourseProgressDto courseProgressDto = new CourseProgressDto();
            courseProgressDto.setCourse(course);
            courseProgressDto.setStudent(user);

            courseProgressDto.setData(map.get("data"));
            if (!courseProgressService.checkCourseProgressExistOrNot(courseProgressDto)) {
                if (map.get("type").equals("lesson")) {
                    courseProgressDto.setCompletedLesson(1);
                    courseProgressDto.setCompletedExercise(0);
                }else{
                    courseProgressDto.setCompletedExercise(1);
                    courseProgressDto.setCompletedLesson(0);
                }

                data = courseProgressService.createCourseProgress(courseProgressDto);
            } else {
                CourseProgressDto courseProgressDto1 = courseProgressService
                        .getCourseProgressExistRecord(courseProgressDto);

                courseProgressDto.setCompletedLesson(courseProgressDto1.getCompletedLesson() + 1);
                courseProgressDto.setCompletedExercise(courseProgressDto1.getCompletedExercise() + 1);
                courseProgressDto.setData((courseProgressDto1.getData() != null) ?
                        courseProgressDto1.getData() + "," + map.get("data") : map.get("data"));

                if (!courseProgressDto1.getData().contains(map.get("data")))
                    courseProgressService.updateCourseProgress(courseProgressDto, map.get("type"));
            }
        }
        return "";
    }
}
