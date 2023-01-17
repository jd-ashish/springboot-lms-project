package com.user.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.user.app.entity.Course;
import com.user.app.entity.Lesson;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class UserDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String name;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String role;
    private Date createdAt;

    private boolean enable;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Course> courses = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Lesson> lessons = new ArrayList<>();

}
