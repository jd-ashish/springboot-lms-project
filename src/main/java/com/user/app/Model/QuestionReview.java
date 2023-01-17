package com.user.app.Model;

import lombok.Data;

@Data
public class QuestionReview {
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    private String userSelectAnswer;
}
