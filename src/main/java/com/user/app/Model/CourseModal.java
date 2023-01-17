package com.user.app.Model;

import com.user.app.dto.UserDto;
import com.user.app.entity.User;
import lombok.Data;

@Data
public class CourseModal {
    private String course_name;
    private String course_topic;
    private Long course_id;
    private User studentDetails;
    private Long enroll_course_id;
    private User user;
    private String is_enrollment;
    private String status;
}
