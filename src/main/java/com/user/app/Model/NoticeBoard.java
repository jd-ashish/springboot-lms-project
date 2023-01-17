package com.user.app.Model;

import com.user.app.entity.User;
import lombok.Data;

import java.util.Date;

@Data
public class NoticeBoard {
    private String message;
    private String date;
    private User user;
    private String href;
}
