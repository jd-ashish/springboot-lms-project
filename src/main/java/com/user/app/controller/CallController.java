package com.user.app.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/call/")
public class CallController {

    @GetMapping("/video1")
    public String video1(){
        return "admin/videocall/video-call-one";
    }

    @RequestMapping("/video2")
    public String video2(){
        return "admin/videocall/video-call-two";
    }
}
