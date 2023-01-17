package com.user.app.controller;


import com.user.app.dto.UserDto;
import com.user.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/auth/")
public class AuthController {

    @Autowired
    private UserService userService;


    @PostMapping("signup")
    public String signup(Model model, HttpServletResponse response,
                         @ModelAttribute("user") UserDto userDto) {
        model.addAttribute("user", userDto);
        userService.signup(userDto);
        model.addAttribute("success", true);
        return "index";
    }

//    @PostMapping("login")
//    public String login(Model model,
//                        HttpServletRequest request, HttpServletResponse response,
//                        @ModelAttribute("user") UserDto userDto) {
//        model.addAttribute("user", userDto);
//
//        UserDto userDtoResult = null;
//        try {
//            userDtoResult = userService.login(userDto);
//
//            Cookies.makeCookies(response, LOGIN_COOKIES_NAME, new Gson().toJson(userDtoResult));
//            return "redirect:/dashboard";
//        } catch (Throwable e) {
//            System.out.println(e.getMessage());
//            model.addAttribute("message", e.getMessage());
//            model.addAttribute("isLogin", false);
//            return "index";
//        }
//    }
}
