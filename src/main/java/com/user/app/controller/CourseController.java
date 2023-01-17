package com.user.app.controller;

import com.google.gson.Gson;
import com.user.app.commons.dynamic.Javascript;
import com.user.app.commons.utils.Cookies;
import com.user.app.commons.utils.EncodeDecode;
import com.user.app.commons.utils.Utils;
import com.user.app.dto.CourseDto;
import com.user.app.dto.UserDto;
import com.user.app.entity.Course;
import com.user.app.entity.User;
import com.user.app.repositories.UserRepo;
import com.user.app.service.CourseService;
import com.user.app.service.QuestionExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.user.app.commons.constants.AppConstants.LOGIN_COOKIES_NAME;
import static com.user.app.commons.utils.Utils.getAuthUsername;

@Controller
@RequestMapping("/course/")
public class CourseController {


    @Autowired
    private QuestionExerciseService questionExerciseService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CourseService courseService;

    @ModelAttribute
    public void commonMethod(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("user",
                    userRepo.findByEmail(getAuthUsername()).get());
            model.addAttribute("name", principal.getName());
        }
        model.addAttribute("user", null);
    }

    @PostMapping("/create")
    public String createCourse(Model model, @ModelAttribute("course") CourseDto courseDto) {
        courseDto.setUser(userRepo.findByEmail(getAuthUsername()).get());
        if (courseDto.getId() == 0) {
            courseService.createCourse(courseDto);
        }else{
            //update course here
            courseService.updateCourse(courseDto);
        }
        return "redirect:/course";
    }


    @GetMapping("/delete/{id}")
    public String deleteCourse(Model model, @PathVariable("id") Integer id) {
        courseService.deleteCourse(id);
        return "redirect:/course";
    }

}
