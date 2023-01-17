package com.user.app.controller.admin;

import com.user.app.commons.utils.Utils;
import com.user.app.entity.User;
import com.user.app.repositories.QuestionExerciseRepository;
import com.user.app.repositories.UserRepo;
import com.user.app.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;

import static com.user.app.commons.utils.Utils.getAuthUsername;

@Controller
@RequestMapping("/admin/")
public class AdminCourseController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private QuestionExerciseService questionExerciseService;

    @Autowired
    private CourseService courseService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private QuestionExerciseRepository questionExerciseRepository;
    @Autowired
    private CourseProgressService courseProgressService;


    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamReportService examReportService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EnrollCourseService enrollCourseService;

    @GetMapping("/course")
    public String course(Model model, Principal principal,
                         @RequestParam(required = false) String editCourse,
                         @RequestParam(required = false) String name,
                         @RequestParam(required = false) String topic
    ) {
        if (principal == null) return "redirect:/";
        User user = userRepo.findByEmail(getAuthUsername()).get();
        try {
            model.addAttribute("user", user);
            model.addAttribute("name", principal.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("edit", editCourse);
        model.addAttribute("name", name);
        model.addAttribute("topic", topic);
        model.addAttribute("title", "admin Course list");
        model.addAttribute("courseList", courseService.getCourseList());
        model.addAttribute("utils", Utils.class);
        return "admin/admin/course-list";
    }
    @GetMapping("/users")
    @RolesAllowed("ROLE_ADMIN")
    public String getStudent(Model model, Principal principal) {
        if (principal == null) return "redirect:/";
        User user = userRepo.findByEmail(getAuthUsername()).get();
        model.addAttribute("user", user);
        model.addAttribute("name", principal.getName());
        model.addAttribute("utils", Utils.class);
        model.addAttribute("title", "Student list");
        model.addAttribute("userList", userRepo.findAll());
        return "admin/student/user-list";
    }

    @GetMapping("/lessons")
    @RolesAllowed("ROLE_ADMIN")
    public String getLesson(Model model, Principal principal) {
        if (principal == null) return "redirect:/";
        User user = userRepo.findByEmail(getAuthUsername()).get();
        model.addAttribute("user", user);
        model.addAttribute("name", principal.getName());
        model.addAttribute("utils", Utils.class);
        model.addAttribute("title", "Student list");
        model.addAttribute("lessons", lessonService.getLessonList());
        return "admin/admin/lesson-list";
    }
}
