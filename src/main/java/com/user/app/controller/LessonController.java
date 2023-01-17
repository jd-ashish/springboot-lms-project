package com.user.app.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.user.app.commons.dto.ApiResponse;
import com.user.app.commons.utils.Cookies;
import com.user.app.commons.utils.EncodeDecode;
import com.user.app.dto.CourseDto;
import com.user.app.dto.LessonDto;
import com.user.app.entity.User;
import com.user.app.repositories.UserRepo;
import com.user.app.service.LessonService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static com.user.app.commons.constants.AppConstants.LOGIN_COOKIES_NAME;
import static com.user.app.commons.utils.Utils.getAuthUsername;

@Controller
@RequestMapping("/lesson")
public class LessonController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private LessonService lessonService;

    @ModelAttribute
    public void commonMethod(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("user",
                    userRepo.findByEmail(getAuthUsername()).get());
            model.addAttribute("name", principal.getName());
        }
        model.addAttribute("user", null);
    }

    @SneakyThrows
    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        lessonService.delete(id);
        return "redirect:/lesson";
    }

    //    @PostMapping("/create")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> createCourse(Model model,
                                          @RequestBody Map<String, Object> map) {

        ApiResponse apiResponse = new ApiResponse();
        if (!map.get("id").equals("0")) {

            try {
                lessonService.createLesson(map, userRepo.findByEmail(getAuthUsername()).get());
                apiResponse.setSuccess(true);
                apiResponse.setMessage("Lesson update successfully!");
            } catch (Exception e) {
                apiResponse.setSuccess(false);
                apiResponse.setMessage(e.getMessage() + " , Please try after some time.");
            }

            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        }
        if (lessonService.getLessonByLessonName(map.get("lessonsName").toString()) != null) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("This lesson already available");
        }
        if (map.get("lessonsName").toString().equals("") || map.get("content").toString().equals("")) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("all filed necessary");
        }

        try {
            lessonService.createLesson(map, userRepo.findByEmail(getAuthUsername()).get());
            apiResponse.setSuccess(true);
            apiResponse.setMessage("Lesson created successfully!");
        } catch (Exception e) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage(e.getMessage() + " , Please try after some time.");
        }
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }
}
