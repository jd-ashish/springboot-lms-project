package com.user.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.user.app.entity.Lesson;
import com.user.app.entity.User;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class CourseDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    private String course_name;
    private String course_topic;
    private User user;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date updatedAt;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Lesson> lessons = new ArrayList<>();


    private Long total_lesson;

    private Long total_questions;


}
