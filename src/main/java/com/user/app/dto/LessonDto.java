package com.user.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.user.app.entity.Course;
import com.user.app.entity.User;
import lombok.Data;

import java.util.Date;

@Data
public class LessonDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String lessonsName;
    private String lessonContent;
    private User user;
    private Course course;



    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date updatedAt;
}
